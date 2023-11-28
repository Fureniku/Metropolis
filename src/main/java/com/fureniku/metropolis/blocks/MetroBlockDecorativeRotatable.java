package com.fureniku.metropolis.blocks;

import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.utils.Debug;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.registries.RegistryObject;

/**
 * Decorative non-full block which can be rotated
 */
public class MetroBlockDecorativeRotatable extends MetroBlockDecorative {

    protected static final DirectionProperty DIRECTION = HorizontalDirectionalBlock.FACING;
    private final VoxelShape BLOCK_SHAPE;

    /**
     * Constructor for decorative blocks which have a specific shape. This shape is rotated automatically.
     * @param props
     * @param shape
     */
    public MetroBlockDecorativeRotatable(Properties props, VoxelShape shape, String modelName, TextureSet... textures) {
        super(props, modelName, textures);
        BLOCK_SHAPE = shape;
        this.registerDefaultState(this.stateDefinition.any().setValue(DIRECTION, Direction.NORTH));
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
        return BLOCK_SHAPE;
    }
}
