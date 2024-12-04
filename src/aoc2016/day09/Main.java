package aoc2016.day09;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "09";

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
            return decompress(lines.get(0)).length();
        }

        public long calculateP2(List<String> lines) {

            String line = lines.get(0);
            return calculateLengthRecursively(line, 1);
        }

        private String decompress(String inputLine) {
            StringBuilder sb = new StringBuilder();
            Pattern p = Pattern.compile("\\((\\d+)x(\\d+)\\)");

            for(int i=0; i<inputLine.length(); i++) {
                char c = inputLine.charAt(i);
                if(c == '(') {
                    Matcher m = p.matcher(inputLine.substring(i));
                    if(m.find()) {
                        String group = m.group();
                        int length= Integer.parseInt(m.group(1));
                        int repetitions = Integer.parseInt(m.group(2));
                        String sectionToRepeat = inputLine.substring(i+group.length(), i+group.length()+length);
                        for(int j=0;j<repetitions; j++) {
                            sb.append(sectionToRepeat);
                        }
                        i+=group.length()+length-1;
                    }
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        }

        Pattern p = Pattern.compile("\\((\\d+)x(\\d+)\\)");

        private long calculateLengthRecursively(String inputLine, int numRepeats) {
            long count = 0;

            for(int i=0; i<inputLine.length(); i++) {
                if(inputLine.charAt(i) == '(') {
                    Matcher m = p.matcher(inputLine.substring(i));
                    if(m.find()) {
                        String group = m.group();
                        int length= Integer.parseInt(m.group(1));
                        int repetitions = Integer.parseInt(m.group(2));
                        String sectionToRepeat = inputLine.substring(i+group.length(), i+group.length()+length);
                        count += calculateLengthRecursively(sectionToRepeat, repetitions);
                        i+=group.length()+length-1;
                    }
                } else {
                    count++;
                }
            }

            return count * numRepeats;
        }
    }
}
