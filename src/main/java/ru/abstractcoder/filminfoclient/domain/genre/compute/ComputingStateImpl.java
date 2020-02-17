package ru.abstractcoder.filminfoclient.domain.genre.compute;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class ComputingStateImpl implements ComputingState {

    private final AverageVoteComputer averageVoteComputer;

    @Getter
    @Setter
    private boolean stopped = false;

    private final CompletableFuture<BigDecimal> resultFuture = new CompletableFuture<>();
    private final Mono<BigDecimal> resultMono = Mono.fromFuture(resultFuture);

    public void checkAndUpdate() {
        if (stopped) {
            return;
        }

        if (averageVoteComputer.checkAndLoad()) {
            resultFuture.complete(averageVoteComputer.getResult());
        }
    }

    @Override
    public double getProgress() {
        return averageVoteComputer.getComputingProgress();
    }

    @Override
    public Mono<BigDecimal> getResult() {
        return resultMono;
    }

    @Override
    public boolean isCompleted() {
        return resultFuture.isDone();
    }

    @Override
    public Optional<BigDecimal> getResultOptional() {
        return resultFuture.isDone() ? Optional.of(averageVoteComputer.getResult()) : Optional.empty();
    }

}
