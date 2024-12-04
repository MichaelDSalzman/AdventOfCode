package aoc2022.day12;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String day = "12";

        // FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        System.out.println("P1 : " + problem.calculateP1(lines));
        System.out.println("P2 : " + problem.calculateP2(lines));
    }

    public static class Problem {

        public int calculateP1(List<String> lines) {
            Node startNode = findStartingNode(parseLinesIntoNodes(lines));

            Map<String, Integer> numStepsToReach = new HashMap<>();
            numStepsToReach.put(startNode.getCoords(), 0);
            Set<Node> visitedNodes = new HashSet<>();
            Queue<Node> queue = new ArrayDeque<>();
            queue.add(startNode);

            while (!queue.isEmpty()) {
                Node currentNode = queue.remove();

                if (currentNode.getValue().equals("E")) {
                    return numStepsToReach.get(currentNode.getCoords());
                }

                Node finalCurrentNode = currentNode;
                currentNode.getConnected().forEach(c -> {
                    numStepsToReach.put(c.getCoords(), numStepsToReach.get(finalCurrentNode.getCoords()) + 1);
                });

                visitedNodes.add(currentNode);
                queue.addAll(currentNode.getConnected());
                queue.removeAll(visitedNodes);
            }

            return -1;
        }

        public int calculateP2(List<String> lines) {
            Map<String, Node> nodeMap = parseLinesIntoNodes(lines);
            List<Node> possibleStartingNodes = nodeMap.values().stream()
                .filter(n -> n.getValue().equals("S") || n.getValue().equals("a")).toList();
            int minSteps = Integer.MAX_VALUE;

            for (Node startNode : possibleStartingNodes) {
                Map<String, Integer> numStepsToReach = new HashMap<>();
                numStepsToReach.put(startNode.getCoords(), 0);
                Set<Node> visitedNodes = new HashSet<>();
                Queue<Node> queue = new ArrayDeque<>();
                queue.add(startNode);

                while (!queue.isEmpty()) {
                    Node currentNode = queue.remove();
                    if (currentNode.getValue().equals("E")) {
                        int numSteps = numStepsToReach.get(currentNode.getCoords());
                        if (minSteps > numSteps) {
                            minSteps = numSteps;
                            break;
                        }
                    }

                    currentNode.getConnected().forEach(c -> numStepsToReach.put(c.getCoords(),
                        numStepsToReach.get(currentNode.getCoords()) + 1));

                    visitedNodes.add(currentNode);
                    queue.addAll(currentNode.getConnected());
                    queue.removeAll(visitedNodes);
                }
            }

            return minSteps;
        }


        private Map<String, Node> parseLinesIntoNodes(List<String> lines) {
            Map<String, Node> nodeMap = new HashMap<>();

            for (int rowNum = 0; rowNum < lines.size(); rowNum++) {
                String line = lines.get(rowNum);

                for (int colNum = 0; colNum < line.length(); colNum++) {
                    Node node = new Node(String.valueOf(line.charAt(colNum)), rowNum, colNum);
                    nodeMap.put(node.getCoords(), node);
                }
            }

            for (String keys : nodeMap.keySet()) {
                Node node = nodeMap.get(keys);
                for (String neighborCoords : node.getNeighborCoords()) {
                    Node neighbor = nodeMap.get(neighborCoords);
                    if (neighbor != null) {
                        node.tryConnect(neighbor);
                    }
                }
            }

            return nodeMap;
        }

        private Node findStartingNode(Map<String, Node> nodeMap) {
            for (String key : nodeMap.keySet()) {
                Node node = nodeMap.get(key);
                if (node.getValue().equals("S")) {
                    return node;
                }
            }

            return null;
        }
    }

    public static class Node {
        private final List<Node> connected = new ArrayList<>();
        private String value;
        private Integer rowNum;
        private Integer colNum;

        public Node(String value, Integer rowNum, Integer colNum) {
            this.value = value;
            this.rowNum = rowNum;
            this.colNum = colNum;
        }

        private void tryConnect(Node node) {
            if (node.getHeight() - 1 <= this.getHeight()) {
                this.connected.add(node);
            }
        }

        public String getValue() {
            return value;
        }

        public int getHeight() {
            if (value.equals("S")) {
                return 1;
            } else if (value.equals("E")) {
                return 26;
            }
            return value.charAt(0) - 96;
        }

        public Integer getRowNum() {
            return rowNum;
        }

        public Integer getColNum() {
            return colNum;
        }

        public List<Node> getConnected() {
            return connected;
        }

        public String getCoords() {
            return this.getRowNum() + "," + getColNum();
        }

        public List<String> getNeighborCoords() {
            return Arrays.asList(
                (this.getRowNum() - 1) + "," + this.getColNum(),
                (this.getRowNum() + 1) + "," + this.getColNum(),
                this.getRowNum() + "," + (this.getColNum() - 1),
                this.getRowNum() + "," + (this.getColNum() + 1)
            );
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node node = (Node) o;
            return Objects.equals(rowNum, node.rowNum) && Objects.equals(colNum,
                node.colNum);
        }

        @Override
        public int hashCode() {
            return Objects.hash(rowNum, colNum);
        }

        @Override
        public String toString() {
            return "Node{" +
                "value='" + value + '\'' +
                ", rowNum=" + rowNum +
                ", colNum=" + colNum +
                ", connected=" + connected.stream().map(Node::getCoords).toList() +
                '}';
        }

    }
}
