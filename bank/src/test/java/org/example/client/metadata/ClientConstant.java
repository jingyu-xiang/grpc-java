package org.example.client.metadata;

import io.grpc.Metadata;

public class ClientConstant {

  public static Metadata getClientToken() {
    Metadata metadata = new Metadata();
    metadata.put(
        Metadata.Key.of("client-token", Metadata.ASCII_STRING_MARSHALLER),
        "super secret token"
    );
    return metadata;
  }

}
