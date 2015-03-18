package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        // needed data-values
        String outputText = null;
        int timerLength = 0;

        /**
         * 1) Get the timer length
         */
        System.out.println("Nach wie vielen Sekunden soll die Uhr klingeln? ");
        BufferedReader inputTime = new BufferedReader(new InputStreamReader(System.in));
        try {
            String line = inputTime.readLine();
            timerLength = Integer.parseInt(line);
        } catch(IOException ioException) {
            System.out.println("Kein gültiger Ausgabetext ... beende Applikation!");
            System.exit(1);
        }

        /**
         * 2) Get the output text
         */
        System.out.println("Wie lautet der Ausgabetext? ");
        BufferedReader inputText = new BufferedReader(new InputStreamReader(System.in));
        try {
            outputText = inputText.readLine();
        } catch(IOException ioException) {
            System.out.println("Kein gültiger Ausgabetext ... beende Applikation!");
            System.exit(1);
        }

        /**
         * 3) Start the clock
         */
        eieruhr(timerLength, outputText);
    }

    /**
     *
     * @param time
     * @param outputText
     */
    public static void eieruhr(int time, String outputText) {

        // create a new 'Uhr'-object and start a Thread for it
        Uhr eieruhr = new Uhr(time, outputText);
        eieruhr.start();
    }
}
