package org.example.server;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.example.db.AccountDatabase;
import org.example.models.Balance;
import org.example.models.BalanceCheckRequest;
import org.example.models.BankServiceGrpc;
import org.example.models.DepositRequest;
import org.example.models.Money;
import org.example.models.WithdrawRequest;
import org.example.server.request.DepositStreamRequest;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

  /**
   * unary demo
   * @param request BalanceCheckRequest
   * @param responseObserver StreamObserver<Balance>
   */
  @Override
  public void getBalance(
      BalanceCheckRequest request, // request
      StreamObserver<Balance> responseObserver // response emitter, emitting Balance object(s)
  ) {
    final int accountNumber = request.getAccountNumber();

    int balanceFromDb;

    try {
      balanceFromDb = AccountDatabase.getBalance(accountNumber);
    } catch (NullPointerException e) {
      balanceFromDb = -1;
    }

    final Balance balance = Balance.newBuilder()
        .setAmount(balanceFromDb)
        .build();

    responseObserver.onNext(balance);
    responseObserver.onCompleted();
  }

  /**
   * server stream demo
   * @param request WithdrawRequest
   * @param responseObserver StreamObserver<Money>
   */
  @Override
  public void withDraw(
      WithdrawRequest request, // request
      StreamObserver<Money> responseObserver // response emitter, emitting Money object(s)
  ) {
    int accountNumber = request.getAccountNumber();
    int amount = request.getAmount();
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

  /**
   *
   * @param responseObserver StreamObserver<Balance>
   * @return DepositStreamReq
   */
  @Override
  public StreamObserver<DepositRequest> deposit(
      StreamObserver<Balance> responseObserver // response emitter, emitting Balance object(s)
  ) {
    return new DepositStreamRequest(responseObserver); // request receiver, receiving DepositRequest object(s)
  }

}
