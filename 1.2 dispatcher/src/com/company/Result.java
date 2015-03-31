package com.company;

/**
 * Created by simon on 17.03.15.
 */
public class Result {

    /**
     * Object attributes
     */
    private int[] resultArray = null;
    private int resultPointer = 0;
    private int resultCapacity = 0;

    /**
     * Object constructor.
     * Creates our resultArray.
     * @param n Number of predicted results
     */
    public Result(int n) {
        // create the resultArray
        this.resultArray = new int[n];
        this.resultCapacity = n;
    }

    /**
     * Adds an integer value to the 'resultArray' if there is still enough space.
     * @param result Interger value that should be saved
     */
    public synchronized void addResult(int result) {
        // is there still space in the array?
        if(this.resultPointer < this.resultCapacity) {
            // add result and increment pointer
            this.resultArray[this.resultPointer] = result;
            System.out.println("Ergebnis gespeichert: " + this.resultArray[this.resultPointer]);
            this.resultPointer++;

            // check if all possible results were added
            if(this.resultPointer == this.resultCapacity) {
                System.out.println("Alle Ergebnisse erfasst ...");

                // notify blocked Threads
                notifyAll();
            }
        }
        else {
            System.out.println("Es können keine weiteren Ergebnisse gespeichert werden!");
        }
    }

    /**
     * Returns all saved results.
     * @return
     */
    public synchronized int[] getResults() {
        // block request until all calculations have finished
        try {
            wait();
        } catch(InterruptedException ie) {
            System.out.println("Interrupted Execution" + ie.getMessage());
            System.exit(1);
        }

        return this.resultArray;
    }

}
