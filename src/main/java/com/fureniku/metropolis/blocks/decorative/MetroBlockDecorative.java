package com.fureniku.metropolis.blocks.decorative;

import com.fureniku.metropolis.Metropolis;
import com.fureniku.metropolis.blockentity.MetroEntityBlockDecorative;
import com.fureniku.metropolis.blocks.IToggleable;
import com.fureniku.metropolis.blocks.MetroBlockBase;
import com.fureniku.metropolis.blocks.decorative.builders.MetroBlockDecorativeBuilder;
import com.fureniku.metropolis.blocks.decorative.helpers.*;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.BlockOffsetDirection;
import com.fureniku.metropolis.enums.ToggleType;
import com.fureniku.metropolis.utils.Debug;
import com.fureniku.metropolis.utils.SimpleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public abstract class MetroBlockDecorative extends MetroBlockBase implements IToggleable {

    private final VoxelShape BLOCK_SHAPE;
    protected TextureSet[] _resources;
    protected String _modelName;
    protected String _modelDir;

    private RotationHelper _rotationHelper;
    private ToggleHelper _toggleHelper;
    private ConnectHorizontalHelper _connectHorizontalHelper;
    private OffsetHelper _offsetHelper;

    private boolean _baseInitialized = false;

    public MetroBlockDecorative(Properties props, VoxelShape shape, String modelDir, String modelName, String tag, boolean dynamicShape, TextureSet... textures) {
        super(dynamicShape ? props.dynamicShape() : props);
        setupHelpers(getHelpers());
        _baseInitialized = true;
        BLOCK_SHAPE = shape != null ? shape : Block.box(0, 0, 0, 16, 16, 16);
        _resources = textures;
        _modelDir = modelDir;
        _modelName = modelName;

        //BlockState stateHolder = ;
        setTag(tag);

        this.registerDefaultState(this.getStateDefinition().any().setValue(ConnectHorizontalHelper.NORTH, false).setValue(ConnectHorizontalHelper.EAST, false).setValue(ConnectHorizontalHelper.SOUTH, false).setValue(ConnectHorizontalHelper.WEST, false));
    }

    @FunctionalInterface
    public interface MetroBlockStateFactory {
        MetroBlockDecorative makeBlock(Properties props, VoxelShape shape, String modelDir, String modelName, String tag, boolean dynamicShape, TextureSet... textures);
    }

    public static MetroBlockStateFactory getBlockFactory(HelperBase... helpersIn) {
        return (props, shape, modelDir, modelName, tag, dynamicShape, textures) -> new MetroBlockDecorative(props, shape, modelDir, modelName, tag, SimpleUtils.containsType(OffsetHelper.class, helpersIn), textures) {
            @Override
            public ArrayList<HelperBase> getHelpers() {
                return new ArrayList<>(Arrays.asList(helpersIn));
            }
        };
    }

    public abstract ArrayList<HelperBase> getHelpers();

    public void setupHelpers(ArrayList<HelperBase> helpers) {
        for (int i = 0; i < helpers.size(); i++) {
            //stateHolder = _helpers.get(i).setDefaultState(stateHolder);
            assignHelper(helpers.get(i));
        }
        _helpers = helpers;
    }

    public void assignHelper(HelperBase helper) {
        switch (helper.getType()) {
            case OFFSET -> _offsetHelper = (OffsetHelper) helper;
            case ROTATION -> _rotationHelper = (RotationHelper) helper;
            case TOGGLE -> _toggleHelper = (ToggleHelper) helper;
            case CONNECTION_HORIZONTAL -> _connectHorizontalHelper = (ConnectHorizontalHelper) helper;
        }
        if (_rotationHelper != null && _toggleHelper != null) {
            _toggleHelper.setRotatable();
        }
    }

    @Override
    protected void createBlockState(StateDefinition.Builder<Block, BlockState> builder) {
        Debug.Log("[1234] create blockstate");
        /*if (_toggleHelper != null) builder.add(_toggleHelper.TOGGLED);
        if (_rotationHelper != null) builder.add(_rotationHelper.DIRECTION);
        if (_connectHorizontalHelper != null) */
        builder.add(ConnectHorizontalHelper.NORTH, ConnectHorizontalHelper.EAST, ConnectHorizontalHelper.SOUTH, ConnectHorizontalHelper.WEST);
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState();
        Block block = state.getBlock();
        if (_rotationHelper != null) state = _rotationHelper.getPlacementState(context, state, block);
        if (_connectHorizontalHelper != null) state = _connectHorizontalHelper.getPlacementState(context, state, block);
        return state;
    }

    @Override
    protected VoxelShape getShapeFromBlockState(BlockState state) {
        if (_rotationHelper != null && _connectHorizontalHelper != null) {
            //replace with a new subhelper which generates the rotated connecting shapes on construction
            //return shape;
        }

        if (_toggleHelper != null) {
            Direction dir = _rotationHelper != null ? state.getValue(_rotationHelper.DIRECTION) : Direction.UP;
            return _toggleHelper.getShapeFromBlockState(state, dir);
        }

        if (_rotationHelper != null) {
            return _rotationHelper.getShapeFromBlockState(state);
        }

        if (_connectHorizontalHelper != null) {
            return _connectHorizontalHelper.getShapeFromBlockState(state);
        }
        return BLOCK_SHAPE;
    }

    @Override
    public void generateBlockState(RegistryObject<Block> blockRegistryObject, MetroBlockStateProvider blockStateProvider) {
        Block block = blockRegistryObject.get();
        BlockModelBuilder bmb = blockStateProvider.prepareModels(block, _modelDir, _modelName, _resources);
        boolean generatedBlock = false;

        if (_helpers.size() == 1) {
            //Easy approach - just use the helper's blockstate generator
            if (_helpers.get(0) instanceof HelperBlockstate) {
                ((HelperBlockstate) _helpers.get(0)).generateBlockstate(_resources, _modelDir, _modelName, block, blockStateProvider);
                generatedBlock = true;
            }
        } else {
            //Combine multiple generators, ree
            //connect horizontal + rotate
            //connect horizontal + toggle
            //connect horizontal + rotate + toggle
            //rotate + toggle
        }

        if (!generatedBlock) {
            blockStateProvider.simpleBlock(block, bmb);
            blockStateProvider.simpleBlockItem(block, bmb);
        }
    }

    //region Offset
    @Override
    protected Vec3 getOffset(BlockState blockState, BlockGetter level, BlockPos pos) {
        if (_offsetHelper != null) {
            Vec3 offset = _offsetHelper.getOffset(level, pos);
            blockState.offsetFunction = Optional.of((state, lvl, blockPos) -> offset);
            return offset;
        }
        return Vec3.ZERO;
    }
    //endregion

    //region Togglable
    @Override
    public void onNeighbourChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos) {
        if (_toggleHelper != null) {
            _toggleHelper.neighbourChanged(state, level, pos, this);
        }

        if (_connectHorizontalHelper != null) {
            _connectHorizontalHelper.neighbourChanged(state, level, pos, this);
        }
    }

    @Override
    protected void onRightClickRemote(BlockState state, BlockPos pos, Player player) {
        if (_toggleHelper != null) {
            _toggleHelper.rightClick(state, pos, player, this);
        }
    }

    @Override
    public void toggleBlock(Level level, BlockPos pos, BlockState state) {
        if (_toggleHelper != null) {
            _toggleHelper.toggleBlock(state);
        }
    }

    @Override
    public void setToggledState(Level level, BlockPos pos, BlockState state, boolean toggled) {
        if (_toggleHelper != null) {
            setBlock(level, pos, _toggleHelper.setToggledState(state, toggled));
        }
    }
    //endregion
}
