package ru.abstractcoder.filminfoclient.domain.genre.loader;

import reactor.core.publisher.Mono;
import ru.abstractcoder.filminfoclient.domain.genre.Genre;

import java.util.List;

public interface GenreLoader {

    Mono<List<Genre>> loadGenres();

}