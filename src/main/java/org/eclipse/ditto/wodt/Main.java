package org.eclipse.ditto.wodt;

import org.eclipse.ditto.wodt.common.ThingUtils;

/*
 * This class this class simulates the behavior of a Physical Asset
 * by sending status updates to a Ditto Thing.
 */
public class Main {

    private static final int SLEEP_TIME = 5000;
    private static final ThingUtils thingUtils = new ThingUtils();

    public static void main(String[] args) {
        thingUtils.onStart();

        while(true) {
            try {
                thingUtils.simulatePAChanges();
                Thread.sleep(SLEEP_TIME + (int)(Math.random() * SLEEP_TIME * 2));
            } catch (InterruptedException e) {
            }
        }
    }
}
