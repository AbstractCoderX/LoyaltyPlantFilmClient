package ru.abstractcoder.filminfoclient.domain.film;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import lombok.Value;
import ru.abstractcoder.filminfoclient.domain.genre.Genre;

import java.time.LocalDate;

@Value
//по сути, единственные поля, которое нужны в рамках этой задачи этo voteAverage и genreIds, ну да ладно
public class Film {

    private int id;
    private int voteCount;
    private boolean video;
    private double voteAverage;
    private String title;
    private String popularity;
    private String originalLanguage;
    private String originalTitle;
    private IntSet genreIds;
    private boolean adult;
    private String overview;
    private LocalDate releaseDate;

    @JsonCreator
    public Film(int id, int voteCount, boolean video, double voteAverage,
            String title, String popularity, String originalLanguage,
            String originalTitle, int[] genreIds, boolean adult, String overview,
            @JsonProperty("release_date") short[] releaseDateArray) {
        Preconditions.checkArgument(releaseDateArray.length == 3,
                "invalid release_date array length, excepted 3, but got %d",
                releaseDateArray.length
        );
        releaseDate = LocalDate.of(releaseDateArray[0], releaseDateArray[1], releaseDateArray[2]);

        this.id = id;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
        this.title = title;
        this.popularity = popularity;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.genreIds = new IntOpenHashSet(genreIds);
        this.adult = adult;
        this.overview = overview;
    }

    public boolean containsGenre(int genreId) {
        return genreIds.contains(genreId);
    }

    public boolean containsGenre(Genre genre) {
        return containsGenre(genre.getId());
    }

}
