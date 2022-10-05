package org.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.models.Balance;
import org.example.models.BalanceCheck;
import org.example.models.BankServiceGrpc;
import org.example.models.BankServiceGrpc.BankServiceBlockingStub;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class BankClientTest {

  private BankServiceBlockingStub bankServiceBlockingStub;

  @BeforeAll
  public void setup() {
    // server listener
    final ManagedChannel channel = ManagedChannelBuilder
        .forAddress("localhost", 6565)
        .usePlaintext()
        .build();

    // BankService stub
    this.bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(channel);
  }

  @Test
  public void balanceTest () {
    final BalanceCheck balanceCheckRequest = BalanceCheck.newBuilder()
        .setAccountNumber(2)
        .build();

    final Balance balance = this.bankServiceBlockingStub.getBalance(balanceCheckRequest);

    System.out.println(balance);
  }
}
