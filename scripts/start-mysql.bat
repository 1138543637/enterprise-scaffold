@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion
title Enterprise Scaffold - Start MySQL

REM ============================================================
REM start-mysql.bat
REM 作用：检查并启动本机 MySQL，默认端口 3306
REM 放置位置：D:\Code\enterprise-scaffold\scripts\start-mysql.bat
REM 前提：MYSQL_PASSWORD 已写入 Windows 系统环境变量
REM 可选：如果你的 MySQL 服务名不是 MySQL80，请修改 MYSQL_SERVICE_NAME
REM ============================================================

set "MYSQL_PORT=3306"

if not defined MYSQL_SERVICE_NAME (
    set "MYSQL_SERVICE_NAME=MySQL80"
)

echo.
echo ============================================================
echo [MySQL 启动检查]
echo 端口：%MYSQL_PORT%
echo 服务名：%MYSQL_SERVICE_NAME%
echo ============================================================
echo.

if not defined MYSQL_PASSWORD (
    echo [错误] 没有读取到系统环境变量 MYSQL_PASSWORD。
    echo 请确认你已经在 Windows 系统变量中设置 MYSQL_PASSWORD。
    echo 例如：MYSQL_PASSWORD=123456
    echo.
    pause
    exit /b 1
)

where mysql >nul 2>nul
if errorlevel 1 (
    echo [警告] 没有在 PATH 中找到 mysql 命令。
    echo 这不一定影响 MySQL 服务启动，但会影响连接测试。
    echo 建议把 MySQL 的 bin 目录加入 PATH。
    echo.
) else (
    echo [通过] 已找到 mysql 命令。
)

echo [检查] 正在检查 3306 端口是否已经被占用...
set "MYSQL_PID="
for /f "tokens=5" %%a in ('netstat -ano ^| findstr /R /C:":%MYSQL_PORT% .*LISTENING"') do (
    set "MYSQL_PID=%%a"
)

if defined MYSQL_PID (
    echo [提示] 端口 %MYSQL_PORT% 已被进程占用，PID=!MYSQL_PID!
    tasklist /FI "PID eq !MYSQL_PID!"
    echo.

    where mysql >nul 2>nul
    if not errorlevel 1 (
        mysql -uroot -p%MYSQL_PASSWORD% -e "SELECT VERSION();" >nul 2>nul
        if not errorlevel 1 (
            echo [成功] MySQL 已经在运行，并且 root 可以连接。
            echo 你可以继续启动后端。
            echo.
            pause
            exit /b 0
        ) else (
            echo [警告] 3306 端口被占用，但 root 连接失败。
            echo 可能原因：
            echo 1. MYSQL_PASSWORD 系统变量不是当前 MySQL root 密码
            echo 2. 3306 被其他程序占用
            echo 3. MySQL 正在启动但尚未完成
            echo.
            pause
            exit /b 1
        )
    ) else (
        echo [提示] 端口已占用，但无法用 mysql 命令测试连接。
        echo 如果这是 MySQL 服务，可以继续启动后端。
        echo.
        pause
        exit /b 0
    )
)

echo [检查] 3306 端口未占用，准备启动 MySQL 服务：%MYSQL_SERVICE_NAME%
echo.

sc query "%MYSQL_SERVICE_NAME%" >nul 2>nul
if errorlevel 1 (
    echo [错误] 找不到 MySQL 服务：%MYSQL_SERVICE_NAME%
    echo.
    echo 你可以执行下面命令查看本机 MySQL 服务名：
    echo sc query type^= service state^= all ^| findstr /I mysql
    echo.
    echo 如果查到的服务名不是 MySQL80，请修改本文件中的 MYSQL_SERVICE_NAME。
    echo 例如：
    echo set "MYSQL_SERVICE_NAME=MySQL"
    echo.
    pause
    exit /b 1
)

echo [启动] 正在启动 MySQL 服务...
net start "%MYSQL_SERVICE_NAME%"
echo.

echo [等待] 正在等待 MySQL 监听 3306 端口...
for /l %%i in (1,1,30) do (
    set "MYSQL_PID="
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr /R /C:":%MYSQL_PORT% .*LISTENING"') do (
        set "MYSQL_PID=%%a"
    )
    if defined MYSQL_PID (
        goto MYSQL_STARTED
    )
    timeout /t 1 /nobreak >nul
)

echo [错误] 等待 30 秒后，MySQL 仍未监听 3306 端口。
echo 请检查 MySQL 服务是否启动失败。
echo.
pause
exit /b 1

:MYSQL_STARTED
echo [成功] MySQL 已监听 3306 端口。
where mysql >nul 2>nul
if not errorlevel 1 (
    mysql -uroot -p%MYSQL_PASSWORD% -e "SELECT VERSION();" >nul 2>nul
    if not errorlevel 1 (
        echo [成功] root 连接测试通过。
    ) else (
        echo [警告] MySQL 端口已启动，但 root 连接测试失败。
        echo 请检查 MYSQL_PASSWORD 系统变量。
    )
)
echo.
pause
exit /b 0
