package aoc2024.day06;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2024";
        String day = "06";

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

        // Traverse a maze and calculate how many spots the guard visits. When the guard
        //  hits an obstacle, rotate 90 degrees clockwise
        public int calculateP1(List<String> lines) {
            Maze maze = buildMap(lines);
            Guard g = findGuard(lines);

            try {
                return runThroughMap(maze, g).size();
            } catch (StuckInLoopException e) {
                throw new RuntimeException(e);
            }
        }

        // Traverse a maze and calculate how many obstacles could be placed in the guard's
        //  path to put him into an infinite loop. Same movement rules as P1
        public int calculateP2(List<String> lines) {

            // Find all the spots the guard will take. Iterate over the spots, add an obstacle
            //  there and see if traversing the maze from the starting point results in a loop
            Maze originalMaze = buildMap(lines);
            Guard originalGuard = findGuard(lines);
            Point startingPoint = originalGuard.getLocation();
            Set<Point> allVisitedPoints;
            allVisitedPoints = runThroughMap(originalMaze, originalGuard.clone());

            // Can't put an obstacle on starting point
            allVisitedPoints.remove(startingPoint);

            int obstaclesThatCauseLoop = 0;

            for (Point potentialObstacle : allVisitedPoints) {
                Set<Point> updatedObstacles = new HashSet<>(originalMaze.obstacles);
                updatedObstacles.add(potentialObstacle);
                Maze updatedMaze = new Maze(originalMaze.horizontalSize,
                    originalMaze.verticalSize,
                    updatedObstacles);

                try {
                    runThroughMap(updatedMaze, originalGuard.clone());
                } catch (StuckInLoopException e) {
                    obstaclesThatCauseLoop++;
                }
            }

            return obstaclesThatCauseLoop;
        }

        // Convert the list of strings into a list of obstacles and size
        private Maze buildMap(List<String> lines) {
            Set<Point> obstacles = new HashSet<>();
            for(int horizontal=0; horizontal<lines.get(0).length(); horizontal++) {
                for(int vertical=0; vertical<lines.size(); vertical++) {
                    char c = lines.get(vertical).charAt(horizontal);
                    if(c == '#') {
                        obstacles.add(new Point(horizontal, vertical));
                    }
                }
            }
            return new Maze(lines.get(0).length(), lines.size(), obstacles);
        }

        // Find where the guard starts
        private Guard findGuard(List<String> lines) {
            for(int horizontal=0; horizontal<lines.get(0).length(); horizontal++) {
                for(int vertical=0; vertical<lines.size(); vertical++) {
                    char c = lines.get(vertical).charAt(horizontal);
                    if(c != '.' && c != '#') {
                        return new Guard(new Point(horizontal, vertical), Direction.valueOf(c));
                    }
                }
            }

            throw new RuntimeException("Guard not found");
        }

        // Given a current location and direction, what's the next point in that direction
        private Point getNextPoint(Point currentPoint, Direction direction) {
            switch (direction) {
                case UP -> {
                    return new Point(currentPoint.horizontal, currentPoint.vertical-1);
                }
                case DOWN -> {
                    return new Point(currentPoint.horizontal, currentPoint.vertical+1);
                }
                case LEFT -> {
                    return new Point(currentPoint.horizontal-1, currentPoint.vertical);
                }
                case RIGHT -> {
                    return new Point(currentPoint.horizontal+1, currentPoint.vertical);
                }
            }
            throw new RuntimeException("SOMETHING BAD");
        }

        // Is there an obstacle at a certain point
        private boolean obstacleAtPoint(Maze maze, Point p) {
            return maze.obstacles.contains(p);
        }

        // Is the point gone off the maze boundaries
        private boolean isPointOffMaze(Maze maze, Point p) {
            return p.horizontal < 0
                || p.horizontal >= maze.horizontalSize
                || p.vertical < 0
                || p.vertical >= maze.verticalSize;
        }

        // Move the guard through the maze and return a set of all the points he visits (including start)
        //  If guard gets stuck in loop (visited same point twice going the same direction), throw
        //  a StuckInLoopException

        private Set<Point> runThroughMap(Maze maze, Guard g) throws StuckInLoopException {
            // Keeping track of spots that have been visited, but also what direction
            //  the guard was traveling through that spot when it was visited.
            //  If the guard visits the same spot going the same direction, he's stuck in a loop
            //  If the guard visits the same spot, but is going a different direction,
            //  he's still traveling
            Map<Point, List<Direction>> visitedSpots = new HashMap<>();
            visitedSpots.compute(g.getLocation(), (p, l) -> {
                if(l == null) {
                    l = new ArrayList<>();
                }
                l.add(g.getDirection());
                return l;
            });

            boolean stillOnMap = true;
            while(stillOnMap) {
                Point nextPoint = getNextPoint(g.getLocation(), g.getDirection());
                if(isPointOffMaze(maze, nextPoint)) {
                    stillOnMap = false;
                } else {
                    if (obstacleAtPoint(maze, nextPoint)) {
                        g.rotate();
                    } else if(visitedSpots.containsKey(nextPoint)
                        && visitedSpots.get(nextPoint).contains(g.getDirection())) {

                        throw new StuckInLoopException();
                    }
                    else {
                        g.setLocation(nextPoint);
                        visitedSpots.compute(nextPoint, (p, l) -> {
                            if(l == null) {
                                l = new ArrayList<>();
                            }
                            l.add(g.getDirection());
                            return l;
                        });
                    }
                }
            }

            return visitedSpots.keySet();
        }
    }

    record Point(int horizontal, int vertical){ }

    enum Direction {
        UP('^'),
        DOWN('v'),
        LEFT('<'),
        RIGHT('>');

        private final Character representation;

        Direction(Character representation) {
            this.representation = representation;
        }

        public static Direction valueOf(Character c) {
            return Arrays.stream(Direction.values())
                .filter(d -> d.representation == c)
                .findFirst()
                .orElse(null);
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static
    class Guard{
        @NonNull
        private Point location;
        @NonNull
        private Direction direction;

        public void rotate() {
            switch(direction){
                case UP -> direction = Direction.RIGHT;
                case RIGHT -> direction = Direction.DOWN;
                case DOWN -> direction = Direction.LEFT;
                case LEFT -> direction = Direction.UP;
            }
        }

        @Override
        protected Guard clone() {
            return new Guard(this.location, this.direction);
        }
    }

    record Maze(int horizontalSize, int verticalSize, Set<Point> obstacles){}

    static class StuckInLoopException extends RuntimeException{}

}
