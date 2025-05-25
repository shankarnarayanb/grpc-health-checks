@echo off
REM Convenience script to run the Health gRPC Client

echo ╔══════════════════════════════════════════════════════════════╗
echo ║                   🧪 HEALTH gRPC CLIENT                      ║
echo ║                       Test Script                            ║
echo ╚══════════════════════════════════════════════════════════════╝
echo.

REM Check if Maven is available
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Maven not found in PATH. Please install Maven first.
    pause
    exit /b 1
)

echo 🧪 Running Health gRPC Client tests...
echo.
echo    This will test all three methods:
echo    • Ping (liveness check)
echo    • CheckHealth (comprehensive health)
echo    • WatchHealth (streaming health updates)
echo.
echo Make sure the server is running first (run-server.bat)
echo.

call mvn exec:java -Pclient
echo.
echo 🏁 Client tests completed!
pause
