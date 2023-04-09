package org.example.client.metadata;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.MetadataUtils;
import org.example.models.*;
import org.example.models.BankServiceGrpc.BankServiceBlockingStub;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class MetadataClientTest {

  private BankServiceBlockingStub bankServiceBlockingStub;

  @BeforeAll
  public void setup() {
    // server listener
    final ManagedChannel channel = ManagedChannelBuilder.forAddress(
        "localhost", 6565
        )
        .intercept(
            MetadataUtils.newAttachHeadersInterceptor(ClientConstant.getClientToken())
        )
        .usePlaintext()
        .build();

    // blocking BankService stub
    this.bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(channel);
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
      if (exception.getStatus().equals(Status.UNAUTHENTICATED)) {
        System.out.println("UNAUTHENTICATED!!!");
      }
    }
  }
}
