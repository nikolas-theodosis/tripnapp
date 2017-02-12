package uk.ac.gla.idi.beaconexample;

import android.bluetooth.BluetoothDevice;

// simple class to hold data about a beacon
public class BeaconInfo {
    public BluetoothDevice device;
    public String address;
    public String name;
    public int rssi;

    private static final int WINDOW_SIZE = 9;
    private int[] window = new int[WINDOW_SIZE];
    private int windowptr = 0;

    public BeaconInfo(BluetoothDevice device, String address, String name, int rssi) {
        this.device = device;
        this.address = address;
        this.name = name;
        this.rssi = rssi;
        for(int i=0;i<WINDOW_SIZE;i++)
            this.window[i] = rssi;
    }

    // called when a new scan result for this beacon is parsed
    public void updateRssi(int newRssi) {
        this.rssi = newRssi;
        window[windowptr] = newRssi;
        windowptr = (windowptr + 1) % WINDOW_SIZE;
    }

    // returns the latest raw RSSI reading for this beacon
    public double getRssi() {
        return this.rssi;
    }

    // returns a very simple moving average of the last WINDOW_SIZE
    // RSSI values received for this beacon
    public double getFilteredRssi() {
        double mean = 0.0;
        for(int i=0;i<WINDOW_SIZE;i++) {
            mean += window[i];
        }
        mean /= WINDOW_SIZE;
        return mean;
    }

    @Override
    public boolean equals(Object o) {
        // test if beacon objects are equal using their addresses
        if(o != null && o instanceof BeaconInfo) {
            BeaconInfo other = (BeaconInfo) o;
            if(other.address.equals(address))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        // as with equals() use addresses to test equality
        return address.hashCode();
    }
}