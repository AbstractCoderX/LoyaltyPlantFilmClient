package ru.abstractcoder.filminfoclient.domain.genre.compute;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;

public interface ComputingState {

    double getProgress();

    Mono<BigDecimal> getResult();

    boolean isCompleted();

    Optional<BigDecimal> getResultOptional();

    boolean isStopped();

    void setStopped(boolean stopped);

}
