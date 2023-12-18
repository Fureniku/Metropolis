package com.fureniku.metropolis.blocks.decorative.helpers;

import com.fureniku.metropolis.enums.HelperType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class HelperBase {

    public abstract BlockState setDefaultState(BlockState state);
    public abstract HelperType getType();

}
