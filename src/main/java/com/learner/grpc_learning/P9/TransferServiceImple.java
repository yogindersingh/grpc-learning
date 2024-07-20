package com.learner.grpc_learning.P9;

import com.learner.grpc_learning.proto.p9.TransferRequest;
import com.learner.grpc_learning.proto.p9.TransferResponse;
import com.learner.grpc_learning.proto.p9.TransferServiceGrpc;
import com.learner.grpc_learning.responseObservers.TransferRequestStreamObserver;
import io.grpc.stub.StreamObserver;

public class TransferServiceImple extends TransferServiceGrpc.TransferServiceImplBase {

  @Override
  public StreamObserver<TransferRequest> transferMoney(StreamObserver<TransferResponse> responseObserver) {
    return new TransferRequestStreamObserver(responseObserver);
  }
}
