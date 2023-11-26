package com.fureniku.metropolis.datagen;

import net.minecraft.resources.ResourceLocation;

public class TextureSet {

    private String _name;
    private ResourceLocation _location;

    public TextureSet(String key, ResourceLocation value) {
        _name = key;
        _location = value;
    }

    public String getKey() {
        return _name;
    }

    public ResourceLocation getTexture() {
        return _location;
    }
}
