package ru.abstractcoder.filminfoclient.domain.genre.compute;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.abstractcoder.filminfoclient.domain.genre.repository.GenreRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreComputingServiceImpl implements GenreComputingService {

    private final GenreRepository genreRepository;
    private final AverageVoteComputerFactory averageVoteComputerFactory;

    private final Int2ObjectMap<ComputingStateImpl> stateMap = new Int2ObjectOpenHashMap<>();

    @Override
    public ComputingState computeVoteAverage(int genreId) {
        if (!genreRepository.existsById(genreId)) {
            throw new IllegalArgumentException("unknown genre id: " + genreId);
        }

        return stateMap.computeIfAbsent(genreId,
                (__) -> new ComputingStateImpl(averageVoteComputerFactory.create(genreId))
        );
    }

    @Override
    public Optional<ComputingState> getState(int genreId) {
        return Optional.ofNullable(stateMap.get(genreId));
    }

    // private Mono<Genre> loadGenreMono(int genreId) {
    //     if (genreRepository.isLoaded()) {
    //         return justGetGenre(genreId);
    //     }
    //     return genreRepository.loadOrGetLoadingMono()
    //             .flatMap((__) -> justGetGenre(genreId));
    // }
    //
    // private Mono<Genre> justGetGenre(int genreId) {
    //     return Mono.justOrEmpty(genreRepository.findById(genreId));
    // }

    @Scheduled(fixedRate = 1L)
    public void checkStates() {
        stateMap.values().forEach(computingState -> {
            if (computingState.isStopped()) {
                return;
            }

            computingState.checkAndUpdate();
        });
    }

}
