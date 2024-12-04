package aoc2016.day01;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "01";

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

            String[] directions = lines.get(0).split(", ");
            int x = 0;
            int y = 0;
            int direction = 0; //N = 0, E = 1, S = 2, W = 3
            for(String instruction : directions) {
                if(instruction.charAt(0) == 'R') {
                    direction = (direction+1)%4;
                } else {
                    direction = (direction+3)%4;
                }

                int distance = Integer.parseInt(instruction.substring(1));
                switch (direction) {
                    case 0 -> y+=distance;
                    case 1 -> x+=distance;
                    case 2 -> y-=distance;
                    case 3 -> x-=distance;
                }
            }
            return Math.abs(x) + Math.abs(y);
        }

        public int calculateP2(List<String> lines) {

            String[] directions = lines.get(0).split(", ");
            int x = 0;
            int y = 0;
            int direction = 0; //N = 0, E = 1, S = 2, W = 3
            Set<String> visitedLocations = new HashSet<>();
            visitedLocations.add("0,0");
            for(String instruction : directions) {
                if(instruction.charAt(0) == 'R') {
                    direction = (direction+1)%4;
                } else {
                    direction = (direction+3)%4;
                }

                int distance = Integer.parseInt(instruction.substring(1));
                for(int i=0; i<distance; i++) {
                    switch (direction) {
                        case 0 -> y ++;
                        case 1 -> x ++;
                        case 2 -> y --;
                        case 3 -> x --;
                    }
                    if(visitedLocations.contains(String.format("%d,%d", x, y))) {
                        return Math.abs(x) + Math.abs(y);
                    }

                    visitedLocations.add(String.format("%d,%d", x, y));
                }
            }
            return -1;
        }
    }
}
