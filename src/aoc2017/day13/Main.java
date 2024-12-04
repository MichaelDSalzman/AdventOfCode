package aoc2017.day13;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
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

        public long calculateP1(List<String> lines) {
            List<List<Integer>> layers = getLayers(lines);
            long severity = 0;
            for(List<Integer> layer : layers) {
                if(isLayerAtTop(layer.get(0), layer.get(1), 0)) {
                    severity += ((long) layer.get(0) * layer.get(1));
                }
            }
            return severity;
        }

        public int calculateP2(List<String> lines) {

            List<List<Integer>> layers = getLayers(lines);

            int waitTime = 0;
            while(true) {
                boolean layerHit = false;

                for(List<Integer> layer : layers) {
                    if(isLayerAtTop(layer.get(0), layer.get(1), waitTime)) {
                        layerHit = true;
                        break;
                    }
                }

                if(!layerHit) {
                    return waitTime;
                }

                waitTime++;
            }
        }

        private List<List<Integer>> getLayers(List<String> lines) {
            List<List<Integer>> layers = new ArrayList<>();

            for (String line : lines) {
                List<Integer> layer = new ArrayList<>();

                String[] split = line.split("\\s*:\\s*");
                layer.add(Integer.parseInt(split[0]));
                layer.add(Integer.parseInt(split[1]));

                layers.add(layer);
            }

            return layers;

        }

        private boolean isLayerAtTop(int depth, int range, int waitTime) {
            return (depth + waitTime) % (range - 2 + range) == 0;
        }
    }
}
