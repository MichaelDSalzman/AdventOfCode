package aoc2022.day09;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String day = "09";

        // FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println ("P2 : " + problem.calculateP2(lines));
    }

    public static class Problem {

        public int calculateP1(List<String> lines) {
            Map<String, Boolean> visitedLocations = new HashMap<>();
            visitedLocations.put("0,0", true);

            int currentHeadRow = 0;
            int currentHeadCol = 0;
            int currentTailRow = 0;
            int currentTailCol = 0;

            for (String line: lines) {
                String[] movement = line.split(" ");
                String direction = movement[0];
                int distance = Integer.parseInt(movement[1]);

                if(direction.equals("R")) {
                    for(int i=0; i<distance; i++) {
                        currentHeadCol++;
                        if (Math.abs(currentHeadCol-currentTailCol) > 1 || Math.abs(currentHeadRow - currentTailRow) > 1) {
                            currentTailRow = currentHeadRow;
                            currentTailCol = currentHeadCol - 1;
                            visitedLocations.put(currentTailRow + "," + currentTailCol, true);
                        }
                    }
                }

                if(direction.equals("L")) {
                    for(int i=0; i<distance; i++) {
                        currentHeadCol--;
                        if (Math.abs(currentHeadCol-currentTailCol) > 1 || Math.abs(currentHeadRow - currentTailRow) > 1) {
                            currentTailRow = currentHeadRow;
                            currentTailCol = currentHeadCol + 1;
                            visitedLocations.put(currentTailRow + "," + currentTailCol, true);
                        }
                    }
                }
                if(direction.equals("U")) {
                    for(int i=0; i<distance; i++) {
                        currentHeadRow--;
                        if (Math.abs(currentHeadCol-currentTailCol) > 1 || Math.abs(currentHeadRow - currentTailRow) > 1) {
                            currentTailRow = currentHeadRow + 1;
                            currentTailCol = currentHeadCol;
                            visitedLocations.put(currentTailRow + "," + currentTailCol, true);
                        }
                    }
                }
                if(direction.equals("D")) {
                    for(int i=0; i<distance; i++) {
                        currentHeadRow++;
                        if (Math.abs(currentHeadCol-currentTailCol) > 1 || Math.abs(currentHeadRow - currentTailRow) > 1) {
                            currentTailRow = currentHeadRow - 1;
                            currentTailCol = currentHeadCol;
                            visitedLocations.put(currentTailRow + "," + currentTailCol, true);
                        }
                    }
                }
            }

            return visitedLocations.size();
        }

        public int calculateP2(List<String> lines) {
            Coordinate head = new Coordinate(0,0);
            List<Coordinate> knotCoordinates = new ArrayList<>();
            for(int i=0; i<9; i++) {
                knotCoordinates.add(new Coordinate(0, 0));
            }

            Set<Coordinate> visitedLocations = new HashSet<>();
            visitedLocations.add(new Coordinate(0,0));

            for (String line: lines) {
                String[] movement = line.split(" ");
                String direction = movement[0];
                int distance = Integer.parseInt(movement[1]);

                for (int distanceIndex=0; distanceIndex < distance; distanceIndex++) {
                    switch (direction) {
                        case "U" -> head.setY(head.getY() + 1);
                        case "D" -> head.setY(head.getY() - 1);
                        case "R" -> head.setX(head.getX() + 1);
                        case "L" -> head.setX(head.getX() - 1);
                    }

                    Coordinate previousKnot = head;
                    for(Coordinate knotCoordinate : knotCoordinates) {
                        int xDiff = previousKnot.getX() - knotCoordinate.getX();
                        int yDiff = previousKnot.getY() - knotCoordinate.getY();

                        // Previous knot and current knot are close enough so no movement is needed
                        if (Math.abs(xDiff) <= 1 && Math.abs(yDiff) <= 1) {
                            // Do nothing
                        } else {
                            // Same row, but need to move left or right
                            if (yDiff == 0) {
                                if (xDiff > 0) {
                                    knotCoordinate.setX(knotCoordinate.getX() + 1);
                                } else {
                                    knotCoordinate.setX(knotCoordinate.getX() - 1);
                                }
                            }

                            // Same column, but need to move up or down
                            else if (xDiff == 0) {
                                if (yDiff > 0) {
                                    knotCoordinate.setY(knotCoordinate.getY() + 1);
                                } else {
                                    knotCoordinate.setY(knotCoordinate.getY() - 1);
                                }
                            }

                            // Need to do a diagonal
                            else {
                                if (xDiff > 0) {
                                    knotCoordinate.setX(knotCoordinate.getX() + 1);
                                } else {
                                    knotCoordinate.setX(knotCoordinate.getX() - 1);
                                }

                                if (yDiff > 0) {
                                    knotCoordinate.setY(knotCoordinate.getY() + 1);
                                } else {
                                    knotCoordinate.setY(knotCoordinate.getY() - 1);
                                }
                            }
                        }

                        previousKnot = knotCoordinate;
                    }

                    visitedLocations.add(knotCoordinates.get(knotCoordinates.size() - 1).clone());
                }
            }

            return visitedLocations.size();
        }

        public static class Coordinate {
            private int x;
            private int y;

            public Coordinate(int x, int y) {
                this.x = x;
                this.y = y;
            }

            public int getX() {
                return x;
            }

            public void setX(int x) {
                this.x = x;
            }

            public int getY() {
                return y;
            }

            public void setY(int y) {
                this.y = y;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                Coordinate that = (Coordinate) o;
                return x == that.x && y == that.y;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }

            @Override
            public String toString() {
                return "Coordinate{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
            }

            public Coordinate clone() {
                return new Coordinate(this.getX(), this.getY());
            }
        }
    }
}
