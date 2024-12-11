package aoc2024.day11;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2024";
        String day = "11";

        // FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        long start = System.currentTimeMillis();
        System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println("P1 took " + (System.currentTimeMillis() - start) + " millis");
        start = System.currentTimeMillis();
        System.out.println ("P2 : " + problem.calculateP2(lines));
        System.out.println("P2 took " + (System.currentTimeMillis() - start) + " millis");
    }

    public static class Problem {

        public Object calculateP1(List<String> lines) {

            StoneLine line = StoneLine.valueOf(lines);

            for(int i=0; i<25; i++) {
                line = line.blink();
            }

            return line.getNumberOfStones();
        }

        public Object calculateP2(List<String> lines) {
            StoneLine line = StoneLine.valueOf(lines);

            for(int i=0; i<75; i++) {
                line = line.blink();
            }

            return line.getNumberOfStones();
        }
    }

    record StoneLine(Map<Long, Long> stones) {
        private static final Map<Long, List<Long>> stoneRecord = new HashMap<>();

        public static StoneLine valueOf(List<String> lines) {
            Map<Long, Long> stones = new HashMap<>();
            Arrays.stream(
                lines.get(0).split("\\s+"))
                .map(Long::parseLong)
                .forEach(l -> stones.put(l, 1L));

            return new StoneLine(stones);
        }

        public StoneLine blink() {
            Map<Long, Long> newStoneOccurenceMap = new HashMap<>();

            // Iterate over the stones that currently exist (this will also get you the number of times they exist)
            stones().forEach((s, numTimesOldStoneAppearsInList) -> {
                // Get the already computed result list
                List<Long> resultList = stoneRecord.get(s);

                //If resultList is null (result has never been computed), compute it and add it to the record
                if(resultList == null) {
                    if (s == 0) {
                        resultList = List.of(1L);
                    } else if (String.valueOf(s).length() % 2 == 0) {
                        String str = String.valueOf(s);
                        Long l1 = Long.parseLong(str.substring(0, str.length() / 2));
                        Long l2 = Long.parseLong(str.substring(str.length() / 2));
                        resultList = List.of(l1, l2);
                    } else {
                        resultList = List.of(s * 2024);
                    }

                    stoneRecord.put(s, resultList);
                }

                // Add the new results to the new map. Number of times the new number should occur in new list because
                //  it was in the old list that many times, plus how many times it's already in the new list
                resultList.forEach(stoneValue -> {
                    Long numTimesNewStoneAlreadyAppearsInNewList = newStoneOccurenceMap.getOrDefault(stoneValue, 0L);
                    newStoneOccurenceMap.put(stoneValue, numTimesOldStoneAppearsInList + numTimesNewStoneAlreadyAppearsInNewList);
                });
            });

            return new StoneLine(newStoneOccurenceMap);
        }

        // Sum up all the stone occurrences
        Long getNumberOfStones() {
            return stones().values().stream().mapToLong(Long::longValue).sum();
        }

        int getNumStoneRecords() {
            return stoneRecord.keySet().size();
        }
    }
}
