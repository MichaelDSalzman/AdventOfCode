package aoc2024.day12;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
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
            Grid grid = Grid.valueOf(lines);
            // grid.regions.forEach(r -> System.out.println("REGION PLANT: " + r.plant + " SIZE PLOTS: " + r.plots.size() + " COST: " + r.cost(true)));
            return grid.regions.stream().map(r -> r.cost(true)).mapToInt(Integer::intValue).sum();
        }

        public Object calculateP2(List<String> lines) {
            Grid grid = Grid.valueOf(lines);
            // grid.regions.forEach(r -> System.out.println("REGION PLANT: " + r.plant + " SIZE PLOTS: " + r.plots.size() + " COST: " + r.cost(false)));
            return grid.regions.stream().map(r -> r.cost(false)).mapToInt(Integer::intValue).sum();
        }
    }

    record Point(int horizontal, int vertical){}
    record Region(Set<Point> plots, String plant) {
        record Fence(Direction direction, Point point) {
            enum Direction {
                NORTH, SOUTH, EAST, WEST
            }
        }

        public int cost(boolean countNumFences) {
            Set<Fence> fences = new HashSet<>();

            //calculate fence length
            plots.forEach(plot -> {
                //If no plot in region to north, then there is a fence there, etc etc etc
                if(!plots.contains(new Point(plot.horizontal, plot.vertical-1))) {
                    fences.add(new Fence(Fence.Direction.NORTH, plot));
                }
                if(!plots.contains(new Point(plot.horizontal, plot.vertical+1))) {
                    fences.add(new Fence(Fence.Direction.SOUTH, plot));
                }
                if(!plots.contains(new Point(plot.horizontal-1, plot.vertical))) {
                    fences.add(new Fence(Fence.Direction.WEST, plot));
                }
                if(!plots.contains(new Point(plot.horizontal+1, plot.vertical))) {
                    fences.add(new Fence(Fence.Direction.EAST, plot));
                }
            });

            int fenceFactor = 0;
            if(countNumFences) {
                // number of fences * number plots
                fenceFactor = fences.size();
            } else {
                // Need to combine fences into segments then count segments
                Set<Set<Fence>> fenceSegments = new HashSet<>();
                // iterate over all fences
                for(Fence fence : fences) {
                    // If there's already a fence segment with this fence, continue to next fence
                    if(fenceSegments.stream().anyMatch(fs -> fs.contains(fence))) {
                        continue;
                    }
                    // new fence segment
                    Set<Fence> fencesInSegment = new HashSet<>();
                    // recursively find all fences in segment
                    findFencesInSegment(fences, fence, fencesInSegment);
                    // add fence segment to list
                    fenceSegments.add(fencesInSegment);
                }

                fenceFactor = fenceSegments.size();
            }

            return fenceFactor * plots.size();
        }

        // Recursively find all fences in segment
        private void findFencesInSegment(Set<Fence> allFences, Fence fenceUnderReview, Set<Fence> fencesInSegment) {

            // return if already evaluated this fence
            if(fencesInSegment.contains(fenceUnderReview)) {
                return;
            }

            // add fence to segment
            fencesInSegment.add(fenceUnderReview);

            // If this fence is north/south of the plot, need to look west and east for fences along same segment
            if(fenceUnderReview.direction == Fence.Direction.NORTH || fenceUnderReview.direction == Fence.Direction.SOUTH) {
                Fence fenceToWest = new Fence(fenceUnderReview.direction, new Point(fenceUnderReview.point.horizontal-1, fenceUnderReview.point.vertical));
                Fence fenceToEast = new Fence(fenceUnderReview.direction, new Point(fenceUnderReview.point.horizontal+1, fenceUnderReview.point.vertical));

                if(allFences.contains(fenceToWest)) {
                    findFencesInSegment(allFences, fenceToWest, fencesInSegment);
                }

                if (allFences.contains(fenceToEast)) {
                    findFencesInSegment(allFences, fenceToEast, fencesInSegment);
                }
            } else if(fenceUnderReview.direction == Fence.Direction.WEST || fenceUnderReview.direction == Fence.Direction.EAST) {
                Fence fenceToNorth = new Fence(fenceUnderReview.direction, new Point(fenceUnderReview.point.horizontal, fenceUnderReview.point.vertical-1));
                Fence fenceToSouth = new Fence(fenceUnderReview.direction, new Point(fenceUnderReview.point.horizontal, fenceUnderReview.point.vertical+1));
                if(allFences.contains(fenceToNorth)) {
                    findFencesInSegment(allFences, fenceToNorth, fencesInSegment);
                }
                if (allFences.contains(fenceToSouth)) {
                    findFencesInSegment(allFences, fenceToSouth, fencesInSegment);
                }
            }
        }
    }

    record Grid(List<Region> regions) {

        // Convert the list of strings into records
        public static Grid valueOf(List<String> lines) {
            List<Region> regions = new ArrayList<>();

            // iterate over all the plots
            for(int vertical=0; vertical<lines.size(); vertical++) {
                String line = lines.get(vertical);
                for(int horizontal=0; horizontal<line.length(); horizontal++) {
                    String plant = line.substring(horizontal, horizontal+1);
                    Point point = new Point(horizontal, vertical);

                    //If we've already cataloged this plot, continue to the next
                    if(regions.stream().anyMatch(r -> r.plots.contains(point))){
                        continue;
                    }

                    // New region discovered
                    Region region = new Region(new HashSet<>(), plant);
                    // Find all the plots that go into this region
                    populateRegion(region, point, lines);

                    regions.add(region);
                }
            }

            return new Grid(regions);
        }

        // Given a region, a point, and a grid, find all the plots in the region
        private static void populateRegion(Region region, Point plot, List<String> lines) {
            // If the region already contains this plot, we've seen it before, just return
            if(region.plots.contains(plot)) {
                return;
            }

            // Add point to region
            region.plots.add(plot);

            // get plot to the west
            int horizontal = plot.horizontal-1;
            int vertical = plot.vertical;
            if(horizontal>=0) {
                String plant = lines.get(vertical).substring(horizontal, horizontal+1);
                // if plot to west is same plant, try and add it to the region by recursively calling this method
                if(plant.equals(region.plant)) {
                    populateRegion(region, new Point(horizontal, vertical), lines);
                }
            }
            // get plot to the east
            horizontal = plot.horizontal+1;
            vertical = plot.vertical;
            if(horizontal<=lines.get(vertical).length()-1) {
                String plant = lines.get(vertical).substring(horizontal, horizontal+1);
                if(plant.equals(region.plant)) {
                    populateRegion(region, new Point(horizontal, vertical), lines);
                }
            }

            // get plot to the south
            horizontal = plot.horizontal;
            vertical = plot.vertical+1;
            if(vertical<=lines.size()-1) {
                String plant = lines.get(vertical).substring(horizontal, horizontal+1);
                if(plant.equals(region.plant)) {
                    populateRegion(region, new Point(horizontal, vertical), lines);
                }
            }
            // get plot to the north
            horizontal = plot.horizontal;
            vertical = plot.vertical-1;
            if(vertical>=0) {
                String plant = lines.get(vertical).substring(horizontal, horizontal+1);
                if(plant.equals(region.plant)) {
                    populateRegion(region, new Point(horizontal, vertical), lines);
                }
            }
        }
    }
}
