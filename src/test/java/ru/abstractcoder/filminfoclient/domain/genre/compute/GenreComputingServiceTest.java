package ru.abstractcoder.filminfoclient.domain.genre.compute;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableScheduling
class GenreComputingServiceTest {

    @Autowired
    private GenreComputingService genreComputingService;

    @Test
    void progressNonDecreasing() throws InterruptedException {
        ComputingState computingState = genreComputingService.computeVoteAverage(10752);

        double progress1 = computingState.getProgress();
        Thread.sleep(500);
        double progress2 = computingState.getProgress();
        assertTrue(progress1 <= progress2);
    }

    @Test
    void pause() throws InterruptedException {
        ComputingState computingState = genreComputingService.computeVoteAverage(10752);
        Thread.sleep(500);

        computingState.setStopped(true);
        Thread.sleep(700);

        double progress1 = computingState.getProgress();
        Thread.sleep(1000);
        double progress2 = computingState.getProgress();

        assertEquals(progress1, progress2);
    }

}