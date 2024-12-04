package aoc2015.day23;

import java.io.IOException;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "23";

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
            int a = 1;
            int b = 0;
            int index = 0;
            int count = 0;
            while(true) {
                count++;
                String line = lines.get(index);

                if(line.startsWith("jio")) {
                    String register = line.split(",")[0].split(" ")[1];
                    if(register.equals("a") && a == 1) {
                        Integer distance = Integer.parseInt(line.split(" ")[2]);
                        index += distance;
                    } else if(register.equals("b") && b == 1) {
                        Integer distance = Integer.parseInt(line.split(" ")[2]);
                        index += distance;
                    } else {
                        index++;
                    }
                } else if(line.startsWith("jie")) {
                    String register = line.split(",")[0].split(" ")[1];
                    if(register.equals("a") && a % 2 == 0) {
                        Integer distance = Integer.parseInt(line.split(" ")[2]);
                        index += distance;
                    } else if(register.equals("b") && b % 2 == 0) {
                        Integer distance = Integer.parseInt(line.split(" ")[2]);
                        index += distance;
                    } else {
                        index++;
                    }
                } else if(line.startsWith("inc")) {
                    String register = line.split(" ")[1];
                    if(register.equals("a")) {
                        a++;
                    } else {
                        b++;
                    }
                    index++;
                } else if(line.startsWith("tpl")) {
                    String register = line.split(" ")[1];
                    if(register.equals("a")) {
                        a*=3;
                    } else {
                        b*=3;
                    }
                    index++;
                } else if(line.startsWith("jmp")) {
                    Integer distance = Integer.parseInt(line.split(" ")[1]);
                    index += distance;
                } else if(line.startsWith("hlf")) {
                    String register = line.split(" ")[1];
                    if(register.equals("a")) {
                        a/=2;
                    } else {
                        b/=2;
                    }
                    index++;
                } else {
                    throw new RuntimeException("WHAT IS THIS " + line);
                }

                if(index >= lines.size()) {
                    System.out.println(count);
                    return b;
                }
            }
        }

        public int calculateP2(List<String> lines) {
            return -1;
        }
    }
}
