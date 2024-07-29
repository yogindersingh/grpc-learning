package com.learner.grpc_learning.P11;

import com.learner.grpc_learning.P11.Repository.AccountRepository;
import com.learner.grpc_learning.P11.Validator.RequestValidator;
import com.learner.grpc_learning.proto.p11.BankRequest;
import com.learner.grpc_learning.proto.p11.BankResponse;
import com.learner.grpc_learning.proto.p11.BankServiceGrpc;
import com.learner.grpc_learning.proto.p11.WithdrawalRequest;
import com.learner.grpc_learning.proto.p11.WithdrawalResponse;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;

public class BankServiceWithValidationImpl extends BankServiceGrpc.BankServiceImplBase {

  @Override
  public void getAccountDetails(BankRequest request,
                                StreamObserver<com.learner.grpc_learning.proto.p11.BankResponse> responseObserver) {
    RequestValidator.validateAccountId(request.getAccountNumber())
        .ifPresentOrElse(responseObserver::onError, () -> startFetchDetails(responseObserver, request));
  }

  void startFetchDetails(StreamObserver<BankResponse> responseObserver, BankRequest request) {
    com.learner.grpc_learning.proto.p11.BankResponse bankResponse =
        com.learner.grpc_learning.proto.p11.BankResponse.newBuilder().setAccountNumber(request.getAccountNumber())
            .setBalance(AccountRepository.getAccountDetails(request.getAccountNumber())).build();
    responseObserver.onNext(bankResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getWithdrawalAmount(WithdrawalRequest request,
                                  StreamObserver<com.learner.grpc_learning.proto.p11.WithdrawalResponse> responseObserver) {
    RequestValidator.validateAccountId(request.getAccountNumber())
        .or(() -> RequestValidator.validateAmountIsMultipleOfTen(request.getAmount()))
        .or(() -> RequestValidator.validateAmountIsLessThanBalance(request.getAmount(), request.getAccountNumber()))
        .ifPresentOrElse(responseObserver::onError, () -> startWithdrawaingMoney(request, responseObserver));
  }

  private static void startWithdrawaingMoney(WithdrawalRequest request,
                                             StreamObserver<WithdrawalResponse> responseObserver) {
    int amount = request.getAmount();
    Integer amountWidhrawed = 0;
    for (int i = 0; i <  10 && !Context.current().isCancelled(); i++) {
      amountWidhrawed = amountWidhrawed+amount / 10;
      responseObserver.onNext(WithdrawalResponse.newBuilder().setAmount(amount / 10).build());
    }
    AccountRepository.updateAccountDetails(request.getAccountNumber(),
        AccountRepository.getAccountDetails(request.getAccountNumber())-amountWidhrawed);
    responseObserver.onCompleted();
  }

}
