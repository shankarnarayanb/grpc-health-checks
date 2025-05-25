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
 * Implementation of the Health gRPC service.
 * This is where all your business logic goes!
 */
public class HealthServiceImpl extends HealthServiceGrpc.HealthServiceImplBase {
    
    private static final Logger logger = LoggerFactory.getLogger(HealthServiceImpl.class);
    private static final String SERVICE_VERSION = "1.0.0";
    
    // This executor is used for the streaming health watch functionality
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    
    /**
     * Simple ping method for liveness checks.
     * This should be fast and lightweight - just confirms the service is running.
     */
    @Override
    public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {
        logger.info("Received ping request");
        
        try {
            // Build the response
            PingResponse response = PingResponse.newBuilder()
                    .setSuccess(true)
                    .setVersion(SERVICE_VERSION)
                    .setTimestampMs(System.currentTimeMillis())
                    .build();
            
            // Send the response
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            logger.info("Ping response sent successfully");
            
        } catch (Exception e) {
            logger.error("Error processing ping request", e);
            responseObserver.onError(e);
        }
    }
    
    /**
     * Comprehensive health check method.
     * This is where you'd check databases, external services, etc.
     */
    @Override
    public void checkHealth(HealthCheckRequest request, StreamObserver<HealthCheckResponse> responseObserver) {
        long startTime = System.currentTimeMillis();
        logger.info("Received health check request with {} components", 
                   request.getComponentsList().size());
        
        try {
            // Create component health map
            Map<String, ComponentHealth> componentHealthMap = new HashMap<>();
            
            // If specific components are requested, check only those
            if (!request.getComponentsList().isEmpty()) {
                for (String component : request.getComponentsList()) {
                    componentHealthMap.put(component, checkComponentHealth(component, request.getIncludeDependencies()));
                }
            } else {
                // Check all default components
                componentHealthMap.put("database", checkComponentHealth("database", request.getIncludeDependencies()));
                componentHealthMap.put("cache", checkComponentHealth("cache", request.getIncludeDependencies()));
                componentHealthMap.put("external-api", checkComponentHealth("external-api", request.getIncludeDependencies()));
            }
            
            // Determine overall status based on component health
            HealthCheckResponse.ServiceStatus overallStatus = determineOverallStatus(componentHealthMap);
            
            long duration = System.currentTimeMillis() - startTime;
            
            // Build the comprehensive response
            HealthCheckResponse.Builder responseBuilder = HealthCheckResponse.newBuilder()
                    .setOverallStatus(overallStatus)
                    .putAllComponents(componentHealthMap)
                    .setTotalCheckDurationMs(duration);
            
            // Add error message if unhealthy
            if (overallStatus == HealthCheckResponse.ServiceStatus.UNHEALTHY) {
                responseBuilder.setErrorMessage("One or more critical components are unhealthy");
            }
            
            HealthCheckResponse response = responseBuilder.build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            logger.info("Health check completed in {}ms with status: {}", duration, overallStatus);
            
        } catch (Exception e) {
            logger.error("Error during health check", e);
            
            // Send error response
            HealthCheckResponse errorResponse = HealthCheckResponse.newBuilder()
                    .setOverallStatus(HealthCheckResponse.ServiceStatus.UNHEALTHY)
                    .setErrorMessage("Health check failed: " + e.getMessage())
                    .setTotalCheckDurationMs(System.currentTimeMillis() - startTime)
                    .build();
            
            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }
    
    /**
     * Streaming health watch - sends periodic health updates to the client.
     * This demonstrates gRPC server-side streaming.
     */
    @Override
    public void watchHealth(HealthWatchRequest request, StreamObserver<HealthCheckResponse> responseObserver) {
        logger.info("Starting health watch for {} components with {}s intervals", 
                   request.getComponentsList().size(), request.getIntervalSeconds());
        
        int intervalSeconds = request.getIntervalSeconds() > 0 ? request.getIntervalSeconds() : 30;
        
        // Schedule periodic health checks
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // Create a health check request based on watch request
                HealthCheckRequest healthRequest = HealthCheckRequest.newBuilder()
                        .addAllComponents(request.getComponentsList())
                        .setIncludeDependencies(true)
                        .setTimeoutSeconds(10)
                        .build();
                
                // Perform health check (reuse the logic from checkHealth)
                StreamObserver<HealthCheckResponse> tempObserver = new StreamObserver<HealthCheckResponse>() {
                    @Override
                    public void onNext(HealthCheckResponse value) {
                        // Forward the health check result to the watching client
                        responseObserver.onNext(value);
                    }
                    
                    @Override
                    public void onError(Throwable t) {
                        logger.error("Error in health watch", t);
                        responseObserver.onError(t);
                    }
                    
                    @Override
                    public void onCompleted() {
                        // Don't complete the main stream, keep watching
                    }
                };
                
                checkHealth(healthRequest, tempObserver);
                
            } catch (Exception e) {
                logger.error("Error in health watch periodic check", e);
                responseObserver.onError(e);
            }
        }, 0, intervalSeconds, TimeUnit.SECONDS);
    }
    
    /**
     * Check the health of a specific component.
     * THIS IS WHERE YOU IMPLEMENT YOUR BUSINESS LOGIC!
     */
    private ComponentHealth checkComponentHealth(String componentName, boolean includeDependencies) {
        logger.debug("Checking health of component: {}", componentName);
        
        try {
            // BUSINESS LOGIC GOES HERE
            // This is where you'd implement actual health checks like:
            // - Database connectivity tests
            // - Cache ping tests  
            // - External API health checks
            // - File system checks
            // - Memory/CPU usage checks
            
            ComponentHealth.Builder healthBuilder = ComponentHealth.newBuilder();
            
            switch (componentName.toLowerCase()) {
                case "database":
                    // Example: Check database connection
                    healthBuilder
                            .setAvailable(checkDatabaseConnection())
                            .setFunctional(testDatabaseQuery())
                            .setResponseTimeMs(measureDatabaseResponseTime());
                    
                    if (!healthBuilder.getAvailable()) {
                        healthBuilder.setErrorDetails("Database connection failed");
                    }
                    break;
                    
                case "cache":
                    // Example: Check cache (Redis, Memcached, etc.)
                    healthBuilder
                            .setAvailable(checkCacheConnection())
                            .setFunctional(testCacheOperations())
                            .setResponseTimeMs(measureCacheResponseTime());
                    
                    if (!healthBuilder.getAvailable()) {
                        healthBuilder.setErrorDetails("Cache service unavailable");
                    }
                    break;
                    
                case "external-api":
                    // Example: Check external service dependency
                    healthBuilder
                            .setAvailable(checkExternalApiConnection())
                            .setFunctional(testExternalApiCall())
                            .setResponseTimeMs(measureExternalApiResponseTime());
                    
                    if (!healthBuilder.getAvailable()) {
                        healthBuilder.setErrorDetails("External API is unreachable");
                    }
                    break;
                    
                default:
                    // Unknown component - assume it's healthy for demo
                    healthBuilder
                            .setAvailable(true)
                            .setFunctional(true)
                            .setResponseTimeMs(1)
                            .setLastCheckInfo("Unknown component - assumed healthy");
                    break;
            }
            
            // Add dependency checks if requested
            if (includeDependencies) {
                // Example: Add dependency information
                DependentComponent dependency = DependentComponent.newBuilder()
                        .setName(componentName + "-dependency")
                        .setAvailable(true)
                        .setVersion("1.0.0")
                        .setLastSuccessfulCheck(System.currentTimeMillis())
                        .build();
                
                healthBuilder.addDependencies(dependency);
            }
            
            return healthBuilder.build();
            
        } catch (Exception e) {
            logger.error("Error checking component health: {}", componentName, e);
            
            return ComponentHealth.newBuilder()
                    .setAvailable(false)
                    .setFunctional(false)
                    .setErrorDetails("Health check failed: " + e.getMessage())
                    .setResponseTimeMs(0)
                    .build();
        }
    }
    
    /**
     * Determine overall service status based on component health.
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
        
        if (allHealthy) {
            return HealthCheckResponse.ServiceStatus.HEALTHY;
        } else if (anyAvailable) {
            return HealthCheckResponse.ServiceStatus.DEGRADED;
        } else {
            return HealthCheckResponse.ServiceStatus.UNHEALTHY;
        }
    }
    
    // ====== PLACEHOLDER METHODS FOR YOUR BUSINESS LOGIC ======
    // Replace these with actual implementation for your system!
    
    private boolean checkDatabaseConnection() {
        // TODO: Implement actual database connection check
        logger.debug("Checking database connection...");
        return true; // Placeholder - always returns healthy
    }
    
    private boolean testDatabaseQuery() {
        // TODO: Run a simple query like "SELECT 1" to verify database functionality
        logger.debug("Testing database query...");
        return true; // Placeholder
    }
    
    private long measureDatabaseResponseTime() {
        // TODO: Measure actual response time of a test query
        return 5L; // Placeholder - 5ms
    }
    
    private boolean checkCacheConnection() {
        // TODO: Implement cache connection check (Redis, Memcached, etc.)
        logger.debug("Checking cache connection...");
        return true; // Placeholder
    }
    
    private boolean testCacheOperations() {
        // TODO: Test basic cache operations (get/set)
        logger.debug("Testing cache operations...");
        return true; // Placeholder
    }
    
    private long measureCacheResponseTime() {
        // TODO: Measure cache response time
        return 2L; // Placeholder - 2ms
    }
    
    private boolean checkExternalApiConnection() {
        // TODO: Check connectivity to external APIs your service depends on
        logger.debug("Checking external API connection...");
        return true; // Placeholder
    }
    
    private boolean testExternalApiCall() {
        // TODO: Make a test API call to verify functionality
        logger.debug("Testing external API call...");
        return true; // Placeholder
    }
    
    private long measureExternalApiResponseTime() {
        // TODO: Measure external API response time
        return 150L; // Placeholder - 150ms
    }
    
    /**
     * Clean up resources when the service is shut down.
     */
    public void shutdown() {
        logger.info("Shutting down health service...");
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
