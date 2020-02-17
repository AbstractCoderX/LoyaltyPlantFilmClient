package ru.abstractcoder.filminfoclient.domain.genre.compute;

import java.util.Optional;

public interface GenreComputingService {

    ComputingState computeVoteAverage(int genreId);

    Optional<ComputingState> getState(int genreId);

}
