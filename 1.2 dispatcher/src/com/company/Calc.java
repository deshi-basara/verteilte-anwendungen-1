package com.company;

/**
 * Created by simon on 17.03.15.
 */
public class Calc extends Thread implements F {

    private int xValue = 0;
    //private F f = null;
    private Result calcR = null;

    /**
     * Object constructor.
     * Sets the passed values as private attributes.
     * @param x     Current value for x
     * @param calcR Result object which holds all calculated values
     */
    public Calc(int x, Result calcR) {
        this.xValue = x;
        //this.f = f;
        this.calcR = calcR;
    }

    @Override
    public int f(int x) {
        return (x * 2);
    }

    @Override
    public void run() {
        // start the calculation and save the result in the 'result'-object
        int result = this.f(this.xValue);
        this.calcR.addResult(result);
    }
}
