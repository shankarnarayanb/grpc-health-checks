package com.shankarnarayanb.health.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Core health check service definition
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.58.0)",
    comments = "Source: health_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class HealthServiceGrpc {

  private HealthServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "health.v1.HealthService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.shankarnarayanb.health.v1.HealthServiceProto.PingRequest,
      com.shankarnarayanb.health.v1.HealthServiceProto.PingResponse> getPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Ping",
      requestType = com.shankarnarayanb.health.v1.HealthServiceProto.PingRequest.class,
      responseType = com.shankarnarayanb.health.v1.HealthServiceProto.PingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.shankarnarayanb.health.v1.HealthServiceProto.PingRequest,
      com.shankarnarayanb.health.v1.HealthServiceProto.PingResponse> getPingMethod() {
    io.grpc.MethodDescriptor<com.shankarnarayanb.health.v1.HealthServiceProto.PingRequest, com.shankarnarayanb.health.v1.HealthServiceProto.PingResponse> getPingMethod;
    if ((getPingMethod = HealthServiceGrpc.getPingMethod) == null) {
      synchronized (HealthServiceGrpc.class) {
        if ((getPingMethod = HealthServiceGrpc.getPingMethod) == null) {
          HealthServiceGrpc.getPingMethod = getPingMethod =
              io.grpc.MethodDescriptor.<com.shankarnarayanb.health.v1.HealthServiceProto.PingRequest, com.shankarnarayanb.health.v1.HealthServiceProto.PingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.shankarnarayanb.health.v1.HealthServiceProto.PingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.shankarnarayanb.health.v1.HealthServiceProto.PingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new HealthServiceMethodDescriptorSupplier("Ping"))
              .build();
        }
      }
    }
    return getPingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckRequest,
      com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse> getCheckHealthMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CheckHealth",
      requestType = com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckRequest.class,
      responseType = com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckRequest,
      com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse> getCheckHealthMethod() {
    io.grpc.MethodDescriptor<com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckRequest, com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse> getCheckHealthMethod;
    if ((getCheckHealthMethod = HealthServiceGrpc.getCheckHealthMethod) == null) {
      synchronized (HealthServiceGrpc.class) {
        if ((getCheckHealthMethod = HealthServiceGrpc.getCheckHealthMethod) == null) {
          HealthServiceGrpc.getCheckHealthMethod = getCheckHealthMethod =
              io.grpc.MethodDescriptor.<com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckRequest, com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CheckHealth"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse.getDefaultInstance()))
              .setSchemaDescriptor(new HealthServiceMethodDescriptorSupplier("CheckHealth"))
              .build();
        }
      }
    }
    return getCheckHealthMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.shankarnarayanb.health.v1.HealthServiceProto.HealthWatchRequest,
      com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse> getWatchHealthMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "WatchHealth",
      requestType = com.shankarnarayanb.health.v1.HealthServiceProto.HealthWatchRequest.class,
      responseType = com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.shankarnarayanb.health.v1.HealthServiceProto.HealthWatchRequest,
      com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse> getWatchHealthMethod() {
    io.grpc.MethodDescriptor<com.shankarnarayanb.health.v1.HealthServiceProto.HealthWatchRequest, com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse> getWatchHealthMethod;
    if ((getWatchHealthMethod = HealthServiceGrpc.getWatchHealthMethod) == null) {
      synchronized (HealthServiceGrpc.class) {
        if ((getWatchHealthMethod = HealthServiceGrpc.getWatchHealthMethod) == null) {
          HealthServiceGrpc.getWatchHealthMethod = getWatchHealthMethod =
              io.grpc.MethodDescriptor.<com.shankarnarayanb.health.v1.HealthServiceProto.HealthWatchRequest, com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "WatchHealth"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.shankarnarayanb.health.v1.HealthServiceProto.HealthWatchRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse.getDefaultInstance()))
              .setSchemaDescriptor(new HealthServiceMethodDescriptorSupplier("WatchHealth"))
              .build();
        }
      }
    }
    return getWatchHealthMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static HealthServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HealthServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HealthServiceStub>() {
        @java.lang.Override
        public HealthServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HealthServiceStub(channel, callOptions);
        }
      };
    return HealthServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static HealthServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HealthServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HealthServiceBlockingStub>() {
        @java.lang.Override
        public HealthServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HealthServiceBlockingStub(channel, callOptions);
        }
      };
    return HealthServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static HealthServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HealthServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HealthServiceFutureStub>() {
        @java.lang.Override
        public HealthServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HealthServiceFutureStub(channel, callOptions);
        }
      };
    return HealthServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Core health check service definition
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Liveness check - fast response to determine if process is running
     * </pre>
     */
    default void ping(com.shankarnarayanb.health.v1.HealthServiceProto.PingRequest request,
        io.grpc.stub.StreamObserver<com.shankarnarayanb.health.v1.HealthServiceProto.PingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPingMethod(), responseObserver);
    }

    /**
     * <pre>
     * Readiness check - comprehensive validation including dependencies
     * </pre>
     */
    default void checkHealth(com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckRequest request,
        io.grpc.stub.StreamObserver<com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCheckHealthMethod(), responseObserver);
    }

    /**
     * <pre>
     * Stream health status updates (demonstrates streaming RPC)
     * </pre>
     */
    default void watchHealth(com.shankarnarayanb.health.v1.HealthServiceProto.HealthWatchRequest request,
        io.grpc.stub.StreamObserver<com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getWatchHealthMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service HealthService.
   * <pre>
   * Core health check service definition
   * </pre>
   */
  public static abstract class HealthServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return HealthServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service HealthService.
   * <pre>
   * Core health check service definition
   * </pre>
   */
  public static final class HealthServiceStub
      extends io.grpc.stub.AbstractAsyncStub<HealthServiceStub> {
    private HealthServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HealthServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HealthServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Liveness check - fast response to determine if process is running
     * </pre>
     */
    public void ping(com.shankarnarayanb.health.v1.HealthServiceProto.PingRequest request,
        io.grpc.stub.StreamObserver<com.shankarnarayanb.health.v1.HealthServiceProto.PingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Readiness check - comprehensive validation including dependencies
     * </pre>
     */
    public void checkHealth(com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckRequest request,
        io.grpc.stub.StreamObserver<com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCheckHealthMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Stream health status updates (demonstrates streaming RPC)
     * </pre>
     */
    public void watchHealth(com.shankarnarayanb.health.v1.HealthServiceProto.HealthWatchRequest request,
        io.grpc.stub.StreamObserver<com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getWatchHealthMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service HealthService.
   * <pre>
   * Core health check service definition
   * </pre>
   */
  public static final class HealthServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<HealthServiceBlockingStub> {
    private HealthServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HealthServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HealthServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Liveness check - fast response to determine if process is running
     * </pre>
     */
    public com.shankarnarayanb.health.v1.HealthServiceProto.PingResponse ping(com.shankarnarayanb.health.v1.HealthServiceProto.PingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Readiness check - comprehensive validation including dependencies
     * </pre>
     */
    public com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse checkHealth(com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCheckHealthMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Stream health status updates (demonstrates streaming RPC)
     * </pre>
     */
    public java.util.Iterator<com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse> watchHealth(
        com.shankarnarayanb.health.v1.HealthServiceProto.HealthWatchRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getWatchHealthMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service HealthService.
   * <pre>
   * Core health check service definition
   * </pre>
   */
  public static final class HealthServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<HealthServiceFutureStub> {
    private HealthServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HealthServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HealthServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Liveness check - fast response to determine if process is running
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.shankarnarayanb.health.v1.HealthServiceProto.PingResponse> ping(
        com.shankarnarayanb.health.v1.HealthServiceProto.PingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Readiness check - comprehensive validation including dependencies
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse> checkHealth(
        com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCheckHealthMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PING = 0;
  private static final int METHODID_CHECK_HEALTH = 1;
  private static final int METHODID_WATCH_HEALTH = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PING:
          serviceImpl.ping((com.shankarnarayanb.health.v1.HealthServiceProto.PingRequest) request,
              (io.grpc.stub.StreamObserver<com.shankarnarayanb.health.v1.HealthServiceProto.PingResponse>) responseObserver);
          break;
        case METHODID_CHECK_HEALTH:
          serviceImpl.checkHealth((com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckRequest) request,
              (io.grpc.stub.StreamObserver<com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse>) responseObserver);
          break;
        case METHODID_WATCH_HEALTH:
          serviceImpl.watchHealth((com.shankarnarayanb.health.v1.HealthServiceProto.HealthWatchRequest) request,
              (io.grpc.stub.StreamObserver<com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getPingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.shankarnarayanb.health.v1.HealthServiceProto.PingRequest,
              com.shankarnarayanb.health.v1.HealthServiceProto.PingResponse>(
                service, METHODID_PING)))
        .addMethod(
          getCheckHealthMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckRequest,
              com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse>(
                service, METHODID_CHECK_HEALTH)))
        .addMethod(
          getWatchHealthMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              com.shankarnarayanb.health.v1.HealthServiceProto.HealthWatchRequest,
              com.shankarnarayanb.health.v1.HealthServiceProto.HealthCheckResponse>(
                service, METHODID_WATCH_HEALTH)))
        .build();
  }

  private static abstract class HealthServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    HealthServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.shankarnarayanb.health.v1.HealthServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("HealthService");
    }
  }

  private static final class HealthServiceFileDescriptorSupplier
      extends HealthServiceBaseDescriptorSupplier {
    HealthServiceFileDescriptorSupplier() {}
  }

  private static final class HealthServiceMethodDescriptorSupplier
      extends HealthServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    HealthServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (HealthServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new HealthServiceFileDescriptorSupplier())
              .addMethod(getPingMethod())
              .addMethod(getCheckHealthMethod())
              .addMethod(getWatchHealthMethod())
              .build();
        }
      }
    }
    return result;
  }
}
