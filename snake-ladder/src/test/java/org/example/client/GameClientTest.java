package org.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import org.example.models.Die;
import org.example.models.GameServiceGrpc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class GameClientTest {

  private GameServiceGrpc.GameServiceStub gameServiceStub;

  @BeforeAll
  public void setup() {
    // server listener
    final ManagedChannel channel = ManagedChannelBuilder
        .forAddress("localhost", 6565)
        .usePlaintext()
        .build();

    // async BankService stub
    this.gameServiceStub = GameServiceGrpc.newStub(channel);
  }

  @Test
  public void clientGame() throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1); // test

    final GameStateResponseObserver gameStateResponseObserver =
        new GameStateResponseObserver(countDownLatch);

    final StreamObserver<Die> dieRequestObserver =
        gameServiceStub.roll(gameStateResponseObserver);

    gameStateResponseObserver.setDieRequestObserver(dieRequestObserver);

    // client start the game by rolling the 1st dice
    int dieVal = ThreadLocalRandom.current().nextInt(1, 7);
    final Die die = Die.newBuilder().setValue(dieVal).build();
    dieRequestObserver.onNext(die);

    countDownLatch.await(); // test
  }

}
