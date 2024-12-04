package aoc2023.day03;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        // FileReader fileReader = new FileReader("src/day03/sample.txt");
        FileReader fileReader = new FileReader("src/day03/input.txt");

        List<String> lines = fileReader.getLines();
        Problem p = new Problem();
        System.out.println("P1 TOTAL " + p.calculateP1(lines));
        System.out.println("P2 TOTAL " + p.calculateP2(lines));
    }

    public static class Problem {

        public int calculateP1(List<String > lines) {
            int partSum = 0;

            for (int rowNum = 0; rowNum<lines.size(); rowNum++) {
                List<FoundNumber> foundNumbers = findNumbersInLine(lines.get(rowNum), rowNum);

                for(FoundNumber foundNumber : foundNumbers) {
                    boolean adjacentToSymbol = false;

                    for (int index = foundNumber.getStartIndex(); index <= foundNumber.endIndex; index++) {

                        for(int searchRow = rowNum - 1; searchRow <= rowNum + 1; searchRow++) {
                            for (int searchColumn = index - 1; searchColumn <= index + 1; searchColumn++) {
                                if(searchRow < 0 || searchColumn < 0 || searchRow >= lines.size() || searchColumn >= lines.get(rowNum).length()) {
                                    continue;
                                }
                                String searchValue = String.valueOf(lines.get(searchRow).charAt(searchColumn));
                                try {
                                    if(searchValue.equals(".")) {
                                        continue;
                                    }
                                    Integer.parseInt(searchValue);
                                    continue;
                                } catch (NumberFormatException ex) {
                                    // Not a number, keep checking
                                }

                                adjacentToSymbol = true;
                            }
                        }
                    }

                    if(adjacentToSymbol) {
                        partSum += foundNumber.getValue();
                    }
                }
            }
            return partSum;
        }

        public int calculateP2(List<String > lines) {
            int gearSum = 0;

            List<FoundNumber> foundNumbers = new ArrayList<>();
            List<FoundGear> foundGears = new ArrayList<>();

            for (int rowNum = 0; rowNum < lines.size(); rowNum++) {
                foundNumbers.addAll(findNumbersInLine(lines.get(rowNum), rowNum));
                foundGears.addAll(findGearsInLine(lines.get(rowNum), rowNum));
            }

            for(FoundGear gear : foundGears) {
                List<FoundNumber> numbersAdjacentToGear = new ArrayList<>();

                for(FoundNumber foundNumber: foundNumbers) {
                    if(numberAdjacentToGear(gear, foundNumber)) {
                        numbersAdjacentToGear.add(foundNumber);
                    }
                }

                if(numbersAdjacentToGear.size() == 2) {
                    int product =  numbersAdjacentToGear.get(0).getValue() * numbersAdjacentToGear.get(1).getValue();
                    gearSum+= product;
                }
            }

            return gearSum;
        }

        private List<FoundNumber> findNumbersInLine(String line, int rowNum) {
            List<FoundNumber> foundNumbers = new ArrayList<>();

            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(line);

            while (matcher.find()) {
                foundNumbers.add(new FoundNumber(rowNum, Integer.parseInt(matcher.group()), matcher.start(), matcher.end()-1));
            }

            return foundNumbers;
        }

        private List<FoundGear> findGearsInLine(String line, int rowNum) {
            List<FoundGear> foundGears = new ArrayList<>();

            Pattern pattern = Pattern.compile("\\*");
            Matcher matcher = pattern.matcher(line);

            while (matcher.find()) {
                foundGears.add(new FoundGear(rowNum, matcher.start()));
            }

            return foundGears;
        }

        private boolean numberAdjacentToGear(FoundGear gear, FoundNumber number) {
            if(Math.abs(gear.getRowNum() - number.getRowNum()) <= 1) {
                return gear.getIndex() >= number.getStartIndex() - 1
                    && gear.getIndex() <= number.getEndIndex() + 1;
            }

            return false;
        }
    }

    public static class FoundNumber {

        private final int rowNum;
        private final int value;
        private int startIndex;
        private int endIndex;

        public FoundNumber(int rowNum, int value, int startIndex, int endIndex) {
            this.rowNum = rowNum;
            this.value = value;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }
        public int getRowNum() {
            return rowNum;
        }

        public int getValue() {
            return value;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public int getEndIndex() {
            return endIndex;
        }
    }

    public static class FoundGear {

        private int rowNum;
        private int index;

        public FoundGear(int rowNum, int index) {
            this.rowNum = rowNum;
            this.index = index;
        }
        public int getRowNum() {
            return rowNum;
        }

        public int getIndex() {
            return index;
        }
    }
}
