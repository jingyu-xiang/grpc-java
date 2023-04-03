package org.example.service.deadline.server;

import io.grpc.ServerBuilder;
import org.example.service.bank_bussiness.BankService;
import org.example.service.bank_bussiness.TransferService;
import org.example.service.deadline.DeadlineService;

import java.io.IOException;

public class Server {

  public static void main(String[] args) throws IOException, InterruptedException {
    final io.grpc.Server server = ServerBuilder
        .forPort(6565)
        .addService(new DeadlineService())
        .build();

    server.start();
    System.out.println("server running on port " + 6565);
    server.awaitTermination();
  }

}
