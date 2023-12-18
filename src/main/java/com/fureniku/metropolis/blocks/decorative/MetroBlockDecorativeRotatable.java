package com.fureniku.metropolis.blocks.decorative;

import com.fureniku.metropolis.blocks.decorative.builders.MetroBlockDecorativeBuilder;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.BlockOffsetDirection;
import com.fureniku.metropolis.utils.ShapeUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.registries.RegistryObject;

/**
 * Decorative non-full block which can be rotated
 */
public class MetroBlockDecorativeRotatable extends MetroBlockDecorative {

    protected static final DirectionProperty DIRECTION = HorizontalDirectionalBlock.FACING;
    private final VoxelShape BLOCK_SHAPE_NORTH;
    private final VoxelShape BLOCK_SHAPE_EAST;
    private final VoxelShape BLOCK_SHAPE_SOUTH;
    private final VoxelShape BLOCK_SHAPE_WEST;

    /**
     * Constructor for decorative blocks which have a specific shape. This shape is rotated automatically.
     * @param props
     * @param shape
     */
    public MetroBlockDecorativeRotatable(Properties props, VoxelShape shape, String modelName, BlockOffsetDirection offsetDirection, TextureSet... textures) {
        super(props, shape, modelName, offsetDirection, textures);
        BLOCK_SHAPE_NORTH = shape;
        BLOCK_SHAPE_EAST = ShapeUtils.rotateVoxelShape(shape, Direction.EAST);
        BLOCK_SHAPE_SOUTH = ShapeUtils.rotateVoxelShape(shape, Direction.SOUTH);
        BLOCK_SHAPE_WEST = ShapeUtils.rotateVoxelShape(shape, Direction.WEST);
        this.registerDefaultState(this.stateDefinition.any().setValue(DIRECTION, Direction.NORTH));
    }

    public MetroBlockDecorativeRotatable(MetroBlockDecorativeBuilder builder) {
        this(builder.getProps(), builder.getShape(), builder.getModelName(), builder.getOffsetDirection(), builder.getTextures());
    }

    @Override
    public void generateBlockState(RegistryObject<Block> blockRegistryObject, MetroBlockStateProvider blockStateProvider) {
        Block block = blockRegistryObject.get();
        BlockModelBuilder bmb;
        if (_modelName == null || _resources == null) {
            bmb = blockStateProvider.getModelFilesWithTexture(block, "", "blocks/decorative/" + block.getName(), blockStateProvider.modLoc("blocks/decorative/" + block.getName()));
        } else {
            bmb = blockStateProvider.getModelFilesWithTexture(block, "", "blocks/decorative/" + _modelName, _resources[0].getTexture());
            if (_resources.length > 1) {
                for (int i = 1; i < _resources.length; i++) {
                    bmb = bmb.texture(_resources[i].getKey(), _resources[i].getTexture());
                }
            }
        }
        blockStateProvider.horizontalBlock(block, bmb);
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState blockstate = this.defaultBlockState().setValue(DIRECTION, context.getHorizontalDirection());
        return blockstate;
    }

    @Override
    protected void createBlockState(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DIRECTION);
    }

    @Override
    protected VoxelShape getShapeFromBlockState(BlockState pState) {
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
