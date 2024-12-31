package aoc2017.day22;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
            return burst(lines, 10000, false, false);
        }

        public Object calculateP2(List<String> lines) {
            return burst(lines, 10000000, true, false);
        }

        private long burst(List<String> lines, long numBursts, boolean smartVirus, boolean printOutput) {
            Map<String, Status> cells = new HashMap<>();
            Long iterationsThatCausedInfection = 0L;

            for(int vertical=0; vertical<lines.size(); vertical++) {
                String line = lines.get(vertical);
                for(int horizontal=0; horizontal<line.length(); horizontal++) {
                    if(line.charAt(horizontal) == '#') {
                        cells.put(horizontal + "," + vertical, Status.INFECTED);
                    }
                }
            }

            long virusVertical = lines.size()/2;
            long virusHorizontal = lines.get(0).length()/2;
            Direction virusDirection = Direction.UP;

            for(int iteration=0; iteration<numBursts; iteration++) {
                String currentLocation = virusHorizontal + "," + virusVertical;
                if(!smartVirus) {
                    if(cells.get(currentLocation) == Status.INFECTED) {
                        cells.remove(currentLocation);
                        virusDirection = Direction.values()[(virusDirection.ordinal() + 1) % 4];
                    } else if (cells.get(currentLocation) == Status.CLEAN || cells.get(currentLocation) == null){
                        iterationsThatCausedInfection++;
                        cells.put(currentLocation, Status.INFECTED);
                        virusDirection = Direction.values()[((virusDirection.ordinal()+4-1) % 4)];
                    }
                } else {
                    if(cells.get(currentLocation) == Status.INFECTED) {
                        cells.put(currentLocation, Status.FLAGGED);
                        virusDirection = Direction.values()[(virusDirection.ordinal() + 1) % 4];
                    } else if (cells.get(currentLocation) == Status.CLEAN || cells.get(currentLocation) == null){
                        cells.put(currentLocation, Status.WEAKENED);
                        virusDirection = Direction.values()[((virusDirection.ordinal()+4-1) % 4)];
                    } else if (cells.get(currentLocation) == Status.WEAKENED) {
                        iterationsThatCausedInfection++;
                        cells.put(currentLocation, Status.INFECTED);
                    } else if (cells.get(currentLocation) == Status.FLAGGED) {
                        cells.put(currentLocation, Status.CLEAN);
                        virusDirection = Direction.values()[(virusDirection.ordinal() + 2) % 4];
                    }
                }

                switch(virusDirection) {
                    case UP -> virusVertical--;
                    case DOWN -> virusVertical++;
                    case LEFT -> virusHorizontal--;
                    case RIGHT -> virusHorizontal++;
                }
            }

            if(printOutput) {
                print(cells);
            }
            return iterationsThatCausedInfection;
        }

        private void print(Map<String, Status> cells) {
            System.out.println("printing");

            int minHorizontal = 0;
            int maxHorizontal = 0;
            int minVertical = 0;
            int maxVertical = 0;

            for(String cell : cells.keySet()) {
                String[] parts = cell.split(",");
                int horizontal = Integer.parseInt(parts[0]);
                int vertical = Integer.parseInt(parts[1]);

                minHorizontal = Math.min(horizontal, minHorizontal);
                maxHorizontal = Math.max(horizontal, maxHorizontal);
                minVertical = Math.min(vertical, minVertical);
                maxVertical = Math.max(vertical, maxVertical);
            }

            char[][] grid = new char[maxVertical-minVertical+1][maxHorizontal-minHorizontal+1];
            for(char[] line : grid) {
                Arrays.fill(line, '.');
            }

            for(String cell : cells.keySet()) {
                char c = '.';
                switch(cells.get(cell)) {
                    case FLAGGED -> c = 'F';
                    case INFECTED -> c = '#';
                    case WEAKENED -> c = 'W';
                }

                String[] parts = cell.split(",");
                int horizontal = Integer.parseInt(parts[0]);
                int vertical = Integer.parseInt(parts[1]);
                grid[vertical-minVertical][horizontal-minHorizontal] = c;
            }

            StringBuilder builder = new StringBuilder();
            for(char[] line : grid) {
                builder.append(new String(line));
                builder.append("\n");
            }

            System.out.println(builder);

            System.out.println("Done printing");
        }
    }

    public enum Status {
        CLEAN, INFECTED, FLAGGED, WEAKENED;
    }

    // ordinal + 1 = turning right
    // ordinal - 1 = turning left
    public enum Direction {
        UP, RIGHT, DOWN, LEFT;
    }
}
