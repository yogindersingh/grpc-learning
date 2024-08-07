package com.learner.grpc_learning.P11.responseObserver;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;

import java.util.Set;

public class UserValidatorServerResponseObserver implements ServerInterceptor {

  public static final Set<String> userList= Set.of("user1", "user2", "user3", "user4", "user5", "user6");

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata,
                                                               ServerCallHandler<ReqT, RespT> serverCallHandler) {
    if (serverCall.getMethodDescriptor().getType().clientSendsOneMessage() || (metadata.get(Metadata.Key.of(
        "Authorization", Metadata.ASCII_STRING_MARSHALLER))!=null && userList.contains(metadata.get(Metadata.Key.of(
        "Authorization", Metadata.ASCII_STRING_MARSHALLER)))) )
    {
         return serverCallHandler.startCall(serverCall,metadata);
    }
    serverCall.close(Status.UNAUTHENTICATED, metadata);
    return null;
  }
}
