package org.example.server;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.example.db.AccountDatabase;
import org.example.models.Balance;
import org.example.models.BalanceCheck;
import org.example.models.BankServiceGrpc;
import org.example.models.Money;
import org.example.models.WithdrawAction;

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

    final Balance balance = Balance.newBuilder()
        .setAmount(balanceFromDb)
        .build();

    // if all the field of a rpc message is the default value, then the message will show as an empty object
    System.out.println(balance.getAmount());
    responseObserver.onNext(balance);
    responseObserver.onCompleted();
  }

  @Override
  public void withDraw(
      WithdrawAction request,
      StreamObserver<Money> responseObserver
  ) {
    int accountNumber = request.getAccountNumber();
    int amount = request.getAmount(); // 10, 20 ,30
    int balance = AccountDatabase.getBalance(accountNumber);

    if (balance < amount) {
      final Status status = Status.FAILED_PRECONDITION.withDescription(
          "No enough money, you only have " + balance
      );
      responseObserver.onError(status.asException());
      return;
    }

    // all validations passed
    // each time deduct $10
    for (int i = 0; i < (amount / 10); i++) {
      final Money money = Money.newBuilder().setValue(10).build();
      responseObserver.onNext(money);
      AccountDatabase.deductBalance(accountNumber, 10);
    }

    responseObserver.onCompleted();
  }
}
