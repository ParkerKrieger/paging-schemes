import java.util.LinkedList;
import java.util.Queue;

public class TaskFIFO implements Runnable {
    private int[] sequence;
    private final int MAX_MEMORY_FRAMES;
    private final int MAX_PAGE_REFERENCE;
    private int[] pageFaults;

    public TaskFIFO (int[] sequence, int maxMemoryFrames, int maxPageReference, int[] pageFaults){
        this.sequence = sequence;
        this.MAX_MEMORY_FRAMES = maxMemoryFrames;
        this.MAX_PAGE_REFERENCE = maxPageReference;
        this.pageFaults = pageFaults;
    }

    @Override
    public void run() {
        Queue<Integer> memory = new LinkedList<>();
        int pageFaults = 0;
        for (int value : sequence) {
            if (!memory.contains(value)){
                if (memory.size() >= this.MAX_MEMORY_FRAMES + 1){
                    memory.remove();
                }
                memory.add(value);
                pageFaults++;
            }
        }
        this.pageFaults[this.MAX_MEMORY_FRAMES] = pageFaults;
    }
}
