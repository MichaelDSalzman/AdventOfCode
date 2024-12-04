package aoc2022.day03;

import java.io.IOException;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String day = "03";

        FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/input.txt");
        // util.FileReader fileReader = new util.FileReader("src/aoc2022/day" + day + "/sample.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println ("P2 : " + problem.calculateP2(lines));
    }

    public static class Problem {

        public int calculateP1(List<String> lines) {
            int sum = 0;
            for (String line : lines) {
                int length = line.length();
                String comp1 = line.substring(0, length/2);
                String comp2 = line.substring(length/2);
                System.out.println (comp1 + " : " + comp2);

                for(int i =0; i<comp1.length(); i++){
                    String character = String.valueOf(comp1.charAt(i));

                    if(comp2.contains(character)){
                        sum += translateCharPriority(character);
                        i = comp1.length();
                    }
                }
            }

            return sum;
        }

        private int translateCharPriority(String character) {
            if(character.toLowerCase().equals(character)) {
                return character.getBytes()[0] - 96;
            } else {
                return character.getBytes()[0] - 38;
            }
        }

        public int calculateP2(List<String> lines) {

            int sum = 0;

            for(int i=0; i<lines.size(); i+=3) {
                String bag1 = lines.get(i);
                String bag2 = lines.get(i+1);
                String bag3 = lines.get(i+2);

                for(int j =0; j<bag1.length(); j++){
                    String character = String.valueOf(bag1.charAt(j));
                    if(bag2.contains(character) && bag3.contains(character)) {
                        sum += translateCharPriority(character);
                        j = bag1.length();
                    }
                }
            }

            return sum;
        }
    }
}
