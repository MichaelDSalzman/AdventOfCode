package aoc2023.day08;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2023";
        String day = "08";

        // FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        long start = System.currentTimeMillis();
        // System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println("P1 took " + (System.currentTimeMillis() - start) + " millis");
        start = System.currentTimeMillis();
        System.out.println("P2 : " + problem.calculateP2(lines));
        System.out.println("P2 took " + (System.currentTimeMillis() - start) + " millis");
    }

    public static class Problem {

        public int calculateP1(List<String> lines) {
            String instructions = lines.get(0);
            Map<String, Node> nodeMap = generateMap(lines.subList(2, lines.size()));

            int stepCount = 0;
            Node currentNode = nodeMap.get("AAA");
            String desiredNodeName = "ZZZ";
            while (!currentNode.name.equals(desiredNodeName)) {
                char direction = instructions.charAt(stepCount % instructions.length());
                stepCount++;

                if ('R' == direction) {
                    currentNode = currentNode.getRightChild();
                } else {
                    currentNode = currentNode.getLeftChild();
                }
            }
            return stepCount;
        }

        public long calculateP2(List<String> lines) {

            String instructions = lines.get(0);
            Map<String, Node> nodeMap = generateMap(lines.subList(2, lines.size()));

            List<Node> currentNodes = new ArrayList<>();
            for (String key : nodeMap.keySet()) {
                if (key.endsWith("A")) {
                    currentNodes.add(nodeMap.get(key));
                }
            }

            List<Long> stepsForNodes = new ArrayList<>();
            for(Node node : currentNodes) {
                long step = 0;
                while (!node.name.endsWith("Z")) {
                    char direction = instructions.charAt((int) (step % instructions.length()));
                    step++;

                    if ('R' == direction) {
                        node = node.getRightChild();
                    } else {
                        node = node.getLeftChild();
                    }
                }

                stepsForNodes.add(step);
            }

            System.out.println(stepsForNodes);
            System.out.println(gcd(stepsForNodes));
            System.out.println(lcm(stepsForNodes));
            return lcm(stepsForNodes);
        }
        private static long gcd(long a, long b)
        {
            while (b > 0)
            {
                long temp = b;
                b = a % b; // % is remainder
                a = temp;
            }
            return a;
        }

        private static long gcd(List<Long> input)
        {
            long result = input.get(0);
            for(int i = 1; i < input.size(); i++) result = gcd(result, input.get(i));
            return result;
        }

        private static long lcm(long a, long b)
        {
            return a * (b / gcd(a, b));
        }

        private static long lcm(List<Long> input)
        {
            long result = input.get(0);
            for(int i = 1; i < input.size(); i++) result = lcm(result, input.get(i));
            return result;
        }

        private Map<String, Node> generateMap(List<String> lines) {

            Pattern p = Pattern.compile("(.+) = \\((.*), (.*)\\)");
            Map<String, Node> nodeMap = new HashMap<>();
            for (String line : lines) {
                Matcher m = p.matcher(line);
                if (m.find()) {
                    String name = m.group(1);
                    String leftChildName = m.group(2);
                    String rightChildName = m.group(3);

                    Node node = nodeMap.get(name);
                    if (node == null) {
                        node = new Node(name);
                        nodeMap.put(name, node);
                    }

                    Node leftChild = nodeMap.get(leftChildName);
                    if (leftChild == null) {
                        leftChild = new Node(leftChildName);
                        nodeMap.put(leftChildName, leftChild);
                    }

                    node.setLeftChild(leftChild);

                    Node rightChild = nodeMap.get(rightChildName);
                    if (rightChild == null) {
                        rightChild = new Node(rightChildName);
                        nodeMap.put(rightChildName, rightChild);
                    }

                    node.setRightChild(rightChild);
                }
            }

            return nodeMap;
        }

        public static class Node {
            private final String name;
            private Node leftChild;
            private Node rightChild;

            public Node(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public Node getLeftChild() {
                return leftChild;
            }

            public void setLeftChild(Node leftChild) {
                this.leftChild = leftChild;
            }

            public Node getRightChild() {
                return rightChild;
            }

            public void setRightChild(Node rightChild) {
                this.rightChild = rightChild;
            }

            @Override
            public String toString() {
                return "Node{" +
                    "name='" + name + '\'' +
                    ", leftChild=" + leftChild.name +
                    ", rightChild=" + rightChild.name +
                    '}';
            }
        }
    }
}
