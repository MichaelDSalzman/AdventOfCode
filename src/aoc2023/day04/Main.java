package aoc2023.day04;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String day = "04";

        // FileReader fileReader = new FileReader("src/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println ("P2 : " + problem.calculateP2(lines));
    }

    public static class Problem {

        public int calculateP1(List<String> lines) {
            int pointTotal = 0;

            for (String line : lines) {
                String[] cardInfo = line.split(":");
                String[] numbers = cardInfo[1].trim().split("\\|");

                List<String> winningNumbers = List.of(numbers[0].trim().split("\\s+"));
                List<String> myNumbers = List.of(numbers[1].trim().split("\\s+"));

                int numMatching = 0;
                for(String myNumber : myNumbers) {
                    if(winningNumbers.contains(myNumber)) {
                        numMatching++;
                    }
                }

                int point = (int) Math.pow(2, numMatching - 1);

                pointTotal += point;
            }

            return pointTotal;
        }

        public int calculateP2(List<String> lines) {
            Integer[] scratchCardCount = new Integer[lines.size()];

            for(int i=0; i<lines.size();i++) {
                scratchCardCount[i]=1;
            }

            for (int index = 0; index < lines.size(); index++) {
                String line = lines.get(index);

                String[] cardInfo = line.split(":");
                String[] numbers = cardInfo[1].trim().split("\\|");

                List<String> winningNumbers = List.of(numbers[0].trim().split("\\s+"));
                List<String> myNumbers = List.of(numbers[1].trim().split("\\s+"));

                int numMatching = 0;
                for(String myNumber : myNumbers) {
                    if(winningNumbers.contains(myNumber)) {
                        numMatching++;
                    }
                }

                for(int i=0; i<numMatching; i++) {
                    scratchCardCount[i+index+1] += scratchCardCount[index];
                }
            }

            int cardTotal = 0;
            for(Integer cardCount : scratchCardCount){
                cardTotal += cardCount;
            }

            return cardTotal;
        }
    }
}
