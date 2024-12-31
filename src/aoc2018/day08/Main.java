package aoc2018.day08;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
            return buildTree(
                Arrays.stream(lines.get(0)
                    .split("\\s+"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList()))
                .sumMetadata();
        }

        public Object calculateP2(List<String> lines) {
            return buildTree(
                Arrays.stream(lines.get(0)
                        .split("\\s+"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList()))
                .value();
        }

        private Node buildTree(List<Integer> intList) {
            int numberOfChildren = intList.remove(0);
            int numberOfMetadata = intList.remove(0);

            Node node = new Node(new ArrayList<>(), new ArrayList<>());
            for(int i=0; i<numberOfChildren; i++) {
                node.children.add(buildTree(intList));
            }

            for(int i=0; i<numberOfMetadata; i++) {
                node.metadata.add(intList.remove(0));
            }

            return node;
        }
    }


    public record Node(List<Node> children, List<Integer> metadata){
        public int sumMetadata() {
            return metadata.stream()
                .mapToInt(Integer::intValue)
                .sum()
                + children.stream()
                .map(Node::sumMetadata)
                .mapToInt(Integer::intValue)
                .sum();
        }

        public long value() {
            long value = 0L;
            if(children.isEmpty()) {
                value = metadata.stream().mapToInt(Integer::intValue).sum();
            } else {
                value = metadata.stream()
                    .map(m -> m-1)
                    .map(m -> children.size() > m ? children.get(m).value() : 0L)
                    .mapToLong(Long::longValue)
                    .sum();
            }
            return value;
        }
    }
}
