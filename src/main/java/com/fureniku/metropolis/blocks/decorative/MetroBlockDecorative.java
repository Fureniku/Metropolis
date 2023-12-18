package com.fureniku.metropolis.blocks.decorative;

import com.fureniku.metropolis.blocks.IToggleable;
import com.fureniku.metropolis.blocks.MetroBlockBase;
import com.fureniku.metropolis.blocks.decorative.builders.MetroBlockDecorativeBuilder;
import com.fureniku.metropolis.blocks.decorative.helpers.*;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.BlockOffsetDirection;
import com.fureniku.metropolis.enums.ToggleType;
import com.fureniku.metropolis.utils.SimpleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
import java.util.Optional;

public class MetroBlockDecorative extends MetroBlockBase implements IToggleable {

    private final VoxelShape BLOCK_SHAPE;
    protected TextureSet[] _resources;
    protected String _modelName;
    protected String _modelDir;

    private final ArrayList<HelperBase> _helpers;

    private RotationHelper _rotationHelper;
    private ToggleHelper _toggleHelper;
    private OffsetHelper _offsetHelper;

    public MetroBlockDecorative(Properties props, VoxelShape shape, String modelDir, String modelName, ArrayList<HelperBase> helpers, TextureSet... textures) {
        super(SimpleUtils.containsType(helpers, OffsetHelper.class) ? props.dynamicShape() : props);
        BLOCK_SHAPE = shape;
        _resources = textures;
        _modelDir = modelDir;
        _modelName = modelName;
        _helpers = helpers;
        BlockState stateHolder = this.getStateDefinition().any();

        for (int i = 0; i < _helpers.size(); i++) {
            stateHolder = _helpers.get(i).setDefaultState(stateHolder);
            assignHelper(helpers.get(i));
        }

        this.registerDefaultState(stateHolder);
    }

    private void assignHelper(HelperBase helper) {
        switch (helper.getType()) {
            case OFFSET -> _offsetHelper = (OffsetHelper) helper;
            case ROTATION -> _rotationHelper = (RotationHelper) helper;
            case TOGGLE -> _toggleHelper = (ToggleHelper) helper;
        }
    }

    @Override
    protected void createBlockState(StateDefinition.Builder<Block, BlockState> builder) {
        if (_toggleHelper != null) builder.add(_toggleHelper.TOGGLED);
        if (_rotationHelper != null) builder.add(_rotationHelper.DIRECTION);
    }

    @Override
    protected VoxelShape getShapeFromBlockState(BlockState pState) {
        //TODO how do we handle this now
        return BLOCK_SHAPE;
    }

    @Override
    public void generateBlockState(RegistryObject<Block> blockRegistryObject, MetroBlockStateProvider blockStateProvider) {
        Block block = blockRegistryObject.get();
        BlockModelBuilder bmb = prepareModels(block, blockStateProvider);
        boolean generatedBlock = false;
        for (int i = 0; i < _helpers.size(); i++) {
            if (_helpers.get(i) instanceof HelperBlockstate) {
                HelperBlockstate helper = (HelperBlockstate) _helpers.get(i);
                //helper.generateBlockstate();
                generatedBlock = true;
            }
        }

        blockStateProvider.simpleBlockWithItem(block, applyTexturesToModels(bmb)[0]);
    }

    //TODO resolve
    protected BlockModelBuilder applyTexturesToModel(BlockModelBuilder bmb) {
        if (_resources.length > 1) {
            for (int i = 1; i < _resources.length; i++) {
                bmb = bmb.texture(_resources[i].getKey(), _resources[i].getTexture());
            }
        }
        return bmb;
    }

//TODO resolve
    protected BlockModelBuilder[] applyTexturesToModels(BlockModelBuilder... bmb) {
        if (_resources.length > 1) {
            for (int i = 0; i < bmb.length; i++) {
                for (int j = 1; j < _resources.length; j++) {
                    bmb[i] = bmb[i].texture(_resources[j].getKey(), _resources[j].getTexture());
                }
            }
        }
        return bmb;
        if (!generatedBlock) {
            blockStateProvider.simpleBlock(block, bmb);
        }
        blockStateProvider.simpleBlockItem(block, bmb);
    }

//TODO resolve
    public BlockModelBuilder prepareModels(Block block, MetroBlockStateProvider blockStateProvider) {
        BlockModelBuilder bmb;
        if (_modelName == null || _resources == null) {
            bmb = blockStateProvider.getModelFilesWithTexture(block, "", _modelDir + block.getName(), blockStateProvider.modLoc("blocks/decorative/" + block.getName()));
        } else {
            bmb = blockStateProvider.getModelFilesWithTexture(block, "", _modelDir + _modelName, _resources[0].getTexture());
            if (_resources.length > 1) {
                for (int i = 1; i < _resources.length; i++) {
                    bmb = bmb.texture(_resources[i].getKey(), _resources[i].getTexture());
                }
            }
        }
        return bmb;
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
