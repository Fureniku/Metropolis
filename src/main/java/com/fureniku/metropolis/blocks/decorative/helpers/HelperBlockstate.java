package com.fureniku.metropolis.blocks.decorative.helpers;

import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public abstract class HelperBlockstate extends HelperBase {

    public abstract void generateBlockstate(TextureSet[] resources, String modelDir, String modelName, Block block, MetroBlockStateProvider blockStateProvider);
    public abstract BlockState getPlacementState(BlockPlaceContext context, BlockState currentState, Block block);

    public BlockState setState(BlockState state, Property prop, Comparable value) {
        return state.setValue(prop, value);
    }
}
