package com.fureniku.metropolis.blockentity;

import com.fureniku.metropolis.Metropolis;
import com.fureniku.metropolis.test.RegistrationTest;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MetroBlockEntity extends BlockEntity {

    private BlockEntityType _type;

    public MetroBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public MetroBlockEntity(BlockPos pos, BlockState state) {
        super(Metropolis.registrationTest.TEST_BLOCK_ENTITY_DECORATIVE_ENTITY.get(), pos, state);
    }

    public BlockEntityType getBlockEntityType() {
        return null;
    }
}
