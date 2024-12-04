package aoc2015.day05;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "05";

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
            int numNice = 0;
            Pattern doubleChar = Pattern.compile("(\\w)\\1+");
            for(String line : lines) {
                if(line.contains("ab") || line.contains("cd")|| line.contains("pq")|| line.contains("xy")) {
                    continue;
                }
                if(!doubleChar.matcher(line).find()) {
                    continue;
                }

                int vowelCount=countCharacter(line, 'a')+countCharacter(line, 'e')+countCharacter(line, 'i')+countCharacter(line, 'o')+countCharacter(line, 'u');

                if(vowelCount<3) {
                    continue;
                }
                numNice++;
            }

            return numNice;
        }

        private int countCharacter(String line, char character){
            int count = 0;
            for(char currentChar : line.toCharArray()) {
                if(currentChar == character) {
                    count++;
                }
            }
            return count;
        }

        public int calculateP2(List<String> lines) {
            int numNice = 0;
            for(String line : lines) {
                boolean foundRepeat = false;
                for(int i=0; i<line.length()-1; i++){
                    String stringToCheck = line.substring(i, i+2);
                    if(line.indexOf(stringToCheck) + 1 < line.lastIndexOf(stringToCheck)) {
                        foundRepeat = true;
                        break;
                    }
                }
                if(!foundRepeat) {
                    continue;
                }

                boolean foundSandwich = false;
                for(int i=0; i<line.length()-2; i++){
                    if(line.charAt(i) == line.charAt(i+2)) {
                        foundSandwich = true;
                        break;
                    }
                }

                if(foundSandwich && foundRepeat) {
                    numNice++;
                }
            }

            return numNice;
        }
    }
}
