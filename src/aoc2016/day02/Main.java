package aoc2016.day02;

import java.io.IOException;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "02";

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

        public String calculateP1(List<String> lines) {
            StringBuilder codeBuilder = new StringBuilder();

            int x = 0;
            int y = 0;

            for(String line : lines) {
                for(int i=0; i<line.length(); i++) {
                    char character = line.charAt(i);
                    switch (character) {
                        case 'R' -> x = Math.min(1, x+1);
                        case 'L' -> x = Math.max(-1, x-1);
                        case 'D' -> y = Math.min(1, y+1);
                        case 'U' -> y = Math.max(-1, y-1);
                    }
                }

                codeBuilder.append(((y+1) * 3) + (x+2));
            }

            return codeBuilder.toString();
        }

        public String calculateP2(List<String> lines) {
            StringBuilder codeBuilder = new StringBuilder();

            int x = 0;
            int y = 2;

            char[][] grid = new char[][]{
                {'X','X','1','X','X'},
                {'X','2','3','4','X'},
                {'5','6','7','8','9'},
                {'X','A','B','C','X'},
                {'X','X','D','X','X'},
            };

            for(String line : lines) {
                for(int i=0; i<line.length(); i++) {
                    char character = line.charAt(i);
                    int newX = x;
                    int newY = y;
                    switch (character) {
                        case 'R' -> newX = Math.min(4, newX+1);
                        case 'L' -> newX = Math.max(0, newX-1);
                        case 'D' -> newY = Math.min(4, newY+1);
                        case 'U' -> newY = Math.max(0, newY-1);
                    }
                    if(grid[newY][newX] != 'X') {
                        x = newX;
                        y = newY;
                    }
                }

                codeBuilder.append(grid[y][x]);
            }
            return codeBuilder.toString();
        }
    }
}
