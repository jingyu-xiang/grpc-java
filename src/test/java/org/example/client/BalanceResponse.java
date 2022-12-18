package org.example.client;

import io.grpc.stub.StreamObserver;
import org.example.models.Balance;

import java.util.concurrent.CountDownLatch;

public class BalanceResponse implements StreamObserver<Balance> {

  private final CountDownLatch latch;

  public BalanceResponse(CountDownLatch latch) {
    this.latch = latch;
  }

  @Override
  public void onNext(Balance balance) {
    System.out.println("Final Balance: " + balance.getAmount());
  }

  @Override
  public void onError(Throwable throwable) {
    System.out.println(throwable.getMessage());
    latch.countDown();
  }

  @Override
  public void onCompleted() {
    System.out.println("Deposit done");
    latch.countDown();
  }
}
