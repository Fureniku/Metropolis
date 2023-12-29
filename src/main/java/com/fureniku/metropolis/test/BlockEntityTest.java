package com.fureniku.metropolis.test;

import com.fureniku.metropolis.blockentity.MetroBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityTest extends MetroBlockEntity {

    public BlockEntityTest(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
