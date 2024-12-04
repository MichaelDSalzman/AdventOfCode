package aoc2015.day06;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
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

        public int calculateP1(List<String> lines) {

            boolean[][] lights = new boolean[1000][1000];

            Pattern p = Pattern.compile("(.*) (\\d+),(\\d+) through (\\d+),(\\d+)");
            for(String line : lines) {
                Matcher m = p.matcher(line);
                if(m.find()) {
                   String instruction = m.group(1);
                    int startX = Integer.parseInt(m.group(2));
                    int startY = Integer.parseInt(m.group(3));
                    int endX = Integer.parseInt(m.group(4));
                    int endY = Integer.parseInt(m.group(5));

                    for(int y=startY; y<=endY; y++) {
                        for(int x=startX; x<=endX; x++) {
                            if(instruction.equals("turn on")) {
                                lights[x][y] = true;
                            } else if(instruction.equals("turn off")) {
                                lights[x][y] = false;
                            } else if(instruction.equals("toggle")) {
                                lights[x][y] = !lights[x][y];
                            }
                        }
                    }
                }
            }

            int count = 0;
            for(int y=0; y<1000; y++) {
                for(int x=0; x<1000; x++) {
                    if(lights[x][y]) {
                        count++;
                    }
                }
            }

            return count;
        }

        public int calculateP2(List<String> lines) {

            int[][] lights = new int[1000][1000];

            Pattern p = Pattern.compile("(.*) (\\d+),(\\d+) through (\\d+),(\\d+)");
            for(String line : lines) {
                Matcher m = p.matcher(line);
                if(m.find()) {
                    String instruction = m.group(1);
                    int startX = Integer.parseInt(m.group(2));
                    int startY = Integer.parseInt(m.group(3));
                    int endX = Integer.parseInt(m.group(4));
                    int endY = Integer.parseInt(m.group(5));

                    for(int y=startY; y<=endY; y++) {
                        for(int x=startX; x<=endX; x++) {
                            if(instruction.equals("turn on")) {
                                lights[x][y]++;
                            } else if(instruction.equals("turn off")) {
                                lights[x][y]--;
                                if(lights[x][y]<0) {
                                    lights[x][y] = 0;
                                }
                            } else if(instruction.equals("toggle")) {
                                lights[x][y] += 2;
                            }
                        }
                    }
                }
            }

            int brightness = 0;
            for(int y=0; y<1000; y++) {
                for(int x=0; x<1000; x++) {
                    brightness += lights[x][y];
                }
            }

            return brightness;
        }
    }
}
