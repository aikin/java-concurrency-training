package com.concurrency.levenshteindistance;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Starter {
    public static void main(String[] args) {

        try {
            Stream<String> wordLinesStream = Files.lines(Paths.get(ClassLoader.getSystemResource("words.txt").toURI()));
            Stream<String> misspellLinesStream = Files.lines(Paths.get(ClassLoader.getSystemResource("misspells.txt").toURI()));

            final String[] words = wordLinesStream.map(String::trim).toArray(String[]::new);

            List<Misspell> misspells = misspellLinesStream
                    .map(l -> {
                        String[] splits = l.trim().split("->");
                        return new Misspell(splits[0], splits[1]);
                    })
                    .collect(Collectors.toList());


            final Distance distance = new Distance(words);
            long start = System.currentTimeMillis();

            List<DistancePair> distancePairs = misspells.stream()
                    .map(misspell -> {
                        DistancePair distancePair = distance.bestMatch(misspell.getMissWord());
                        if (Objects.equals(distancePair.getWord(), misspell.getCorrectWord())) {
                            return distancePair;
                        }
                        return new DistancePair(Integer.MAX_VALUE);
                    })
                    .filter(pair -> !Objects.equals(pair.getDistance(), Integer.MAX_VALUE))
                    .collect(Collectors.toList());


            long end = System.currentTimeMillis();

            System.out.println("Elapsed time: " + (end - start) + "ms");
            System.out.println("Elapsed count: " + distancePairs.size());

        } catch (IOException e) {
            e.printStackTrace();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    private static class Misspell {
        private final String missWord;
        private final String correctWord;

        public Misspell(String missWord, String correctWord) {
            this.missWord = missWord;
            this.correctWord = correctWord;
        }

        public String getMissWord() {
            return missWord;
        }

        public String getCorrectWord() {
            return correctWord;
        }
    }
}
