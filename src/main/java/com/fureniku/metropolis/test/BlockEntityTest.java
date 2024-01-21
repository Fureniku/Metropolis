package com.fureniku.metropolis.test;

import com.fureniku.metropolis.Metropolis;
import com.fureniku.metropolis.blockentity.MetroBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityTest extends MetroBlockEntity {

    public BlockEntityTest(BlockPos pos, BlockState state) {
        super(Metropolis.registrationTest.TEST_BLOCK_ENTITY_DECORATIVE_ENTITY.get(), pos, state);
    }
}
