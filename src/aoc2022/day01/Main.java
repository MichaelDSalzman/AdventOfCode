package aoc2022.day01;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        FileReader fileReader = new FileReader("src/aoc2022/day01/01.txt");
        // util.FileReader fileReader = new util.FileReader("src/aoc2022/day01/sample.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println ("P2 : " + problem.calculateP2(lines));
    }

    public static class Problem {

        public int calculateP1(List<String> lines) {
            int maxCals = Integer.MIN_VALUE;
            int total = 0;
            for (String line : lines) {
                if (line.isEmpty()) {
                    if(total > maxCals) {
                        maxCals = total;
                    }

                    total = 0;
                } else {
                    total += Integer.parseInt(line);
                }
            }

            return maxCals;
        }

        public int calculateP2(List<String> lines) {
            List<Integer> calories = new ArrayList<>();

            int total = 0;
            for (String line : lines) {
                if (line.isEmpty()) {
                    calories.add(total);
                    total = 0;
                } else {
                    total += Integer.parseInt(line);
                }
            }

            calories.add(total);

            calories.sort(Integer::compareTo);

            int combinedTotal = calories.get(calories.size()-1) + calories.get(calories.size()-2) + calories.get(calories.size()-3);

            System.out.println(calories.get(calories.size()-1) + ":" +calories.get(calories.size()-2) + ":" + calories.get(calories.size()-3));
            System.out.println(calories);

            return combinedTotal;
        }
    }
}
