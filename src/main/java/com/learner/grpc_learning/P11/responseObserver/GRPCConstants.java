package com.learner.grpc_learning.P11.responseObserver;

import io.grpc.Context;

public class GRPCConstants {
  public static final Context.Key<String> USER_ROLE_KEY = Context.key("user-role");
}

