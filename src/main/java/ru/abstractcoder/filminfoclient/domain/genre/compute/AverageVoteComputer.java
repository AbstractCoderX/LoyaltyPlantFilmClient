package ru.abstractcoder.filminfoclient.domain.genre.compute;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import reactor.netty.resources.LoopResources;
import ru.abstractcoder.filminfoclient.domain.film.Film;
import ru.abstractcoder.filminfoclient.domain.film.loader.FilmLoader;
import ru.abstractcoder.filminfoclient.domain.page.PageData;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Log4j2
public class AverageVoteComputer {

    private final int genreId;
    private final FilmLoader filmLoader;

    private volatile int totalPages = -1;
    private volatile boolean loadStarted = false;

    private final Queue<Integer> pageQueue = new ConcurrentLinkedQueue<>();

    private final AtomicReference<BigDecimal> totalAverageVote = new AtomicReference<>(BigDecimal.ZERO);
    private final AtomicInteger filmsLoaded = new AtomicInteger();
    private final AtomicInteger pagesLoaded = new AtomicInteger();
    private volatile BigDecimal result = null;

    public boolean checkAndLoad() {
        if (!loadStarted) {
            log.debug("loading first page");

            Mono<PageData<Film>> firstPageMono = filmLoader.loadFilms(1);
            loadStarted = true;

            firstPageMono.subscribe(filmPageData -> {
                totalPages = filmPageData.getTotalPages() - 1;
                addPageData(filmPageData);

                for (int i = 2; i <= LoopResources.DEFAULT_IO_WORKER_COUNT && i <= totalPages; i++) {
                    pageQueue.add(i);
                }
            });

            return false;
        }

        if (result != null) {
            return true;
        }

        Integer page = pageQueue.poll();
        if (page != null) {
            filmLoader.loadFilms(page).subscribe(this::addPageData);
        }

        return false;
    }

    private void addPageData(PageData<Film> pageData) {
        log.debug("calculation for page {}", pageData.getPage());
        int filmAmount = 0;
        BigDecimal vote = BigDecimal.ZERO;
        for (Film film : pageData.getValues()) {
            if (!film.containsGenre(genreId)) {
                continue;
            }

            filmAmount++;
            vote = vote.add(film.getVoteAverage());
        }

        filmsLoaded.addAndGet(filmAmount);
        totalAverageVote.accumulateAndGet(vote, BigDecimal::add);
        if (pagesLoaded.incrementAndGet() == totalPages) {
            result = totalAverageVote.get().divide(BigDecimal.valueOf(filmsLoaded.get()), MathContext.DECIMAL64);
        }

        log.debug("current state: pagesLoaded = {}, filmsIncluded = {}, totalAverageVote = {}",
                pagesLoaded.get(), filmsLoaded.get(), totalAverageVote.get()
        );

        int page = pageData.getPage() + LoopResources.DEFAULT_IO_WORKER_COUNT;
        if (page <= totalPages) {
            pageQueue.add(page);
            log.debug("page {} added", page);
        }
    }

    public double getComputingProgress() {
        return ((double) pagesLoaded.get()) / totalPages;
    }

    public BigDecimal getResult() {
        Preconditions.checkState(result != null, "computing not completed yet");
        return result;
    }

}
