@echo off
REM Convenience script to run the Health gRPC Service

echo ╔══════════════════════════════════════════════════════════════╗
echo ║                    🏥 HEALTH gRPC SERVICE                    ║
echo ║                      Build and Run Script                    ║
echo ╚══════════════════════════════════════════════════════════════╝
echo.

REM Check if Maven is available
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Maven not found in PATH. Please install Maven first.
    pause
    exit /b 1
)

echo 🔨 Building project...
call mvn clean compile
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Build failed!
    pause
    exit /b 1
)

echo.
echo ✅ Build successful!
echo.
echo 🚀 Starting Health gRPC Server on port 9090...
echo.
echo    Available methods:
echo    • health.v1.HealthService/Ping
echo    • health.v1.HealthService/CheckHealth  
echo    • health.v1.HealthService/WatchHealth
echo.
echo Press Ctrl+C to stop the server
echo.

call mvn clean compile exec:java -Pserver
