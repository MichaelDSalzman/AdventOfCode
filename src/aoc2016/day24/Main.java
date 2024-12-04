package aoc2016.day24;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.apache.commons.collections4.iterators.PermutationIterator;
import org.apache.commons.math3.util.Combinations;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "24";

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
            return calculateShortest(lines, false);
        }


        public int calculateP2(List<String> lines) {
            return calculateShortest(lines, true);
        }

        private int calculateShortest(List<String> lines, boolean returnHome) {

            Map<String, Coordinate> coordinateMap = findPoints(lines);
            List<String> pointNames = coordinateMap.keySet().stream().toList();
            Map<String, Map<String, Integer>> distanceMap = new HashMap<>();

            Combinations c = new Combinations(pointNames.size(), 2);
            Iterator<int[]> iter = c.iterator();
            while(iter.hasNext()) {
                int[] combo = iter.next();
                String key1 = pointNames.get(combo[0]);
                String key2 = pointNames.get(combo[1]);
                if(key1.equals(key2)) {
                    continue;
                }

                int distance = numStepsBetweenPoints(coordinateMap.get(key1), coordinateMap.get(key2), lines);

                Map<String,Integer> map = distanceMap.getOrDefault(key1, new HashMap<>());
                map.put(key2, distance);
                distanceMap.put(key1, map);
                map = distanceMap.getOrDefault(key2, new HashMap<>());
                map.put(key1, distance);
                distanceMap.put(key2, map);
            }

            int minTotalDistance = Integer.MAX_VALUE;
            PermutationIterator pi = new PermutationIterator(coordinateMap.keySet());
            while(pi.hasNext()) {
                List<String> path = pi.next();
                if(!path.get(0).equals("0")) {
                    continue;
                }
                if(returnHome) {
                    path.add("0");
                }
                int currentPathDistance = 0;
                for(int i=1; i<path.size(); i++) {
                    String from = path.get(i-1);
                    String to = path.get(i);
                    currentPathDistance += distanceMap.get(from).get(to);
                }
                minTotalDistance = Math.min(minTotalDistance, currentPathDistance);
            }
            return minTotalDistance;
        }


        public Map<String, Coordinate> findPoints(List<String> lines) {
            Map<String, Coordinate> coordinateMap = new HashMap<>();

            for(int lineNum=0; lineNum < lines.size(); lineNum++) {
                String line = lines.get(lineNum);
                for(int i=0; i<line.length(); i++){
                    char c = line.charAt(i);
                    if(c != '#' && c != '.') {
                        coordinateMap.put(String.valueOf(c), new Coordinate(i, lineNum));
                    }
                }
            }

            return coordinateMap;
        }

        private int numStepsBetweenPoints(Coordinate start, Coordinate end, List<String> map) {
            char[][] charMap = new char[map.get(0).length()][map.size()];
            for(int lineNum=0; lineNum<map.size(); lineNum++){
                String line = map.get(lineNum);
                for(int i=0; i<line.length(); i++) {
                    charMap[i][lineNum] = line.charAt(i);
                }
            }

            Queue<CoordinateStep> stepQueue = new ArrayDeque<>();
            Set<CoordinateStep> visitedCoords = new HashSet<>();

            stepQueue.add(new CoordinateStep(start.x(), start.y(), 0));
            while(!stepQueue.isEmpty()) {
                CoordinateStep coordinateStep = stepQueue.remove();

                if(visitedCoords.contains(coordinateStep) || coordinateStep.x() < 0 || coordinateStep.x() >= map.get(0).length() || coordinateStep.y() < 0 || coordinateStep.y() >= map.size() || charMap[coordinateStep.x()][coordinateStep.y()] == '#') {
                    continue;
                }

                visitedCoords.add(coordinateStep);
                if(coordinateStep.x() == end.x() && coordinateStep.y() == end.y()) {
                    return coordinateStep.numSteps();
                }

                stepQueue.add(new CoordinateStep( coordinateStep.x()+1, coordinateStep.y(),
                    coordinateStep.numSteps() + 1));
                stepQueue.add(new CoordinateStep(coordinateStep.x()-1, coordinateStep.y(),
                    coordinateStep.numSteps() + 1));
                stepQueue.add(new CoordinateStep(coordinateStep.x(), coordinateStep.y()+1,
                    coordinateStep.numSteps() + 1));
                stepQueue.add(new CoordinateStep(coordinateStep.x(), coordinateStep.y()-1,
                    coordinateStep.numSteps() + 1));
            }

            return -1;
        }

        public record Coordinate(int x, int y) {}

        public record CoordinateStep(int x, int y, int numSteps) {
            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                CoordinateStep that = (CoordinateStep) o;
                return x == that.x && y == that.y;
            }
        }
    }
}
