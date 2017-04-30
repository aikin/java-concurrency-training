package com.concurrency.levenshteindistance;


import com.concurrency.levenshteindistance.common.DistancePair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Starter {
    private static final Logger logger = Logger.getLogger("levenshtein.logger");

    private Starter() {
        throw new IllegalAccessError("Starter class");
    }

    public static void main(String[] args) {

        try {

            long start = System.currentTimeMillis();

//            Distance distance = new Distance(getWords());

//            ThreadPoolDistance distance = new ThreadPoolDistance(getWords(), 4096); // words.count/availableProcessors.count

//            ForkJoinDistance distance = new ForkJoinDistance(getWords(), 4096);
//            ForkJoinStreamDistance distance = new ForkJoinStreamDistance(getWords(), 4096);

//            CompletableFutureDistance distance = new CompletableFutureDistance(getWords(), 5120);
//            CompletableFutureWithCompletionHandlerDistance distance = new CompletableFutureWithCompletionHandlerDistance(getWords(), 5120);
//            CompletableFutureWithCombineDistance distance = new CompletableFutureWithCombineDistance(getWords(), 4096);
//            CompletableFutureStreamDistance distance = new CompletableFutureStreamDistance(getWords(), 2048);

//            ChunkParallelDistance distance = new ChunkParallelDistance(getWords(), 4096);
            
            NonChunkParallelDistance distance = new NonChunkParallelDistance(getWords());

            List<DistancePair> distancePairs = countDistancePairs(getMisspells(), distance);

            long end = System.currentTimeMillis();

            distance.shutdown();

            logger.log(Level.INFO, "Elapsed time: {0} ms", end - start);
            logger.log(Level.INFO, "Distance pairs size: {0}", distancePairs.size());

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private static List<DistancePair> countDistancePairs(final List<Misspell> misspells, final IDistance distance) {
        return misspells
                .stream()
                .parallel()
                .map(misspell -> {
                    DistancePair distancePair = distance.bestMatch(misspell.getMissWord());
                    if (Objects.equals(distancePair.getWord(), misspell.getCorrectWord())) {
                        return distancePair;
                    }
                    return new DistancePair(Integer.MAX_VALUE);
                })
                .filter(pair -> !Objects.equals(pair.getDistance(), Integer.MAX_VALUE))
                .collect(Collectors.toList());
    }

    private static String[] getWords() throws IOException, URISyntaxException {
        return readFileLines("words.txt").map(String::trim).toArray(String[]::new);
    }

    private static List<Misspell> getMisspells() throws IOException, URISyntaxException {
        return readFileLines("misspells.txt")
                .map(l -> {
                    String[] splits = l.trim().split("->");
                    return new Misspell(splits[0], splits[1]);
                })
                .collect(Collectors.toList());
    }

    private static Stream<String> readFileLines(String wordsFileName) throws IOException, URISyntaxException {
        return Files.lines(Paths.get(ClassLoader.getSystemResource(wordsFileName).toURI()));
    }

    private static class Misspell {
        private final String missWord;
        private final String correctWord;

        Misspell(String missWord, String correctWord) {
            this.missWord = missWord;
            this.correctWord = correctWord;
        }

        String getMissWord() {
            return missWord;
        }

        String getCorrectWord() {
            return correctWord;
        }
    }
}
