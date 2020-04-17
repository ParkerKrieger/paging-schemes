import java.util.LinkedList;
import java.util.Queue;

/**
 * This class implements the Runnable interface to simulate a FIFO page replacement algorithm.
 * @author Parker Krieger
 */
public class TaskFIFO implements Runnable {
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
    public TaskFIFO (int[] sequence, int maxMemoryFrames, int maxPageReference, int[] pageFaults){
        this.sequence = sequence;
        this.MAX_MEMORY_FRAMES = maxMemoryFrames;
        this.MAX_PAGE_REFERENCE = maxPageReference;
        this.pageFaults = pageFaults;
    }

    /**
     * This method will simulate a FIFO page replacement scheme.
     * This method outputs its results in the pageFaults array.
     */
    @Override
    public void run() {
        Queue<Integer> memory = new LinkedList<>();
        boolean[] pageTable = new boolean[this.MAX_PAGE_REFERENCE + 1];
        int pageFaults = 0;
        for (int value : sequence) {
            if (!pageTable[value]){
                if (memory.size() >= this.MAX_MEMORY_FRAMES + 1){
                    int removed = memory.remove();
                    pageTable[removed] = false;
                }
                memory.add(value);
                pageTable[value] = true;
                pageFaults++;
            }
        }
        this.pageFaults[this.MAX_MEMORY_FRAMES] = pageFaults;
    }
}
