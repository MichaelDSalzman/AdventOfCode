package aoc2016.day19;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "19";

        FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/sample.txt");
        // FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/input.txt");
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
            // return calculateWinner(5);
            return calculateWinner(3018458);
        }

        public int calculateP2(List<String> lines) {

            return calculateWinnerWhenStealingAcross(3018458);
            // return calculateWinnerWhenStealingAcross(10);
        }

        private int calculateWinner(int numElves) {
            List<Elf> elves = new ArrayList<>();
            for(int i=1; i<=numElves; i++) {
                elves.add(new Elf(i, true));
            }

            boolean previousElfStole = false;
            while(elves.size() > 1) {
                List<Elf> newElfList = new ArrayList<>();
                for(int i=0; i<elves.size(); i++) {
                    if(!previousElfStole) {
                        newElfList.add(elves.get(i));
                    }
                    previousElfStole = !previousElfStole;
                }
                elves = newElfList;
            }

            return elves.get(0).num;
        }


        private int calculateWinnerWhenStealingAcross(int numElves) {
            List<Integer> elves = new ArrayList<>();
            for(int i=1; i<=numElves; i++) {
                elves.add(i);
            }

            int elfNum = 1;
            int index = 0 ;
            while(elves.size() > 1) {
                if(elves.size() % 10000 == 0) {
                    System.out.println(elves.size());
                }
                elfNum = elves.get(index);
                int elfIndexToRemove = (index + elves.size() / 2) % elves.size();
                elves.remove(elfIndexToRemove);
                index = (elves.indexOf(elfNum) + 1) % elves.size();
            }

            return elves.get(0);
        }

        public record Elf(int num, boolean hasPresent) {}
    }
}
