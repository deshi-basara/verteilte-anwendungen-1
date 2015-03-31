package com.company;

public class Main {

    public static void main(String[] args) {
        // amount of results/Threads
        final int n = 100;

        // start the execution
        execute(n);
    }

    /**
     * Executes the calculation of 'n'-results in Threads and returns the results after
     * all Threads have synced.
     * @param n Amount of results we want to calculate
     * @return  Result integer array
     */
    public static int[] execute(int n) {
        // create the 'result'-object
        Result calcR = new Result(n);

        // start for each parameter 'n' a Thread for the calculation
        for(int i = 0; i < n; i++) {
            Calc calcF = new Calc(i, calcR);
            calcF.start();
        }

        return calcR.getResults();
    }
}