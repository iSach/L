package be.isach.eru.runtime;

import be.isach.eru.Eru;
/**
 * Project: samaritan
 *
 * Package: be.isach.samaritan.runtime
 * Created by: Sacha
 * Created on: 30th mai, 2016
 * at 07:24
 *
 * This thread is called by Runtime when the program shuts down.
 */
public class ShutdownThread extends Thread {

    /**
     * Samaritan instance.
     */
    private Eru eru;

    /**
     * Shutdown Thread constructor.
     *
     * @param eru Samaritan instance.
     */
    public ShutdownThread(Eru eru) {
        this.eru = eru;
    }

    /**
     * Called when Samaritan shuts down.
     */
    @Override
    public void run() {
       eru.shutdown();
    }
}
