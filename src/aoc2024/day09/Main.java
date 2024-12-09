package aoc2024.day09;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2024";
        String day = "09";

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

        /**
         * Treat the problem as a List of Integers since the files are split up anyway
         * @param lines
         * @return
         */
        public Object calculateP1(List<String> lines) {
            String line = lines.get(0);
            List<Integer> disk = new ArrayList<>();

            // Build up the disk by iterating over input line
            for(int i=0; i< line.length(); i++) {
                boolean idxOnFile = i%2==0;
                int fileId = i/2;
                int blockLength = Integer.parseInt(line.substring(i, i+1));
                for(int b=0; b<blockLength; b++) {
                    disk.add(idxOnFile ? fileId : null);
                }
            }

            // Iterate outside-in until you meet in the middle
            // Keep track of two indices, one in the front moving to back (looking for empty spaces),
            // one in back moving to front (looking for files blocks)
            int frontToBackIdx = 0;
            int backToFrontIdx = disk.size()-1;
            while(frontToBackIdx < backToFrontIdx) {
                // If the spot in the front is not empty, move to the next spot
                if(disk.get(frontToBackIdx) != null) {
                    frontToBackIdx++;
                }
                // if the spot at the back is not a file, move to the next spot
                else if(disk.get(backToFrontIdx) == null) {
                    backToFrontIdx--;
                }
                // Otherwise, we have the location of an empty spot and the location of a file to move
                else {
                    // Move the file block to the front and null out where it came from
                    disk.set(frontToBackIdx, disk.get(backToFrontIdx));
                    disk.set(backToFrontIdx, null);
                }
            }

            // Calculate checksum
            return calculateChecksum(disk);
        }

        // Treat disk as a list of blocks since we'll have to slice and dice empty locations
        public Object calculateP2(List<String> lines) {
            String line = lines.get(0);
            List<Block> disk = new ArrayList<>();

            // Build up the disk of blocks
            for(int i=0; i< line.length(); i++) {
                boolean idxOnFile = i%2==0;
                int fileId = i/2;
                int blockLength = Integer.parseInt(line.substring(i, i+1));
                disk.add(new Block(
                    idxOnFile ? BlockType.FILE : BlockType.EMPTY,
                    blockLength,
                    idxOnFile ? fileId: null));
            }

            // Iterate from back to front, look for files
            for(int backToFront = disk.size() - 1; backToFront>=0; backToFront--) {
                if(disk.get(backToFront).blockType != BlockType.FILE) {
                    continue;
                }

                Block fileBlock = disk.get(backToFront);

                // Iterate from front to back and look for an empty location that can fit the file being moved
                for(int frontToBack=0; frontToBack<backToFront; frontToBack++){

                    // If the block is empty and big enough, we can move the file into it
                    if(disk.get(frontToBack).blockType == BlockType.EMPTY && disk.get(frontToBack).size >= fileBlock.size) {
                        Block emptyBlock = disk.get(frontToBack);

                        // Null out the block at the back
                        disk.set(backToFront, new Block(BlockType.EMPTY, fileBlock.size, null));
                        // Put the file at the front in the empty block
                        disk.set(frontToBack, fileBlock);
                        // If the empty space was bigger than the file, there needs to be padding
                        if (emptyBlock.size > fileBlock.size) {
                            // Add a new empty block after the file with a size equal to the difference of the old gap and the new file
                            disk.add(frontToBack + 1,
                                new Block(BlockType.EMPTY, emptyBlock.size - fileBlock.size, null));
                        }

                        break;
                    }
                }
            }

            // Convert the list of blocks to a list of ints
            List<Integer> diskAsIntegers = new ArrayList<>();
            for(Block block: disk) {
                Integer value = block.fileId;
                for(int i=0; i<block.size; i++) {
                    diskAsIntegers.add(value);
                }
            }

            return calculateChecksum(diskAsIntegers);
        }

        private Long calculateChecksum(List<Integer> disk) {
            Long checksum = 0L;
            for(int i=0; i<disk.size(); i++) {
                if(disk.get(i) == null) {
                    continue;
                }
                checksum += ((long) i * disk.get(i));
            }
            return checksum;
        }
    }

    enum BlockType {
        FILE,
        EMPTY;
    }

    record Block(BlockType blockType, int size, Integer fileId) {}
}
