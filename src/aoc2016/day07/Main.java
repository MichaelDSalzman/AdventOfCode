package aoc2016.day07;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "07";

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

            // Pattern bracketsPattern = Pattern.compile("\\[(\\w+)]");
            // Pattern abbaPattern = Pattern.compile("(.)(.)\\2\\1");
            // int count = 0;
            // for(String line : lines) {
            //     Matcher bracketsMatcher = bracketsPattern.matcher(line);
            //     boolean foundAbbaInBracket = false;
            //     while(bracketsMatcher.find()) {
            //         String value = bracketsMatcher.group();
            //         if(abbaPattern.matcher(value).find()) {
            //             foundAbbaInBracket = true;
            //         }
            //     }
            //     if(foundAbbaInBracket) {
            //         continue;
            //     }
            //
            //     List<String> pieces = Arrays.stream(line.replaceAll("\\[\\w+]", " ").split(" ")).toList();
            //     for(String piece : pieces) {
            //         Matcher pieceMatcher = abbaPattern.matcher(piece);
            //         if(pieceMatcher.find() && !pieceMatcher.group(1).equals(pieceMatcher.group(2))) {
            //             count++;
            //             break;
            //         }
            //     }
            // }
            // return count;

            int count = 0;
            for(String line : lines) {
                boolean inBrackets = false;
                boolean foundAbba = false;
                for(int i=0; i<line.length()-3; i++) {
                    if(line.charAt(i) == '[') {
                        inBrackets = true;
                    } else if(line.charAt(i) == ']') {
                        inBrackets = false;
                    } else if(line.charAt(i) == line.charAt(i+3) && line.charAt(i+1) == line.charAt(i+2) && line.charAt(i) != line.charAt(i+1)) {
                        if(inBrackets) {
                            foundAbba = false;
                            break;
                        }
                        foundAbba = true;
                    }
                }
                if(foundAbba) {
                    count++;
                }
            }

            return count;
        }

        public int calculateP2(List<String> lines) {

            int count = 0;
            for(String line : lines) {
                boolean inBrackets = false;
                for(int i=0; i<line.length()-2; i++) {
                    if(line.charAt(i) == '[') {
                        inBrackets = true;
                    } else if(line.charAt(i) == ']') {
                        inBrackets = false;
                    } else if(line.charAt(i) == line.charAt(i+2) && line.charAt(i) != line.charAt(i+1) && line.charAt(i+1) != '[' && line.charAt(i+1) != ']' && !inBrackets) {
                        String aba = line.substring(i, i+3);
                        String pattern = "\\[\\w*" + line.charAt(i+1) + line.charAt(i) + line.charAt(i+1) + "\\w*]";
                        Pattern p = Pattern.compile(pattern);
                        if(p.matcher(line).find()) {
                            count++;
                            break;
                        }
                    }
                }
            }

            return count;
        }
    }
}
