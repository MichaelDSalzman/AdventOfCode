package aoc2024.day15;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
            Room room = Room.valueOf(lines, false);
            Directions directions = Directions.valueOf(lines);

            executeProgram(room, directions);
            return room.getSumOfGps();
        }

        public Object calculateP2(List<String> lines) {

            Room room = Room.valueOf(lines, true);
            Directions directions = Directions.valueOf(lines);

            executeProgram(room, directions);

            return room.getSumOfGps();
        }

        private void executeProgram(Room room, Directions directions) {
            for(Direction direction : directions.directions) {
                // room.print(direction);
                Point robotLocation = room.robot;
                if(canMoveInDirection(room, robotLocation, direction)) {
                    move(room, robotLocation, direction);
                }
            }
            // room.print(null);
        }

        private boolean canMoveInDirection(Room room, Point location, Direction direction) {
            Point desiredLocation = location.getPointInDirection(direction);

            if(room.isBoxAtLocation(desiredLocation)) {
                Box box = room.getBoxAtLocation(desiredLocation);
                if(box.points.contains(location) && box.points.contains(desiredLocation)) {
                    return true;
                }
                return box.points.stream().allMatch(boxPoint -> canMoveInDirection(room, boxPoint, direction));
            } else if (room.isWallAtLocation(desiredLocation)) {
                return false;
            } else {
                return true;
            }
        }

        private void move(Room room, Point location, Direction direction) {
            Set<Box> boxesToMove = findAllBoxesThatWillBeMoved(room, location, direction);
            room.moveBoxes(boxesToMove, direction);
            room.moveRobot(direction);
        }

        private Set<Box> findAllBoxesThatWillBeMoved(Room room, Point location, Direction direction) {
            Set<Box> boxes = new HashSet<>();
            Point desiredLocation = location.getPointInDirection(direction);
            if(room.isBoxAtLocation(desiredLocation)) {
                Box box = room.getBoxAtLocation(desiredLocation);
                boxes.add(box);
                box.points.stream()
                    .filter(p -> !box.points.contains(p.getPointInDirection(direction)))
                    .map(p -> findAllBoxesThatWillBeMoved(room, p, direction))
                    .forEach(boxes::addAll);
            }
            return boxes;
        }
    }

    public record Point(int horizontal, int vertical){
        public Point getPointInDirection(Direction direction) {
            Point desiredLocation = null;

            switch (direction) {
                case NORTH -> desiredLocation = new Point(horizontal, vertical-1);
                case SOUTH -> desiredLocation = new Point(horizontal, vertical+1);
                case EAST -> desiredLocation = new Point(horizontal+1, vertical);
                case WEST -> desiredLocation = new Point(horizontal-1, vertical);
            }

            return desiredLocation;
        }
    }

    public record Box(Set<Point> points) {
        public Point getLeftMost() {
            Point leftMost = null;
            for(Point point : points) {
                if(leftMost == null) {
                    leftMost = point;
                } else if (point.horizontal < leftMost.horizontal) {
                    leftMost = point;
                }
            }
            return leftMost;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Room {
        private Point robot;
        private Set<Box> boxes;
        private Set<Point> walls;

        public boolean isWallAtLocation(Point location) {
            return walls.contains(location);
        }

        public boolean isBoxAtLocation(Point location) {
            return boxes.stream().anyMatch(b -> b.points.contains(location));
        }

        public static Room valueOf(List<String> lines, boolean doubleInSize) {
            Point robot = null;
            Set<Box> boxes = new HashSet<>();
            Set<Point> walls = new HashSet<>();

            for(int vertical=0; vertical<lines.size(); vertical++) {
                String line = lines.get(vertical);
                if(!line.startsWith("#")) {
                    continue;
                }
                for(int horizontal=0; horizontal<line.length(); horizontal++) {
                    String value = line.substring(horizontal, horizontal+1);
                    if(!doubleInSize) {
                        if (value.equals("#")) {
                            walls.add(new Point(horizontal, vertical));
                        } else if(value.equals("@")) {
                            robot = new Point(horizontal, vertical);
                        } else if(value.equals("O")) {
                            Point boxPoint = new Point(horizontal, vertical);
                            boxes.add(new Box(Set.of(boxPoint)));
                        }
                    } else {
                        if (value.equals("#")) {
                            walls.add(new Point(2*horizontal, vertical));
                            walls.add(new Point(2*horizontal + 1, vertical));
                        } else if(value.equals("@")) {
                            robot = new Point(2*horizontal, vertical);
                        } else if(value.equals("O")) {
                            Point boxPoint1 = new Point(2*horizontal, vertical);
                            Point boxPoint2 = new Point(2*horizontal+1, vertical);
                            boxes.add(new Box(Set.of(boxPoint1, boxPoint2)));
                        }
                    }
                }
            }

            return new Room(robot, boxes, walls);
        }

        public long getSumOfGps() {
            long sum = 0;
            for(Box box : boxes) {
                Point leftMost = box.getLeftMost();

                sum += (100L * leftMost.vertical + leftMost.horizontal);
            }

            return sum;
        }

        public void moveRobot(Direction direction) {
            robot = robot.getPointInDirection(direction);
        }

        public void moveBoxes(Set<Box> boxes, Direction direction) {
            Set<Box> newBoxes = boxes.stream()
                .map(box ->
                    new Box(box.points.stream()
                        .map(p -> p.getPointInDirection(direction))
                        .collect(Collectors.toSet())))
                .collect(Collectors.toSet());

            this.boxes.removeAll(boxes);
            this.boxes.addAll(newBoxes);
        }

        public Box getBoxAtLocation(Point location) {
            return boxes.stream().filter(b -> b.points.contains(location)).findFirst().orElse(null);
        }

        public void print(Direction d) {
            System.out.println(d);
            for(int vertical=0; vertical<=walls.stream().map(Point::vertical).mapToInt(Integer::intValue).max().orElseThrow(); vertical++) {
                StringBuilder sb = new StringBuilder();
                for(int horizontal=0; horizontal<=walls.stream().map(Point::horizontal).mapToInt(Integer::intValue).max().orElseThrow(); horizontal++) {
                    Point p = new Point(horizontal, vertical);
                    if(this.isBoxAtLocation(p)) {
                        Box b = getBoxAtLocation(p);
                        if(b.points.size() == 1) {
                            sb.append("O");
                        } else if(b.getLeftMost().equals(p)) {
                            sb.append("[");
                        } else {
                            sb.append("]");
                        }
                    } else if(this.isWallAtLocation(p)) {
                        sb.append("#");
                    } else if(Objects.equals(this.robot, p)) {
                        sb.append("@");
                    } else {
                        sb.append(".");
                    }
                }
                System.out.println(sb);
            }
        }
    }

    public enum Direction {
        NORTH, SOUTH, EAST, WEST
    }
    public record Directions(List<Direction> directions) {

        public static Directions valueOf(List<String> lines) {
            List<Direction> directions = new ArrayList<>();

            for(String line : lines) {
                if(line.contains("#")) {
                    continue;
                }

                for(int c=0; c<line.length(); c++) {
                    switch(line.charAt(c)) {
                        case '<' -> directions.add(Direction.WEST);
                        case '>' -> directions.add(Direction.EAST);
                        case 'v' -> directions.add(Direction.SOUTH);
                        case '^' -> directions.add(Direction.NORTH);
                    }
                }
            }

            return new Directions(directions);
        }
    }
}
