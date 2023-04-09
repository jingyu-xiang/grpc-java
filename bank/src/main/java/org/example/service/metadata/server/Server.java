package org.example.service.metadata.server;

import io.grpc.ServerBuilder;
import org.example.service.metadata.AuthInterceptor;
import org.example.service.metadata.MetadataService;

import java.io.IOException;

public class Server {

  public static void main(String[] args) throws IOException, InterruptedException {
    final io.grpc.Server server = ServerBuilder
        .forPort(6565)
        .intercept(new AuthInterceptor())
        .addService(new MetadataService())
        .build();

    server.start();
    System.out.println("server running on port " + 6565);
    server.awaitTermination();
  }

}
