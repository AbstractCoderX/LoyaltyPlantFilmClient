package ru.abstractcoder.filminfoclient.domain.film.loader;

import reactor.core.publisher.Mono;
import ru.abstractcoder.filminfoclient.domain.film.Film;
import ru.abstractcoder.filminfoclient.domain.page.PageData;

public interface FilmLoader {

    Mono<PageData<Film>> loadFilms(int page);

}
