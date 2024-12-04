package aoc2015.day18;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "18";

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
            Map<String, Boolean> lights = new HashMap<>();

            for(int i=0; i<lines.get(0).length(); i++) {
                String line = lines.get(i);
                for(int j=0; j<line.length(); j++) {
                    String light = line.substring(j, j+1);
                    lights.put(i + "," + j, light.equals("#"));
                }
            }

            lights.put("0,0", true);
            lights.put((lines.size()-1) + ",0", true);
            lights.put("0," + (lines.size()-1), true);
            lights.put((lines.size()-1) + "," + (lines.size()-1), true);

            int step = 0;
            Map<String, Boolean> lightsCopy = new HashMap<>();
            while(step < 100) {
                step++;
                for(String key : lights.keySet()) {
                    Boolean lightOn = lights.get(key);
                    int x = Integer.parseInt(key.split(",")[0]);
                    int y = Integer.parseInt(key.split(",")[1]);
                    int neighborCount = 0;
                    for(int i=-1; i<=1; i++) {
                        for(int j=-1; j<=1; j++) {
                            if(i == 0 && j == 0) {
                                continue;
                            }
                            Boolean neighbor = lights.get((x+i) + "," + (y+j));
                            if(neighbor != null && neighbor) {
                                neighborCount++;
                            }
                        }
                    }

                    if(lightOn && (neighborCount == 2 || neighborCount == 3)) {
                        lightsCopy.put(key, true);
                    } else if(!lightOn && neighborCount == 3) {
                        lightsCopy.put(key, true);
                    } else {
                        lightsCopy.put(key, false);
                    }
                }

                lights = lightsCopy;

                lights.put("0,0", true);
                lights.put((lines.size()-1) + ",0", true);
                lights.put("0," + (lines.size()-1), true);
                lights.put((lines.size()-1) + "," + (lines.size()-1), true);

                lightsCopy = new HashMap<>();
            }

            return lights.values().stream().filter(b -> b).toList().size();
        }

        public int calculateP2(List<String> lines) {
            return -1;
        }
    }
}
