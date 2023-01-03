package org.example.services;

import io.grpc.stub.StreamObserver;
import org.example.models.Die;
import org.example.models.GameServiceGrpc;
import org.example.models.GameState;
import org.example.models.Player;

public class GameService extends GameServiceGrpc.GameServiceImplBase {

  /**
   * receive Die stream to generate GameState Stream
   * @param responseObserver StreamObserver<GameState>
   * @return StreamObserver<Die>
   */
  @Override
  public StreamObserver<Die> roll(StreamObserver<GameState> responseObserver) {
    Player client = Player.newBuilder()
        .setName("client")
        .setPosition(0)
        .build();

    Player server = Player.newBuilder()
        .setName("server")
        .setPosition(0)
        .build();

    return new DieRequestObserver(client, server, responseObserver);
  }

}
