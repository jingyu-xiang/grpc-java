package org.example.server;

import io.grpc.ServerBuilder;

import java.io.IOException;

public class Server {

  public static void main(String[] args) throws IOException, InterruptedException {
    final io.grpc.Server server = ServerBuilder
        .forPort(6565)
        .addService(new BankService())
        .build();

    server.start();
    System.out.println("server running on port " + 6565);
    server.awaitTermination();
  }

}
