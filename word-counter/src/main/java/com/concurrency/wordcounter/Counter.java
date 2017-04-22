package com.concurrency.wordcounter;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Counter implements Runnable {
    private static final Logger logger = Logger.getLogger("counter.logger");
    private final BlockingQueue<Page> pageBlockingQueue;
    private final ConcurrentHashMap<String, Integer> counts;
    private final HashMap<String, Integer> localCounts;

    public Counter(BlockingQueue<Page> pageBlockingQueue, ConcurrentHashMap<String, Integer> counts) {
        this.pageBlockingQueue = pageBlockingQueue;
        this.counts = counts;
        this.localCounts = new HashMap<>();
    }

    @Override
    public void run() {
        try {

            while (true) {
                Page page = pageBlockingQueue.take();
                if (page.isPoisonPill()) {
                    break;
                }
                Words words = new Words(page.getText());
                for (String word : words) {
                    countWord(word);
                }
            }
            mergeCounts();
        } catch (InterruptedException e) {
            logger.log(Level.FINE, e.getMessage());
        }
    }

    private void mergeCounts() {
        for (Map.Entry<String, Integer> entry : localCounts.entrySet()) {
            String word = entry.getKey();
            Integer count = entry.getValue();

            while (true) {
                Integer currentCount = counts.get(word);
                if (currentCount == null) {
                    if (counts.putIfAbsent(word, count) == null) {
                        break;
                    }
                } else if (counts.replace(word, currentCount, currentCount + count)) {
                    break;
                }
            }
        }
    }


    private void countWord(String word) {
        Integer currentCount = counts.get(word);
        if (currentCount == null) {
            localCounts.put(word, 1);
        } else {
            localCounts.put(word, currentCount + 1);
        }
    }


}
