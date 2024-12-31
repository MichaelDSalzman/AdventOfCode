package aoc2018.day09;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = null;
        String day = null;

        Pattern p = Pattern.compile("aoc(\\d+)\\.day(\\d+).*");
        Matcher m = p.matcher(Main.class.getName());
        if(m.find()) {
            year = m.group(1);
            day = m.group(2);
        }

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
            return getHighScore(lines.get(0), 1);
        }

        public Object calculateP2(List<String> lines) {
            return getHighScore(lines.get(0), 100);
        }

        private long getHighScore(String line, int multiplier) {
            Matcher m = Pattern.compile("(\\d+) players; last marble is worth (\\d+) points").matcher(line);

            if(m.find()) {
                int numPlayers = Integer.parseInt(m.group(1));
                int lastMarbleNumPoints = Integer.parseInt(m.group(2)) * multiplier;

                long[] playerScores = new long[numPlayers];
                Deque<Integer> marbles = new ArrayDeque<>(lastMarbleNumPoints);
                marbles.add(0);

                for(int turn=1; turn<=lastMarbleNumPoints; turn++) {
                    if(turn % 23 == 0) {
                        // DO SCORING
                        int score = turn;
                        for(int i=0; i<7; i++) {
                            Integer marble = marbles.removeLast();
                            marbles.addFirst(marble);
                        }
                        score += marbles.removeFirst();
                        playerScores[turn % numPlayers] += score;
                        continue;
                    }
                    marbles.addLast(marbles.removeFirst());
                    marbles.addLast(marbles.removeFirst());
                    marbles.addFirst(turn);
                }

                return Arrays.stream(playerScores).max().orElseThrow();
            }

            throw new RuntimeException();
        }
    }
}
