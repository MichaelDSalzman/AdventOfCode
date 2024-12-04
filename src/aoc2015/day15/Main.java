package aoc2015.day15;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "15";

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

            List<List<Integer>> ingredientProperties = new ArrayList<>();
            Pattern p = Pattern.compile(".*: capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)");
            for(String line : lines) {
                Matcher m = p.matcher(line);
                m.find();
                List<Integer> properties = Arrays.asList(
                    Integer.parseInt(m.group(1)),
                    Integer.parseInt(m.group(2)),
                    Integer.parseInt(m.group(3)),
                    Integer.parseInt(m.group(4)),
                    Integer.parseInt(m.group(5))
                );
                ingredientProperties.add(properties);
            }

            int bestScore = 0;
            for(int ingredient1Count = 0; ingredient1Count <= 100; ingredient1Count++) {
                for(int ingredient2Count = 0; ingredient2Count <= (100 - ingredient1Count); ingredient2Count++) {
                    for(int ingredient3Count = 0; ingredient3Count <= (100 - ingredient1Count - ingredient2Count); ingredient3Count++) {
                        int ingredient4Count = 100 - ingredient1Count - ingredient2Count - ingredient3Count;

                        int[] propertyScores = new int[5];

                        for(int i=0; i<5; i++) {
                            propertyScores[i] =
                                ingredientProperties.get(0).get(i) * ingredient1Count
                                    + ingredientProperties.get(1).get(i) * ingredient2Count
                                    + ingredientProperties.get(2).get(i) * ingredient3Count
                                    + ingredientProperties.get(3).get(i) * ingredient4Count;
                        }

                        if(propertyScores[0] <= 0 || propertyScores[1] <= 0 || propertyScores[2] <= 0 || propertyScores[3] <= 0 || propertyScores[4] != 500) {
                            continue;
                        }

                        int totalScore = propertyScores[0] *propertyScores[1] *propertyScores[2] *propertyScores[3];// *propertyScores[4];
                        if(totalScore > bestScore) {
                            System.out.println(totalScore);
                            bestScore = totalScore;
                        }
                    }
                }
            }

            return bestScore;
        }

        public int calculateP2(List<String> lines) {
            return -1;
        }
    }
}
