package org.example.client.bank_bussiness;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.client.bank_bussiness.TransferResponseObserver;
import org.example.models.TransferRequest;
import org.example.models.TransferServiceGrpc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferClientTest {

  private TransferServiceGrpc.TransferServiceStub transferServiceStub;

  @BeforeAll
  public void setup() {
    // server listener
    final ManagedChannel channel = ManagedChannelBuilder
        .forAddress("localhost", 6565)
        .usePlaintext()
        .build();

    // async BankService stub
    this.transferServiceStub = TransferServiceGrpc.newStub(channel);
  }

  @Test
  public void transfer() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);

    final TransferResponseObserver TransferResponseObserver = new TransferResponseObserver(latch);
    final StreamObserver<TransferRequest> transferRequestObserver =
        transferServiceStub.transfer(TransferResponseObserver);

    for (int i = 0; i < 100; i++) {
      final TransferRequest transferRequest = TransferRequest.newBuilder()
          .setFrom(ThreadLocalRandom.current().nextInt(1, 11))
          .setTo(ThreadLocalRandom.current().nextInt(1, 11))
          .setAmount(ThreadLocalRandom.current().nextInt(1, 21))
          .build();

      transferRequestObserver.onNext(transferRequest);
    }

    transferRequestObserver.onCompleted();

    latch.await();
  }

}
