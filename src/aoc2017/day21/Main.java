package aoc2017.day21;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "21";

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
            List<Rule> rules = generateRules(lines);

            Grid grid =
                new Grid(new char[][]
                    {
                        {'.', '#', '.'},
                        {'.', '.', '#'},
                        {'#', '#', '#'}
                    }
                );

            // for(int i=0; i<2; i++) {
            for(int i=0; i<3; i++) {
                System.out.println(Arrays.deepToString(grid.grid));
                grid = iterate(grid, rules);
            }

            System.out.println(Arrays.deepToString(grid.grid));

            return grid.countTrue();
        }

        public int calculateP2(List<String> lines) {
            return -1;
        }

        private List<Rule> generateRules(List<String> lines) {
            List<Rule> rules = new ArrayList<>();

            for(String line : lines) {
                String[] split = line.split("\\s+=>\\s+");
                String[] inputRuleSplit = split[0].split("/");
                String[] outputRuleSplit = split[1].split("/");

                Rule rule = new Rule(transformStringArray(inputRuleSplit), transformStringArray(outputRuleSplit));
                rules.add(rule);
                rule = rotateRule(rule);
                rules.add(rule);
                rule = rotateRule(rule);
                rules.add(rule);
                rule = rotateRule(rule);
                rules.add(rule);
                rule = flipRule(rule);
                rules.add(rule);
                rule = rotateRule(rule);
                rules.add(rule);
                rule = rotateRule(rule);
                rules.add(rule);
                rule = rotateRule(rule);
                rules.add(rule);
            }

            return rules;
        }

        private char[][] transformStringArray(String[] input) {
            char[][] boolArray = new char[input.length][input.length];
            for(int i=0; i<input.length; i++) {
                char[] charArr = input[i].toCharArray();

                boolArray[i] = charArr;
            }
            return boolArray;
        }

        private Rule rotateRule(Rule rule) {
            char[][] inputGrid = rule.input;
            char[][] tranferGrid = new char[inputGrid.length][inputGrid.length];

            int row = 0;
            for(int i=inputGrid.length-1; i>=0; i--) {
                for(int j=0; j<inputGrid.length; j++) {
                    tranferGrid[row][j] = inputGrid[j][i];
                }
                row++;
            }

            return new Rule(tranferGrid, rule.output);
        }

        private Rule flipRule(Rule rule) {
            char[][] inputGrid = rule.input;
            char[][] tranferGrid = new char[inputGrid.length][inputGrid.length];

            for(int i=0; i<inputGrid.length; i++) {
                for(int j=0; j<inputGrid.length; j++) {
                    tranferGrid[i][inputGrid.length - j - 1] = inputGrid[i][j];
                }
            }

            return new Rule(tranferGrid, rule.output);
        }

        private Grid iterate(Grid inputGrid, List<Rule> rules) {
            int originalSize = inputGrid.grid.length;
            int newSize;
            int subSize;

            if(originalSize % 2 == 0) {
                newSize = originalSize + (originalSize/2);
                subSize = 2;
            } else {
                newSize = originalSize + (originalSize/3);
                subSize = 3;
            }

            char[][] newGrid = new char[newSize][newSize];

            for(int i=0; i<(inputGrid.grid.length%subSize); i++) {
                for(int j=0; j<(inputGrid.grid.length%subSize); j++) {
                    if(subSize == 2) {
                    } else {

                    }
                }
            }

            return new Grid(newGrid);
        }
    }

    public static class Grid {
        char[][] grid;

        public Grid(char[][] grid) {
            this.grid = grid;
        }

        public int countTrue() {
            int sum = 0;
            for(char[] line : grid) {
                for(char pixel : line) {
                    if(pixel == '#') {
                        sum++;
                    }
                }
            }

            return sum;
        }
    }

    public static class Rule {
        char[][] input;
        char[][] output;

        public Rule(char[][] input, char[][] output) {
            this.input = input;
            this.output = output;
        }

        public boolean gridMatches(Grid grid) {
            if(grid.grid.length != input.length) {
                return false;
            }

            for(int i=0; i<input.length; i++) {
                if(!Arrays.equals(input[i], grid.grid[i])) {
                    return false;
                }
            }

            return true;
        }
    }
}
