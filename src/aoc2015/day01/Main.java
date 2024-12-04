package aoc2015.day01;

import java.io.IOException;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
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
            int currentFloor = 0;
            String elevatorMap = lines.get(0);
            for(int i=0; i< elevatorMap.length(); i++) {
                if(elevatorMap.charAt(i) == '(') {
                    currentFloor++;
                } else if(elevatorMap.charAt(i) == ')') {
                    currentFloor--;
                }

                if(currentFloor == -1) {
                    return i+1;
                }
            }
            return currentFloor;
        }

        public int calculateP2(List<String> lines) {
            return -1;
        }
    }
}
