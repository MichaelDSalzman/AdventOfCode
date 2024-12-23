package aoc2024.day23;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
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
            Set<Set<String>> networks = findNetworks(Graph.valueOf(lines));
            // Find the number of networks that have exactly 3 nodes and at least one of the nodes starts with a "t"
            return networks.stream()
                .filter(network -> network.size() == 3 && network.stream()
                    .anyMatch(node -> node.startsWith("t")))
                .collect(Collectors.toSet()).size();
        }

        public Object calculateP2(List<String> lines) {
            Set<Set<String>> networks = findNetworks(Graph.valueOf(lines));

            // Find the network that is the largest
            Set<String> largestNetwork = networks.stream().reduce(new HashSet<>(), (a,b) -> a.size() > b.size() ? a : b);
            return StringUtils.join(largestNetwork.stream().sorted().toList(), ",");
        }

        public Set<Set<String>> findNetworks(Graph graph) {
            Set<Set<String>> allKnownNetworks = new HashSet<>();
            for(String nodeName : graph.nodes.keySet()) {
                Node node = graph.nodes.get(nodeName);

                Set<String> network = new HashSet<>();
                network.add(node.value);
                findNetworks(allKnownNetworks, network, node, graph);
            }

            return allKnownNetworks;
        }

        // start with A
        // new network containing just A
        // look at neighbors of A (B, E, F)
        //   have a network with A, looking at B
        //     is B connected to all nodes in network (just A)? Yes
        //     Create new network with all nodes from old network (A) + B (A, B)
        //     Look at neighbors of B (C, D, E, A)
        //       have a network with A,B, looking at C
        //         is C connected to all nodes in network (A,B), No
        //       have a network with A.B looking at D
        //         is D connected to all nodes in network (A,B), No
        //       A,B looking at E
        //         is E connected to all nodes in network (A,B), Yes
        //         Create new network with all nodes from old network (A,B) + E => (A,B,E)
        //         Look at neighbors of E (C, D, B, A, F)
        //           have ABE, looking at C, C not connected
        //           have ABE, looking at D, D not connected
        //           have ABE, looking at B, B already in network
        //           have ABE, looking at A, A already in network
        //           have ABE, looking at F, F not connected
        // etc etc etc
        private void findNetworks(Set<Set<String>> allKnownNetworks, Set<String> networkBeingBuilt, Node currentNode, Graph graph) {
            Set<String> network = new HashSet<>(networkBeingBuilt);
            network.add(currentNode.value);
            allKnownNetworks.add(network);

            for(String neighbor : currentNode.neighbors) {
                // Current network already has this neighbor
                if(network.contains(neighbor)) {
                    continue;
                }

                //there's already a network with this combination
                Set<String> tempSet = new HashSet<>(network);
                tempSet.add(neighbor);
                if(allKnownNetworks.contains(tempSet)) {
                    continue;
                }

                // All nodes currently in network being built connected to this neighbor?
                if(network.stream().allMatch(nodeName -> graph.nodes().get(nodeName).neighbors.contains(neighbor))) {
                    findNetworks(allKnownNetworks, network, graph.nodes.get(neighbor), graph);
                }
            }
        }

        public record Node(String value, Set<String> neighbors) {}
        public record Graph(Map<String, Node> nodes) {
            public static Graph valueOf(List<String> lines) {
                Map<String, Node> nodes = new HashMap<>();

                for(String line : lines) {
                    String[] nodeNames = line.split("-");

                    Node left = nodes.getOrDefault(nodeNames[0], new Node(nodeNames[0], new HashSet<>()));
                    Node right = nodes.getOrDefault(nodeNames[1], new Node(nodeNames[1], new HashSet<>()));

                    left.neighbors.add(right.value);
                    right.neighbors.add(left.value);
                    nodes.put(left.value, left);
                    nodes.put(right.value, right);
                }

                return new Graph(nodes);
            }
        }
    }
}
