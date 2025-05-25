package com.shankarnarayanb.health;

import com.shankarnarayanb.health.v1.HealthServiceGrpc;
import com.shankarnarayanb.health.v1.HealthServiceProto.*;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * A simple client for testing the Health gRPC Service.
 * This demonstrates how to call each of the service methods.
 */
public class HealthClient {
    
    private static final Logger logger = LoggerFactory.getLogger(HealthClient.class);
    
    private final HealthServiceGrpc.HealthServiceBlockingStub blockingStub;
    private final HealthServiceGrpc.HealthServiceStub asyncStub;
    
    /**
     * Construct client for accessing server using the existing channel.
     */
    public HealthClient(Channel channel) {
        // 'blocking' stubs are used for synchronous calls
        blockingStub = HealthServiceGrpc.newBlockingStub(channel);
        // 'async' stubs are used for asynchronous calls (including streaming)
        asyncStub = HealthServiceGrpc.newStub(channel);
    }
    
    /**
     * Test the Ping method - simple liveness check.
     */
    public void testPing() {
        logger.info("🏓 Testing Ping method...");
        
        try {
            PingRequest request = PingRequest.newBuilder().build();
            
            PingResponse response = blockingStub.ping(request);
            
            logger.info("✅ Ping successful!");
            logger.info("   Success: {}", response.getSuccess());
            logger.info("   Version: {}", response.getVersion());
            logger.info("   Timestamp: {} ({})", response.getTimestampMs(), 
                       new java.util.Date(response.getTimestampMs()));
            
        } catch (StatusRuntimeException e) {
            logger.error("❌ Ping failed: {}", e.getStatus());
        }
    }
    
    /**
     * Test the CheckHealth method - comprehensive health check.
     */
    public void testHealthCheck() {
        logger.info("🏥 Testing CheckHealth method...");
        
        try {
            HealthCheckRequest request = HealthCheckRequest.newBuilder()
                    .addComponents("database")
                    .addComponents("cache")
                    .setIncludeDependencies(true)
                    .setTimeoutSeconds(10)
                    .build();
            
            HealthCheckResponse response = blockingStub.checkHealth(request);
            
            logger.info("✅ Health check completed!");
            logger.info("   Overall Status: {}", response.getOverallStatus());
            logger.info("   Duration: {}ms", response.getTotalCheckDurationMs());
            
            if (!response.getErrorMessage().isEmpty()) {
                logger.warn("   Error Message: {}", response.getErrorMessage());
            }
            
            logger.info("   Component Details:");
            response.getComponentsMap().forEach((name, health) -> {
                logger.info("     📊 {}: Available={}, Functional={}, ResponseTime={}ms",
                           name, health.getAvailable(), health.getFunctional(), 
                           health.getResponseTimeMs());
                
                if (!health.getErrorDetails().isEmpty()) {
                    logger.warn("        Error: {}", health.getErrorDetails());
                }
                
                // Log dependencies if present
                if (!health.getDependenciesList().isEmpty()) {
                    health.getDependenciesList().forEach(dep -> {
                        logger.info("        🔗 Dependency {}: Available={}, Version={}", 
                                   dep.getName(), dep.getAvailable(), dep.getVersion());
                    });
                }
            });
            
        } catch (StatusRuntimeException e) {
            logger.error("❌ Health check failed: {}", e.getStatus());
        }
    }
    
    /**
     * Test the WatchHealth method - streaming health updates.
     */
    public void testHealthWatch() {
        logger.info("👀 Testing WatchHealth method (streaming)...");
        
        try {
            HealthWatchRequest request = HealthWatchRequest.newBuilder()
                    .addComponents("database")
                    .addComponents("cache")
                    .setIntervalSeconds(5)  // Check every 5 seconds
                    .build();
            
            // Use a CountDownLatch to wait for a few streaming responses
            CountDownLatch finishLatch = new CountDownLatch(3); // Wait for 3 responses
            
            StreamObserver<HealthCheckResponse> responseObserver = new StreamObserver<HealthCheckResponse>() {
                @Override
                public void onNext(HealthCheckResponse response) {
                    logger.info("📡 Received health update:");
                    logger.info("   Overall Status: {}", response.getOverallStatus());
                    logger.info("   Timestamp: {}", new java.util.Date());
                    
                    response.getComponentsMap().forEach((name, health) -> {
                        logger.info("     {} - Available: {}, Functional: {}", 
                                   name, health.getAvailable(), health.getFunctional());
                    });
                    
                    finishLatch.countDown();
                }
                
                @Override
                public void onError(Throwable t) {
                    logger.error("❌ Health watch failed", t);
                    finishLatch.countDown();
                }
                
                @Override
                public void onCompleted() {
                    logger.info("✅ Health watch completed");
                    finishLatch.countDown();
                }
            };
            
            // Start the streaming call
            asyncStub.watchHealth(request, responseObserver);
            
            // Wait for responses (or timeout after 20 seconds)
            if (!finishLatch.await(20, TimeUnit.SECONDS)) {
                logger.info("⏰ Health watch test completed (timeout reached)");
            }
            
        } catch (Exception e) {
            logger.error("❌ Health watch failed", e);
        }
    }
    
    /**
     * Test server-side streaming using the blocking stub.
     */
    public void testHealthWatchBlocking() {
        logger.info("📺 Testing WatchHealth method (blocking iterator)...");
        
        try {
            HealthWatchRequest request = HealthWatchRequest.newBuilder()
                    .addComponents("database")
                    .setIntervalSeconds(3)  // Check every 3 seconds
                    .build();
            
            Iterator<HealthCheckResponse> responses = blockingStub.watchHealth(request);
            
            int count = 0;
            while (responses.hasNext() && count < 3) {  // Get 3 responses then stop
                HealthCheckResponse response = responses.next();
                count++;
                
                logger.info("📊 Health Update #{}", count);
                logger.info("   Status: {}", response.getOverallStatus());
                logger.info("   Components: {}", response.getComponentsMap().size());
                
                if (count >= 3) {
                    logger.info("✅ Received {} health updates, stopping test", count);
                    break;
                }
            }
            
        } catch (StatusRuntimeException e) {
            logger.error("❌ Health watch (blocking) failed: {}", e.getStatus());
        }
    }
    
    /**
     * Main method to run the client tests.
     */
    public static void main(String[] args) throws Exception {
        
        // Parse server details from command line or use defaults
        String target = "localhost:9090";  // Default target
        if (args.length > 0) {
            target = args[0];
        }
        
        logger.info("🚀 Starting Health gRPC Client");
        logger.info("   Target: {}", target);
        
        // Create a channel (connection) to the server
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                .usePlaintext()  // Disable TLS for simplicity
                .build();
        
        try {
            // Create the client
            HealthClient client = new HealthClient(channel);
            
            System.out.println();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                   🧪 TESTING HEALTH SERVICE                  ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();
            
            // Test all methods
            client.testPing();
            System.out.println();
            
            client.testHealthCheck();
            System.out.println();
            
            // Test streaming (choose one method)
            logger.info("Choose streaming test method: [1] Async or [2] Blocking Iterator");
            client.testHealthWatch();  // Async version
            
            System.out.println();
            logger.info("✅ All tests completed!");
            
        } finally {
            // Shut down the channel to allow the application to exit cleanly
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
