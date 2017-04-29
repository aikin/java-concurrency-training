package com.concurrency.levenshteindistance;


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

            List<DistancePair> distancePairs = countDistancePairs(getMisspells(), new Distance(getWords()));

            long end = System.currentTimeMillis();

            logger.log(Level.INFO, "Elapsed time: {0} ms", end - start);
            logger.log(Level.INFO, "Distance pairs size: {0}", distancePairs.size());

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private static List<DistancePair> countDistancePairs(final List<Misspell> misspells, final Distance distance) {
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
