package org.example.client.response;

import io.grpc.stub.StreamObserver;
import org.example.models.Balance;

import java.util.concurrent.CountDownLatch;

public class BalanceStreamResponse implements StreamObserver<Balance> {

  private final CountDownLatch latch;

  public BalanceStreamResponse(CountDownLatch latch) {
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
