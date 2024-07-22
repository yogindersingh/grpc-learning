package com.learner.grpc_learning.P10;

import com.learner.grpc_learning.proto.p10.FlowControlServiceGrpc;
import com.learner.grpc_learning.proto.p10.input;
import com.learner.grpc_learning.proto.p10.output;
import io.grpc.stub.StreamObserver;

public class FlowControlImpl extends FlowControlServiceGrpc.FlowControlServiceImplBase {
  @Override
  public StreamObserver<input> getMessages(StreamObserver<output> responseObserver) {
    return new ResponseObserver(responseObserver);
  }

  private static class ResponseObserver implements StreamObserver<input> {

    private StreamObserver<output> responseObserver;
    private int emitted=0;

    public ResponseObserver(StreamObserver<output> responseObserver) {
      this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(input input) {
      int count=0;
      for(int i=emitted+1;i<100;i++)
      {
        if(count<input.getSize()) {
          responseObserver.onNext(output.newBuilder().setValue(i).build());
        } else {
          break;
        }
        count++;
      }
      emitted=emitted+input.getSize();
      if(emitted>=100)
      {
        this.onCompleted();
      }
    }

    @Override
    public void onError(Throwable throwable) {
      responseObserver.onError(throwable);
    }

    @Override
    public void onCompleted() {
      responseObserver.onCompleted();
    }
  }


}
