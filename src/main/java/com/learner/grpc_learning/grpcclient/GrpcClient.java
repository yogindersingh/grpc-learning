package com.learner.grpc_learning.grpcclient;

import com.learner.grpc_learning.proto.p9.BankRequest;
import com.learner.grpc_learning.proto.p9.BankResponse;
import com.learner.grpc_learning.proto.p9.BankServiceGrpc;
import com.learner.grpc_learning.proto.p9.WithdrawalRequest;
import com.learner.grpc_learning.proto.p9.WithdrawalResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class GrpcClient {

  public static void main(String[] args) throws InterruptedException {

    Logger logger = LoggerFactory.getLogger(GrpcClient.class);

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().build();

    BankServiceGrpc.BankServiceBlockingStub bankServiceStub = BankServiceGrpc.newBlockingStub(channel);
    BankRequest request = BankRequest.newBuilder().setAccountNumber(1).build();
    BankResponse response = bankServiceStub.getAccountDetails(
        request);
    logger.info("{}", response);

    BankServiceGrpc.BankServiceStub bankServiceStub1 = BankServiceGrpc.newStub(channel);

    ResponseObserver<BankResponse> responseObserver = new ResponseObserver<>(1);
    bankServiceStub1.getAccountDetails(request, responseObserver);
    responseObserver.await();

    logger.info("response from observer : {}",responseObserver.getList());


    WithdrawalRequest withdrawalRequ = WithdrawalRequest.newBuilder().setAmount(100).setAccountNumber(1).build();
    Iterator<WithdrawalResponse> resp = bankServiceStub.getWithdrawalAmount(withdrawalRequ);

    while (resp.hasNext())
    {
      WithdrawalResponse resp1 = resp.next();
      logger.info("{}", resp1);
    }

    ResponseObserverStream<WithdrawalResponse> responseObserver1 = new ResponseObserverStream<>(1);
    bankServiceStub1.getWithdrawalAmount(withdrawalRequ,responseObserver1);
    responseObserver1.await();
    channel.shutdown();
  }


}
