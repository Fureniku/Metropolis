package com.fureniku.metropolis.items;

public class MetroItemSimple extends MetroItemBase {

    public MetroItemSimple() {
        this(64);
    }

    public MetroItemSimple(int stackSize) {
        super(new Properties().stacksTo(stackSize));
    }
}
