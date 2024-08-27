package com.learner.grpc_learning.P11.responseObserver;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

public class ServerResponseObserver implements ServerInterceptor {
  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata,
                                                               ServerCallHandler<ReqT, RespT> serverCallHandler) {

    // if needs to check for metadata in request
//    if (!metadata.containsKey(Metadata.Key.of("api-key", Metadata.ASCII_STRING_MARSHALLER)))
//    {
//      return new ServerCall.Listener<ReqT>() {};
//    }
    return serverCallHandler.startCall(serverCall, metadata);
  }
}
