package org.example.server;

import io.grpc.stub.StreamObserver;
import org.example.db.AccountDatabase;
import org.example.models.Balance;
import org.example.models.DepositRequest;

public class DepositStreamReq implements StreamObserver<DepositRequest> {

  private final StreamObserver<Balance> responseObserver;

  private int accountBalance;

  public DepositStreamReq(StreamObserver<Balance> responseObserver) {
    this.responseObserver = responseObserver;
  }

  @Override
  public void onNext(DepositRequest depositRequest) {
    int accountNumber = depositRequest.getAccountNumber();
    int amount = depositRequest.getAmount();
    accountBalance = AccountDatabase.addBalance(accountNumber, amount);
  }

  @Override
  public void onError(Throwable throwable) {
    System.out.println(throwable.getMessage());
  }

  @Override
  public void onCompleted() {
    final Balance balance = Balance.newBuilder().setAmount(accountBalance).build();
    responseObserver.onNext(balance);
    responseObserver.onCompleted();
  }
}
