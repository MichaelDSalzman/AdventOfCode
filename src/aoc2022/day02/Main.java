package aoc2022.day02;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String day = "02";

        FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/input.txt");
        // util.FileReader fileReader = new util.FileReader("src/aoc2022/day" + day + "/sample.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println ("P2 : " + problem.calculateP2(lines));
    }

    public static class Problem {

        public int calculateP1(List<String> lines) {
            int score = 0;

            for (String line: lines) {
                String[] shapes = line.split(" ");

                //HIM
                // A = ROCK
                // B = PAPER
                // C = SCISSOR
                //ME
                // X = ROCK
                // Y = PAPER
                // Z = SCISSOR
                if(shapes[1].equals("X")) {
                    score += 1;
                    if(shapes[0].equals("A")) {
                        score += 3;
                    } else if (shapes[0].equals("C")) {
                        score += 6;
                    }
                }
                else if(shapes[1].equals("Y")) {
                    score += 2;
                    if(shapes[0].equals("A")) {
                        score += 6;
                    } else if (shapes[0].equals("B")) {
                        score += 3;
                    }
                }
                else if(shapes[1].equals("Z")) {
                    score += 3;
                    if(shapes[0].equals("B")) {
                        score += 6;
                    } else if (shapes[0].equals("C")) {
                        score += 3;
                    }
                }
            }
            return score;
        }

        public int calculateP2(List<String> lines) {
            int score = 0;

            for (String line: lines) {
                String[] shapes = line.split(" ");

                //HIM
                // A = ROCK
                // B = PAPER
                // C = SCISSOR
                //ME
                // X = LOSE
                // Y = DRAW
                // Z = WIN

                // He throws rock
                if(shapes[0].equals("A")) {
                    // I need to lose by throwing scissors
                    if(shapes[1].equals("X")) {
                        // No points for losing
                        score += 0;
                        // 3 points for scissors
                        score += 3;
                    }
                    // Need to draw with rock
                    if(shapes[1].equals("Y")) {
                        // 3 points for drawing
                        score += 3;
                        // 3 points for rock
                        score += 1;
                    }
                    // Need to win with paper
                    if(shapes[1].equals("Z")) {
                        // 6 points for winning
                        score += 6;
                        // 3 points for paper
                        score += 2;
                    }
                }

                // He throws paper
                if(shapes[0].equals("B")) {
                    // I need to lose by throwing rock
                    if(shapes[1].equals("X")) {
                        // No points for losing
                        score += 0;
                        // points for shape
                        score += 1;
                    }
                    // Need to draw with paper
                    if(shapes[1].equals("Y")) {
                        // 3 points for drawing
                        score += 3;
                        // points for shape
                        score += 2;
                    }
                    // Need to win with scissor
                    if(shapes[1].equals("Z")) {
                        // 6 points for winning
                        score += 6;
                        // points for shape
                        score += 3;
                    }
                }

                // He throws scissor
                if(shapes[0].equals("C")) {
                    // I need to lose by throwing paper
                    if(shapes[1].equals("X")) {
                        // No points for losing
                        score += 0;
                        // points for shape
                        score += 2;
                    }
                    // Need to draw with scissor
                    if(shapes[1].equals("Y")) {
                        // 3 points for drawing
                        score += 3;
                        // points for shape
                        score += 3;
                    }
                    // Need to win with rock
                    if(shapes[1].equals("Z")) {
                        // 6 points for winning
                        score += 6;
                        // points for shape
                        score += 1;
                    }
                }
            }

            return score;
        }
    }
}
