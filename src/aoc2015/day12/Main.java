package aoc2015.day12;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "12";

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
            JSONObject root = new JSONObject(lines.get(0));
            int count = iterateOverMap(root.toMap(), false);

            return count;
        }

        public int calculateP2(List<String> lines) {
            JSONObject root = new JSONObject(lines.get(0));
            int count = iterateOverMap(root.toMap(), true);

            return count;
        }

        private int iterateOverMap(Map<String, Object> map, boolean skipRed) {
            int count = 0;
            if(skipRed && map.containsValue("red")) {
                return 0;
            }

            for(String key : map.keySet()) {
                Object o = map.get(key);
                if (o instanceof Map) {
                    count += iterateOverMap((Map) o, skipRed);
                } else if (o instanceof List) {
                    count += iterateOverList((List) o, skipRed);
                } else if (o instanceof Integer) {
                    count += (Integer) o;
                }
            }

            return count;
        }

        private int iterateOverList(List list, boolean skipRed) {
            int count = 0;
            for(Object o : list) {
                if(o instanceof Map) {
                    count += iterateOverMap((Map) o, skipRed);
                } else if (o instanceof List) {
                    count += iterateOverList((List) o, skipRed);
                } else if (o instanceof Integer) {
                    count += (Integer) o;
                }
            }

            return count;
        }

        private int iterateOverJsonArray(JSONArray jsonArray) {
            int count = 0;


            return count;
        }
    }
}
