package aoc2016.day20;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "20";

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

            List<Range> ranges = new ArrayList<>();
            for(String line : lines) {
                String[] parts = line.split("-");
                Range range = new Range(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
                ranges.add(range);
            }
            Collections.sort(ranges);

            Long lastEndRange = null;
            for(Range range : ranges) {
                if(lastEndRange == null) {
                    if(range.start == 0) {
                        lastEndRange = range.end;
                        continue;
                    }
                }

                // new range is completely within the already discovered range
                if(lastEndRange > range.start && lastEndRange > range.end) {
                    continue;
                }

                // new range leaves a gap of one so it must be the answer
                if(range.start > lastEndRange + 1) {
                    return lastEndRange + 1;
                }

                // No new gap, but only keep the new range end if it's more than the current range end
                if(range.end > lastEndRange) {
                    lastEndRange = range.end;
                }
            }
            return -1;
        }

        public long calculateP2(List<String> lines) {
            List<Range> ranges = new ArrayList<>();
            for(String line : lines) {
                String[] parts = line.split("-");
                Range range = new Range(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
                ranges.add(range);
            }
            Collections.sort(ranges);

            long numAllowedIps = 0;
            Long lastEndRange = null;
            for(Range range : ranges) {
                if(lastEndRange == null) {
                    if(range.start == 0) {
                        lastEndRange = range.end;
                        continue;
                    }
                }

                // new range is completely within the already discovered range
                if(lastEndRange > range.start && lastEndRange > range.end) {
                    continue;
                }

                // new range leaves a gap of one so count the gaps
                if(range.start > lastEndRange + 1) {
                    numAllowedIps += range.start - (lastEndRange+1);
                }

                // No new gap, but only keep the new range end if it's more than the current range end
                if(range.end > lastEndRange) {
                    lastEndRange = range.end;
                }
            }
            return numAllowedIps;
        }

        public static class Range implements Comparable<Range> {

            private final long start;
            private final long end;

            public Range(long start, long end) {
                this.start = start;
                this.end = end;
            }

            @Override
            public int compareTo(Range o) {
                if(start != o.start) {
                    if(start < o.start) {
                        return -1;
                    }
                    return 1;
                }

                if(end < o.end) {
                    return -1;
                } else if (end > o.end) {
                    return 1;
                }
                return 0;
            }
        }
    }
}
