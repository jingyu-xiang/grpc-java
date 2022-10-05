package org.example.server;

import org.example.db.AccountDatabase;
import org.example.models.Balance;
import org.example.models.BalanceCheck;
import org.example.models.BankServiceGrpc;
import io.grpc.stub.StreamObserver;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

  @Override
  public void getBalance(
      BalanceCheck request, // request
      StreamObserver<Balance> responseObserver // response
  ) {
    final int accountNumber = request.getAccountNumber();

    Integer balanceFromDb;

    try {
      balanceFromDb = AccountDatabase.getBalance(accountNumber);
    } catch (NullPointerException e) {
      balanceFromDb = -1;
    }

    System.out.println(balanceFromDb);

    final Balance balance = Balance.newBuilder()
        .setAmount(balanceFromDb)
        .build();

    responseObserver.onNext(balance);
    responseObserver.onCompleted();
  }

}
