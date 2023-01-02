package org.example.client.response;

import io.grpc.stub.StreamObserver;
import org.example.models.TransferResponse;

import java.util.concurrent.CountDownLatch;

public class TransferResponseObserver implements StreamObserver<TransferResponse> {
  private final CountDownLatch latch;

  public TransferResponseObserver(CountDownLatch latch) {
    this.latch = latch;
  }

  @Override
  public void onNext(TransferResponse transferResponse) {
    System.out.println(transferResponse.getStatus());
    transferResponse.getAccountsList().stream()
        .map(account -> account.getAccountNumber() + ":" + account.getAmount())
        .forEach(System.out::println);
    System.out.println("--------------------------");
  }

  @Override
  public void onError(Throwable throwable) {
    System.out.println(throwable.getMessage());
    latch.countDown();
  }

  @Override
  public void onCompleted() {
    System.out.println("All transfers done");
    latch.countDown();
  }
}
