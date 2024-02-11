package com.fureniku.metropolis.client.screens;

import com.fureniku.metropolis.utils.Debug;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class UIRenderHandler implements IGuiOverlay {

    public UIRenderHandler(ForgeGui forgeGui, GuiGraphics guiGraphics, float v, int i, int i1) {
        render(forgeGui, guiGraphics, v, i, i1);
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        // Maybe this draw a rectangle on the screen.
        // First, second, third and fourth arguments may set a position.
        // Fifth argument may set color.
        guiGraphics.fill(100, 120, 140, 160, 0xFFFFFF);

        // This draw text on the screen. The method have no problem.
        // Third and fourth arguments set a position.
        // Fifth argument sets text color.
        // Sixth argument sets shadow.
        guiGraphics.drawString(Minecraft.getInstance().font, "test", 100, 140, 0xFFFFFF, true);
    }
}
