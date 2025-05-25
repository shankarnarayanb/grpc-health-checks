# Health gRPC Service

A comprehensive gRPC-based health check service built with Java and Protocol Buffers. This service provides multiple health check methods including simple ping, detailed health assessment, and streaming health monitoring.

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+

### Build and Run

1. **Compile the project:**
   ```bash
   mvn clean compile
   ```

2. **Start the server:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.shankarnarayanb.health.HealthServer"
   ```
   
   Or with custom port:
   ```bash
   mvn exec:java -Dexec.mainClass="com.shankarnarayanb.health.HealthServer" -Dexec.args="8080"
   ```

3. **Test the service:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.shankarnarayanb.health.HealthClient"
   ```

## 📡 Service Methods

### 1. Ping - Simple Liveness Check
- **Purpose**: Fast health check to verify the service is running
- **Method**: `health.v1.HealthService/Ping`
- **Use Case**: Load balancer health checks, basic monitoring

### 2. CheckHealth - Comprehensive Health Assessment  
- **Purpose**: Detailed health check including dependencies
- **Method**: `health.v1.HealthService/CheckHealth`  
- **Use Case**: Deployment readiness, detailed monitoring

### 3. WatchHealth - Streaming Health Monitoring
- **Purpose**: Continuous health status updates via streaming
- **Method**: `health.v1.HealthService/WatchHealth`
- **Use Case**: Real-time monitoring dashboards, alerting systems

## 🛠️ Implementing Your Business Logic

The main file where you'll implement your health check logic is:
**`src/main/java/com/shankarnarayanb/health/HealthServiceImpl.java`**

### Key Areas to Customize:

1. **Database Health Check** (`checkDatabaseConnection()`)
   ```java
   private boolean checkDatabaseConnection() {
       // TODO: Replace with your actual database connection test
       // Example: Test connection pool, run "SELECT 1", etc.
       return true;
   }
   ```

2. **Cache Health Check** (`checkCacheConnection()`)
   ```java
   private boolean checkCacheConnection() {
       // TODO: Check Redis, Memcached, or your cache layer
       // Example: Ping cache, test get/set operations
       return true;
   }
   ```

3. **External API Health Check** (`checkExternalApiConnection()`)
   ```java
   private boolean checkExternalApiConnection() {
       // TODO: Check external services your app depends on
       // Example: HTTP health endpoints, API key validation
       return true;
   }
   ```

### Adding New Components

To add new health check components:

1. **Add a new case** in `checkComponentHealth()` method:
   ```java
   case "your-component":
       healthBuilder
           .setAvailable(checkYourComponent())
           .setFunctional(testYourComponentFunctionality())
           .setResponseTimeMs(measureYourComponentResponseTime());
       break;
   ```

2. **Implement the check methods**:
   ```java
   private boolean checkYourComponent() {
       // Your component health check logic
       return true;
   }
   ```

## 🧪 Testing Your Service

### Using the Built-in Client
```bash
mvn exec:java -Dexec.mainClass="com.shankarnarayanb.health.HealthClient"
```

### Using grpcurl (if installed)
```bash
# Simple ping
grpcurl -plaintext localhost:9090 health.v1.HealthService/Ping

# Health check with specific components
grpcurl -plaintext -d '{"components":["database","cache"],"include_dependencies":true}' \
  localhost:9090 health.v1.HealthService/CheckHealth

# Streaming health watch
grpcurl -plaintext -d '{"components":["database"],"interval_seconds":5}' \
  localhost:9090 health.v1.HealthService/WatchHealth
```

### Using BloomRPC or gRPC GUI Tools
1. Load the proto file: `proto/health_service.proto`
2. Connect to: `localhost:9090`
3. Call methods with appropriate request payloads

## 📁 Project Structure

```
health-grpc-service/
├── proto/
│   └── health_service.proto          # Protocol Buffer definition
├── generated-sources/
│   └── main/java/                    # Auto-generated gRPC/Protobuf classes
├── src/main/java/com/shankarnarayanb/health/
│   ├── HealthServiceImpl.java        # 🔧 Business logic implementation
│   ├── HealthServer.java             # gRPC server
│   └── HealthClient.java             # Test client
├── pom.xml                           # Maven configuration
└── README.md                         # This file
```

## 🔄 gRPC vs REST - Key Differences

Since you're familiar with REST APIs, here are the key differences:

| Aspect | REST | gRPC |
|--------|------|------|
| **Protocol** | HTTP/JSON | HTTP/2 + Protocol Buffers |
| **Performance** | Text-based, larger payloads | Binary, compact |
| **Streaming** | Limited (SSE, WebSocket) | Built-in bidirectional streaming |
| **Type Safety** | Runtime validation | Compile-time contracts |
| **Browser Support** | Native | Requires grpc-web |
| **Human Readable** | JSON is readable | Binary format |

## 🚀 Next Steps

1. **Customize Health Checks**: Replace placeholder methods with your actual health check logic
2. **Add Authentication**: Implement gRPC interceptors for authentication if needed
3. **Add Metrics**: Integrate with monitoring systems (Prometheus, etc.)
4. **Add Persistence**: Store health check history if needed
5. **Containerize**: Create Docker images for deployment
6. **Add Tests**: Write unit and integration tests

## 📖 Learning Resources

- [gRPC Java Documentation](https://grpc.io/docs/languages/java/)
- [Protocol Buffers Guide](https://developers.google.com/protocol-buffers/docs/overview)
- [gRPC Concepts](https://grpc.io/docs/what-is-grpc/core-concepts/)

## 🎯 Common Use Cases for Health Services

- **Kubernetes Readiness/Liveness Probes**
- **Load Balancer Health Checks**  
- **Service Mesh Health Monitoring**
- **CI/CD Pipeline Health Validation**
- **Microservice Dependency Monitoring**
- **Auto-scaling Health Metrics**

---

Happy coding! 🚀 

Start by running the server, then the client, and explore the different health check methods. Remember to implement your actual business logic in the `HealthServiceImpl.java` file.
