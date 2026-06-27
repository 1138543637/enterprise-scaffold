@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion
title Enterprise Scaffold - Start Frontend

REM ============================================================
REM start-frontend.bat
REM 作用：检查 5173 端口、Node、pnpm，然后启动 Vue3 前端
REM 放置位置：D:\Code\enterprise-scaffold\scripts\start-frontend.bat
REM ============================================================

set "FRONTEND_PORT=5173"
set "BACKEND_PORT=8080"

for %%I in ("%~dp0..") do set "PROJECT_ROOT=%%~fI"
set "FRONTEND_DIR=%PROJECT_ROOT%\scaffold-frontend"

echo.
echo ============================================================
echo [前端启动检查]
echo 项目根目录：%PROJECT_ROOT%
echo 前端目录：%FRONTEND_DIR%
echo 前端端口：%FRONTEND_PORT%
echo ============================================================
echo.

if not exist "%FRONTEND_DIR%\package.json" (
    echo [错误] 没有找到前端 package.json：
    echo %FRONTEND_DIR%\package.json
    echo.
    echo 请确认本脚本放在：
    echo D:\Code\enterprise-scaffold\scripts\start-frontend.bat
    echo.
    pause
    exit /b 1
)

where node >nul 2>nul
if errorlevel 1 (
    echo [错误] 没有找到 node 命令，请检查 Node.js 是否安装并加入 PATH。
    echo.
    pause
    exit /b 1
) else (
    echo [通过] 已找到 node 命令。
)

where pnpm >nul 2>nul
if errorlevel 1 (
    echo [错误] 没有找到 pnpm 命令，请检查 pnpm 是否安装并加入 PATH。
    echo.
    pause
    exit /b 1
) else (
    echo [通过] 已找到 pnpm 命令。
)

echo [检查] 正在检查后端端口 %BACKEND_PORT%...
set "BACKEND_PID="
for /f "tokens=5" %%a in ('netstat -ano ^| findstr /R /C:":%BACKEND_PORT% .*LISTENING"') do (
    set "BACKEND_PID=%%a"
)

if not defined BACKEND_PID (
    echo [警告] 没有检测到 8080 后端端口。
    echo 前端仍可启动，但登录和接口请求会失败。
    echo 建议先运行 scripts\start-backend.bat。
    echo.
) else (
    echo [通过] 检测到后端端口 8080 已监听。
)

echo [检查] 正在检查前端端口 %FRONTEND_PORT%...
set "FRONTEND_PID="
for /f "tokens=5" %%a in ('netstat -ano ^| findstr /R /C:":%FRONTEND_PORT% .*LISTENING"') do (
    set "FRONTEND_PID=%%a"
)

if defined FRONTEND_PID (
    echo [提示] 端口 %FRONTEND_PORT% 已被占用，PID=!FRONTEND_PID!
    tasklist /FI "PID eq !FRONTEND_PID!"
    echo.

    echo [检查] 尝试访问当前前端地址...
    powershell -NoProfile -Command "try { Invoke-WebRequest -Uri 'http://localhost:5173' -UseBasicParsing -TimeoutSec 3 > $null; exit 0 } catch { exit 1 }"
    if not errorlevel 1 (
        echo [提示] http://localhost:5173 可以访问，前端可能已经在运行。
        choice /C YN /M "是否仍然结束占用 5173 的进程并重新启动前端"
    ) else (
        echo [警告] 5173 被占用，但无法正常访问。
        choice /C YN /M "是否强制结束占用 5173 的进程"
    )

    if errorlevel 2 (
        echo [取消] 未结束进程，前端不启动。
        echo.
        pause
        exit /b 1
    )

    taskkill /PID !FRONTEND_PID! /F
    if errorlevel 1 (
        echo [错误] 结束进程失败，请手动关闭占用 5173 的程序。
        echo.
        pause
        exit /b 1
    )
    timeout /t 2 /nobreak >nul
)

cd /d "%FRONTEND_DIR%"

if not exist "%FRONTEND_DIR%\node_modules" (
    echo [安装] node_modules 不存在，正在执行 pnpm install...
    pnpm install
    if errorlevel 1 (
        echo [错误] pnpm install 失败，请检查网络或 package.json。
        echo.
        pause
        exit /b 1
    )
)

echo.
echo [启动] 即将启动 Vue3 前端。
echo 请不要关闭这个窗口；关闭窗口会停止前端服务。
echo.
echo 前端访问地址：
echo http://localhost:5173
echo.

pnpm dev

echo.
echo [提示] 前端进程已退出。
pause
exit /b 0
