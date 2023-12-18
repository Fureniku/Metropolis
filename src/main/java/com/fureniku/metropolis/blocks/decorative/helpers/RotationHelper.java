package com.fureniku.metropolis.blocks.decorative.helpers;

import com.fureniku.metropolis.blocks.decorative.MetroBlockDecorative;
import com.fureniku.metropolis.enums.HelperType;
import com.fureniku.metropolis.utils.SimpleUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RotationHelper extends HelperBlockstate {

    public static final DirectionProperty DIRECTION = HorizontalDirectionalBlock.FACING;
    private final VoxelShape BLOCK_SHAPE_NORTH;
    private final VoxelShape BLOCK_SHAPE_EAST;
    private final VoxelShape BLOCK_SHAPE_SOUTH;
    private final VoxelShape BLOCK_SHAPE_WEST;

    public RotationHelper(VoxelShape shape, MetroBlockDecorative block, StateHolder state) {
        BLOCK_SHAPE_NORTH = shape;
        BLOCK_SHAPE_EAST = SimpleUtils.rotateVoxelShape(shape, Direction.EAST);
        BLOCK_SHAPE_SOUTH = SimpleUtils.rotateVoxelShape(shape, Direction.SOUTH);
        BLOCK_SHAPE_WEST = SimpleUtils.rotateVoxelShape(shape, Direction.WEST);
    }

    @Override
    public BlockState setDefaultState(BlockState state) {
        state.setValue(DIRECTION, Direction.NORTH);
        return state;
    }

    @Override
    public HelperType getType() {
        return HelperType.ROTATION;
    }

    @Override
    public Property getProperty() {
        return DIRECTION;
    }
}
