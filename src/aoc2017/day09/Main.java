package aoc2017.day09;

import java.io.IOException;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "09";

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

        public int calculateP1(List<String> lines) {
            return parseStreamForScore(lines.get(0));
        }

        public int calculateP2(List<String> lines) {
            return parseStreamCountGarbage(lines.get(0));
        }

        private int parseStreamForScore(String stream) {
            int score = 0;
            int currentHeight = 0;
            boolean insideGarbage = false;
            boolean skipNext = false;

            for(char c : stream.toCharArray()) {

                if(skipNext) {
                    skipNext = false;
                    continue;
                }

                if(c == '!') {
                    skipNext = true;
                } else if(c == '<') {
                    insideGarbage = true;
                } else if(c == '>') {
                    insideGarbage = false;
                }
                if(!insideGarbage) {
                    if (c == '{') {
                        currentHeight++;
                        score += currentHeight;
                    } else if (c == '}') {
                        currentHeight--;
                    }
                }
            }

            return score;
        }

        private int parseStreamCountGarbage(String stream) {
            int garbageCount = 0;
            boolean insideGarbage = false;
            boolean skipNext = false;

            for(char c : stream.toCharArray()) {

                if(skipNext) {
                    skipNext = false;
                    continue;
                }

                if(c == '!') {
                    skipNext = true;
                } else if(c == '<' && !insideGarbage) {
                    insideGarbage = true;
                } else if(c == '>' && insideGarbage) {
                    insideGarbage = false;
                } else if(insideGarbage) {
                    garbageCount++;
                }
            }

            return garbageCount;
        }
    }
}
