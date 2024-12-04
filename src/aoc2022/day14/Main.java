package aoc2022.day14;

import java.io.IOException;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2022";
        String day = "14";

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
            String[][] map = generateMap(lines, false);

            int sandCounter = 0;

            try {
                while (true) {
                    int sandLocationX = 500;
                    int sandLocationY = 0;
                    boolean sandFalling = true;
                    sandCounter++;
                    while (sandFalling) {
                        if (map[sandLocationY][sandLocationX] == null) {
                            if (map[sandLocationY + 1][sandLocationX] == null) {
                                sandLocationY++;
                            } else if (map[sandLocationY + 1][sandLocationX - 1] == null) {
                                sandLocationY++;
                                sandLocationX--;
                            } else if (map[sandLocationY + 1][sandLocationX + 1] == null) {
                                sandLocationY++;
                                sandLocationX++;
                            } else {
                                map[sandLocationY][sandLocationX] = ".";
                                sandFalling = false;
                            }
                        }
                    }
                }
            } catch(Exception ex) {
                drawMap(map);
                return sandCounter-1;
            }
        }

        public int calculateP2(List<String> lines) {
            String[][] map = generateMap(lines, true);

            int sandCounter = 0;

            while(true) {
                int sandLocationX = 500;
                int sandLocationY = 0;
                boolean sandFalling = true;
                sandCounter++;
                while(sandFalling) {
                    if (map[sandLocationY][sandLocationX] == null) {
                        if(map[sandLocationY+1][sandLocationX] == null) {
                            sandLocationY++;
                        } else if (map[sandLocationY+1][sandLocationX-1] == null) {
                            sandLocationY++;
                            sandLocationX--;
                        } else if (map[sandLocationY+1][sandLocationX+1] == null) {
                            sandLocationY++;
                            sandLocationX++;
                        } else {
                            map[sandLocationY][sandLocationX] = ".";
                            sandFalling = false;
                        }
                    }
                    else {
                        drawMap(map);
                        return sandCounter - 1;
                    }
                }
            }
        }

        private String[][] generateMap(List<String> lines, boolean includeFloor) {

            // find the lowest (highest y) spot
            int lowest = Integer.MIN_VALUE;
            for(String line : lines) {
                String[] coordsList = line.split("->");
                for(String coords : coordsList ) {
                    String[] coordParts = coords.trim().split(",");
                    int y = Integer.parseInt(coordParts[1]);
                    if(y > lowest) {
                        lowest = y;
                    }
                }
            }

            // width will be 500 + lowest spot + 10 (for good measure)
            // since 500,0 is where sand will drop and sand spreads like a pyramid
            int width = 500 + lowest + 10;

            // Flipping array so first index is y and second is x because that's closer to how we visualize it
            String[][] map = new String[lowest+3][width];

            for(String line : lines) {
                String[] coordsList = line.split("->");
                Integer previousCoordX = null;
                Integer previousCoordY = null;
                for(String coords : coordsList ) {
                    String[] coordParts = coords.trim().split(",");

                    int x = Integer.parseInt(coordParts[0]);
                    int y = Integer.parseInt(coordParts[1]);
                    map[y][x] = "R";
                    if(previousCoordX != null) {
                        if(x-previousCoordX != 0) {
                            int diff = previousCoordX-x;
                            for(int i=0; i<=Math.abs(diff); i++) {
                                int newX = x+(i * (diff<0 ? -1 : 1));
                                map[y][newX] = "R";
                            }
                        } else if(y-previousCoordY != 0) {
                            int diff = previousCoordY-y;
                            for(int i=0; i<=Math.abs(diff); i++) {
                                int newY = y+(i * (diff<0 ? -1 : 1));
                                map[newY][x] = "R";
                            }
                        }
                    }

                    previousCoordX = x;
                    previousCoordY = y;
                }
            }

            if(includeFloor) {
                for (int i = 0; i < map[lowest].length; i++) {
                    map[lowest+2][i] = "R";
                }
            }

            return map;
        }

        private void drawMap(String[][] map) {
            for(int i=0; i<map.length; i++) {
                String row = "";
                for(int j=0; j<map[i].length; j++) {
                    if(map[i][j] == null) {
                        row += " ";
                    } else {
                        row += map[i][j];
                    }
                }
                System.out.println(row);
            }
        }
    }
}
