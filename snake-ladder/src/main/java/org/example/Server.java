package org.example;

import io.grpc.ServerBuilder;
import java.io.IOException;
import org.example.services.GameService;

public class Server {

  public static void main(String[] args) throws IOException, InterruptedException {
    final io.grpc.Server server = ServerBuilder.forPort(6565)
        .addService(new GameService())
        .build();

    server.start();
    System.out.println("server running on port " + 6565);
    server.awaitTermination();
  }
}