package org.example.service.request;

import io.grpc.stub.StreamObserver;
import org.example.db.AccountDatabase;
import org.example.models.Account;
import org.example.models.TransferRequest;
import org.example.models.TransferResponse;
import org.example.models.TransferStatus;

public class TransferRequestObserver implements StreamObserver<TransferRequest> {

  private final StreamObserver<TransferResponse> transferResponseObserver;

  public TransferRequestObserver(StreamObserver<TransferResponse> transferResponseObserver) {
    this.transferResponseObserver = transferResponseObserver;
  }

  @Override
  public void onNext(TransferRequest transferRequest) {
    int from =  transferRequest.getFrom();
    int to = transferRequest.getTo();
    int amount = transferRequest.getAmount();

    int fromBalance = AccountDatabase.getBalance(from);

    TransferStatus status  = TransferStatus.FAILED;
    if (fromBalance >= amount && from != to) {
      AccountDatabase.deductBalance(from, amount);
      AccountDatabase.addBalance(to, amount);
      status = TransferStatus.SUCCESS;
    }

    final Account fromAccount = Account.newBuilder()
        .setAccountNumber(from)
        .setAmount(AccountDatabase.getBalance(from))
        .build();
    final Account toAccount = Account.newBuilder()
        .setAccountNumber(to)
        .setAmount(AccountDatabase.getBalance(to))
        .build();

    final TransferResponse transferResponse = TransferResponse.newBuilder()
        .setStatus(status)
        .addAccounts(fromAccount)
        .addAccounts(toAccount)
        .build();

    transferResponseObserver.onNext(transferResponse);
  }

  @Override
  public void onError(Throwable throwable) {
    System.out.println(throwable.getMessage());
  }

  @Override
  public void onCompleted() {
      AccountDatabase.printAccountsDetails();
      transferResponseObserver.onCompleted();
  }
}
