package aoc2018.day10;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            return findAndPrint(lines);
        }

        public Object calculateP2(List<String> lines) {
            return findAndPrint(lines);
        }

        private long findAndPrint(List<String> lines) {
            List<Point> points = lines.stream().map(Point::valueOf).toList();

            long tick = 0;
            while(!findPattern(points, 5)) {
                tick++;
                points = points.stream().map(Point::iterate).toList();
            }
            print(points);
            return tick;
        }

        private boolean findPattern(List<Point> points, int sequentialPoints) {
            Map<String, Boolean> pointCoords = new HashMap<>();
            points.forEach(p -> pointCoords.put(p.horizontal + "," + p.vertical, true));

            for(Point p : points) {
                boolean foundPattern = true;
                for(int i=0; i<sequentialPoints && foundPattern; i++) {
                    foundPattern = pointCoords.getOrDefault((p.horizontal + i) + "," + (p.vertical), false);
                }
                if(foundPattern) {
                    return true;
                }
            }
            return false;
        }

        private void print(List<Point> points) {
            int minHorizontal = points.stream().map(Point::horizontal).min(Integer::compareTo).orElseThrow();
            int maxHorizontal = points.stream().map(Point::horizontal).max(Integer::compareTo).orElseThrow();
            int minVertical = points.stream().map(Point::vertical).min(Integer::compareTo).orElseThrow();
            int maxVertical = points.stream().map(Point::vertical).max(Integer::compareTo).orElseThrow();

            char[][] grid = new char[maxVertical - minVertical + 1][maxHorizontal - minHorizontal + 1];
            Arrays.stream(grid).forEach(l -> Arrays.fill(l, '.'));

            for(Point p : points) {
                grid[p.vertical - minVertical][p.horizontal - minHorizontal] = '#';
            }

            Arrays.stream(grid).forEach(System.out::println);
        }
    }

    public record Point(int horizontal, int vertical, int velocityHorizontal, int velocityVertical) {
        public static Point valueOf(String line) {
            Matcher m = Pattern.compile("position=<\\s*(-?\\d+),\\s*(-?\\d+)> velocity=<\\s*(-?\\d+),\\s*(-?\\d+)>").matcher(line);
            if(m.find()) {
                return new Point(
                    Integer.parseInt(m.group(1)),
                    Integer.parseInt(m.group(2)),
                    Integer.parseInt(m.group(3)),
                    Integer.parseInt(m.group(4))
                );
            }
            throw new RuntimeException();
        }

        public Point iterate() {
            return new Point(horizontal + velocityHorizontal, vertical+velocityVertical, velocityHorizontal, velocityVertical);
        }
    }
}
