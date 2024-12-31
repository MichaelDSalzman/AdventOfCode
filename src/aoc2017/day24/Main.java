package aoc2017.day24;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = null;
        String day = null;

        Pattern p = Pattern.compile("aoc(\\d+)\\.day(\\d+).*");
        Matcher m = p.matcher(Main.class.getName());
        if(m.find()) {
            year = m.group(1);
            day = m.group(2);
        }

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

        public Object calculateP1(List<String> lines) {
            List<Bridge> allBridges = findAllBridges(lines, 0);
            return allBridges.stream().map(Bridge::getStrength).mapToInt(Integer::intValue).max().orElseThrow();
        }

        public Object calculateP2(List<String> lines) {
            List<Bridge> allBridges = findAllBridges(lines, 0);
            int longestLength = allBridges.stream().map(b -> b.components.size()).mapToInt(Integer::intValue).max().orElseThrow();
            return allBridges.stream()
                .filter(b -> b.components.size() == longestLength)
                .map(Bridge::getStrength)
                .mapToInt(Integer::intValue)
                .max()
                .orElseThrow();
        }

        public List<Bridge> findAllBridges(List<String> lines, int startingValue) {
            List<Component> allComponents = lines.stream()
                .map(Component::valueOf)
                .toList();

            List<Component> startingComponents = allComponents.stream()
                .filter(c -> c.hasPortWithValue(startingValue))
                .toList();

            List<Bridge> allBridges = new ArrayList<>();
            for(Component startingComponent : startingComponents) {
                List<Component> newAvailableComponents = new ArrayList<>(allComponents);
                newAvailableComponents.remove(startingComponent);

                allBridges.addAll(
                    continueBuildingBridge(
                        new Bridge(
                            List.of(startingComponent)),
                        newAvailableComponents,
                        startingComponent.total() - startingValue));
            }

            return allBridges;
        }

        private List<Bridge> continueBuildingBridge(Bridge currentBridge,
                                                    List<Component> availableComponents,
                                                    int openPort) {
            List<Bridge> bridges = new ArrayList<>();
            bridges.add(currentBridge);

            for(Component availableComponent : availableComponents.stream()
                .filter(c -> c.hasPortWithValue(openPort))
                .toList()) {
                List<Component> newComponentList = new ArrayList<>(currentBridge.components);
                newComponentList.add(availableComponent);
                Bridge newBridge = new Bridge(newComponentList);

                List<Component> newAvailableComponents = new ArrayList<>(availableComponents);
                newAvailableComponents.remove(availableComponent);

                bridges.addAll(
                    continueBuildingBridge(
                        newBridge,
                        newAvailableComponents,
                        availableComponent.total() - openPort));
            }

            return bridges;
        }
    }

    public record Bridge(List<Component> components) {
        int getStrength() {
            return components.stream().map(Component::total).mapToInt(Integer::intValue).sum();
        }
    }
    public record Component(int left, int right) {
        public static Component valueOf(String line) {
            String[] parts = line.split("/");
            return new Component(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }

        public boolean hasPortWithValue(int portValue) {
            return left == portValue || right == portValue;
        }

        public int total() {
            return left + right;
        }
    }
}
