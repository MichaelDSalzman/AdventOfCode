package aoc2022.day08;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String day = "08";

        // FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println ("P2 : " + problem.calculateP2(lines));
    }

    public static class Problem {

        public int calculateP1(List<String> lines) {
            Integer[][] trees = new Integer[lines.size()][];

            for(int rowNum=0; rowNum<lines.size(); rowNum++) {
                String row = lines.get(rowNum);
                trees[rowNum] = new Integer[row.length()];
                for(int colNum=0; colNum<row.length(); colNum++){
                    trees[rowNum][colNum] = Integer.parseInt(String.valueOf(row.charAt(colNum)));
                }
            }

            int totalVisibleTrees = 0;
            for(int rowNum=0; rowNum<trees.length; rowNum++) {
                for(int colNum=0; colNum<trees[rowNum].length; colNum++) {
                    if(rowNum == 0 || rowNum == trees.length-1) {
                        totalVisibleTrees++;
                    } else if (colNum==0 || colNum == trees[rowNum].length-1) {
                        totalVisibleTrees++;
                    } else if (
                        treeVisibleFromAbove(trees, rowNum, colNum) ||
                            treeVisibleFromBelow(trees, rowNum, colNum) ||
                                treeVisibleFromLeft(trees, rowNum, colNum) ||
                                    treeVisibleFromRight(trees, rowNum, colNum)
                    ){
                        totalVisibleTrees++;
                    }
                }
            }

            return totalVisibleTrees;
        }

        private boolean treeVisibleFromAbove(Integer[][] trees, Integer rowNum, Integer colNum) {
            Integer targetTreeHeight = trees[rowNum][colNum];

            for(int i=rowNum-1; i>=0; i--) {
                if(trees[i][colNum] >= targetTreeHeight) {
                    return false;
                }
            }

            return true;
        }

        private boolean treeVisibleFromBelow(Integer[][] trees, Integer rowNum, Integer colNum) {
            Integer targetTreeHeight = trees[rowNum][colNum];

            for(int i=rowNum+1; i<trees.length; i++) {
                if(trees[i][colNum] >= targetTreeHeight) {
                    return false;
                }
            }

            return true;
        }

        private boolean treeVisibleFromLeft(Integer[][] trees, Integer rowNum, Integer colNum) {
            Integer targetTreeHeight = trees[rowNum][colNum];

            for(int i=colNum-1; i>=0; i--) {
                if(trees[rowNum][i] >= targetTreeHeight) {
                    return false;
                }
            }

            return true;
        }

        private boolean treeVisibleFromRight(Integer[][] trees, Integer rowNum, Integer colNum) {
            Integer targetTreeHeight = trees[rowNum][colNum];

            for(int i=colNum+1; i<trees[rowNum].length; i++) {
                if(trees[rowNum][i] >= targetTreeHeight) {
                    return false;
                }
            }

            return true;
        }

        public int calculateP2(List<String> lines) {
            Integer[][] trees = new Integer[lines.size()][];

            for(int rowNum=0; rowNum<lines.size(); rowNum++) {
                String row = lines.get(rowNum);
                trees[rowNum] = new Integer[row.length()];
                for(int colNum=0; colNum<row.length(); colNum++){
                    trees[rowNum][colNum] = Integer.parseInt(String.valueOf(row.charAt(colNum)));
                }
            }

            Integer maxScore = 0;
            for(int rowNum=0; rowNum<trees.length; rowNum++) {
                for (int colNum = 0; colNum < trees[rowNum].length; colNum++) {
                    int aboveScore = numTreesSeenAbove(trees, rowNum, colNum);
                    int belowScore = numTreesSeenBelow(trees, rowNum, colNum);
                    int leftScore = numTreesSeenLeft(trees, rowNum, colNum);
                    int rightScore = numTreesSeenRight(trees, rowNum, colNum);

                    int totalScore = aboveScore * belowScore * leftScore * rightScore;
                    if(totalScore > maxScore) {
                        maxScore = totalScore;
                    }
                }
            }

            return maxScore;
        }

        private int numTreesSeenAbove(Integer[][] trees, int rowNum, int colNum) {
            if(rowNum == 0) {
                return 0;
            }

            Integer targetTreeHeight = trees[rowNum][colNum];
            int treesSeen = 0;

            for(int i=rowNum-1; i>=0; i--) {
                treesSeen++;

                if(trees[i][colNum] >= targetTreeHeight) {
                    return treesSeen;
                }
            }

            return treesSeen;
        }

        private int numTreesSeenBelow(Integer[][] trees, int rowNum, int colNum) {
            if(rowNum == trees.length-1) {
                return 0;
            }

            Integer targetTreeHeight = trees[rowNum][colNum];
            int treesSeen = 0;

            for(int i=rowNum+1; i<trees.length; i++) {
                treesSeen++;

                if(trees[i][colNum] >= targetTreeHeight) {
                    return treesSeen;
                }
            }

            return treesSeen;
        }

        private int numTreesSeenLeft(Integer[][] trees, int rowNum, int colNum) {
            if(colNum == 0) {
                return 0;
            }

            Integer targetTreeHeight = trees[rowNum][colNum];
            int treesSeen = 0;

            for(int i=colNum-1; i>=0; i--) {
                treesSeen++;

                if(trees[rowNum][i] >= targetTreeHeight) {
                    return treesSeen;
                }
            }

            return treesSeen;
        }

        private int numTreesSeenRight(Integer[][] trees, int rowNum, int colNum) {
            if(colNum == trees[rowNum].length-1) {
                return 0;
            }

            Integer targetTreeHeight = trees[rowNum][colNum];
            int treesSeen = 0;

            for(int i=colNum+1; i<trees[rowNum].length; i++) {
                treesSeen++;

                if(trees[rowNum][i] >= targetTreeHeight) {
                    return treesSeen;
                }
            }

            return treesSeen;
        }
    }
}
