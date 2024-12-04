package aoc2022.day11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String day = "11";

        // FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println ("P2 : " + problem.calculateP2(lines));
    }

    public static class Problem {

        public Long calculateP1(List<String> lines) {

            List<Monkey> monkeys = getMonkeys(lines);
            return runGame(monkeys, 20, true);
        }

        public Long calculateP2(List<String> lines) {

            List<Monkey> monkeys = getMonkeys(lines);
            return runGame(monkeys, 10000, false);
        }

        private List<Monkey> getMonkeys(List<String> lines) {
            List<Monkey> monkeys = new ArrayList<>();

            Monkey currentMonkey = null;

            for(String line : lines) {
                if(line.startsWith("Monkey")) {
                    currentMonkey = new Monkey();
                    monkeys.add(currentMonkey);
                } else if (line.isEmpty()) {
                } else if(line.trim().startsWith("Starting items")) {
                    Pattern p = Pattern.compile("\\d+");
                    Matcher m = p.matcher(line);
                    while(m.find()) {
                        currentMonkey.getItems().add(Double.parseDouble(m.group()));
                    }
                } else if (line.trim().startsWith("Operation")) {
                    currentMonkey.setOperation(line.split(":")[1].trim());
                } else if (line.trim().startsWith("Test")) {
                    currentMonkey.setTestDivisible(Integer.parseInt(line.split("divisible by")[1].trim()));
                } else if (line.trim().startsWith("If true")) {
                    currentMonkey.setTrueMonkey(Integer.parseInt(line.split("monkey")[1].trim()));
                } else if (line.trim().startsWith("If false")) {
                    currentMonkey.setFalseMonkey(Integer.parseInt(line.split("monkey")[1].trim()));
                }
            }

            return monkeys;
        }

        private Long runGame(List<Monkey> monkeys, int numRounds, boolean shouldDivide) {

            Integer superMod = monkeys.stream().map(
                Monkey::getTestDivisible).reduce(1, (total, element) -> total * element);


            for (int i=0; i<numRounds; i++) {
                if(i % 1000 == 0) {
                    System.out.println(i + " " + monkeys);
                }
                for (Monkey monkey : monkeys) {
                    for (Double item : monkey.getItems()) {
                        monkey.incrementNumInspections();
                        item %= superMod;
                        item = monkey.performOperation(item);
                        if (shouldDivide) {
                            item = Math.floor(item / 3);
                        }

                        Double mod = item % monkey.getTestDivisible();
                        if (mod == 0) {
                            monkeys.get(monkey.getTrueMonkey()).getItems().add(item);
                        } else {
                            monkeys.get(monkey.getFalseMonkey()).getItems().add(item);
                        }
                    }
                    monkey.setItems(new ArrayList<>());
                }
            }

            List<Long> numInspections = monkeys.stream().map(Monkey::getNumInspections).sorted()
                .toList();

            return numInspections.get(numInspections.size()-1) * numInspections.get(numInspections.size()-2);
        }

    }

    public static class Monkey {
        private List<Double> items = new ArrayList<>();
        private String operation;
        private Integer testDivisible;
        private int trueMonkey;
        private int falseMonkey;
        private Long numInspections = 0L;

        public Long getNumInspections() {
            return numInspections;
        }

        public void incrementNumInspections() {
            this.numInspections++;
        }

        public List<Double> getItems() {
            return items;
        }

        public String getOperation() {
            return operation;
        }

        public Integer getTestDivisible() {
            return testDivisible;
        }

        public int getTrueMonkey() {
            return trueMonkey;
        }

        public int getFalseMonkey() {
            return falseMonkey;
        }

        public void setItems(List<Double> items) {
            this.items = items;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public void setTestDivisible(Integer testDivisible) {
            this.testDivisible = testDivisible;
        }

        public void setTrueMonkey(Integer trueMonkey) {
            this.trueMonkey = trueMonkey;
        }

        public void setFalseMonkey(Integer falseMonkey) {
            this.falseMonkey = falseMonkey;
        }

        public Double performOperation(Double item) {
            String equation = this.getOperation().split("=")[1].trim();
            String[] equationParts = equation.split(" ");

            Double op1, op2;
            if(equationParts[0].equals("old")) {
                op1 = item;
            } else {
                op1 = Double.parseDouble(equationParts[0]);
            }

            if(equationParts[2].equals("old")) {
                op2 = item;
            } else {
                op2 = Double.parseDouble(equationParts[2]);
            }

            if("+".equals(equationParts[1])) {
                return op1 + op2;
            }

            if("-".equals(equationParts[1])) {
                return op1 - (op2);
            }

            if("*".equals(equationParts[1])) {
                return op1 * (op2);
            }

            if("/".equals(equationParts[1])) {
                return op1 / (op2);
            }
            return -1D;
        }

        @Override
        public String toString() {
            return "Monkey{" +
                "items=" + items +
                ", operation='" + operation + '\'' +
                ", testDivisible=" + testDivisible +
                ", trueMonkey=" + trueMonkey +
                ", falseMonkey=" + falseMonkey +
                ", numInspections=" + numInspections +
                '}';
        }
    }
}
