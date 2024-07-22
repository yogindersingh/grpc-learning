package com.learner.grpc_learning.grpcclient;

import com.learner.grpc_learning.proto.p10.FlowControlServiceGrpc;
import com.learner.grpc_learning.proto.p10.input;
import com.learner.grpc_learning.proto.p10.output;
import com.learner.grpc_learning.proto.p9.BankRequest;
import com.learner.grpc_learning.proto.p9.BankResponse;
import com.learner.grpc_learning.proto.p9.BankServiceGrpc;
import com.learner.grpc_learning.proto.p9.DepositRequest;
import com.learner.grpc_learning.proto.p9.TransferRequest;
import com.learner.grpc_learning.proto.p9.TransferResponse;
import com.learner.grpc_learning.proto.p9.TransferServiceGrpc;
import com.learner.grpc_learning.proto.p9.WithdrawalRequest;
import com.learner.grpc_learning.proto.p9.WithdrawalResponse;
import com.learner.grpc_learning.responseObservers.ResponseObserver;
import com.learner.grpc_learning.responseObservers.ResponseObserverStream;
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

    logger.info("response from observer : {}", responseObserver.getList());


    WithdrawalRequest withdrawalRequ = WithdrawalRequest.newBuilder().setAmount(100).setAccountNumber(1).build();
    Iterator<WithdrawalResponse> resp = bankServiceStub.getWithdrawalAmount(withdrawalRequ);

    while (resp.hasNext()) {
      WithdrawalResponse resp1 = resp.next();
      logger.info("{}", resp1);
    }

    ResponseObserverStream<WithdrawalResponse> responseObserver1 = new ResponseObserverStream<>(1);
    bankServiceStub1.getWithdrawalAmount(withdrawalRequ, responseObserver1);
    responseObserver1.await();

    StreamObserver<DepositRequest> depositStreamObserver = bankServiceStub1.saveAmount(
        new StreamObserver<BankResponse>() {
          @Override
          public void onNext(BankResponse bankResponse) {
            logger.info("{}", bankResponse);
          }

          @Override
          public void onError(Throwable throwable) {
            logger.error("Error : ", throwable);
          }

          @Override
          public void onCompleted() {
            logger.info("completed");
          }
        });

    depositStreamObserver.onNext(DepositRequest.newBuilder().setAccountNumber(1).build());
    depositStreamObserver.onNext(DepositRequest.newBuilder().setMoney(3).build());
    depositStreamObserver.onNext(DepositRequest.newBuilder().setMoney(3).build());
    depositStreamObserver.onCompleted();

    TransferServiceGrpc.TransferServiceStub transferServiceStub = TransferServiceGrpc.newStub(channel);
    ResponseObserverStream<TransferResponse> responseObserver2 = new ResponseObserverStream<>(1);
    StreamObserver<TransferRequest> transferRequst = transferServiceStub.transferMoney(
        responseObserver2);
    transferRequst.onNext(TransferRequest.newBuilder().setFromAccount(1).setToAccount(1).setAmount(10).build());
    transferRequst.onNext(TransferRequest.newBuilder().setFromAccount(1).setToAccount(2).setAmount(10).build());
    transferRequst.onNext(TransferRequest.newBuilder().setFromAccount(1).setToAccount(2).setAmount(10000).build());
    transferRequst.onNext(TransferRequest.newBuilder().setFromAccount(2).setToAccount(3).setAmount(100).build());
    transferRequst.onCompleted();
    responseObserver2.await();

    FlowControlServiceGrpc.FlowControlServiceStub flowControlServiceGrpc = FlowControlServiceGrpc.newStub(channel);
    StreamObserver<input> requestStream = flowControlServiceGrpc.getMessages(
        new StreamObserver<output>() {
          @Override
          public void onNext(output output) {
            logger.info("response recieved - {}", output);
          }

          @Override
          public void onError(Throwable throwable) {
            logger.error("Error : ", throwable);
          }

          @Override
          public void onCompleted() {
            logger.info("completed");
          }
        });

    requestStream.onNext(input.newBuilder().setSize(10).build());

    Thread.sleep(500);
    requestStream.onNext(input.newBuilder().setSize(30).build());
    Thread.sleep(500);
    requestStream.onNext(input.newBuilder().setSize(30).build());
    Thread.sleep(500);
    requestStream.onNext(input.newBuilder().setSize(30).build());
    Thread.sleep(500);
    requestStream.onNext(input.newBuilder().setSize(30).build());

    Thread.sleep(1000);

    channel.shutdown();
  }


}
