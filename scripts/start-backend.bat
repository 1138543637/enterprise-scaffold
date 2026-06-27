@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion
title Enterprise Scaffold - Start Backend

REM ============================================================
REM start-backend.bat
REM 作用：检查 MySQL、8080 端口、Maven，然后启动 Spring Boot 后端
REM 放置位置：D:\Code\enterprise-scaffold\scripts\start-backend.bat
REM 前提：MYSQL_PASSWORD 已写入 Windows 系统环境变量
REM ============================================================

set "BACKEND_PORT=8080"
set "MYSQL_PORT=3306"
set "JWT_SECRET=enterprise-scaffold-local-dev-secret-please-change-32"

for %%I in ("%~dp0..") do set "PROJECT_ROOT=%%~fI"
set "BACKEND_DIR=%PROJECT_ROOT%\scaffold-backend"
set "UPLOAD_DIR=%PROJECT_ROOT%\uploads"
set "LOCAL_UPLOAD_PATH=%UPLOAD_DIR%"

echo.
echo ============================================================
echo [后端启动检查]
echo 项目根目录：%PROJECT_ROOT%
echo 后端目录：%BACKEND_DIR%
echo 后端端口：%BACKEND_PORT%
echo 上传目录：%LOCAL_UPLOAD_PATH%
echo ============================================================
echo.

if not exist "%BACKEND_DIR%\pom.xml" (
    echo [错误] 没有找到后端 pom.xml：
    echo %BACKEND_DIR%\pom.xml
    echo.
    echo 请确认本脚本放在：
    echo D:\Code\enterprise-scaffold\scripts\start-backend.bat
    echo.
    pause
    exit /b 1
)

if not exist "%UPLOAD_DIR%" (
    echo [创建] 上传目录不存在，正在创建：
    echo %UPLOAD_DIR%
    mkdir "%UPLOAD_DIR%"
    echo.
)

if not defined MYSQL_PASSWORD (
    echo [错误] 没有读取到系统环境变量 MYSQL_PASSWORD。
    echo 请确认你已经在 Windows 系统变量中设置 MYSQL_PASSWORD。
    echo 例如：MYSQL_PASSWORD=123456
    echo.
    pause
    exit /b 1
)

where java >nul 2>nul
if errorlevel 1 (
    echo [错误] 没有找到 java 命令，请检查 Java 17 是否安装并加入 PATH。
    echo.
    pause
    exit /b 1
) else (
    echo [通过] 已找到 java 命令。
)

where mvn >nul 2>nul
if errorlevel 1 (
    echo [错误] 没有找到 mvn 命令，请检查 Maven 是否安装并加入 PATH。
    echo.
    pause
    exit /b 1
) else (
    echo [通过] 已找到 mvn 命令。
)

echo [检查] 正在检查 MySQL 端口 %MYSQL_PORT%...
set "MYSQL_PID="
for /f "tokens=5" %%a in ('netstat -ano ^| findstr /R /C:":%MYSQL_PORT% .*LISTENING"') do (
    set "MYSQL_PID=%%a"
)

if not defined MYSQL_PID (
    echo [错误] MySQL 端口 %MYSQL_PORT% 未监听。
    echo 请先运行 scripts\start-mysql.bat 启动 MySQL。
    echo.
    pause
    exit /b 1
)

where mysql >nul 2>nul
if not errorlevel 1 (
    mysql -uroot -p%MYSQL_PASSWORD% -e "USE enterprise_scaffold; SELECT 1;" >nul 2>nul
    if errorlevel 1 (
        echo [错误] MySQL 已启动，但无法连接 enterprise_scaffold 数据库。
        echo 请检查：
        echo 1. MYSQL_PASSWORD 是否正确
        echo 2. enterprise_scaffold 数据库是否已初始化
        echo.
        pause
        exit /b 1
    ) else (
        echo [通过] MySQL 连接和 enterprise_scaffold 数据库检查通过。
    )
) else (
    echo [警告] 未找到 mysql 命令，跳过数据库连接测试，只检查端口。
)

echo [检查] 正在检查后端端口 %BACKEND_PORT%...
set "BACKEND_PID="
for /f "tokens=5" %%a in ('netstat -ano ^| findstr /R /C:":%BACKEND_PORT% .*LISTENING"') do (
    set "BACKEND_PID=%%a"
)

if defined BACKEND_PID (
    echo [提示] 端口 %BACKEND_PORT% 已被占用，PID=!BACKEND_PID!
    tasklist /FI "PID eq !BACKEND_PID!"
    echo.

    echo [检查] 尝试判断是否已经是 Enterprise Scaffold 后端...
    powershell -NoProfile -Command "try { $r = Invoke-WebRequest -Uri 'http://localhost:8080/api/health' -UseBasicParsing -TimeoutSec 3; if ($r.Content -like '*enterprise-scaffold backend running*') { exit 0 } else { exit 2 } } catch { exit 1 }"
    if not errorlevel 1 (
        echo [成功] 后端已经在运行，不需要重复启动。
        echo 测试地址：http://localhost:8080/api/health
        echo.
        pause
        exit /b 0
    )

    echo [警告] 8080 被占用，但不像当前项目后端。
    choice /C YN /M "是否强制结束占用 8080 的进程"
    if errorlevel 2 (
        echo [取消] 未结束进程，后端不启动。
        echo.
        pause
        exit /b 1
    )

    taskkill /PID !BACKEND_PID! /F
    if errorlevel 1 (
        echo [错误] 结束进程失败，请手动关闭占用 8080 的程序。
        echo.
        pause
        exit /b 1
    )
    timeout /t 2 /nobreak >nul
)

echo.
echo [启动] 即将启动 Spring Boot 后端。
echo 请不要关闭这个窗口；关闭窗口会停止后端服务。
echo.
echo 后端健康检查地址：
echo http://localhost:8080/api/health
echo.

cd /d "%BACKEND_DIR%"
mvn spring-boot:run

echo.
echo [提示] 后端进程已退出。
pause
exit /b 0
