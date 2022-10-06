package org.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import org.example.models.Balance;
import org.example.models.BalanceCheck;
import org.example.models.BankServiceGrpc;
import org.example.models.BankServiceGrpc.BankServiceBlockingStub;
import org.example.models.BankServiceGrpc.BankServiceStub;
import org.example.models.Money;
import org.example.models.WithdrawAction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class BankClientTest {

  private BankServiceBlockingStub bankServiceBlockingStub;

  private BankServiceStub bankServiceStub;

  @BeforeAll
  public void setup() {
    // server listener
    final ManagedChannel channel = ManagedChannelBuilder
        .forAddress("localhost", 6565)
        .usePlaintext()
        .build();

    // blocking BankService stub
    this.bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(channel);

    // async BankService stub
    this.bankServiceStub = BankServiceGrpc.newStub(channel);
  }

  @Test
  public void balanceTest() {
    final BalanceCheck balanceCheckRequest = BalanceCheck.newBuilder()
        .setAccountNumber(2)
        .build();

   this.bankServiceBlockingStub.getBalance(balanceCheckRequest);
  }

  @Test
  public void withdrawTest() {
    final WithdrawAction withdrawActionReq = WithdrawAction.newBuilder()
        .setAccountNumber(4)
        .setAmount(40)
        .build();

    final Iterator<Money> moneyIterator = this.bankServiceBlockingStub
        .withDraw(withdrawActionReq);

    moneyIterator.forEachRemaining(
        money -> System.out.println("received: " + money.getValue())
    );

    System.out.println("done!");
  }

  @Test
  public void withdrawAsyncTest() throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1); // for test

    final WithdrawAction withdrawReq = WithdrawAction.newBuilder()
        .setAccountNumber(10)
        .setAmount(50)
        .build();

    this.bankServiceStub.withDraw(
        withdrawReq,
        new MoneyStreamingResponse(countDownLatch)
    );

    countDownLatch.await(); // for test
  }
}
