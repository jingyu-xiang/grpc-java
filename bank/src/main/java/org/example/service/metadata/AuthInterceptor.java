package org.example.service.metadata;

import io.grpc.*;

import java.util.Objects;

public class AuthInterceptor implements ServerInterceptor {

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> serverCall,
      Metadata metadata,
      ServerCallHandler<ReqT, RespT> serverCallHandler
  ) {
    final String clientToken = metadata.get(ServerConstant.TOKEN);
    if(validate(clientToken)) {
      return serverCallHandler.startCall(serverCall, metadata);
    } else {
      serverCall.close(Status.UNAUTHENTICATED, metadata);
      // dummy return
      return new ServerCall.Listener<>() {};
    }
  }

  private boolean validate(String token) {
    System.out.println("HI");
    return Objects.nonNull(token) && token.equals("super secret token");
  }

}
