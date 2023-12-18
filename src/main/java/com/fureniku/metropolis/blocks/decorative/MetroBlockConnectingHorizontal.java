package com.fureniku.metropolis.blocks.decorative;

import com.fureniku.metropolis.blocks.decorative.builders.MetroBlockDecorativeConnectingBuilder;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.BlockOffsetDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.registries.RegistryObject;

public class MetroBlockConnectingHorizontal extends MetroBlockDecorative {

    protected static final BooleanProperty NORTH = BooleanProperty.create("north");
    protected static final BooleanProperty EAST = BooleanProperty.create("east");
    protected static final BooleanProperty SOUTH = BooleanProperty.create("south");
    protected static final BooleanProperty WEST = BooleanProperty.create("west");

    private boolean _checkUp = false;
    private boolean _checkDown = false;

    public MetroBlockConnectingHorizontal(Properties props, VoxelShape shape, String modelDir, String modelName, BlockOffsetDirection offsetDirection, boolean checkUp, boolean checkDown, TextureSet... textures) {
        super(props, shape, modelDir, modelName, offsetDirection, textures);
        _checkUp = checkUp;
        _checkDown = checkDown;
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false));
    }

    public MetroBlockConnectingHorizontal(MetroBlockDecorativeConnectingBuilder builder) {
        this(builder.getProps(), builder.getShape(), builder.getModelDirectory(), builder.getModelName(), builder.getOffsetDirection(), builder.getCheckUp(), builder.getCheckDown(), builder.getTextures());
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState blockstate = checkNewState(context.getLevel(), this.defaultBlockState(), context.getClickedPos(), this.defaultBlockState().getBlock());
        return blockstate;
    }

    @Override
    protected void createBlockState(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public void onNeighbourChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos) {
        BlockState newState = checkNewState(level, state, pos, block);
        if (newState != state) {
            setBlock(level, pos, newState);
        }
    }

    protected BlockState checkNewState(Level level, BlockState currentState, BlockPos pos, Block block) {
        boolean north = checkMatchOnSide(level, pos.north(), block);
        boolean east = checkMatchOnSide(level, pos.east(), block);
        boolean south = checkMatchOnSide(level, pos.south(), block);
        boolean west = checkMatchOnSide(level, pos.west(), block);
        return currentState.setValue(NORTH, north).setValue(EAST, east).setValue(SOUTH, south).setValue(WEST, west);
    }

    protected boolean checkMatchOnSide(Level level, BlockPos posToCheck, Block block) {
        if (_checkUp) {
            if (checkMatch(level, posToCheck.above(), block)) {
                return true;
            }
        }
        if (_checkDown) {
            if (checkMatch(level, posToCheck.below(), block)) {
                return true;
            }
        }
        return checkMatch(level, posToCheck, block);
    }

    protected boolean checkMatch(Level level, BlockPos posToCheck, Block block) {
        return level.getBlockState(posToCheck).getBlock() == block;
    }

    @Override
    public void generateBlockState(RegistryObject<Block> blockRegistryObject, MetroBlockStateProvider blockStateProvider) {
        Block block = blockRegistryObject.get();
        BlockModelBuilder modelNormal = blockStateProvider.getModelFilesWithTexture(block, "_standard", "blocks/decorative/" + _modelName, _resources[0].getTexture());
        BlockModelBuilder modelConnection = blockStateProvider.getModelFilesWithTexture(block, "_connection", "blocks/decorative/" + _modelName + "_connection", _resources[0].getTexture());

        if (_resources.length > 1) {
            for (int i = 1; i < _resources.length; i++) {
                modelNormal = modelNormal.texture(_resources[i].getKey(), _resources[i].getTexture());
                modelConnection = modelConnection.texture(_resources[i].getKey(), _resources[i].getTexture());
            }
        }

        blockStateProvider.horizontalConnectingBlock(block, modelNormal, modelConnection);
        blockStateProvider.simpleBlockItem(block, modelNormal);
    }
}
