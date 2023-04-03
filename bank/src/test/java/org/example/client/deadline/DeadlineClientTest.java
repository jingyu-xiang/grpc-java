package org.example.client.deadline;

import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.example.client.bank_bussiness.response.DepositResponseObserver;
import org.example.client.bank_bussiness.response.MoneyResponseObserver;
import org.example.models.*;
import org.example.models.BankServiceGrpc.BankServiceBlockingStub;
import org.example.models.BankServiceGrpc.BankServiceStub;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@TestInstance(Lifecycle.PER_CLASS)
public class DeadlineClientTest {

  private BankServiceBlockingStub bankServiceBlockingStub;
  
  private BankServiceStub bankServiceStub;

  @BeforeAll
  public void setup() {
    // server listener
    final ManagedChannel channel = ManagedChannelBuilder.forAddress(
        "localhost", 6565
        )
        .intercept(new DeadlineInterceptor())
        .usePlaintext()
        .build();

    // blocking BankService stub
    this.bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(channel);

    // async BankService stub
    this.bankServiceStub = BankServiceGrpc.newStub(channel);
  }

  @Test
  public void balanceTest() {
    final BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
        .setAccountNumber(3)
        .build();

    try {
      Balance balanceResponse = bankServiceBlockingStub
          .getBalance(balanceCheckRequest);
      System.out.println(balanceResponse);
    } catch (StatusRuntimeException exception) {
      System.out.println(exception.getMessage());
    }
  }

  @Test
  public void withdrawTest() {
    final WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder()
        .setAccountNumber(7)
        .setAmount(30)
        .build();

    try {
      final Iterator<Money> moneyIterator = bankServiceBlockingStub
          .withDraw(withdrawRequest);

      moneyIterator.forEachRemaining(
          money -> System.out.println("received: " + money.getValue())
      );
      System.out.println("done!");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void withdrawAsyncTest() throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1); // for test

    final WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder()
        .setAccountNumber(10)
        .setAmount(50)
        .build();

    bankServiceStub.withDraw(
        withdrawRequest, // request to send
        new MoneyResponseObserver(countDownLatch) // response receiver, receiving Money object(s)
    );

    countDownLatch.await(); // for test
  }

  @Test
  public void depositAsyncTest() throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1); // for test

    final DepositResponseObserver depositResponseObserver = new DepositResponseObserver(
        countDownLatch
    );
    final StreamObserver<DepositRequest> depositRequestObserver = bankServiceStub
        .deposit(depositResponseObserver);

    // deposit 3 times, each time 10 dollars
    for (int i = 0; i < 3; i++) {
      final DepositRequest depositRequest = DepositRequest.newBuilder()
          .setAccountNumber(8)
          .setAmount(10)
          .build();

      depositRequestObserver.onNext(depositRequest);
    }

    depositRequestObserver.onCompleted();

    countDownLatch.await(); // for test
  }
}
