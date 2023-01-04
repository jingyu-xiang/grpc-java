package com.example.service;

import com.example.model.user.UserGenreUpdateRequest;
import com.example.model.user.UserResponse;
import com.example.model.user.UserResponse.Builder;
import com.example.model.user.UserSearchRequest;
import com.example.model.common.Genre;
import com.example.model.user.UserServiceGrpc.UserServiceImplBase;
import com.example.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import javax.transaction.Transactional;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class UserService extends UserServiceImplBase {

  @Autowired
  private UserRepository userRepository;

  @Override
  public void getUserGenre(
      UserSearchRequest request,
      StreamObserver<UserResponse> responseObserver
  ) {
    final Builder responseBuilder = UserResponse.newBuilder();

    userRepository.findById(request.getLoginId())
        .ifPresent(user -> responseBuilder
            .setName(user.getName())
            .setLoginId(user.getLoginId())
            .setGenre(Genre.valueOf(user.getGenre().toUpperCase())));

    responseObserver.onNext(responseBuilder.build());
    responseObserver.onCompleted();
  }

  @Override
  @Transactional
  public void updateUserGenre(
      UserGenreUpdateRequest request,
      StreamObserver<UserResponse> responseObserver
  ) {
    final Builder responseBuilder = UserResponse.newBuilder();

    userRepository.findById(request.getLoginId())
        .ifPresent(user -> {
          user.setGenre(request.getGenre().toString());
          responseBuilder
              .setName(user.getName())
              .setLoginId(user.getLoginId())
              .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
        });

    responseObserver.onNext(responseBuilder.build());
    responseObserver.onCompleted();
  }
}
