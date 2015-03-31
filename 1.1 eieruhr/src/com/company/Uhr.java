package com.company;

/**
 * Created by simon on 17.03.15.
 */
public class Uhr extends Thread {

    /**
     * Class attributes
     */
    private int time;
    private String outputText;

    /**
     * Class constructor.
     * Sets class attributes.
     *
     * @param time
     * @param outputText
     */
    public Uhr(int time, String outputText) {
        // set class attributes
        this.time = time;
        this.outputText = outputText;
    }

    /**
     * Is called whenever a Thread is started
     */
    public void run() {
        // feedback
        System.out.println("Eieruhr läuft ...");

        // let the Thread sleep for its specified time
        //schlafenA(this.time);
        schlafenB(this.time);

        // print the output text
        System.out.println(outputText);
    }

    /**
     * Lets the current Thread sleep for the handed time in milliseconds.
     *
     * @param m Amount of sleeping time in milliseconds
     */
    public static void schlafenA(int m) {
        try {
            Thread.sleep(1000*m);
        } catch (InterruptedException t) {
            System.out.print("Unerwartete Exception während dem Schlafen");
        }
    }

    /**
     * Makes the current Thread sleep for the handed time in milliseconds.
     * Every slept second a countdown with the time left is printed.
     *
     * @param m Amount of sleeping time in milliseconds
     */
    public static void schlafenB(int m) {
        int timeLeft = m;

        // call the 'sleep'-method and print 'timeLeft' until the time is decremented to zero
        while(timeLeft > 0) {
            try {
                // let the Thread sleep for 1 second
                Thread.sleep(1000);

                // decrement the left time
                timeLeft--;

                // feedback
                System.out.println("Verbleibende Zeit: " + timeLeft);
            } catch(InterruptedException t) {
                System.out.print("Unerwartete Exception während dem Schlafen");
            }
        }
    }
}
