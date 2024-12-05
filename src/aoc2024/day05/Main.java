package aoc2024.day05;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2024";
        String day = "05";

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
            return calculate(lines, true);
        }

        public int calculateP2(List<String> lines) {
            return calculate(lines, false);
        }

        public int calculate(List<String> lines, boolean onlyCorrectOrder) {
            List<Rule> allRules = parseRules(lines);
            int total = 0;

            for(String line : lines) {
                if(!line.contains(",")) {
                    continue;
                }

                List<Integer> pages = Arrays.stream(line.split(","))
                    .map(Integer::parseInt)
                    .toList();

                List<Rule> rules = findRulesForBook(pages, allRules);
                boolean orderOk = isBookOrdered(pages, rules);
                if(orderOk && onlyCorrectOrder) {
                    total += pages.get((pages.size()+1)/2-1);
                } else if (!orderOk && !onlyCorrectOrder) {
                    pages = findCorrectOrder(pages, rules);
                    total += pages.get((pages.size()+1)/2-1);
                }
            }
            return total;
        }

        public record Rule(int first, int second) {
        }

        public List<Rule> parseRules(List<String> lines) {
            List<Rule> rules = new ArrayList<>();

            Pattern p = Pattern.compile("(\\d+)\\|(\\d+)");
            for(String line: lines) {
                Matcher m = p.matcher(line);
                if(m.find()) {
                    rules.add(new Rule(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))));
                }
            }

            return rules;
        }

        public List<Rule> findRulesForBook(List<Integer> pages, List<Rule> allRules) {
            List<Rule> rules = new ArrayList<>();

            for(Rule rule: allRules) {
                if(pages.contains(rule.first) && pages.contains(rule.second)) {
                    rules.add(rule);
                }
            }
            return rules;
        }

        public boolean isBookOrdered(List<Integer> pages, List<Rule> rules) {
            for(int pageIndex = 0; pageIndex < pages.size(); pageIndex++) {
                // Iterate over the pages and compare page to all future pages.
                // If there is a rule that places a future page before current page,
                // then order is incorrect
                Integer currentPage = pages.get(pageIndex);
                List<Integer> futurePages = pages.subList(pageIndex+1, pages.size());
                if(rules.stream().anyMatch(r -> futurePages.contains(r.first) && currentPage == r.second)){
                    return false;
                }
            }

            return true;
        }

        /**
         * Iterate over the pages and find the correct order
         * @param incorrectPages
         * @param allRules
         * @return
         */
        public List<Integer> findCorrectOrder(List<Integer> incorrectPages, List<Rule> allRules) {
            List<Integer> correctPages = new ArrayList<>();

            // Find the page that should be first by looking for the page that isn't in the
            //  second position of any rule
            while(incorrectPages.size() != correctPages.size()) {
                List<Integer> pagesToConsider = incorrectPages.stream()
                    .filter(ip -> !correctPages.contains(ip))
                    .toList();

                List<Rule> rulesToConsider = findRulesForBook(pagesToConsider, allRules);
                for(Integer page : pagesToConsider) {
                    if(rulesToConsider.stream().noneMatch(r -> r.second == page)) {
                        correctPages.add(page);
                        break;
                    }
                }
            }

            return correctPages;
        }
    }
}
