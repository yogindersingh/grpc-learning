package com.learner.grpc_learning.P9;

import com.google.protobuf.Empty;
import com.learner.grpc_learning.proto.p9.BankAccounts;
import com.learner.grpc_learning.proto.p9.BankRequest;
import com.learner.grpc_learning.proto.p9.BankResponse;
import com.learner.grpc_learning.proto.p9.BankServiceGrpc;
import com.learner.grpc_learning.proto.p9.DepositRequest;
import com.learner.grpc_learning.proto.p9.WithdrawalRequest;
import com.learner.grpc_learning.proto.p9.WithdrawalResponse;
import com.learner.grpc_learning.responseObservers.DepositRequestStreamObserver;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.Map;

public class BankServiceImpl extends BankServiceGrpc.BankServiceImplBase {

  private Map<Integer, Integer> map = Map.of(1, 1000, 2, 100, 3, 20000);

  @Override
  public void getAccountDetails(BankRequest request, StreamObserver<BankResponse> responseObserver) {
    BankResponse bankResponse =
        BankResponse.newBuilder().setAccountNumber(request.getAccountNumber())
            .setBalance(map.get(request.getAccountNumber()) * 10).build();
    responseObserver.onNext(bankResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getAccounts(Empty request, StreamObserver<BankAccounts> responseObserver) {
    List<BankResponse> responseLost = map.entrySet().stream()
        .map(e -> BankResponse.newBuilder().setAccountNumber(e.getKey()).setBalance(e.getValue()).build()).toList();
    responseObserver.onNext(BankAccounts.newBuilder().addAllBankresponse(responseLost).build());
    responseObserver.onCompleted();

  }

  @Override
  public void getWithdrawalAmount(WithdrawalRequest request, StreamObserver<WithdrawalResponse> responseObserver) {
    Integer amount = request.getAmount();
    Integer amountWidhrawed = 0;
    for (int i = 0; i < amount/10; i++) {
      amountWidhrawed=amountWidhrawed+amount/10;
      responseObserver.onNext(WithdrawalResponse.newBuilder().setAmount(amount/10).build());
    }
    responseObserver.onCompleted();
  }

  @Override
  public StreamObserver<DepositRequest> saveAmount(StreamObserver<BankResponse> responseObserver) {
    return new DepositRequestStreamObserver(responseObserver);
  }
}
