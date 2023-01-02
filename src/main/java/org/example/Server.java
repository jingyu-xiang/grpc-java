package org.example;

import io.grpc.ServerBuilder;
import org.example.service.BankService;
import org.example.service.TransferService;

import java.io.IOException;

public class Server {

  public static void main(String[] args) throws IOException, InterruptedException {
    final io.grpc.Server server = ServerBuilder
        .forPort(6565)
        .addService(new BankService())
        .addService(new TransferService())
        .build();

    server.start();
    System.out.println("server running on port " + 6565);
    server.awaitTermination();
  }

}
