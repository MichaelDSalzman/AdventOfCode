package aoc2024.day14;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws Exception {
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
            // int width = 11;
            // int height = 7;
            int width = 101;
            int height = 103;

            List<Robot> robots = Robot.valueOf(lines);

            List<Point> finalLocations = robots.stream().map(r -> calculateFinalLocation(r, 100, width, height)).toList();
            return getRobotsInQuadrant(finalLocations, width, height).stream().reduce(1, (a, b) -> a * b);
        }

        public Object calculateP2(List<String> lines) throws IOException, InterruptedException {
            // int width = 11;
            // int height = 7;
            int width = 101;
            int height = 103;

            List<Robot> robots = Robot.valueOf(lines);

            int count = 0;
            while(true) {
                boolean interesting = printRobotsIfInteresting(
                    robots.stream().map(Robot::position).toList(), width, height);

                if(interesting) {
                    return count;
                }

                count++;
                robots = robots.stream().map(r -> calculateNextLocation(r, width, height)).toList();
            }
        }

        public Robot calculateNextLocation(Robot robot, int mazeWidth, int mazeHeight) {
            return new Robot(calculateFinalLocation(robot, 1, mazeWidth, mazeHeight), robot.movement);
        }

        public Point calculateFinalLocation(Robot robot, int iterations, int mazeWidth, int mazeHeight) {
            int newHorizontal = (robot.position.horizontal + (iterations * robot.movement.horizontal) + iterations*mazeWidth) % mazeWidth;
            int newVertical = (robot.position.vertical + (iterations * robot.movement.vertical) + iterations*mazeHeight) % mazeHeight;

            return new Point(newHorizontal, newVertical);
        }

        public List<Integer> getRobotsInQuadrant(List<Point> points, int width, int height) {
            int upperLeft = 0;
            int upperRight = 0;
            int lowerLeft = 0;
            int lowerRight = 0;

            for(Point point : points) {
                if(point.horizontal < width/2) {
                    if(point.vertical < height/2) {
                        upperLeft++;
                    } else if(point.vertical > height /2) {
                        lowerLeft++;
                    }
                } else if(point.horizontal > width/2) {
                    if(point.vertical < height/2) {
                        upperRight++;
                    } else if(point.vertical > height /2) {
                        lowerRight++;
                    }
                }
            }

            return List.of(upperLeft, upperRight, lowerLeft, lowerRight);
        }

        private boolean printRobotsIfInteresting(List<Point> points, int mazeWidth, int mazeHeight) {
            List<StringBuilder> lines = new ArrayList<>();
            for(int height=0; height<mazeHeight; height++) {
                StringBuilder builder = new StringBuilder(mazeWidth);
                lines.add(builder);
                builder.append(" ".repeat(mazeWidth));
            }

            for(Point point : points) {
                lines.get(point.vertical).setCharAt(point.horizontal, '#');
            }

            Pattern p = Pattern.compile("#########");
            boolean interesting = false;
            for(StringBuilder line : lines) {
                interesting = p.matcher(line.toString()).find();
                if(interesting) {
                    break;
                }
            }

            if(interesting) {
                System.out.println("-----------------");
                System.out.println(lines.stream().map(StringBuilder::toString)
                    .reduce(" ", (a, b) -> a + "\n" + b));
            }

            return interesting;
        }
    }

    record Point(int horizontal, int vertical) {}
    record Movement(int horizontal, int vertical) {}
    record Robot(Point position, Movement movement) {
        public static List<Robot> valueOf(List<String> lines) {
            List<Robot> robots = new ArrayList<>();

            Pattern p = Pattern.compile("p=(-?\\d+),(-?\\d+)\\s+v=(-?\\d+),(-?\\d+)");
            for(String line : lines) {
                Matcher m = p.matcher(line);
                if(m.find()) {
                    robots.add(new Robot(
                            new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))),
                            new Movement(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)))
                        )
                    );
                } else {
                    System.out.println("PARSE ERROR: " + line);
                }
            }

            return robots;
        }
    }
}
