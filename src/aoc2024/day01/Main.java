package aoc2024.day01;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2024";
        String day = "01";

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
            List<Integer> list1 = new ArrayList<>();
            List<Integer> list2 = new ArrayList<>();
            Pattern p = Pattern.compile("(\\d+)\\s+(\\d+)");
            for(String line:lines) {
                Matcher m = p.matcher(line);
                m.find();
                list1.add(Integer.parseInt(m.group(1)));
                list2.add(Integer.parseInt(m.group(2)));
            }
            Collections.sort(list1);
            Collections.sort(list2);

            int total = 0;
            for(int i=0; i<list1.size(); i++) {
                total += Math.abs(list1.get(i) - list2.get(i));
            }

            return total;
        }

        public int calculateP2(List<String> lines) {
            List<Integer> list1 = new ArrayList<>();
            List<Integer> list2 = new ArrayList<>();
            Map<Integer, Integer> repeatMap = new HashMap<>();

            Pattern p = Pattern.compile("(\\d+)\\s+(\\d+)");
            for(String line:lines) {
                Matcher m = p.matcher(line);
                m.find();
                Integer num1 = Integer.parseInt(m.group(1));
                Integer num2 = Integer.parseInt(m.group(2));
                list1.add(num1);
                list2.add(num2);

                Integer count = repeatMap.get(num2);
                if(count == null) {
                    count = 0;
                }
                repeatMap.put(num2, ++count);
            }

            int similarityScore = 0;
            for(int i=0; i<list1.size(); i++) {
                Integer num = list1.get(i);
                Integer times = repeatMap.getOrDefault(num, 0);
                similarityScore += num * times;
            }
            return similarityScore;
        }
    }
}
