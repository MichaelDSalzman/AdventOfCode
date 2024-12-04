package aoc2017.day07;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "07";

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

        public String calculateP1(List<String> lines) {
            Map<String, Disc> discMap = getDiscs(lines);

            return discMap.keySet().stream().filter(d -> discMap.get(d).getParent() == null).findFirst().get();
        }

        public int calculateP2(List<String> lines) {

            Map<String, Disc> discMap = getDiscs(lines);

            Disc root = discMap.get(discMap.keySet().stream().filter(d -> discMap.get(d).getParent() == null).findFirst().get());

            Disc discToInspect = root;
            while(true) {
                String childNameThatIsWrong = findWrongChild(discToInspect);

                if(childNameThatIsWrong == null) {
                    System.out.println(discToInspect.name);
                    int wrongDiskTotalWeight = getTotalWeight(discToInspect);
                    Disc finalDiscToInspect = discToInspect;
                    int correctWeight = getTotalWeight(discToInspect.getParent().getChildren().stream().filter(d -> !d.getName().equals(
                        finalDiscToInspect.getName())).findFirst().get());

                    return correctWeight - wrongDiskTotalWeight + discToInspect.getWeight();
                } else {
                    discToInspect = discMap.get(childNameThatIsWrong);
                }
            }
        }

        private String findWrongChild(Disc disc) {
            Map<Integer, List<String>> weightToChildren = new HashMap<>();
            for(Disc child : disc.getChildren()) {
                int totalWeight = getTotalWeight(child);
                List<String> children = weightToChildren.getOrDefault(totalWeight, new ArrayList<>());
                children.add(child.name);
                weightToChildren.put(totalWeight, children);
            }

            Optional<Integer> weightWithOneChild = weightToChildren.keySet().stream().filter(w -> weightToChildren.get(w).size() == 1).findFirst();
            return weightWithOneChild.map(integer -> weightToChildren.get(integer).get(0))
                .orElse(null);
        }

        private Map<String, Disc> getDiscs(List<String> lines) {
            Map<String, Disc> discMap = new HashMap<>();

            for(String line : lines) {
                String[] split = line.split("\\s+");
                String name = split[0];
                int weight = Integer.parseInt(split[1].substring(1, split[1].length()-1));
                Disc disc = discMap.getOrDefault(name, new Disc(name));
                disc.setWeight(weight);
                discMap.put(name, disc);

                if(line.contains("->")) {
                    for(int i=3; i<split.length; i++) {
                        String childName = split[i].trim();
                        childName = childName.replace(",", "");

                        Disc child = discMap.getOrDefault(childName, new Disc(childName));
                        child.setParent(disc);
                        discMap.put(childName, child);

                        disc.addChild(child);
                    }
                }
            }

            return discMap;
        }

        private int getTotalWeight(Disc disc) {
            int totalWeight = disc.getWeight();
            for(Disc child : disc.getChildren()) {
                totalWeight += getTotalWeight(child);
            }

            return totalWeight;
        }
    }

    public static class Disc {
        private List<Disc> children = new ArrayList<>();
        private Disc parent;
        private String name;
        private int weight;

        public Disc(String name) {
            this.name = name;
        }

        public List<Disc> getChildren() {
            return children;
        }

        public Disc getParent() {
            return parent;
        }

        public String getName() {
            return name;
        }

        public int getWeight() {
            return weight;
        }

        public void addChild(Disc child) {
            this.children.add(child);
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public void setParent(Disc parent) {
            this.parent = parent;
        }
    }
}
