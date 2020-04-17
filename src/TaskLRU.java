import java.util.ArrayList;

/**
 * This class implements the Runnable class to simulate a Least Recently Used page replacement algorithm
 * @author Parker Krieger
 */
public class TaskLRU implements Runnable {
    private int[] sequence;
    private final int MAX_MEMORY_FRAMES;
    private final int MAX_PAGE_REFERENCE;
    private int[] pageFaults;

    /**
     * @param sequence The sequence of page numbers
     * @param maxMemoryFrames The maximum number of frames in memory
     * @param maxPageReference Greatest possible page number
     * @param pageFaults An array to store the number of page faults
     */
    public TaskLRU (int[] sequence, int maxMemoryFrames, int maxPageReference, int[] pageFaults){
        this.sequence = sequence;
        this.MAX_MEMORY_FRAMES = maxMemoryFrames;
        this.MAX_PAGE_REFERENCE = maxPageReference;
        this.pageFaults = pageFaults;
    }

    /**
     * This method simulates a least recently used page replacement scheme.
     * This methods output can be found in the pageFaults array.
     */
    @Override
    public void run() {
        ArrayList<Integer> memory = new ArrayList<>();
        boolean[] pageTable = new boolean[this.MAX_PAGE_REFERENCE + 1];
        int pageFaults = 0;
        for (int value : sequence){
            if (!pageTable[value]){
                pageFaults++;
                if (memory.size() >= this.MAX_MEMORY_FRAMES + 1){
                    int removed = memory.remove(0);
                    pageTable[removed] = false;
                }
            }
            else{
                memory.remove((Integer) value);
            }
            memory.add(value);
            pageTable[value] = true;
        }
        this.pageFaults[this.MAX_MEMORY_FRAMES] = pageFaults;
    }
}
