package org.example.client;

import io.grpc.stub.StreamObserver;
import org.example.models.Money;

import java.util.concurrent.CountDownLatch;

public class MoneyStreamRes implements StreamObserver<Money> {

  private final CountDownLatch countDownLatch;

  public MoneyStreamRes(CountDownLatch countDownLatch) {
    this.countDownLatch = countDownLatch;
  }

  @Override
  public void onNext(Money money) {
    System.out.println("received asynchronously " + money.getValue());
  }

  @Override
  public void onError(Throwable throwable) {
    System.out.println(throwable.getMessage());
    countDownLatch.countDown();
  }

  @Override
  public void onCompleted() {
    System.out.println("Transaction done");
    countDownLatch.countDown();
  }
}