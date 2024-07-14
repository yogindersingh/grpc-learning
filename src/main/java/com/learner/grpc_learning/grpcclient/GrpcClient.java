package com.learner.grpc_learning.grpcclient;

import com.learner.grpc_learning.P9.BankServiceImpl;
import com.learner.grpc_learning.proto.p9.BankRequest;
import com.learner.grpc_learning.proto.p9.BankResponse;
import com.learner.grpc_learning.proto.p9.BankServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {

  public static void main(String[] args) {

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().build();

    BankServiceGrpc.BankServiceBlockingStub bankServiceStub=BankServiceGrpc.newBlockingStub(channel);
    BankResponse response = bankServiceStub.getAccountDetails(
        BankRequest.newBuilder().setAccountNumber(12).build());

    System.out.println(response);

    channel.shutdown();
  }
}
