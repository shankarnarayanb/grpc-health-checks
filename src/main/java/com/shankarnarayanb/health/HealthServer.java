package com.shankarnarayanb.health;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * gRPC Health Service Server
 * This class starts the gRPC server and handles lifecycle management.
 */
public class HealthServer {
    
    private static final Logger logger = LoggerFactory.getLogger(HealthServer.class);
    private static final int DEFAULT_PORT = 9090;
    
    private Server server;
    private HealthServiceImpl healthService;
    
    /**
     * Start the gRPC server on the specified port.
     */
    public void start(int port) throws IOException {
        logger.info("Starting Health gRPC Server on port {}", port);
        
        // Create the service implementation
        healthService = new HealthServiceImpl();
        
        // Build and start the server
        server = ServerBuilder.forPort(port)
                .addService(healthService)
                .build()
                .start();
        
        logger.info("✅ Health gRPC Server started successfully on port {}", port);
        logger.info("Health Service is ready to accept requests!");
        
        // Add shutdown hook to gracefully stop the server
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** Shutting down gRPC server since JVM is shutting down");
            try {
                HealthServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** Server shut down");
        }));
    }
    
    /**
     * Stop the server gracefully.
     */
    public void stop() throws InterruptedException {
        if (server != null) {
            logger.info("Stopping Health gRPC Server...");
            
            // Shutdown the health service first
            if (healthService != null) {
                healthService.shutdown();
            }
            
            // Shutdown the server
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
            logger.info("Health gRPC Server stopped successfully");
        }
    }
    
    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
    
    /**
     * Main method to start the server.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        
        // Parse port from command line arguments or use default
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                logger.warn("Invalid port number '{}', using default port {}", args[0], DEFAULT_PORT);
                port = DEFAULT_PORT;
            }
        }
        
        // Create and start the server
        final HealthServer server = new HealthServer();
        
        try {
            server.start(port);
            
            // Print some helpful information
            printStartupInfo(port);
            
            // Keep the server running
            server.blockUntilShutdown();
            
        } catch (IOException e) {
            logger.error("Failed to start Health gRPC Server", e);
            System.exit(1);
        } catch (InterruptedException e) {
            logger.info("Server interrupted");
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Print helpful startup information.
     */
    private static void printStartupInfo(int port) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    🏥 HEALTH gRPC SERVICE                    ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║  Status: ✅ RUNNING                                          ║");
        System.out.println(String.format("║  Port:   %d                                               ║", port));
        System.out.println("║  Proto:  health_service.proto                               ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║  Available Methods:                                          ║");
        System.out.println("║  • health.v1.HealthService/Ping                             ║");
        System.out.println("║  • health.v1.HealthService/CheckHealth                      ║");
        System.out.println("║  • health.v1.HealthService/WatchHealth                      ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║  Test with grpcurl:                                          ║");
        System.out.println(String.format("║  grpcurl -plaintext localhost:%d \\                       ║", port));
        System.out.println("║    health.v1.HealthService/Ping                             ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Press Ctrl+C to stop the server");
        System.out.println();
    }
}
