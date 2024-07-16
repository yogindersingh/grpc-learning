package com.learner.grpc_learning.grpcclient;

import io.grpc.stub.StreamObserver;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
public class ResponseObserver<T> implements StreamObserver<T> {

  private final List<T> list = new ArrayList<>();
  private Throwable throwable;
  private final CountDownLatch countDownLatch;

  private ResponseObserver(){
    countDownLatch = new CountDownLatch(1);
  }

  public ResponseObserver(int countDown) {
    this.countDownLatch = new CountDownLatch(countDown);
  }

  @Override
  public void onNext(T t) {
    log.info("response recieved : {}",t);
    countDownLatch.countDown();
  }

  @Override
  public void onError(Throwable throwable) {
    this.throwable = throwable;
    log.info("Error response recieved : ",throwable);
    countDownLatch.countDown();
  }

  @Override
  public void onCompleted() {
    log.info("Completed response recieved");
    countDownLatch.countDown();
  }

  public void await() throws InterruptedException {
    countDownLatch.await(1, TimeUnit.SECONDS);
  }

  public void await(long timeout, TimeUnit unit) throws InterruptedException {
    countDownLatch.await(timeout,unit);
  }
}
