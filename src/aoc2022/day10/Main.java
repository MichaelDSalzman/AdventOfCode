package aoc2022.day10;

import java.io.IOException;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String day = "10";

        // FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println ("P2 : " + problem.calculateP2(lines));
    }

    public static class Problem {

        public int calculateP1(List<String> lines) {
            int total = 0;
            int register = 1;
            int tick = 0;

            for (String line : lines) {
                int timeOpTakes = 0;
                int increment = 0;
                if (line.equals("noop")) {
                    timeOpTakes = 1;
                    increment = 0;
                } else if (line.startsWith("addx")) {
                    timeOpTakes = 2;
                    increment = Integer.parseInt(line.split(" ")[1]);
                }

                for(int i=0; i<timeOpTakes; i++) {
                    tick++;
                    if((tick + 20) % 40 == 0) {
                        System.out.println ("TICK " + tick + " REGISTER " + register);
                        int signalStrength = tick * register;
                        System.out.println("SS " + signalStrength);
                        total += signalStrength;
                    }
                }
                register += increment;
            }

            return total;
        }

        public int calculateP2(List<String> lines) {
            int register = 1;
            int tick = 0;
            StringBuilder sb = new StringBuilder();

            for (String line : lines) {
                int timeOpTakes = 0;
                int increment = 0;
                if (line.equals("noop")) {
                    timeOpTakes = 1;
                    increment = 0;
                } else if (line.startsWith("addx")) {
                    timeOpTakes = 2;
                    increment = Integer.parseInt(line.split(" ")[1]);
                }

                for(int i=0; i<timeOpTakes; i++) {
                    if(Math.abs((tick%40)-register) <= 1) {
                        sb.append("#");
                    } else {
                        sb.append(".");
                    }
                    tick++;
                    if(tick % 40 == 0) {
                        System.out.println(sb);
                        sb = new StringBuilder();
                    }
                }
                register += increment;
            }

            return -1;
        }
    }
}
