package aoc2024.day16;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
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
            return searchForExit(lines).stream()
                .map(path -> path.score)
                .mapToLong(Long::longValue)
                .min().orElseThrow();
        }


        public Object calculateP2(List<String> lines) {
            List<Path> allPathsToExit = searchForExit(lines);
            long minScore = allPathsToExit.stream()
                .map(path -> path.score)
                .mapToLong(Long::longValue)
                .min()
                .orElseThrow();

            List<Path> bestPaths = allPathsToExit.stream().filter(path -> path.score == minScore).toList();

            print(lines, bestPaths);
            Set<Point> exitPoints = new HashSet<>();
            bestPaths.forEach(ep -> exitPoints.addAll(ep.points));
            return exitPoints.size();
        }

        private void print(List<String> lines, List<Path> paths) {
            for(Path path : paths) {
                for (Point point : path.points) {
                    StringBuilder builder = new StringBuilder();
                    String line = lines.get(point.vertical);
                    builder.append(line, 0, point.horizontal());
                    builder.append("O");
                    builder.append(line.substring(point.horizontal() + 1));

                    lines.set(point.vertical, builder.toString());
                }
            }

            lines.forEach(System.out::println);
        }

        private List<Path> searchForExit(List<String> lines) {
            Point start = null, end = null;
            for(int vertical=0; vertical<lines.size(); vertical++) {
                String line = lines.get(vertical);
                if(line.contains("E")) {
                    end = new Point(line.indexOf("E"), vertical);
                }
                if(line.contains("S")) {
                    start = new Point(line.indexOf("S"), vertical);
                }
            }

            Queue<Path> paths = new ArrayDeque<>();
            paths.add(new Path(List.of(start), 0L, Direction.EAST));

            Map<Point, List<Path>> bestPaths = findBestPaths(lines, paths);
            return(bestPaths.get(end));
        }

        private Map<Point, List<Path>> findBestPaths(List<String> lines, Queue<Path> paths) {
            Map<Point, List<Path>> allPaths = new HashMap<>();

            while(!paths.isEmpty()) {
                Path path = paths.remove();
                Point lastPointInPath = path.points.get(path.points.size()-1);

                // If map already has a path to this point
                if (allPaths.containsKey(lastPointInPath)) {
                    // What's the best score to this point
                    long bestScore = allPaths.get(lastPointInPath).stream().mapToLong(Path::score).min().orElseThrow();

                    // If new path is within 1000 points of this best score, include it
                    //   edge case where two paths may reach the same point, but one has a higher score
                    //   TEMPORARILY until they make a turn and the scores even out
                    if(Math.abs(bestScore - path.score) <= 1000) {
                        allPaths.get(lastPointInPath).add(path);
                    }

                    // If new path is better (by a lot), create new list with new path
                    else if(path.score < bestScore){
                        List<Path> bestPathsForPoint = new ArrayList<>();
                        bestPathsForPoint.add(path);
                        allPaths.put(lastPointInPath, bestPathsForPoint);
                    }
                    // else, new path is worse (by a lot), so drop it
                    else {
                        continue;
                    }
                }
                // If map doesn't already have a path to this point, add it
                else {
                    List<Path> bestPathsForPoint = new ArrayList<>();
                    bestPathsForPoint.add(path);
                    allPaths.put(lastPointInPath, bestPathsForPoint);
                }

                // Point open to EAST and not facing WEST
                if (lines.get(lastPointInPath.vertical).charAt(lastPointInPath.horizontal() + 1)
                    != '#'
                    && path.direction != Direction.WEST) {
                    long newScore = path.score + 1L;
                    if (path.direction != Direction.EAST) {
                        newScore += 1000;
                    }
                    Point p = new Point(lastPointInPath.horizontal + 1, lastPointInPath.vertical);
                    List<Point> points = new ArrayList<>(path.points);
                    // If we've already visited this point on this path, don't continue down this path
                    if(!points.contains(p)) {
                        points.add(p);
                        paths.add(new Path(points, newScore, Direction.EAST));
                    }
                }

                // Point open to WEST and not facing EAST
                if (lines.get(lastPointInPath.vertical).charAt(lastPointInPath.horizontal() - 1)
                    != '#'
                    && path.direction != Direction.EAST) {
                    long newScore = path.score + 1L;
                    if (path.direction != Direction.WEST) {
                        newScore += 1000;
                    }
                    Point p = new Point(lastPointInPath.horizontal - 1, lastPointInPath.vertical);
                    List<Point> points = new ArrayList<>(path.points);
                    if(!points.contains(p)) {
                        points.add(p);
                        paths.add(new Path(points, newScore, Direction.WEST));
                    }
                }

                // Point open to NORTH and not facing SOUTH
                if (lines.get(lastPointInPath.vertical - 1).charAt(lastPointInPath.horizontal())
                    != '#'
                    && path.direction != Direction.SOUTH) {
                    long newScore = path.score + 1L;
                    if (path.direction != Direction.NORTH) {
                        newScore += 1000;
                    }
                    Point p = new Point(lastPointInPath.horizontal, lastPointInPath.vertical - 1);
                    List<Point> points = new ArrayList<>(path.points);
                    if(!points.contains(p)) {
                        points.add(p);
                        paths.add(new Path(points, newScore, Direction.NORTH));
                    }
                }

                // Point open to SOUTH and not facing NORTH
                if (lines.get(lastPointInPath.vertical + 1).charAt(lastPointInPath.horizontal())
                    != '#'
                    && path.direction != Direction.NORTH) {
                    long newScore = path.score + 1L;
                    if (path.direction != Direction.SOUTH) {
                        newScore += 1000;
                    }
                    Point p = new Point(lastPointInPath.horizontal, lastPointInPath.vertical + 1);
                    List<Point> points = new ArrayList<>(path.points);
                    if(!points.contains(p)) {
                        points.add(p);
                        paths.add(new Path(points, newScore, Direction.SOUTH));
                    }
                }
            }

            return allPaths;
        }
    }

    public record Path(List<Point> points, long score, Direction direction) {}

    public record Point(int horizontal, int vertical) {}

    public enum Direction {
        NORTH, SOUTH, EAST, WEST
    }
}
