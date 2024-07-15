package com.learner.grpc_learning.grpcclient;

import com.learner.grpc_learning.proto.p9.BankRequest;
import com.learner.grpc_learning.proto.p9.BankResponse;
import com.learner.grpc_learning.proto.p9.BankServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcClient {

  public static void main(String[] args) throws InterruptedException {

    Logger logger = LoggerFactory.getLogger(GrpcClient.class);

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().build();

    BankServiceGrpc.BankServiceBlockingStub bankServiceStub = BankServiceGrpc.newBlockingStub(channel);
    BankRequest request = BankRequest.newBuilder().setAccountNumber(12).build();
    BankResponse response = bankServiceStub.getAccountDetails(
        request);
    logger.info("{}", response);

    BankServiceGrpc.BankServiceStub bankServiceStub1 = BankServiceGrpc.newStub(channel);
    bankServiceStub1.getAccountDetails(request, new StreamObserver<>() {
      @Override
      public void onNext(BankResponse bankResponse) {
        logger.info("bankResponse : {}", bankResponse);
      }

      @Override
      public void onError(Throwable throwable) {
        logger.info("error : ", throwable);
      }

      @Override
      public void onCompleted() {
        logger.info("completed");
      }
    });
    Thread.sleep(1000);
    channel.shutdown();
  }
}
