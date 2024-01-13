package com.fureniku.metropolis.blocks.decorative.helpers;

import com.fureniku.metropolis.blocks.MetroBlockBase;
import com.fureniku.metropolis.blocks.decorative.MetroBlockDecorativeBase;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.BlockConnectionType;
import com.fureniku.metropolis.enums.HelperType;
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

public class ConnectHorizontalHelper extends HelperBlockstate {

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    private boolean _checkUp = false;
    private boolean _checkDown = false;
    protected boolean _centerFourSided = false;
    protected boolean _independentModelsPerSide = false;
    protected boolean _connectSolid = true;

    protected final String _connectedModelName;
    protected final String _itemModelName;
    protected final VoxelShape[] _shapeByIndex;
    protected final BlockConnectionType _connectionType;


    public ConnectHorizontalHelper(boolean checkUp, boolean checkDown, boolean centerFourSided, boolean independentModelsPerSide, boolean connectSolid, String connectedModelName, String itemModelName, BlockConnectionType connectionType, VoxelShape[] shapes) {
        _checkUp = checkUp;
        _checkDown = checkDown;
        _centerFourSided = centerFourSided;
        _independentModelsPerSide = independentModelsPerSide;
        _connectSolid = connectSolid;
        _connectedModelName = connectedModelName;
        _itemModelName = itemModelName;
        _shapeByIndex = shapes;
        _connectionType = connectionType;
    }

    @Override
    public BlockState setDefaultState(BlockState state) {
        return state.setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false);
    }

    @Override
    public HelperType getType() {
        return HelperType.CONNECTION_HORIZONTAL;
    }

    @Override
    public StateDefinition.Builder<Block, BlockState> addDefaultState(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH);
        builder.add(EAST);
        builder.add(SOUTH);
        builder.add(WEST);
        return builder;
    }

    @Override
    public void generateBlockState(TextureSet[] resources, String modelDir, String modelName, Block block, MetroBlockStateProvider blockStateProvider) {
        String connectedName = _connectedModelName != null ? _connectedModelName : modelName + "_connection";
        String itemName = _itemModelName != null ? _itemModelName : modelName;
        BlockModelBuilder modelItem = blockStateProvider.getModelFilesWithTexture(block, "_standard", modelDir + itemName, resources[0].getTexture());
        BlockModelBuilder modelNormal = blockStateProvider.getModelFilesWithTexture(block, "_standard", modelDir + modelName, resources[0].getTexture());
        BlockModelBuilder modelConnection = blockStateProvider.getModelFilesWithTexture(block, "_connection", modelDir + connectedName, resources[0].getTexture());

        if (resources.length > 1) {
            for (int i = 1; i < resources.length; i++) {
                modelItem = modelItem.texture(resources[i].getKey(), resources[i].getTexture());
                modelNormal = modelNormal.texture(resources[i].getKey(), resources[i].getTexture());
                modelConnection = modelConnection.texture(resources[i].getKey(), resources[i].getTexture());
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

    @Override
    public BlockState getPlacementState(BlockPlaceContext context, BlockState currentState, Block block) {
        return checkNewState(context.getLevel(), currentState, context.getClickedPos(), block);
    }

    public VoxelShape getShapeFromBlockState(BlockState state) {
        return ShapeUtils.getShapeAtIndex(state, _shapeByIndex);
    }

    public void neighbourChanged(BlockState state, Level level, BlockPos pos, MetroBlockDecorativeBase block) {
        BlockState newState = checkNewState(level, state, pos, block);
        if (newState != state) {
            block.setBlock(level, pos, newState);
        }
    }

    private BlockState checkNewState(Level level, BlockState currentState, BlockPos pos, Block block) {
        boolean north = checkMatchOnSide(level, pos.north(), block, Direction.NORTH);
        boolean east = checkMatchOnSide(level, pos.east(), block, Direction.EAST);
        boolean south = checkMatchOnSide(level, pos.south(), block, Direction.SOUTH);
        boolean west = checkMatchOnSide(level, pos.west(), block, Direction.WEST);
        return currentState.setValue(NORTH, north).setValue(EAST, east).setValue(SOUTH, south).setValue(WEST, west);
    }

    private boolean checkMatchOnSide(Level level, BlockPos posToCheck, Block block, Direction dir) {
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

    private boolean checkMatch(Level level, BlockPos posToCheck, Block block, Direction dir) {
        BlockState stateCheck = level.getBlockState(posToCheck);
        Block blockCheck = stateCheck.getBlock();
        BlockConnectionType type = _connectionType;
        if (_connectSolid && stateCheck.isFaceSturdy(level, posToCheck, dir.getOpposite())) {
            return true;
        }

        switch (type) {
            case SAME:
                return blockCheck == block;
            case SAMECLASS:
                return blockCheck.getClass() == block.getClass();
            case TAG:
                if (block instanceof MetroBlockBase && blockCheck instanceof MetroBlockBase) {
                    MetroBlockBase blockMetro = (MetroBlockBase) block;
                    MetroBlockBase blockCheckMetro = (MetroBlockBase) blockCheck;
                    return blockMetro.getTag().equals(blockCheckMetro.getTag());
                }
                return false;
            case CONNECTING:
                if (block instanceof MetroBlockBase && blockCheck instanceof MetroBlockBase) {
                    return ((MetroBlockBase) block).hasHelper(ConnectHorizontalHelper.class) && ((MetroBlockBase) blockCheck).hasHelper(ConnectHorizontalHelper.class);
                }
                return false;
            case METRO:
                return blockCheck instanceof MetroBlockDecorativeBase && block instanceof MetroBlockDecorativeBase;
            case ALL:
                return !(blockCheck instanceof AirBlock);
        }
        return false;
    }

    //region Builder
    public static class Builder {
        private boolean _checkUp = false;
        private boolean _checkDown = false;
        private boolean _centerFourSided = false;
        private boolean _independentModelsPerSide = false;
        private boolean _connectSolid = true;

        private String _connectedModelName;
        private String _itemModelName;
        private BlockConnectionType _connectionType;
        private VoxelShape[] _shapes;

        public Builder() {}

        public Builder(Builder partial) {
            _checkUp = partial._checkUp;
            _checkDown = partial._checkDown;
            _centerFourSided = partial._centerFourSided;
            _independentModelsPerSide = partial._independentModelsPerSide;
            _connectSolid = partial._connectSolid;
            _connectedModelName = partial._connectedModelName;
            _itemModelName = partial._itemModelName;
            _connectionType = partial._connectionType;
            _shapes = partial._shapes;
        }

        public Builder(BlockConnectionType connectionType, VoxelShape[] shapes) {
            _connectionType = connectionType;
            _shapes = shapes;
        }

        public Builder setCheckUp() {
            _checkUp = true;
            return this;
        }

        public Builder setCheckDown() {
            _checkDown = true;
            return this;
        }

        public Builder setCheckBoth() {
            _checkUp = true;
            _checkDown = true;
            return this;
        }

        public Builder setCenterFourSided() {
            _centerFourSided = true;
            return this;
        }

        public Builder setIndependentModelsPerSide() {
            _independentModelsPerSide = true;
            return this;
        }

        //In most cases we DO want to connect solid, so we'll only explicitely state when not to.
        public Builder setDontConnectSolid() {
            _connectSolid = false;
            return this;
        }

        public Builder setConnectedModelName(String modelName) {
            _connectedModelName = modelName;
            return this;
        }

        public Builder setItemModelName(String modelName) {
            _itemModelName = modelName;
            return this;
        }

        public Builder setConnectionType(BlockConnectionType connectionType){
            _connectionType = connectionType;
            return this;
        }

        public Builder setShapes(VoxelShape[] shapes){
            _shapes = shapes;
            return this;
        }

        public ConnectHorizontalHelper build() {
            return new ConnectHorizontalHelper(_checkUp, _checkDown, _centerFourSided, _independentModelsPerSide, _connectSolid, _connectedModelName, _itemModelName, _connectionType, _shapes);
        }
    }
    //endregion
}
