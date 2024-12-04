package aoc2017.day03;

import java.io.IOException;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "03";

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
            return calculateDistance(265149);
        }

        public int calculateP2(List<String> lines) {
            return findLargestOver(265149);
        }

        private int calculateDistance(int goal) {
            int i = 1;
            int square = i*i;
            while(square < goal) {
                i+=2;
                square = i*i;
            }

            int direction = -1;
            int numTimesCounted = 0;
            int distance = i-1;
            int value = square;
            while(value != goal) {
                value--;
                distance += direction;
                numTimesCounted++;
                if(numTimesCounted == (i-1)/2){
                    numTimesCounted = 0;
                    direction *= -1;
                }
            }

            return distance;
        }

        private int findLargestOver(int goal) {
            int[][] values = new int[1000][1000];
            int x = 500;
            int y = 500;

            values[x][y] = 1;
            int direction = 0;
            int magnitude = 1;
            int numTimesGoneInDirection = 0;
            int newTotal = 1;
            while(newTotal < goal) {
                switch(direction) {
                    case 0 -> x++;
                    case 1 -> y++;
                    case 2 -> x--;
                    case 3 -> y--;
                }

                newTotal = values[x-1][y+1] +values[x][y+1] +values[x+1][y+1] +values[x-1][y] +values[x+1][y] +values[x-1][y-1] +values[x][y-1] +values[x+1][y-1];
                values[x][y] = newTotal;
                numTimesGoneInDirection++;
                if(numTimesGoneInDirection == magnitude) {
                    numTimesGoneInDirection = 0;
                    direction++;
                    direction%=4;
                    if(direction == 2 || direction == 0) {
                        magnitude++;
                    }
                }
            }

            return newTotal;
        }
    }
}

// 0 = right
// 1 = up
// 2 = left
// 3 = down

// 2 - 0
// 3 - 1
// 4 - 2
// 5 - 2
// 6 - 3
// 7 - 3
// 8 - 0/4
// 9 - 0/4
// 10 - 0/4
// 11 - 1
// 12 - 1
// 13 - 1
// 14 - 2
// 15 - 2

