package aoc2024.day10;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2024";
        String day = "10";

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
            TopoMap topoMap = new TopoMap(lines);
            List<Point> trailHeads = topoMap.findAllPointsWithValue(0);
            int total = 0;
            for(Point trailHead : trailHeads) {
                total += new HashSet<>(calculateScore(topoMap, trailHead, 9)).size();
            }
            return total;
        }

        public int calculateP2(List<String> lines) {
            TopoMap topoMap = new TopoMap(lines);
            List<Point> trailHeads = topoMap.findAllPointsWithValue(0);
            int total = 0;
            for(Point trailHead : trailHeads) {
                total += calculateScore(topoMap, trailHead, 9).size();
            }
            return total;
        }

        /**
         * Given a map and a starting point and the desired height, return a list of points
         * of the desired height that you can reach from the starting point by incrementing by 1 each step
         * and counting unique paths. The list may include the same point multiple times if you can reach it
         * from multiple paths
         * @param topoMap
         * @param currentPoint
         * @param desiredHeight
         * @return
         */
        public List<Point> calculateScore(TopoMap topoMap, Point currentPoint, int desiredHeight) {
            // If the point you're on is the desired height, return it in a new list
            if(currentPoint.value == desiredHeight) {
                return List.of(currentPoint);
            }

            List<Point> list = new ArrayList<>();
            // Find all the points around the current point that are one higher than current point
            topoMap.getPointsAroundWithValue(currentPoint, currentPoint.value+1)
                // recursively calculate the score for each of those points and add the points you can
                // reach to the main list
                .forEach(p -> list.addAll(calculateScore(topoMap, p, desiredHeight)));
            return list;
        }
    }

    record Point(int horizontal, int vertical, int value) {}

    record TopoMap(List<String> lines) {

        // what's the value at the coords given
        int valueAt(int horizontal, int vertical) {
            if(horizontal < 0 || vertical < 0 || horizontal >= lines.get(0).length() || vertical >= lines.size()) {
                return -1;
            }

            return Integer.parseInt(lines.get(vertical).substring(horizontal, horizontal+1));
        }

        // find all points with the desired value (used to find trailheads)
        List<Point> findAllPointsWithValue(int value) {
            List<Point> points = new ArrayList<>();
            for(int vertical=0; vertical<lines.size(); vertical++) {
                String line = lines.get(vertical);
                for(int horizontal =0; horizontal<line.length(); horizontal++){
                    int pointValue = Integer.parseInt(line.substring(horizontal, horizontal+1));
                    if( pointValue == value) {
                        points.add(new Point(horizontal, vertical, pointValue));
                    }
                }
            }

            return points;
        }

        // Look at the points orthogonal to the point and find ones that have the desired value
        List<Point> getPointsAroundWithValue(Point point, int desiredValue) {
            List<Point> points = new ArrayList<>();
            for(int horizontal=-1; horizontal<=1; horizontal++) {
                for(int vertical=-1; vertical<=1; vertical++) {
                    if(Math.abs(horizontal) == Math.abs(vertical)) {
                        continue;
                    }
                    if(valueAt(point.horizontal + horizontal, point.vertical + vertical) == desiredValue) {
                        points.add(new Point(point.horizontal + horizontal, point.vertical + vertical, desiredValue));
                    }
                }
            }

            return points;
        }
    }
}
