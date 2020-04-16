import java.util.ArrayList;

public class TaskMRU implements Runnable {
    private int[] sequence;
    private final int MAX_MEMORY_FRAMES;
    private final int MAX_PAGE_REFERENCE;
    private int[] pageFaults;

    public TaskMRU (int[] sequence, int maxMemoryFrames, int maxPageReference, int[] pageFaults){
        this.sequence = sequence;
        this.MAX_MEMORY_FRAMES = maxMemoryFrames;
        this.MAX_PAGE_REFERENCE = maxPageReference;
        this.pageFaults = pageFaults;
    }

    @Override
    public void run() {
        ArrayList<Integer> memory = new ArrayList<>();
        boolean[] pageTable = new boolean[this.MAX_PAGE_REFERENCE + 1];
        int pageFaults = 0;
        for (int value : sequence){
            if (!pageTable[value]){
                pageFaults++;
                if (memory.size() >= this.MAX_MEMORY_FRAMES + 1){
                    int removed = memory.remove(memory.size() - 1);
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
