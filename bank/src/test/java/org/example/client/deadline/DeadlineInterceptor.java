package org.example.client.deadline;

import io.grpc.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DeadlineInterceptor implements ClientInterceptor {

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
      MethodDescriptor<ReqT, RespT> methodDescriptor,
      CallOptions callOptions,
      Channel channel
  ) {
    // ddl maybe set in client rpc call, in this case, use client ddl, else use default ddl
    final Deadline deadline = callOptions.getDeadline();
    if (Objects.isNull(deadline)) {
      callOptions = callOptions.withDeadline(Deadline.after(4, TimeUnit.SECONDS));
    }

    // default return
    return channel.newCall(methodDescriptor, callOptions);
  }

}
