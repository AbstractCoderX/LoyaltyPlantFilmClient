package ru.abstractcoder.filminfoclient.domain.genre.compute;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.abstractcoder.filminfoclient.domain.film.loader.FilmLoader;

@Component
@RequiredArgsConstructor
public class AverageVoteComputerFactory {

    private final FilmLoader filmLoader;

    public AverageVoteComputer create(int genreId) {
        return new AverageVoteComputer(genreId, filmLoader);
    }

}
