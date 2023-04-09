package org.example.service.metadata;

import io.grpc.stub.StreamObserver;
import org.example.db.AccountDatabase;
import org.example.models.*;

public class MetadataService extends BankServiceGrpc.BankServiceImplBase {

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

    responseObserver.onNext(balance);
    responseObserver.onCompleted();
  }

}
