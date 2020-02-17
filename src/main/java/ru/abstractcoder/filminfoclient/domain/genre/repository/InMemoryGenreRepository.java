package ru.abstractcoder.filminfoclient.domain.genre.repository;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.abstractcoder.filminfoclient.domain.genre.Genre;
import ru.abstractcoder.filminfoclient.domain.genre.loader.GenreLoader;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InMemoryGenreRepository implements GenreRepository {

    private final Int2ObjectMap<Genre> genreMap = new Int2ObjectOpenHashMap<>();

    @Autowired
    public InMemoryGenreRepository(GenreLoader genreLoader) {
        List<Genre> genres = genreLoader.loadGenres().block();
        Preconditions.checkState(genres != null && !genres.isEmpty(),
                "not any genre available"
        );

        genres.forEach(genre -> genreMap.put(genre.getId(), genre));
    }

    @Override
    public Optional<Genre> findById(int genreId) {
        return Optional.ofNullable(genreMap.get(genreId));
    }

    @Override
    public boolean existsById(int genreId) {
        return genreMap.containsKey(genreId);
    }

}
