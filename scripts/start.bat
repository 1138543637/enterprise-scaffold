@echo off
chcp 65001 >nul

echo ========================================
echo Enterprise Scaffold 后端启动脚本
echo ========================================
echo.

cd /d D:\Code\enterprise-scaffold\scaffold-backend

set "MYSQL_PASSWORD=123456"
set "JWT_SECRET=enterprise-scaffold-local-dev-secret-please-change-32"
set "LOCAL_UPLOAD_PATH=D:\Code\enterprise-scaffold\uploads"

echo 当前执行目录：
cd
echo.

echo 当前 MYSQL_PASSWORD=%MYSQL_PASSWORD%
echo 当前 JWT_SECRET=%JWT_SECRET%
echo 当前 LOCAL_UPLOAD_PATH=%LOCAL_UPLOAD_PATH%
echo.

echo 开始启动 Spring Boot 后端...
echo.

mvn spring-boot:run

echo.
echo 后端已停止，或者启动失败。
pause