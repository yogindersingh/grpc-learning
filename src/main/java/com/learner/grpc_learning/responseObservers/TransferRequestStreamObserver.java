package com.learner.grpc_learning.responseObservers;

import com.learner.grpc_learning.proto.p9.BankResponse;
import com.learner.grpc_learning.proto.p9.TransferRequest;
import com.learner.grpc_learning.proto.p9.TransferResponse;
import com.learner.grpc_learning.proto.p9.TransferStatus;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;

public class TransferRequestStreamObserver implements StreamObserver<TransferRequest> {

  private final StreamObserver<TransferResponse> responseObserver;
  private Map<Integer, Integer> accountMap = new HashMap<>(Map.of(1, 1000, 2, 100, 3, 20000));


  public TransferRequestStreamObserver(StreamObserver<TransferResponse> responseObserver) {
    this.responseObserver = responseObserver;
  }

  @Override
  public void onNext(TransferRequest transferRequest) {
    int fromAccount = transferRequest.getFromAccount();
    int toAccount = transferRequest.getToAccount();
    int amount = transferRequest.getAmount();
    TransferResponse transferResponse=transferAmount(fromAccount,toAccount,amount);
    this.responseObserver.onNext(transferResponse);
  }

  private TransferResponse transferAmount(int fromAccount, int toAccount, int amount) {
    TransferResponse.Builder builder = TransferResponse.newBuilder();
    if(fromAccount != toAccount && accountMap.get(fromAccount)>=amount){
      accountMap.put(fromAccount,accountMap.get(fromAccount)-amount);
      accountMap.put(toAccount,accountMap.get(toAccount)+amount);
      builder.setTransferStatus(TransferStatus.COMPLETED);
    }
    builder.setFromAccount(BankResponse.newBuilder().setAccountNumber(fromAccount).setBalance(accountMap.get(fromAccount)).build());
    builder.setToAccount(BankResponse.newBuilder().setAccountNumber(toAccount).setBalance(accountMap.get(toAccount)).build());
    return builder.build();
  }

  @Override
  public void onError(Throwable throwable) {

  }

  @Override
  public void onCompleted() {
      this.responseObserver.onCompleted();
  }

}
