package aoc2024.day18;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
            // return findShortestPath(lines, 7, 12);
            return findShortestPath(lines, 71, 1024);
        }

        public Object calculateP2(List<String> lines) {
            // return findPointThatBlocksExit(lines, 7, 12);
            return findPointThatBlocksExit(lines, 71, 1024);
        }

        // Find the byte that blocks off the exit completely by doing a binary search
        public String findPointThatBlocksExit(List<String> lines, int width, int startingNumBytesFallen) {
            int min = startingNumBytesFallen;
            int max = lines.size();

            int numGuesses = 0;
            while(true) {
                numGuesses++;
                int guess = (min+max)/2;
                int answer = findShortestPath(lines, width, guess);
                if(answer == -1) {
                    max = guess;
                } else {
                    min = guess;
                }

                if(min+1 == max) {
                    System.out.println("NUM GUESSES: " + numGuesses);
                    return lines.get(min);
                }
            }
        }

        // Find the shortest number of steps to go from 0,0 to (width-1,width-1)
        public int findShortestPath(List<String> lines, int width, int numBytesFallen) {
            Set<Point> grid = new HashSet<>();

            for(int i=0; i< numBytesFallen; i++) {
                String line = lines.get(i);
                List<Integer> coords = Arrays.stream(line.split(",")).map(Integer::parseInt).toList();
                grid.add(new Point(coords.get(0), coords.get(1)));
            }

            Map<Point, Integer> shortestPathToPoint = new HashMap<>();
            Queue<PointWithDistance> queue = new ArrayDeque<>();
            queue.add(new PointWithDistance(new Point(0,0),0));
            while(!queue.isEmpty()) {
                PointWithDistance pwd = queue.poll();
                if(shortestPathToPoint.containsKey(pwd.point) && shortestPathToPoint.get(pwd.point) < pwd.distance) {
                    continue;
                } else {
                    shortestPathToPoint.put(pwd.point, pwd.distance);
                }

                // Get orthogonal coordinates that are within grid
                for(int i=-1; i<=1; i++) {
                    for(int j=-1; j<=1; j++) {
                        if(Math.abs(i)-Math.abs(j) == 0) {
                            continue;
                        }
                        int newHorizontal = j+pwd.point.horizontal;
                        int newVertical = i+pwd.point.vertical;
                        if(newHorizontal < 0 || newVertical < 0 || newHorizontal >= width || newVertical >= width) {
                            continue;
                        }

                        // If new orthogonal point is not blocked
                        Point newPoint = new Point(newHorizontal, newVertical);
                        if(grid.contains(newPoint)) {
                            continue;
                        }
                        // If new orthogonal point is not already being considered, add it to queue
                        PointWithDistance newPwd = new PointWithDistance(newPoint, pwd.distance + 1);
                        if(!queue.contains(newPwd)) {
                            queue.add(newPwd);
                        }
                    }
                }
            }

            return shortestPathToPoint.getOrDefault(new Point(width-1, width-1), -1);
        }
    }

    public record Point(int horizontal, int vertical){}
    public record PointWithDistance(Point point, int distance){}
}
