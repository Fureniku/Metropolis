package com.fureniku.metropolis.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class MetroScreenContainerBase<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    private final ScreenImage _background;

    public MetroScreenContainerBase(T menu, Inventory inventory, Component component, ScreenImage background) {
        super(menu, inventory, component);
        _background = background;
        this.imageWidth = background.getWidth();
        this.imageHeight = background.getHeight();
    }

    protected abstract ResourceLocation getBackground();

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, getBackground());
        graphics.blit(_background.getResourceLocation(), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
    }
}
