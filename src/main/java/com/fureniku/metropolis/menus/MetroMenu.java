package com.fureniku.metropolis.menus;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public abstract class MetroMenu extends AbstractContainerMenu {

    protected MetroMenu(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }
}
