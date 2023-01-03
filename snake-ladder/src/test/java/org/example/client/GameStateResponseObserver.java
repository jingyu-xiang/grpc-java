package org.example.client;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.example.models.Die;
import org.example.models.GameState;
import org.example.models.Player;

public class GameStateResponseObserver implements StreamObserver<GameState> {

  private final CountDownLatch latch;

  private StreamObserver<Die> dieRequestObserver;

  public GameStateResponseObserver(CountDownLatch latch) {
    this.latch = latch;
  }

  @Override
  public void onNext(GameState gameState) {
    final List<Player> playerList = gameState.getPlayerList();
    playerList.forEach(p -> System.out.println(p.getName() + ":" + p.getPosition()));
    final boolean isGameOver = playerList.stream().anyMatch(p -> p.getPosition() == 100);

    if (isGameOver) {
      System.out.println("Game Over");
      dieRequestObserver.onCompleted(); // this will trigger gameStateResponseObserver.onCompleted();
    } else {
      Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
      roll();
    }

    System.out.println("-----------------------");
  }

  @Override
  public void onError(Throwable throwable) {
    latch.countDown();
  }

  @Override
  public void onCompleted() {
    latch.countDown();
  }

  public void setDieRequestObserver(StreamObserver<Die> dieStreamObserver) {
    this.dieRequestObserver = dieStreamObserver;
  }

  public void roll() {
    int dieVal = ThreadLocalRandom.current().nextInt(1, 7);
    final Die die = Die.newBuilder().setValue(dieVal).build();
    dieRequestObserver.onNext(die);
  }

}
