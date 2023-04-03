package org.example.service.bank_bussiness.request;

import io.grpc.stub.StreamObserver;
import org.example.db.AccountDatabase;
import org.example.models.Balance;
import org.example.models.DepositRequest;

public class DepositRequestObserver implements StreamObserver<DepositRequest> {

  private final StreamObserver<Balance> responseObserver;

  private int accountBalance;

  public DepositRequestObserver(StreamObserver<Balance> responseObserver) {
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

    // trigger a response emitter to emit response to client
    responseObserver.onNext(balance);
    responseObserver.onCompleted();
  }
}
