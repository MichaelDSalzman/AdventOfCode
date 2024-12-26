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

            for(int i=0; i<5; i++) {
                grid = iterate(grid, rules);
            }

            return grid.countTrue();
        }

        public int calculateP2(List<String> lines) {

            List<Rule> rules = generateRules(lines);

            Grid grid =
                new Grid(new char[][]
                    {
                        {'.', '#', '.'},
                        {'.', '.', '#'},
                        {'#', '#', '#'}
                    }
                );

            for(int i=0; i<18; i++) {
                grid = iterate(grid, rules);
            }

            return grid.countTrue();
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
                newSize = (int) (1.5 * originalSize);
                subSize = 2;
            } else {
                newSize = originalSize + (originalSize/3);
                subSize = 3;
            }

            char[][] newGrid = new char[newSize][newSize];

            // Iterate over the input grid, but each iteration jumps to the next starting spot in the grid
            for(int i=0; i<(inputGrid.grid.length); i+=subSize) {
                for(int j=0; j<(inputGrid.grid.length); j+=subSize) {
                    char[][] subGrid = new char[subSize][subSize];
                    for(int vertical=0; vertical<subSize; vertical++) {
                        for(int horizontal=0; horizontal<subSize; horizontal++) {
                            subGrid[vertical][horizontal] = inputGrid.grid[i+vertical][j+horizontal];
                        }
                    }

                    char[][] ruleOutput = rules.stream().filter(r -> r.gridMatches(subGrid)).findFirst().orElseThrow().output;

                    int startingVerticalIndex = ruleOutput.length * i/subSize;
                    int startingHorizontalIndex = ruleOutput.length * j/subSize;

                    for(int vertical=0; vertical<ruleOutput.length; vertical++) {
                        for(int horizontal=0; horizontal<ruleOutput.length; horizontal++) {
                            newGrid[startingVerticalIndex+vertical][startingHorizontalIndex+horizontal] = ruleOutput[vertical][horizontal];
                        }
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

        public boolean gridMatches(char[][] grid) {
            if(grid.length != input.length) {
                return false;
            }

            for(int i=0; i<input.length; i++) {
                if(!Arrays.equals(input[i], grid[i])) {
                    return false;
                }
            }

            return true;
        }
    }
}
