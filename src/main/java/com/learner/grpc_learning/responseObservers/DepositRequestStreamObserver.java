package com.learner.grpc_learning.responseObservers;

import com.learner.grpc_learning.proto.p9.BankResponse;
import  com.learner.grpc_learning.proto.p9.DepositRequest;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;

public class DepositRequestStreamObserver implements StreamObserver<DepositRequest> {

  private final StreamObserver<BankResponse> responseObserver;
  private Map<Integer, Integer> accountMap = new HashMap<>(Map.of(1, 1000, 2, 100, 3, 20000));
  Integer account;

  public DepositRequestStreamObserver(StreamObserver<BankResponse> responseObserver){
    this.responseObserver=responseObserver;
    this.accountMap=accountMap;
  }

  @Override
  public void onNext(DepositRequest depositRequest) {
    switch (depositRequest.getRequeCase()){
      case ACCOUNT_NUMBER -> account = depositRequest.getAccountNumber();
      case MONEY -> accountMap.put(account,
          accountMap.get(account)+depositRequest.getMoney());
    }
  }

  @Override
  public void onError(Throwable throwable) {
    account=null;
    responseObserver.onError(throwable);
  }

  @Override
  public void onCompleted() {
    responseObserver.onNext(BankResponse.newBuilder().setAccountNumber(account).setBalance(accountMap.get(account)).build());
    account=null;
    responseObserver.onCompleted();
  }

}
