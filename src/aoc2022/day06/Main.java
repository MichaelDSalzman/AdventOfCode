package aoc2022.day06;

import java.io.IOException;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String day = "06";

        // FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println ("P2 : " + problem.calculateP2(lines));
    }

    public static class Problem {

        public int calculateP1(List<String> lines) {
            for(String line : lines) {
                for(int i=3; i<line.length(); i++) {
                    String subString = line.substring(i-3, i+1);
                    if (!duplicateCharsFound(subString)) {
                        System.out.println(i + 1);
                        i=line.length();
                    }
                }
            }

            return -1;
        }

        private boolean duplicateCharsFound(String string) {
            for(int i=0; i<string.length(); i++) {
                for(int j=i+1; j<string.length(); j++) {
                    if(string.charAt(i) == string.charAt(j)) {
                        return true;
                    }
                }
            }

            return false;
        }
        public int calculateP2(List<String> lines) {
            for(String line : lines) {
                for(int i=13; i<line.length(); i++) {
                    String subString = line.substring(i-13, i+1);
                    if (!duplicateCharsFound(subString)) {
                        System.out.println(i + 1);
                        i=line.length();
                    }
                }
            }

            return -1;
        }
    }
}
