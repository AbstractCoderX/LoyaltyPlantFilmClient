package ru.abstractcoder.filminfoclient.domain.genre.repository;

import ru.abstractcoder.filminfoclient.domain.genre.Genre;

import java.util.Optional;

public interface GenreRepository {

    Optional<Genre> findById(int genreId);

    boolean existsById(int genreId);

}
