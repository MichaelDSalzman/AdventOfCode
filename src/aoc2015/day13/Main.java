package aoc2015.day13;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections4.iterators.PermutationIterator;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "13";

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
            Map<String, Person> people = new HashMap<>();

            Pattern p = Pattern.compile("(.*) would (gain|lose) (\\d+) happiness units by sitting next to (.*).");
            for(String line : lines){
                Matcher m = p.matcher(line);
                if(m.find()) {
                    String name = m.group(1);
                    String action = m.group(2);
                    Integer quantity = Integer.parseInt(m.group(3));
                    String neighborName = m.group(4);

                    Person person = people.get(name);
                    if (person == null) {
                        person = new Person(name);
                        people.put(name, person);
                    }

                    person.addNeighbor(neighborName, action.equals("gain") ? quantity : quantity * -1);
                }
            }

            int highestHappiness = Integer.MIN_VALUE;
            PermutationIterator<String> iterator = new PermutationIterator<>(people.keySet());

            while(iterator.hasNext()) {
                List<String> sittingOrder = iterator.next();
                int happiness = 0;
                for(int i=0; i<sittingOrder.size(); i++) {
                    Person person = people.get(sittingOrder.get(i));
                    String leftNeighborName = sittingOrder.get((i-1+sittingOrder.size()) % sittingOrder.size());
                    String rightNeighborName = sittingOrder.get((i+1+sittingOrder.size()) % sittingOrder.size());

                    happiness += person.neighborHappiness.get(leftNeighborName);
                    happiness += person.neighborHappiness.get(rightNeighborName);
                }

                if(happiness > highestHappiness) {
                    highestHappiness = happiness;
                }
            }
            return highestHappiness;
        }

        public int calculateP2(List<String> lines) {
            Map<String, Person> people = new HashMap<>();
            people.put("Mike", new Person("Mike"));

            Pattern p = Pattern.compile("(.*) would (gain|lose) (\\d+) happiness units by sitting next to (.*).");
            for(String line : lines){
                Matcher m = p.matcher(line);
                if(m.find()) {
                    String name = m.group(1);
                    String action = m.group(2);
                    Integer quantity = Integer.parseInt(m.group(3));
                    String neighborName = m.group(4);

                    Person person = people.get(name);
                    if (person == null) {
                        person = new Person(name);
                        people.put(name, person);
                    }

                    person.addNeighbor(neighborName, action.equals("gain") ? quantity : quantity * -1);
                    person.addNeighbor("Mike", 0);
                }
            }

            int highestHappiness = Integer.MIN_VALUE;
            PermutationIterator<String> iterator = new PermutationIterator<>(people.keySet());

            while(iterator.hasNext()) {
                List<String> sittingOrder = iterator.next();
                int happiness = 0;
                for(int i=0; i<sittingOrder.size(); i++) {
                    Person person = people.get(sittingOrder.get(i));
                    String leftNeighborName = sittingOrder.get((i-1+sittingOrder.size()) % sittingOrder.size());
                    String rightNeighborName = sittingOrder.get((i+1+sittingOrder.size()) % sittingOrder.size());

                    happiness += Objects.requireNonNullElse(person.neighborHappiness.get(leftNeighborName), 0);
                    happiness += Objects.requireNonNullElse(person.neighborHappiness.get(rightNeighborName), 0);
                }

                if(happiness > highestHappiness) {
                    highestHappiness = happiness;
                }
            }
            return highestHappiness;
        }
    }

    public static class Person {
        private String name;
        private Map<String, Integer> neighborHappiness = new HashMap<>();

        public Person(String name) {
            this.name = name;
        }

        public void addNeighbor(String neighborName, Integer happinessChange) {
            neighborHappiness.put(neighborName, happinessChange);
        }

        public Map<String, Integer> getNeighborHappiness() {
            return Collections.unmodifiableMap(neighborHappiness);
        }
    }
}
