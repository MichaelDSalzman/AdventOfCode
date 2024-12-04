package aoc2017.day11;

import java.io.IOException;
import java.util.List;
import java.util.Stack;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
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

        public int calculateP1(List<String> lines) {
            List<String> instructions = List.of(lines.get(0).split(","));
            return calculateDistance(instructions);
        }

        public int calculateP2(List<String> lines) {
            List<String> instructions = List.of(lines.get(0).split(","));
            return calculateFurthestDistance(instructions);
        }

        private int calculateDistance(List<String> instructions) {
            int x = 0;
            int y = 0;
            int z = 0;

            for(String instruction : instructions) {
                switch(instruction) {
                    case "n" -> {
                        x++;
                        z--;
                    }
                    case "ne" -> {
                        x++;
                        y--;
                    }
                    case "se" -> {
                        z++;
                        y--;
                    }
                    case "s" -> {
                        x--;
                        z++;
                    }
                    case "sw" -> {
                        y++;
                        x--;
                    }
                    case "nw" -> {
                        y++;
                        z--;
                    }
                }
            }

            int distance = 0;
            distance += Math.max(x, 0);
            distance += Math.max(y, 0);
            distance += Math.max(z, 0);
            return distance;
        }

        private int calculateFurthestDistance(List<String> instructions) {
            int x = 0;
            int y = 0;
            int z = 0;
            int maxDistance = Integer.MIN_VALUE;

            for(String instruction : instructions) {
                switch(instruction) {
                    case "n" -> {
                        x++;
                        z--;
                    }
                    case "ne" -> {
                        x++;
                        y--;
                    }
                    case "se" -> {
                        z++;
                        y--;
                    }
                    case "s" -> {
                        x--;
                        z++;
                    }
                    case "sw" -> {
                        y++;
                        x--;
                    }
                    case "nw" -> {
                        y++;
                        z--;
                    }
                }

                int distance = 0;
                distance += Math.max(x, 0);
                distance += Math.max(y, 0);
                distance += Math.max(z, 0);
                maxDistance = Math.max(maxDistance, distance);
            }

            return maxDistance;
        }
    }
}
