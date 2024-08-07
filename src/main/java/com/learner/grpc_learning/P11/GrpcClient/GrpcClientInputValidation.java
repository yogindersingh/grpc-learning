package com.learner.grpc_learning.P11.GrpcClient;

import com.learner.grpc_learning.P11.ClientInterceptorImpl;
import com.learner.grpc_learning.grpcclient.GrpcClient;
import com.learner.grpc_learning.proto.p11.BankRequest;
import com.learner.grpc_learning.proto.p11.BankResponse;
import com.learner.grpc_learning.proto.p11.BankServiceGrpc;
import com.learner.grpc_learning.proto.p11.WithdrawalRequest;
import io.grpc.CallCredentials;
import io.grpc.ClientInterceptor;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class GrpcClientInputValidation {


  public static void main(String[] args) {

    Logger logger = LoggerFactory.getLogger(GrpcClient.class);

    ManagedChannel channel =
        ManagedChannelBuilder.forAddress("localhost", 6565)
//            .executor(Executors.newCachedThreadPool())
            .intercept(new ClientInterceptorImpl()

                //example to pass metadata for every call
//                ,MetadataUtils.newAttachHeadersInterceptor(Metadata.Key.of("api-key"))
            ).usePlaintext()
            .build();

    BankServiceGrpc.BankServiceBlockingStub bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(channel);

    try {
      bankServiceBlockingStub.getAccountDetails(BankRequest.newBuilder().setAccountNumber(100).build());
    } catch (StatusRuntimeException e) {
      logger.error(e.getMessage());
    }

    try {
      bankServiceBlockingStub.getWithdrawalAmount(
          WithdrawalRequest.newBuilder().setAccountNumber(100).setAmount(1).build()).hasNext();
    } catch (StatusRuntimeException exception) {
      logger.error(exception.getMessage());
    }

    try {
      bankServiceBlockingStub.getWithdrawalAmount(
          WithdrawalRequest.newBuilder().setAccountNumber(1).setAmount(1000000).build()).hasNext();
    } catch (StatusRuntimeException e) {
      logger.error(e.getMessage());
    }
    bankServiceBlockingStub
        //example to send the user details too in the request
        //.withCallCredentials(new UserTokenCallCredentials("user1"))
        .getWithdrawalAmount(WithdrawalRequest.newBuilder().setAccountNumber(1).setAmount(1000).build()).hasNext();
    BankResponse resp = bankServiceBlockingStub.withDeadline(Deadline.after(2, TimeUnit.SECONDS)).getAccountDetails(
        BankRequest.newBuilder().setAccountNumber(1).build());

    logger.info(resp.toString());

    BankServiceGrpc.BankServiceStub bankServiceStub = BankServiceGrpc.newStub(channel);

    bankServiceStub.withCompression("gzip").withDeadline(Deadline.after(2, TimeUnit.SECONDS))
        .getAccountDetails(BankRequest.newBuilder().setAccountNumber(10).build(),
            new StreamObserver<BankResponse>() {

              @Override
              public void onNext(BankResponse bankResponse) {
                logger.info(bankResponse.toString());
              }

              @Override
              public void onError(Throwable throwable) {
                logger.error(throwable.getMessage());
              }

              @Override
              public void onCompleted() {
                logger.info("completed");
              }
            });

    try {
      Thread.sleep(10);
    } catch (Exception e) {
      e.printStackTrace();
    }

    channel.shutdown();
  }

  private static class UserTokenCallCredentials extends CallCredentials {

    private String token;

    public UserTokenCallCredentials(String token) {
      this.token = token;
    }

    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
      executor.execute(() -> {
        Metadata.Key<String> key = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
        Metadata metadata = new Metadata();
        metadata.put(key,token);
        metadataApplier.apply(metadata);
      });
    }
  }

}
