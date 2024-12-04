package aoc2016.day13;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "13";

        FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/sample.txt");
        // FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/input.txt");
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
            int favoriteNumber = 1362;

            return findShortestPath(new Node(1, 1, 0), new Node(31, 39, 0), favoriteNumber);
        }

        public int calculateP2(List<String> lines) {
            return travelNumberOfSteps(new Node(1,1, 0), 50, 1362);
        }

        private int findShortestPath(Node start, Node destination, int favoriteNumber) {
            Set<Node> visitedLocations = new HashSet<>();
            Queue<Node> queue = new ArrayDeque<>();
            queue.add(start);

            while(!queue.isEmpty()) {
                Node node = queue.remove();
                if(visitedLocations.contains(node)) {
                    continue;
                }
                visitedLocations.add(node);
                if(node.equals(destination)) {

                    printMap(visitedLocations, favoriteNumber);

                    return node.steps;
                }

                //North
                Node newNode = new Node(node.x, node.y-1, node.steps + 1);
                if(newNode.x>=0 && newNode.y>=0 && isOpen(newNode, favoriteNumber)) {
                    queue.add(newNode);
                }

                //South
                newNode = new Node(node.x, node.y+1, node.steps + 1);
                if(newNode.x>=0 && newNode.y>=0 && isOpen(newNode, favoriteNumber)) {
                    queue.add(newNode);
                }

                //East
                newNode = new Node(node.x+1, node.y, node.steps + 1);
                if(newNode.x>=0 && newNode.y>=0 && isOpen(newNode, favoriteNumber)) {
                    queue.add(newNode);
                }

                //West
                newNode = new Node(node.x-1, node.y, node.steps + 1);
                if(newNode.x>=0 && newNode.y>=0 && isOpen(newNode, favoriteNumber)) {
                    queue.add(newNode);
                }
            }

            return -1;
        }


        private int travelNumberOfSteps(Node start, int numSteps, int favoriteNumber) {
            Set<Node> visitedLocations = new HashSet<>();
            Queue<Node> queue = new ArrayDeque<>();
            queue.add(start);

            while(!queue.isEmpty()) {
                Node node = queue.remove();
                if(visitedLocations.contains(node) || node.steps > numSteps) {
                    continue;
                }
                visitedLocations.add(node);

                //North
                Node newNode = new Node(node.x, node.y-1, node.steps + 1);
                if(newNode.x>=0 && newNode.y>=0 && isOpen(newNode, favoriteNumber)) {
                    queue.add(newNode);
                }

                //South
                newNode = new Node(node.x, node.y+1, node.steps + 1);
                if(newNode.x>=0 && newNode.y>=0 && isOpen(newNode, favoriteNumber)) {
                    queue.add(newNode);
                }

                //East
                newNode = new Node(node.x+1, node.y, node.steps + 1);
                if(newNode.x>=0 && newNode.y>=0 && isOpen(newNode, favoriteNumber)) {
                    queue.add(newNode);
                }

                //West
                newNode = new Node(node.x-1, node.y, node.steps + 1);
                if(newNode.x>=0 && newNode.y>=0 && isOpen(newNode, favoriteNumber)) {
                    queue.add(newNode);
                }
            }

            return visitedLocations.size();
        }

        private void printMap(Set<Node> visitedLocations, int favoriteNum) {

            char[][] map = new char[40][40];
            for(int i=0; i<map.length; i++) {
                char[] line = map[i];
                for(int j=0;j<line.length; j++) {
                    if(isOpen(new Node(j, i, 0), favoriteNum)) {
                        map[j][i] = '.';
                    } else {
                        map[j][i] = '#';
                    }
                }
            }

            for(Node visitedNode : visitedLocations) {
                map[visitedNode.y][visitedNode.x] = 'O';
            }

            for(char[] line : map) {
                System.out.println(String.valueOf(line));
            }
        }

        private boolean isOpen(Node node, int favoriteNum) {
            int x = node.x;
            int y = node.y;
            return Integer.bitCount((x*x) + (3*x) + (2*x*y) + y + (y*y) + favoriteNum) % 2 == 0;
        }
    }

    public static class Node {
        int x, y, steps;

        public Node(int x, int y, int steps) {
            this.x = x;
            this.y = y;
            this.steps = steps;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getSteps() {
            return steps;
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
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
