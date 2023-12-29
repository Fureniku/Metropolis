package com.fureniku.metropolis.blockentity;

import com.fureniku.metropolis.blocks.MetroBlockBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class MetroEntityBlockBase extends MetroBlockBase implements EntityBlock {

    public MetroEntityBlockBase(Properties props) {
        super(props);
    }

    @Nullable
    public abstract BlockEntity createBlockEntity(BlockPos pos, BlockState state);

    /*
     * FORGE START
     * Everything below here is a Minecraft/Forge function, which will call MY functions above.
     * My mods all use my functions. So on updates, I just change these, and everything is fixed... right?
     */

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return createBlockEntity(pos, state);
    }
}
