package ru.abstractcoder.filminfoclient.domain.film.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.abstractcoder.filminfoclient.domain.film.Film;
import ru.abstractcoder.filminfoclient.domain.page.PageData;

@Component
public class RestFilmLoader implements FilmLoader {

    private final WebClient webClient;

    @Autowired
    public RestFilmLoader(@Qualifier("loyaltyPlant") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<PageData<Film>> loadFilms(int page) {
        return webClient.get()
                .uri(builder -> builder
                        .path("/3/discover/movie")
                        .queryParam("api_key", "{api_key}")
                        .queryParam("page", page)
                        .build()
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

}
