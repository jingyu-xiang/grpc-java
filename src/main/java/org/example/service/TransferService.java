package org.example.service;

import io.grpc.stub.StreamObserver;
import org.example.models.TransferRequest;
import org.example.models.TransferResponse;
import org.example.models.TransferServiceGrpc;
import org.example.service.request.TransferRequestObserver;

public class TransferService extends TransferServiceGrpc.TransferServiceImplBase {
  @Override
  public StreamObserver<TransferRequest> transfer(
      StreamObserver<TransferResponse> responseObserver // response emitter, emitting Balance object(s)
  ) {
    return new TransferRequestObserver(responseObserver); // request receiver, receiving DepositRequest object(s)
  }
}
