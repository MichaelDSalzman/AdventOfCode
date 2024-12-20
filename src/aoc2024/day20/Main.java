package aoc2024.day20;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.math3.util.Pair;
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

        private static Maze maze;
        private static Map<Coordinates, Integer> distanceFromStart;
        private static Map<Coordinates, Integer> distanceFromEnd;

        public Object calculateP1(List<String> lines) {
            init(lines);
            Integer normalPathLength = distanceFromStart.get(maze.end);

            System.out.println("NORMAL PATH LENGTH: " + normalPathLength);
            return findPathsByCheating(maze, distanceFromStart, distanceFromEnd, 2, true).stream()
                .filter(p -> normalPathLength - p >= 100)
                .toList()
                .size();
        }

        public Object calculateP2(List<String> lines) {
            init(lines);
            Integer normalPathLength = distanceFromStart.get(maze.end);

            System.out.println("NORMAL PATH LENGTH: " + normalPathLength);
            return findPathsByCheating(maze, distanceFromStart, distanceFromEnd, 20, false).stream()
                .map(p -> normalPathLength - p)
                .filter(p -> p >= 100)
                .toList()
                .size();
        }

        private static void init(List<String> lines) {
            if(maze == null) {
                maze = Maze.valueOf(lines);
            }
            if(distanceFromStart == null) {
                distanceFromStart = findDistanceFromPoint(maze, maze.start);
            }

            if(distanceFromEnd == null) {
                distanceFromEnd = findDistanceFromPoint(maze, maze.end);
            }

        }

        // Given a maze and a starting coordinate, find the best distance to all other points in maze
        private static Map<Coordinates, Integer> findDistanceFromPoint(Maze maze, Coordinates startCoordinate) {
            Map<Coordinates, Integer> distanceMap = new HashMap<>();

            Queue<Pair<Coordinates, Integer>> queue = new ArrayDeque<>();
            queue.add(Pair.create(startCoordinate, 0));
            while(!queue.isEmpty()) {
                Pair<Coordinates, Integer> locationBeingPolled = queue.poll();

                if(!distanceMap.containsKey(locationBeingPolled.getKey())) {
                    distanceMap.put(locationBeingPolled.getKey(), locationBeingPolled.getValue());
                }

                maze.getNeighborsOfType(locationBeingPolled.getKey(), 1, true,
                        Set.of(PointType.EMPTY, PointType.START, PointType.END))
                    .stream()
                    .filter(p -> !distanceMap.containsKey(p))
                    .filter(p -> !queue.contains(Pair.create(p, locationBeingPolled.getValue()+1)))
                    .forEach(neighbor -> queue.add(Pair.create(neighbor, locationBeingPolled.getValue()+1)));
            }

            return distanceMap;
        }

        // Find all paths through the maze if you are allowed to cheat once by warping from your current
        //  point to a point at most "cheatDistance" away. If "cheatMustBeOrthogonal" you must be exactly
        //  north, south, east, or west of the current position when you finish warping
        private List<Integer> findPathsByCheating(Maze maze,
                                                 Map<Coordinates, Integer> startDistanceMap,
                                                 Map<Coordinates, Integer> endDistanceMap,
                                                 int cheatDistance,
                                                 boolean cheatMustBeOrthogonal) {

            List<Integer> paths = new ArrayList<>();

            for(Coordinates coordinates : startDistanceMap.keySet()) {
                Set<Coordinates> neighborsTwoAway = maze.getNeighborsOfType(coordinates, cheatDistance, cheatMustBeOrthogonal, Set.of(PointType.START, PointType.END, PointType.EMPTY));
                for(Coordinates neighbor : neighborsTwoAway) {

                    int distanceToNeighbor = Math.abs(coordinates.horizontal - neighbor.horizontal) + Math.abs(coordinates.vertical - neighbor.vertical);
                    int distanceToEndThroughNeighbor = startDistanceMap.get(coordinates) + distanceToNeighbor + endDistanceMap.get(neighbor);
                        paths.add(distanceToEndThroughNeighbor);
                }
            }

            return paths.stream().sorted().toList();
        }

        public enum PointType {
            START("S"),
            END("E"),
            WALL("#"),
            EMPTY(".");

            private final String value;
            PointType(String value) {
                this.value = value;
            }

            public static PointType fromString(String value) {
                return Arrays.stream(PointType.values()).filter(pt -> pt.value.equals(value)).findFirst().orElse(null);
            }
        }

        public record Coordinates(int horizontal, int vertical) {}

        public record Maze(int width, int height, Coordinates start, Coordinates end, Map<Coordinates, PointType> points) {
            public static Maze valueOf(List<String> lines) {
                int width = lines.get(0).length();
                int height = lines.size();
                Coordinates start = null;
                Coordinates end = null;
                Map<Coordinates, PointType> points = new HashMap<>();

                for(int vertical = 0; vertical < lines.size(); vertical++) {
                    String line = lines.get(vertical);
                    for(int horizontal = 0; horizontal<line.length(); horizontal++) {

                        Coordinates coordinates = new Coordinates(horizontal, vertical);
                        PointType pointType = PointType.fromString(line.substring(horizontal, horizontal+1));
                        points.put(coordinates, pointType);
                        if(pointType.equals(PointType.START)) {
                            start = coordinates;
                        } else if(pointType.equals(PointType.END)) {
                            end = coordinates;
                        }
                    }
                }

                return new Maze(width, height, start, end, points);
            }

            // Find all points in the maze within distance of coordinates that are of a type in "acceptablePointTypes".
            //  If "mustBeOrthogonal", neighbor must be exactly north, south, east, or west of current location
            public Set<Coordinates> getNeighborsOfType(Coordinates coordinates,
                                                       int distance,
                                                       boolean mustBeOrthogonal,
                                                       Set<PointType> acceptablePointTypes) {

                return this.points.keySet().stream()
                    // Filter to only points within distance
                    .filter(p -> Math.abs(p.horizontal - coordinates.horizontal) + Math.abs(p.vertical - coordinates.vertical) <= distance)
                    // Filter to only points of the correct type
                    .filter(p -> acceptablePointTypes.contains(this.points.get(p)))
                    // Filter out the current location
                    .filter(p -> !(p.horizontal == coordinates.horizontal && p.vertical == coordinates.vertical))
                    // If must be orthogonal, filter out any points that don't have the same horizontal or vertical
                    .filter(p -> !mustBeOrthogonal || p.vertical == coordinates.vertical || p.horizontal == coordinates.horizontal)
                    .collect(Collectors.toSet());
            }
        }
    }
}
