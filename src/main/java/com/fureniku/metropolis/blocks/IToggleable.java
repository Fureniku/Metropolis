package com.fureniku.metropolis.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IToggleable {

    void toggleBlock(Level level, BlockPos pos, BlockState state);

    void setToggledState(Level level, BlockPos pos, BlockState state, boolean toggled);
}
