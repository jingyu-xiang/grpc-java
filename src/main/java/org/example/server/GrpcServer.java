package org.example.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    final Server server = ServerBuilder
        .forPort(5555)
        .addService(new BankService())
        .build();

    server.start();
    System.out.println("server running on port " + 6565);
    server.awaitTermination();
  }
}
