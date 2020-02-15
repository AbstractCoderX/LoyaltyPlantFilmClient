package ru.abstractcoder.filminfoclient.domain.genre.loader;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.abstractcoder.filminfoclient.domain.genre.Genre;

import java.util.List;

@Component
public class RestGenreLoader implements GenreLoader {

    private final WebClient webClient;

    @Autowired
    public RestGenreLoader(@Qualifier("loyaltyPlant") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<List<Genre>> loadGenres() {
        return webClient.get()
                .uri(builder -> builder
                        .path("3/genre/movie/list")
                        .queryParam("api_key", "{api_key}")
                        .build()
                )
                .retrieve()
                .bodyToMono(Genres.class)
                .map(Genres::getValues);
    }

    @Data
    private static class Genres {

        private final List<Genre> values;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Genres(@JsonProperty("genres") List<Genre> values) {
            this.values = values;
        }

    }

}
