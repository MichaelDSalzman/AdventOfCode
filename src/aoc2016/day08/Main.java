package aoc2016.day08;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "08";

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
            boolean[][] lights = calculate(lines);
            int sum = 0;
            for(int i=0; i<lights.length; i++) {
                for(int j=0; j<lights[i].length; j++) {
                    if(lights[i][j]) {
                        sum++;
                    }
                }
            }
            return sum;
        }

        public int calculateP2(List<String> lines) {
            boolean[][] lights = calculate(lines);
            for(int i=0; i<lights.length; i++) {
                StringBuilder sb = new StringBuilder();
                for(int j=0; j<lights[i].length; j++) {
                    if(lights[i][j]) {
                        sb.append('*');
                    } else {
                        sb.append(" ");
                    }
                }
                System.out.println(sb);
            }

            return -1;
        }

        private  boolean[][] calculate(List<String> lines) {

            boolean[][] lights = new boolean[6][50];
            // boolean[][] lights = new boolean[3][7];
            Pattern rectP = Pattern.compile("rect (\\d+)x(\\d+)");
            Pattern rotateRowP = Pattern.compile("rotate row y=(\\d+) by (\\d+)");
            Pattern rotateColP = Pattern.compile("rotate column x=(\\d+) by (\\d+)");
            for(String line : lines) {
                if(line.startsWith("rect")) {
                    Matcher rectM = rectP.matcher(line);
                    if(rectM.find()) {
                        int a = Integer.parseInt(rectM.group(1));
                        int b = Integer.parseInt(rectM.group(2));

                        for(int i=0; i<b; i++) {
                            for(int j=0; j<a; j++) {
                                lights[i][j] = true;
                            }
                        }
                    } else {
                        throw new RuntimeException("Boo");
                    }
                } else if(line.startsWith("rotate row")) {
                    Matcher m = rotateRowP.matcher(line);
                    if(m.find()) {
                        int rowNum = Integer.parseInt(m.group(1));
                        int distance = Integer.parseInt(m.group(2));
                        List<Integer> lightsLit = new ArrayList<>();
                        for(int i=0; i<lights[rowNum].length; i++) {
                            if(lights[rowNum][i]) {
                                lightsLit.add(i);
                                lights[rowNum][i] = false;
                            }
                        }
                        for(Integer lightToTurnOn : lightsLit) {
                            lightToTurnOn = (lightToTurnOn + distance) % lights[rowNum].length;
                            lights[rowNum][lightToTurnOn] = true;
                        }
                    } else {
                        throw new RuntimeException("Boo");
                    }
                } else if(line.startsWith("rotate column")) {
                    Matcher m = rotateColP.matcher(line);
                    if(m.find()) {
                        int colNum = Integer.parseInt(m.group(1));
                        int distance = Integer.parseInt(m.group(2));
                        List<Integer> lightsLit = new ArrayList<>();
                        for(int i=0; i<lights.length; i++) {
                            if(lights[i][colNum]) {
                                lightsLit.add(i);
                                lights[i][colNum] = false;
                            }
                        }

                        for(Integer lightToTurnOn : lightsLit) {
                            lightToTurnOn = (lightToTurnOn + distance) % lights.length;
                            lights[lightToTurnOn][colNum] = true;
                        }

                    } else {
                        throw new RuntimeException("Boo");
                    }
                } else {
                    throw new RuntimeException("Boo");
                }
            }

            return lights;
        }
    }
}
