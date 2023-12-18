package com.fureniku.metropolis.blocks.decorative;

import com.fureniku.metropolis.blocks.decorative.builders.MetroBlockDecorativeConnectingBuilder;
import com.fureniku.metropolis.blocks.decorative.helpers.HelperBase;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.BlockConnectionType;
import com.fureniku.metropolis.enums.BlockOffsetDirection;
import com.fureniku.metropolis.utils.ShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.registries.RegistryObject;

import java.util.ArrayList;

public class MetroBlockConnectingHorizontal extends MetroBlockDecorative {

    protected static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    protected static final BooleanProperty EAST = BlockStateProperties.EAST;
    protected static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    protected static final BooleanProperty WEST = BlockStateProperties.WEST;

    private boolean _checkUp = false;
    private boolean _checkDown = false;
    protected boolean _centerFourSided = false;
    protected boolean _independentModelsPerSide = false;

    protected final String _connectedModelName;
    protected final String _itemModelName;

    protected final VoxelShape[] _shapeByIndex;
    protected final String _tag;
    protected final BlockConnectionType _connectionType;

    public MetroBlockConnectingHorizontal(Properties props, String modelName, String connectedModelName, String itemModelName, BlockOffsetDirection offsetDirection,
                                          boolean checkUp, boolean checkDown, boolean centerFourSided, boolean independentModelsPerSide, VoxelShape[] shapes, String tag, BlockConnectionType connectionType, TextureSet... textures) {
        super(props, shapes[0], modelName, offsetDirection, textures);
        _connectedModelName = connectedModelName;
        _itemModelName = itemModelName;

    public MetroBlockConnectingHorizontal(Properties props, VoxelShape shape, String modelDir, String modelName, BlockOffsetDirection offsetDirection, boolean checkUp, boolean checkDown, TextureSet... textures) {
        super(props, shape, modelDir, modelName, new ArrayList<HelperBase>(), textures);
        _checkUp = checkUp;
        _checkDown = checkDown;
        _centerFourSided = centerFourSided;
        _independentModelsPerSide = independentModelsPerSide;

        _shapeByIndex = shapes;
        _tag = tag;
        _connectionType = connectionType;
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false));
    }

    public MetroBlockConnectingHorizontal(MetroBlockDecorativeConnectingBuilder builder) {
        this(builder.getProps(), builder.getShape(), builder.getModelDirectory(), builder.getModelName(), builder.getOffsetDirection(), builder.getCheckUp(), builder.getCheckDown(), builder.getTextures());
    }

    public String getTag() {
        return _tag;
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
    protected VoxelShape getShapeFromBlockState(BlockState state) {
        return ShapeUtils.getShapeAtIndex(state, _shapeByIndex);
    }

    @Override
    public void onNeighbourChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos) {
        BlockState newState = checkNewState(level, state, pos, block);
        if (newState != state) {
            setBlock(level, pos, newState);
        }
    }

    protected BlockState checkNewState(Level level, BlockState currentState, BlockPos pos, Block block) {
        boolean north = checkMatchOnSide(level, pos.north(), block, Direction.NORTH);
        boolean east = checkMatchOnSide(level, pos.east(), block, Direction.EAST);
        boolean south = checkMatchOnSide(level, pos.south(), block, Direction.SOUTH);
        boolean west = checkMatchOnSide(level, pos.west(), block, Direction.WEST);
        return currentState.setValue(NORTH, north).setValue(EAST, east).setValue(SOUTH, south).setValue(WEST, west);
    }

    protected boolean checkMatchOnSide(Level level, BlockPos posToCheck, Block block, Direction dir) {
        if (_checkUp) {
            if (checkMatch(level, posToCheck.above(), block, dir)) {
                return true;
            }
        }
        if (_checkDown) {
            if (checkMatch(level, posToCheck.below(), block, dir)) {
                return true;
            }
        }
        return checkMatch(level, posToCheck, block, dir);
    }

    protected boolean checkMatch(Level level, BlockPos posToCheck, Block block, Direction dir) {
        BlockState stateCheck = level.getBlockState(posToCheck);
        Block blockCheck = stateCheck.getBlock();
        BlockConnectionType type = _connectionType;
        if (_connectionType.isSolid()) {
            if (stateCheck.isFaceSturdy(level, posToCheck, dir.getOpposite())) {
                return true;
            }
            type = _connectionType.getSolidType();
        }

        switch (type) {
            case SAME:
                return blockCheck == block;
            case SAMECLASS:
                return blockCheck.getClass() == block.getClass();
            case TAG:
                if (blockCheck instanceof MetroBlockConnectingHorizontal && block instanceof MetroBlockConnectingHorizontal) {
                    MetroBlockConnectingHorizontal metroBlock = (MetroBlockConnectingHorizontal) block;
                    MetroBlockConnectingHorizontal metroBlockCheck = (MetroBlockConnectingHorizontal) blockCheck;
                    return metroBlock.getTag().equals(metroBlockCheck.getTag());
                }
            case CONNECTING:
                if (blockCheck instanceof MetroBlockConnectingHorizontal && block instanceof MetroBlockConnectingHorizontal) {
                    return true;
                }
            case METRO:
                if (blockCheck instanceof MetroBlockDecorative && block instanceof MetroBlockDecorative) {
                    return true;
                }
            case SOLID:
                return stateCheck.isFaceSturdy(level, posToCheck, dir.getOpposite());
            case ALL:
                return !(blockCheck instanceof AirBlock);
        }
        return false;
    }

    @Override
    public void generateBlockState(RegistryObject<Block> blockRegistryObject, MetroBlockStateProvider blockStateProvider) {
        Block block = blockRegistryObject.get();
        BlockModelBuilder modelItem = blockStateProvider.getModelFilesWithTexture(block, "_standard", "blocks/decorative/" + _itemModelName, _resources[0].getTexture());
        BlockModelBuilder modelNormal = blockStateProvider.getModelFilesWithTexture(block, "_standard", "blocks/decorative/" + _modelName, _resources[0].getTexture());
        BlockModelBuilder modelConnection = blockStateProvider.getModelFilesWithTexture(block, "_connection", "blocks/decorative/" + _modelName + "_connection", _resources[0].getTexture());

        if (_resources.length > 1) {
            for (int i = 1; i < _resources.length; i++) {
                modelItem = modelItem.texture(_resources[i].getKey(), _resources[i].getTexture());
                modelNormal = modelNormal.texture(_resources[i].getKey(), _resources[i].getTexture());
                modelConnection = modelConnection.texture(_resources[i].getKey(), _resources[i].getTexture());
            }
        }

        if (_centerFourSided) {
            //do it the gross way
            blockStateProvider.getMultipartBuilder(block)
                    .part().modelFile(modelNormal).rotationY(0).uvLock(true).addModel().condition(NORTH, false).end()
                    .part().modelFile(modelNormal).rotationY(90).uvLock(true).addModel().condition(EAST, false).end()
                    .part().modelFile(modelNormal).rotationY(180).uvLock(true).addModel().condition(SOUTH, false).end()
                    .part().modelFile(modelNormal).rotationY(270).uvLock(true).addModel().condition(WEST, false).end()
                    .part().modelFile(modelConnection).rotationY(0).uvLock(true).addModel().condition(NORTH, true).end()
                    .part().modelFile(modelConnection).rotationY(90).uvLock(true).addModel().condition(EAST, true).end()
                    .part().modelFile(modelConnection).rotationY(180).uvLock(true).addModel().condition(SOUTH, true).end()
                    .part().modelFile(modelConnection).rotationY(270).uvLock(true).addModel().condition(WEST, true).end();
        } else {
            blockStateProvider.horizontalConnectingBlock(block, modelNormal, modelConnection);
        }

        blockStateProvider.simpleBlockItem(block, modelItem);
    }
}
