package aoc2015.day08;

import java.io.IOException;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
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
            int totalNumChars = 0;
            int numCharsInQuotes = 0;

            for(String line : lines) {
                int lineLength = line.length();
                totalNumChars += lineLength;
                String escapedLine = line;
                escapedLine = escapedLine.substring(1, escapedLine.length()-1);
                escapedLine = escapedLine.replaceAll("\\\\\"", "'");
                escapedLine = escapedLine.replaceAll("\\\\\\\\", "|");
                escapedLine = escapedLine.replaceAll("\\\\x[0-9a-f]{2}", "-");
                int escapedLength = escapedLine.length();
                numCharsInQuotes += escapedLength;
            }

            return totalNumChars - numCharsInQuotes;
        }

        public int calculateP2(List<String> lines) {
            int startingCharCount = 0;
            int exoandedCharCount = 0;

            for(String line : lines) {
                int lineLength = line.length();
                startingCharCount += lineLength;
                String escapedLine = line;
                // escapedLine = escapedLine.substring(1, escapedLine.length()-1);
                escapedLine = escapedLine.replaceAll("\"", "||");
                escapedLine = escapedLine.replaceAll("\\\\", "||");
                // escapedLine = escapedLine.replaceAll("\\\\x[0-9a-f]{2}", "-");
                int escapedLength = escapedLine.length() + 2;
                exoandedCharCount += escapedLength;
            }

            return exoandedCharCount - startingCharCount;
        }
    }
}
