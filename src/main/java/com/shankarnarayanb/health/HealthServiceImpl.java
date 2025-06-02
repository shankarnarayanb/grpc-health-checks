package com.shankarnarayanb.health;

import com.shankarnarayanb.health.v1.HealthServiceGrpc;
import com.shankarnarayanb.health.v1.HealthServiceProto.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║                        🏥 HEALTH SERVICE IMPLEMENTATION                       ║
 * ║                                                                              ║
 * ║  This class extends HealthServiceGrpc.HealthServiceImplBase to provide       ║
 * ║  concrete implementations of the health check service methods defined        ║
 * ║  in health_service.proto.                                                    ║
 * ║                                                                              ║
 * ║  Architecture:                                                               ║
 * ║  • Extends auto-generated HealthServiceImplBase (provides framework hooks)   ║
 * ║  • Implements 3 service methods: ping(), checkHealth(), watchHealth()       ║
 * ║  • Contains business logic for checking system component health              ║
 * ║  • Supports streaming for real-time health monitoring                       ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 */
public class HealthServiceImpl extends HealthServiceGrpc.HealthServiceImplBase {

    // ═══════════════════════════════════════════════════════════════════════════
    // 📋 CONSTANTS AND CONFIGURATION
    // ═══════════════════════════════════════════════════════════════════════════

    private static final Logger logger = LoggerFactory.getLogger(HealthServiceImpl.class);
    private static final String SERVICE_VERSION = "1.0.0";
    private static final int DEFAULT_WATCH_INTERVAL_SECONDS = 30;
    private static final int DEFAULT_HEALTH_CHECK_TIMEOUT_SECONDS = 10;

    // ═══════════════════════════════════════════════════════════════════════════
    // 🔧 INSTANCE VARIABLES AND INITIALIZATION
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Scheduler for the streaming health watch functionality.
     * Used to periodically send health updates to watching clients.
     */
    private final ScheduledExecutorService watchScheduler = Executors.newScheduledThreadPool(2);

    /**
     * Constructor - Initialize any resources needed for health checking.
     * In a real application, you might inject database connections,
     * cache clients, etc. here.
     */
    public HealthServiceImpl() {
        logger.info("🏥 Health Service Implementation initialized");
        logger.info("   Version: {}", SERVICE_VERSION);
        logger.info("   Ready to accept health check requests");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // 🎯 CORE gRPC SERVICE METHODS (Implementation of proto service definition)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * 🏓 PING METHOD - Simple Liveness Check
     *
     * Purpose: Quick check to verify the service process is running and responsive.
     * Use case: Load balancer health checks, basic monitoring.
     * Should be: Fast, lightweight, minimal dependencies.
     *
     * gRPC Pattern: Unary RPC (one request → one response)
     */
    @Override
    public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {
        logger.info("🏓 Received ping request");

        try {
            // Build response using protobuf builder pattern
            PingResponse response = PingResponse.newBuilder()
                    .setSuccess(true)
                    .setVersion(SERVICE_VERSION)
                    .setTimestampMs(System.currentTimeMillis())
                    .build();

            // Send response back to client
            responseObserver.onNext(response);      // Send the data
            responseObserver.onCompleted();         // Signal completion

            logger.info("✅ Ping response sent successfully");

        } catch (Exception e) {
            logger.error("❌ Error processing ping request", e);
            responseObserver.onError(e);            // Send error to client
        }
    }

    /**
     * 🏥 CHECK HEALTH METHOD - Comprehensive Readiness Check
     *
     * Purpose: Thorough validation of service and its dependencies.
     * Use case: Kubernetes readiness probes, detailed monitoring dashboards.
     * Should check: Database connectivity, cache availability, external APIs, etc.
     *
     * gRPC Pattern: Unary RPC (one request → one response)
     */
    @Override
    public void checkHealth(HealthCheckRequest request, StreamObserver<HealthCheckResponse> responseObserver) {
        long startTime = System.currentTimeMillis();
        logger.info("🏥 Received health check request");
        logger.info("   Components requested: {}", request.getComponentsList());
        logger.info("   Include dependencies: {}", request.getIncludeDependencies());
        logger.info("   Timeout: {}s", request.getTimeoutSeconds());

        try {
            // STEP 1: Determine which components to check
            Map<String, ComponentHealth> componentHealthMap = checkRequestedComponents(request);

            // STEP 2: Determine overall service status based on component health
            HealthCheckResponse.ServiceStatus overallStatus = determineOverallStatus(componentHealthMap);

            // STEP 3: Calculate check duration
            long duration = System.currentTimeMillis() - startTime;

            // STEP 4: Build comprehensive response
            HealthCheckResponse.Builder responseBuilder = HealthCheckResponse.newBuilder()
                    .setOverallStatus(overallStatus)
                    .putAllComponents(componentHealthMap)   // Map becomes protobuf map
                    .setTotalCheckDurationMs(duration);

            // STEP 5: Add error message if service is unhealthy
            if (overallStatus == HealthCheckResponse.ServiceStatus.UNHEALTHY) {
                responseBuilder.setErrorMessage("One or more critical components are unhealthy");
            }

            HealthCheckResponse response = responseBuilder.build();

            // STEP 6: Send response to client
            responseObserver.onNext(response);
            responseObserver.onCompleted();

            logger.info("✅ Health check completed in {}ms with status: {}", duration, overallStatus);

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("❌ Error during health check", e);

            // Send error response instead of throwing exception
            HealthCheckResponse errorResponse = HealthCheckResponse.newBuilder()
                    .setOverallStatus(HealthCheckResponse.ServiceStatus.UNHEALTHY)
                    .setErrorMessage("Health check failed: " + e.getMessage())
                    .setTotalCheckDurationMs(duration)
                    .build();

            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }

    /**
     * 👀 WATCH HEALTH METHOD - Streaming Health Updates
     *
     * Purpose: Real-time health monitoring with periodic updates.
     * Use case: Monitoring dashboards, alerting systems.
     *
     * gRPC Pattern: Server-side streaming RPC (one request → multiple responses over time)
     * Key difference: Stream stays open, server sends multiple responses.
     */
    @Override
    public void watchHealth(HealthWatchRequest request, StreamObserver<HealthCheckResponse> responseObserver) {
        int intervalSeconds = request.getIntervalSeconds() > 0
                ? request.getIntervalSeconds()
                : DEFAULT_WATCH_INTERVAL_SECONDS;

        logger.info("👀 Starting health watch stream");
        logger.info("   Components to watch: {}", request.getComponentsList());
        logger.info("   Interval: {}s", intervalSeconds);

        // Schedule periodic health checks using the scheduler
        watchScheduler.scheduleAtFixedRate(() -> {
            try {
                // Create a health check request based on the watch request
                HealthCheckRequest healthRequest = buildHealthCheckRequest(request);

                // Perform health check and get result
                HealthCheckResponse healthUpdate = performHealthCheckSync(healthRequest);

                // Send update to watching client
                responseObserver.onNext(healthUpdate);

                logger.debug("📡 Sent health update to watching client: {}",
                        healthUpdate.getOverallStatus());

            } catch (Exception e) {
                logger.error("❌ Error in health watch periodic check", e);
                responseObserver.onError(e);  // This will end the stream
            }
        }, 0, intervalSeconds, TimeUnit.SECONDS);

        // NOTE: We don't call responseObserver.onCompleted() here!
        // The stream stays open and keeps sending updates until:
        // 1. Client disconnects
        // 2. An error occurs
        // 3. Server shuts down
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // 💼 BUSINESS LOGIC IMPLEMENTATION METHODS
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Check the components requested by the client.
     * This method orchestrates the checking of individual components.
     */
    private Map<String, ComponentHealth> checkRequestedComponents(HealthCheckRequest request) {
        Map<String, ComponentHealth> componentHealthMap = new HashMap<>();

        if (!request.getComponentsList().isEmpty()) {
            // Check specific components requested by client
            for (String component : request.getComponentsList()) {
                componentHealthMap.put(component,
                        checkSingleComponentHealth(component, request.getIncludeDependencies()));
            }
        } else {
            // No specific components requested - check all default components
            componentHealthMap.put("database",
                    checkSingleComponentHealth("database", request.getIncludeDependencies()));
            componentHealthMap.put("cache",
                    checkSingleComponentHealth("cache", request.getIncludeDependencies()));
            componentHealthMap.put("external-api",
                    checkSingleComponentHealth("external-api", request.getIncludeDependencies()));
        }

        return componentHealthMap;
    }

    /**
     * Check the health of a single component.
     * 🎯 THIS IS WHERE YOU IMPLEMENT YOUR SPECIFIC BUSINESS LOGIC!
     *
     * Replace the placeholder methods below with real implementations that check:
     * - Database connectivity and query performance
     * - Cache availability and response times
     * - External API health and response times
     * - File system access and disk space
     * - Memory usage and CPU load
     */
    private ComponentHealth checkSingleComponentHealth(String componentName, boolean includeDependencies) {
        logger.debug("🔍 Checking health of component: {}", componentName);

        try {
            ComponentHealth.Builder healthBuilder = ComponentHealth.newBuilder();

            // Route to specific component health check implementation
            switch (componentName.toLowerCase()) {
                case "database":
                    return checkDatabaseComponentHealth(healthBuilder, includeDependencies);

                case "cache":
                    return checkCacheComponentHealth(healthBuilder, includeDependencies);

                case "external-api":
                    return checkExternalApiComponentHealth(healthBuilder, includeDependencies);

                default:
                    return checkUnknownComponentHealth(componentName, healthBuilder);
            }

        } catch (Exception e) {
            logger.error("❌ Error checking component health: {}", componentName, e);

            return ComponentHealth.newBuilder()
                    .setAvailable(false)
                    .setFunctional(false)
                    .setErrorDetails("Health check failed: " + e.getMessage())
                    .setResponseTimeMs(0)
                    .build();
        }
    }

    /**
     * Determine overall service status based on individual component health.
     * Business logic: How do you want to aggregate component health into overall status?
     */
    private HealthCheckResponse.ServiceStatus determineOverallStatus(Map<String, ComponentHealth> componentHealth) {
        boolean allHealthy = true;
        boolean anyAvailable = false;

        for (ComponentHealth health : componentHealth.values()) {
            if (health.getAvailable()) {
                anyAvailable = true;
            }
            if (!health.getFunctional()) {
                allHealthy = false;
            }
        }

        // Business logic for status determination:
        if (allHealthy) {
            return HealthCheckResponse.ServiceStatus.HEALTHY;
        } else if (anyAvailable) {
            return HealthCheckResponse.ServiceStatus.DEGRADED;  // Some components working
        } else {
            return HealthCheckResponse.ServiceStatus.UNHEALTHY;  // Nothing working
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // 🔧 HELPER AND UTILITY METHODS
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Convert a HealthWatchRequest into a HealthCheckRequest for reuse of logic.
     */
    private HealthCheckRequest buildHealthCheckRequest(HealthWatchRequest watchRequest) {
        return HealthCheckRequest.newBuilder()
                .addAllComponents(watchRequest.getComponentsList())
                .setIncludeDependencies(true)
                .setTimeoutSeconds(DEFAULT_HEALTH_CHECK_TIMEOUT_SECONDS)
                .build();
    }

    /**
     * Perform a synchronous health check and return the result.
     * This is a wrapper around the async checkHealth method for internal use.
     */
    private HealthCheckResponse performHealthCheckSync(HealthCheckRequest request) {
        long startTime = System.currentTimeMillis();

        Map<String, ComponentHealth> componentHealthMap = checkRequestedComponents(request);
        HealthCheckResponse.ServiceStatus overallStatus = determineOverallStatus(componentHealthMap);
        long duration = System.currentTimeMillis() - startTime;

        return HealthCheckResponse.newBuilder()
                .setOverallStatus(overallStatus)
                .putAllComponents(componentHealthMap)
                .setTotalCheckDurationMs(duration)
                .build();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // 🚧 COMPONENT-SPECIFIC HEALTH CHECK IMPLEMENTATIONS
    // TODO: Replace these placeholder methods with your actual business logic!
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * 🗄️ DATABASE HEALTH CHECK
     * TODO: Implement actual database connectivity and query testing
     */
    private ComponentHealth checkDatabaseComponentHealth(ComponentHealth.Builder healthBuilder, boolean includeDependencies) {
        // ⚠️ PLACEHOLDER IMPLEMENTATION - Replace with real database checks!

        // Example of what you might implement:
        // 1. Test database connection: dataSource.getConnection()
        // 2. Run simple query: "SELECT 1"
        // 3. Measure response time
        // 4. Check connection pool status
        // 5. Verify disk space if local database

        boolean isAvailable = performDatabaseConnectionCheck();
        boolean isFunctional = performDatabaseQueryCheck();
        long responseTime = measureDatabaseResponseTime();

        healthBuilder
                .setAvailable(isAvailable)
                .setFunctional(isFunctional)
                .setResponseTimeMs(responseTime);

        if (!isAvailable) {
            healthBuilder.setErrorDetails("Database connection failed");
        }

        // Add dependency information if requested
        if (includeDependencies) {
            DependentComponent dbDependency = DependentComponent.newBuilder()
                    .setName("postgresql-primary")
                    .setAvailable(true)  // TODO: Check actual dependency
                    .setVersion("13.2")  // TODO: Get actual version
                    .setLastSuccessfulCheck(System.currentTimeMillis())
                    .build();

            healthBuilder.addDependencies(dbDependency);
        }

        return healthBuilder.build();
    }

    /**
     * 🗃️ CACHE HEALTH CHECK
     * TODO: Implement actual cache (Redis/Memcached) connectivity testing
     */
    private ComponentHealth checkCacheComponentHealth(ComponentHealth.Builder healthBuilder, boolean includeDependencies) {
        // ⚠️ PLACEHOLDER IMPLEMENTATION - Replace with real cache checks!

        // Example of what you might implement:
        // 1. Test cache connection: redisClient.ping()
        // 2. Test basic operations: SET/GET test key
        // 3. Measure response time
        // 4. Check memory usage
        // 5. Verify cluster health if using Redis Cluster

        boolean isAvailable = performCacheConnectionCheck();
        boolean isFunctional = performCacheOperationsCheck();
        long responseTime = measureCacheResponseTime();

        healthBuilder
                .setAvailable(isAvailable)
                .setFunctional(isFunctional)
                .setResponseTimeMs(responseTime);

        if (!isAvailable) {
            healthBuilder.setErrorDetails("Cache service unavailable");
        }

        if (includeDependencies) {
            DependentComponent cacheDependency = DependentComponent.newBuilder()
                    .setName("redis-cluster")
                    .setAvailable(true)  // TODO: Check actual dependency
                    .setVersion("6.2")   // TODO: Get actual version
                    .setLastSuccessfulCheck(System.currentTimeMillis())
                    .build();

            healthBuilder.addDependencies(cacheDependency);
        }

        return healthBuilder.build();
    }

    /**
     * 🌐 EXTERNAL API HEALTH CHECK
     * TODO: Implement actual external service dependency checks
     */
    private ComponentHealth checkExternalApiComponentHealth(ComponentHealth.Builder healthBuilder, boolean includeDependencies) {
        // ⚠️ PLACEHOLDER IMPLEMENTATION - Replace with real external API checks!

        // Example of what you might implement:
        // 1. Call external API health endpoint
        // 2. Test authentication/authorization
        // 3. Measure response time and error rates
        // 4. Check rate limiting status
        // 5. Verify SSL certificate validity

        boolean isAvailable = performExternalApiConnectionCheck();
        boolean isFunctional = performExternalApiCallCheck();
        long responseTime = measureExternalApiResponseTime();

        healthBuilder
                .setAvailable(isAvailable)
                .setFunctional(isFunctional)
                .setResponseTimeMs(responseTime);

        if (!isAvailable) {
            healthBuilder.setErrorDetails("External API is unreachable");
        }

        if (includeDependencies) {
            DependentComponent apiDependency = DependentComponent.newBuilder()
                    .setName("payment-service-api")
                    .setAvailable(true)      // TODO: Check actual dependency
                    .setVersion("2.1.0")     // TODO: Get actual version
                    .setLastSuccessfulCheck(System.currentTimeMillis())
                    .build();

            healthBuilder.addDependencies(apiDependency);
        }

        return healthBuilder.build();
    }

    /**
     * ❓ UNKNOWN COMPONENT HANDLER
     */
    private ComponentHealth checkUnknownComponentHealth(String componentName, ComponentHealth.Builder healthBuilder) {
        logger.warn("⚠️ Unknown component requested: {}", componentName);

        return healthBuilder
                .setAvailable(true)  // Assume healthy for unknown components
                .setFunctional(true)
                .setResponseTimeMs(1)
                .setLastCheckInfo("Unknown component - assumed healthy")
                .build();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // 🚧 PLACEHOLDER METHODS FOR ACTUAL HEALTH CHECK IMPLEMENTATIONS
    // TODO: Replace these with your real health checking logic!
    // ═══════════════════════════════════════════════════════════════════════════

    // 🗄️ Database Health Check Placeholders
    private boolean performDatabaseConnectionCheck() {
        // TODO: Replace with actual database connection test
        // Example: return dataSource.getConnection().isValid(5);
        logger.debug("🔍 Checking database connection...");
        return true;  // Placeholder - always returns healthy
    }

    private boolean performDatabaseQueryCheck() {
        // TODO: Replace with actual database query test
        // Example: executeQuery("SELECT 1").next();
        logger.debug("🔍 Testing database query...");
        return true;  // Placeholder
    }

    private long measureDatabaseResponseTime() {
        // TODO: Replace with actual response time measurement
        logger.debug("⏱️ Measuring database response time...");
        return 5L;  // Placeholder - 5ms
    }

    // 🗃️ Cache Health Check Placeholders
    private boolean performCacheConnectionCheck() {
        // TODO: Replace with actual cache connection test
        // Example: redisClient.ping().equals("PONG");
        logger.debug("🔍 Checking cache connection...");
        return true;  // Placeholder
    }

    private boolean performCacheOperationsCheck() {
        // TODO: Replace with actual cache operations test
        // Example: redisClient.set("healthcheck", "ok"); redisClient.get("healthcheck");
        logger.debug("🔍 Testing cache operations...");
        return true;  // Placeholder
    }

    private long measureCacheResponseTime() {
        // TODO: Replace with actual cache response time measurement
        logger.debug("⏱️ Measuring cache response time...");
        return 2L;  // Placeholder - 2ms
    }

    // 🌐 External API Health Check Placeholders
    private boolean performExternalApiConnectionCheck() {
        // TODO: Replace with actual external API connection test
        // Example: httpClient.get("https://api.example.com/health").getStatus() == 200;
        logger.debug("🔍 Checking external API connection...");
        return true;  // Placeholder
    }

    private boolean performExternalApiCallCheck() {
        // TODO: Replace with actual external API functionality test
        // Example: Call a test endpoint and verify expected response
        logger.debug("🔍 Testing external API call...");
        return true;  // Placeholder
    }

    private long measureExternalApiResponseTime() {
        // TODO: Replace with actual external API response time measurement
        logger.debug("⏱️ Measuring external API response time...");
        return 150L;  // Placeholder - 150ms
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // 🔄 LIFECYCLE AND CLEANUP METHODS
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Gracefully shutdown the health service and clean up resources.
     * Called when the gRPC server is shutting down.
     */
    public void shutdown() {
        logger.info("🔄 Shutting down health service...");

        // Shutdown the watch scheduler
        watchScheduler.shutdown();
        try {
            if (!watchScheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                logger.warn("⚠️ Watch scheduler did not terminate gracefully, forcing shutdown");
                watchScheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.warn("⚠️ Interrupted while waiting for scheduler shutdown");
            watchScheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // TODO: Add cleanup for any other resources (database connections, etc.)

        logger.info("✅ Health service shutdown completed");
    }
}