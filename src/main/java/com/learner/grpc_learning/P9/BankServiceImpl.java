package com.learner.grpc_learning.P9;

import com.learner.grpc_learning.proto.p9.BankRequest;
import com.learner.grpc_learning.proto.p9.BankResponse;
import com.learner.grpc_learning.proto.p9.BankServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;

public class BankServiceImpl extends BankServiceGrpc.BankServiceImplBase {

  public static Map<Integer,Integer>  map=Map.of(12,23,14,10,120,123);


  @Override
  public void getAccountDetails(BankRequest request, StreamObserver<BankResponse> responseObserver) {
    BankResponse bankResponse =
        BankResponse.newBuilder().setAccountNumber(request.getAccountNumber()).setBalance(map.get(request.getAccountNumber())*10).build();
    responseObserver.onNext(bankResponse);
    responseObserver .onCompleted();
  }
}
