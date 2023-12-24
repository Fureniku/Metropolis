package com.fureniku.metropolis.blocks.decorative.helpers;

import com.fureniku.metropolis.blocks.decorative.MetroBlockDecorative;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.HelperType;
import com.fureniku.metropolis.utils.ShapeUtils;
import com.fureniku.metropolis.utils.SimpleUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;

public class RotationHelper extends HelperBlockstate {

    public static final DirectionProperty DIRECTION = HorizontalDirectionalBlock.FACING;
    private final VoxelShape BLOCK_SHAPE_NORTH;
    private final VoxelShape BLOCK_SHAPE_EAST;
    private final VoxelShape BLOCK_SHAPE_SOUTH;
    private final VoxelShape BLOCK_SHAPE_WEST;

    public RotationHelper(VoxelShape shape, MetroBlockDecorative block) {
        BLOCK_SHAPE_NORTH = shape;
        BLOCK_SHAPE_EAST = ShapeUtils.rotateVoxelShape(shape, Direction.EAST);
        BLOCK_SHAPE_SOUTH = ShapeUtils.rotateVoxelShape(shape, Direction.SOUTH);
        BLOCK_SHAPE_WEST = ShapeUtils.rotateVoxelShape(shape, Direction.WEST);
    }

    @Override
    public BlockState setDefaultState(BlockState state) {
        return state.setValue(DIRECTION, Direction.NORTH);
    }

    @Override
    public HelperType getType() {
        return HelperType.ROTATION;
    }

    @Override
    public void generateBlockstate(TextureSet[] resources, String modelDir, String modelName, Block block, MetroBlockStateProvider blockStateProvider) {
        BlockModelBuilder bmb = blockStateProvider.prepareModels(block, modelDir, modelName, resources);
        blockStateProvider.horizontalBlock(block, bmb);
    }

    @Override
    public BlockState getPlacementState(BlockPlaceContext context, BlockState currentState, Block block) {
        return currentState.setValue(DIRECTION, context.getHorizontalDirection());
    }

    public VoxelShape getShapeFromBlockState(BlockState pState) {
        switch (pState.getValue(DIRECTION)) {
            case EAST -> {
                return BLOCK_SHAPE_EAST;
            }
            case SOUTH -> {
                return BLOCK_SHAPE_SOUTH;
            }
            case WEST -> {
                return BLOCK_SHAPE_WEST;
            }
        }
        return BLOCK_SHAPE_NORTH;
    }
}
