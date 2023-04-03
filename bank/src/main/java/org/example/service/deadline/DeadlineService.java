package org.example.service.deadline;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.example.db.AccountDatabase;
import org.example.models.*;
import org.example.service.bank_bussiness.request.DepositRequestObserver;

import java.util.concurrent.TimeUnit;

public class DeadlineService extends BankServiceGrpc.BankServiceImplBase {

  /**
   * deadline is applied on client side
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

    // simulate time-consuming calls
    Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);

    responseObserver.onNext(balance);
    responseObserver.onCompleted();
  }

  /**
   * server-side deadline achieved by Context
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
      // simulate time-consuming calls
      Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);

      if (!Context.current().isCancelled()) {
        responseObserver.onNext(Money.newBuilder().setValue(10).build());
        System.out.println("Money delivered");
        AccountDatabase.deductBalance(accountNumber, 10);
      } else {
        break;
      }
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
    return new DepositRequestObserver(responseObserver); // request receiver, receiving DepositRequest object(s)
  }

}
