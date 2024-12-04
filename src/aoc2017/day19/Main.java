package aoc2017.day19;

import java.io.IOException;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "19";

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

        public String calculateP1(List<String> lines) {
            return navigateMaze(lines)[0];
        }

        public String calculateP2(List<String> lines) {
            return navigateMaze(lines)[1];
        }

        private String[] navigateMaze(List<String> lines) {
            lines = lines.stream().map(l -> String.format("%-200s", l)).toList();
            StringBuilder sb = new StringBuilder();

            Direction direction = Direction.DOWN;
            int lineNum = 0;
            int index = lines.get(lineNum).indexOf("|");
            boolean navigating = true;
            int numSteps = 0;

            while(navigating) {
                numSteps++;
                switch (direction) {
                    case DOWN -> lineNum++;
                    case UP -> lineNum--;
                    case LEFT -> index--;
                    case RIGHT -> index++;
                }

                char c = lines.get(lineNum).charAt(index);
                switch (c) {
                    case '|', '-' -> {}
                    case '+' -> {
                        switch (direction) {
                            case UP, DOWN -> {
                                if(index > 0 && lines.get(lineNum).charAt(index-1) != ' ') {
                                    direction = Direction.LEFT;
                                } else {
                                    direction = Direction.RIGHT;
                                }
                            }
                            case LEFT, RIGHT -> {
                                if(lineNum > 0 && lines.get(lineNum-1).charAt(index) != ' ') {
                                    direction = Direction.UP;
                                } else {
                                    direction = Direction.DOWN;
                                }
                            }
                        }
                    }
                    case ' ' -> navigating = false;
                    default -> sb.append(c);
                }
            }

            String[] parts = new String[2];
            parts[0] = sb.toString();
            parts[1] = String.valueOf(numSteps);

            return parts;
        }

        private enum Direction {
            UP, DOWN, LEFT, RIGHT;
        }
    }
}
