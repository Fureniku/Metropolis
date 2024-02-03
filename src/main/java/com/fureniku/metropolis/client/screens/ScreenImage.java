package com.fureniku.metropolis.client.screens;

import net.minecraft.resources.ResourceLocation;

public class ScreenImage {

    private ResourceLocation _resourceLocation;
    private int _startLeft;
    private int _startTop;
    private int _width;
    private int _height;

    public ScreenImage(ResourceLocation loc, int startLeft, int startTop, int width, int height) {
        _resourceLocation = loc;
        _startLeft = startLeft;
        _startTop = startTop;
        _width = width;
        _height = height;
    }

    public ResourceLocation getResourceLocation() {
        return _resourceLocation;
    }

    public int getStartLeft() {
        return _startLeft;
    }

    public int getStartTop() {
        return _startTop;
    }

    public int getWidth() {
        return _width;
    }

    public int getHeight() {
        return _height;
    }
}
