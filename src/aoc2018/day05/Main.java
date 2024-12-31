package aoc2018.day05;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = null;
        String day = null;

        Pattern p = Pattern.compile("aoc(\\d+)\\.day(\\d+).*");
        Matcher m = p.matcher(Main.class.getName());
        if(m.find()) {
            year = m.group(1);
            day = m.group(2);
        }

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

        public Object calculateP1(List<String> lines) {
            return collapse(lines.get(0)).length();
        }

        public Object calculateP2(List<String> lines) {
            Set<String> allChars = new HashSet<>();
            for(String part : lines.get(0).split("")) {
                allChars.add(part.toUpperCase());
            }

            int min = Integer.MAX_VALUE;
            for(String c : allChars) {
                System.out.println("Removing all '"
                    + c
                    + "'");
                String line = lines.get(0);
                int length = collapse(line.replaceAll(c.toUpperCase(), "").replaceAll(c.toLowerCase(), "")).length();
                if(length < min) {
                    min = length;
                }
            }
            return min;
        }

        public String collapse(String line) {

            int stringLength = line.length();
            boolean keepGoing = true;
            while(keepGoing) {
                StringBuilder builder = new StringBuilder();
                for(int i=0; i<stringLength-1; i++) {
                    char c1 = line.charAt(i);
                    char c2 = line.charAt(i+1);
                    if(((Character.isUpperCase(c1) && Character.isLowerCase(c2))
                        || (Character.isUpperCase(c2) && Character.isLowerCase(c1)))
                        && Character.toUpperCase(c1) == Character.toUpperCase(c2)) {

                        builder.append(line.substring(i+2));
                        break;
                    } else {
                        builder.append(c1);
                        if(i == stringLength-2) {
                            builder.append(c2);
                        }
                    }
                }

                line = builder.toString();
                if(line.length() == stringLength) {
                    keepGoing = false;
                }

                stringLength = line.length();
            }

            return line;
        }
    }
}
