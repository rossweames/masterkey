package com.eames.masterkey.model;

/**
 * This class represents a master key system bitting list.
 */
public class BittingList {

    private  String source;
    private int[] master;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int[] getMaster() {
        return master;
    }

    public void setMaster(int[] master) {
        this.master = master;
    }
}
