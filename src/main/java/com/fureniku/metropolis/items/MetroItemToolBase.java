package com.fureniku.metropolis.items;

public class MetroItemToolBase extends MetroItemBase {

    public MetroItemToolBase(int durability) {
        super(new Properties().stacksTo(1).fireResistant().defaultDurability(durability));
    }
}
