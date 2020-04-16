import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Assign6 {

    static final int SIMULATIONS = 1000;
    static final int MAX_MEMORY_FRAMES = 100;
    static final int MAX_PAGE_REFERENCE = 250;
    static final int SEQUENCE_SIZE = 1000;

    public static void main(String[] args) {

        try {
            ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            int[][] fifoPageFaults = new int[SIMULATIONS][MAX_MEMORY_FRAMES];
            int[][] mruPageFaults = new int[SIMULATIONS][MAX_MEMORY_FRAMES];
            int[][] lruPageFaults = new int[SIMULATIONS][MAX_MEMORY_FRAMES];
            long start = System.currentTimeMillis();
            for (int i = 0; i < SIMULATIONS; i++) {
                int[] sequence = new int[1000];
                for (int index = 0; index < SEQUENCE_SIZE; index++) {
                    sequence[index] = (int) ((Math.random() * MAX_PAGE_REFERENCE) + 1);
                }
                for (int j = 0; j < MAX_MEMORY_FRAMES; j++) {
                    threadPool.execute(new TaskFIFO(sequence, j, MAX_PAGE_REFERENCE, fifoPageFaults[i]));
                    threadPool.execute(new TaskMRU(sequence, j, MAX_PAGE_REFERENCE, mruPageFaults[i]));
                    threadPool.execute(new TaskLRU(sequence, j, MAX_PAGE_REFERENCE, lruPageFaults[i]));
                }
            }

            threadPool.shutdown();
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
            long end = System.currentTimeMillis();

            System.out.println("Simulation took " + (end - start) + " ms\n");
            report(fifoPageFaults, mruPageFaults, lruPageFaults);

            System.out.println("Belady's Anomaly Report for FIFO");
            belady(fifoPageFaults);
            System.out.println("Belady's Anomaly Report for MRU");
            belady(mruPageFaults);
            System.out.println("Belady's Anomaly Report for LRU");
            belady(lruPageFaults);

        }
        catch(Exception e){
            System.out.println("Something went wrong.");
        }
    }

    public static void belady (int[][] data){
        int count = 0;
        int max = 0;
        for (int i = 0; i < SIMULATIONS; i++){
            for (int j = 1; j < MAX_MEMORY_FRAMES; j++){
                int diff = data[i][j] - data[i][j - 1];
                if (diff > 0){
                    System.out.printf("\tdetected - Previous %d : Current %d (%d)\n",
                            data[i][j - 1],
                            data[i][j],
                            diff);
                    count++;
                    if (diff > max){
                        max = diff;
                    }
                }
            }
        }
        System.out.printf("\t Anomaly detected %d times with a max difference of %d\n", count, max);
    }

    public static void report (int[][] fifo, int[][] mru, int[][] lru){
        int fifoCount = 0;
        int mruCount = 0;
        int lruCount = 0;

        for (int i = 0; i < SIMULATIONS; i ++){
            for (int j = 0; j < MAX_MEMORY_FRAMES; j++){
                int min = Math.min(fifo[i][j], Math.min(mru[i][j], lru[i][j]));
                if (fifo[i][j] == min){
                    fifoCount++;
                }
                if (mru[i][j] == min){
                    mruCount++;
                }
                if (lru[i][j] == min){
                    lruCount++;
                }
            }
        }

        System.out.println("FIFO min PF: " + fifoCount);
        System.out.println("MRU min PF: " + mruCount);
        System.out.println("LRU min PF: " + lruCount + "\n");
    }
}
