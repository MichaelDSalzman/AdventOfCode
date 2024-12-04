package aoc2015.day03;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "03";

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
            String map = lines.get(0);
            Set<String > visitedHouses = new HashSet<>();
            int x = 0;
            int y = 0;
            visitedHouses.add(x + "," + y);

            for(String character : map.split("")) {
                if(character.equals("^")) {
                    y++;
                } else if (character.equals("v")) {
                    y--;
                } else if(character.equals(">")) {
                    x++;
                } else if(character.equals("<")) {
                    x--;
                }

                visitedHouses.add(x + "," + y);
            }
            return visitedHouses.size();
        }

        public int calculateP2(List<String> lines) {

            String map = lines.get(0);
            Set<String > visitedHouses = new HashSet<>();
            int santaX = 0;
            int santaY = 0;
            int roboX = 0;
            int roboY = 0;
            visitedHouses.add(santaX + "," + santaY);
            visitedHouses.add(roboX + "," + roboY);

            for(int i=0; i<map.length(); i++) {
                String character = map.substring(i,i+1);
                if(i%2==0) {
                    if (character.equals("^")) {
                        santaY++;
                    } else if (character.equals("v")) {
                        santaY--;
                    } else if (character.equals(">")) {
                        santaX++;
                    } else if (character.equals("<")) {
                        santaX--;
                    }
                } else {

                    if (character.equals("^")) {
                        roboY++;
                    } else if (character.equals("v")) {
                        roboY--;
                    } else if (character.equals(">")) {
                        roboX++;
                    } else if (character.equals("<")) {
                        roboX--;
                    }
                }

                visitedHouses.add(santaX + "," + santaY);
                visitedHouses.add(roboX + "," + roboY);
            }
            return visitedHouses.size();
        }
    }
}
