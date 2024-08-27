package com.learner.grpc_learning.P11;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.Deadline;
import io.grpc.MethodDescriptor;

import java.util.concurrent.TimeUnit;

public class ClientInterceptorImpl implements ClientInterceptor {

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
                                                             CallOptions callOptions, Channel channel) {
    if (callOptions.getDeadline() == null) {
      callOptions = callOptions.withDeadline(Deadline.after(10, TimeUnit.SECONDS));
    }
    return channel.newCall(methodDescriptor, callOptions);
  }
}

