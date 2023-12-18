package com.fureniku.metropolis.blocks.decorative;

import com.fureniku.metropolis.blocks.decorative.builders.MetroBlockDecorativeConnectingBuilder;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.BlockConnectionType;
import com.fureniku.metropolis.enums.BlockOffsetDirection;
import com.fureniku.metropolis.enums.ToggleType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.registries.RegistryObject;

public class MetroBlockConnectingHorizontalToggle extends MetroBlockConnectingHorizontal {

    protected static final BooleanProperty TOGGLED = BooleanProperty.create("toggle");
    protected final VoxelShape[] _toggleShapeByIndex;
    protected String _toggledModelName;
    protected String _connectedModelName;
    protected String _connectedToggledModelName;
    private boolean _centerFourSidedToggled;
    private boolean _hideBaseOnToggle;
    private Item _item;
    private ToggleType _type;

    public MetroBlockConnectingHorizontalToggle(Properties props,
                                                String modelName, String connectedModelName, String toggledModelName, String connectedToggledModelName, String itemModelName,
                                                BlockOffsetDirection offsetDirection, boolean checkUp, boolean checkDown, boolean centerFourSided, boolean independentModelsPerSide, boolean centerFourSidedToggled, boolean hideBaseOnToggle,
                                                VoxelShape[] shapes, String tag, BlockConnectionType connectionType, Item item, TextureSet... textures) {
        this(props, modelName, connectedModelName, toggledModelName, connectedToggledModelName, itemModelName, offsetDirection, checkUp, checkDown, centerFourSided, independentModelsPerSide, centerFourSidedToggled, hideBaseOnToggle, shapes, tag, connectionType, ToggleType.ITEM, textures);
        this._item = item;
    }

    public MetroBlockConnectingHorizontalToggle(Properties props,
                                                String modelName, String connectedModelName, String toggledModelName, String connectedToggledModelName, String itemModelName,
                                                BlockOffsetDirection offsetDirection, boolean checkUp, boolean checkDown, boolean centerFourSided, boolean independentModelsPerSide, boolean centerFourSidedToggled, boolean hideBaseOnToggle,
                                                VoxelShape[] shapes, String tag, BlockConnectionType connectionType, ToggleType type, TextureSet... textures) {
        super(props, modelName, toggledModelName, itemModelName, offsetDirection, checkUp, checkDown, centerFourSided, independentModelsPerSide, shapes, tag, connectionType, textures);
        _toggleShapeByIndex = shapes;
        _toggledModelName = toggledModelName;
        _connectedModelName = connectedModelName;
        _connectedToggledModelName = connectedToggledModelName;
        _type = type;
        _centerFourSidedToggled = centerFourSidedToggled;
        _independentModelsPerSide = independentModelsPerSide;
        _hideBaseOnToggle = hideBaseOnToggle;
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(TOGGLED, false));
    }

    public MetroBlockConnectingHorizontalToggle(MetroBlockDecorativeConnectingBuilder builder, Item item) {
        this(builder.getProps(), builder.getModelName(), builder.getConnectedModelName(), builder.getToggledModelName(), builder.getConnectedToggledModelName(), builder.getItemModelName(),
                builder.getOffsetDirection(), builder.getCheckUp(), builder.getCheckDown(), builder.getCenterFourSided(), builder.getIndependentModelsPerSide(), builder.getCenterFourSidedToggled(), builder.getHideBaseOnToggled(),
                builder.getShapes(), builder.getTag(), builder.getConnectionType(), item, builder.getTextures());
    }

    public MetroBlockConnectingHorizontalToggle(MetroBlockDecorativeConnectingBuilder builder) {
        this(builder.getProps(), builder.getModelName(), builder.getConnectedModelName(), builder.getToggledModelName(), builder.getConnectedToggledModelName(), builder.getItemModelName(),
                builder.getOffsetDirection(), builder.getCheckUp(), builder.getCheckDown(), builder.getCenterFourSided(), builder.getIndependentModelsPerSide(), builder.getCenterFourSidedToggled(), builder.getHideBaseOnToggled(),
                builder.getShapes(), builder.getTag(), builder.getConnectionType(), builder.getToggleType(), builder.getTextures());
    }

    @Override
    protected void createBlockState(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, TOGGLED);
    }

    protected void toggleBlock(Level level, BlockPos pos, BlockState state) {
        setToggledState(level, pos, state, !state.getValue(TOGGLED));
    }

    protected void setToggledState(Level level, BlockPos pos, BlockState state, boolean toggled) {
        setBlock(level, pos, state.setValue(TOGGLED, toggled));
    }

    @Override
    protected void onRightClickRemote(BlockState state, BlockPos pos, Player player) {
        switch (_type) {
            case REDSTONE_INTERACT:
            case INTERACT:
                toggleBlock(player.level(), pos, state);
                return;
            case ITEM:
                if (_item != null) {
                    ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
                    if (heldItem.getItem() == _item) {
                        toggleBlock(player.level(), pos, state);
                    }
                }
        }
    }

    //This isn't pretty, but this whole class is starting to get messy at this point.
    //First have to decide whether each connection has a unique model or not - required for anything with rotation within the model to avoid lighting issues.
    //Then update all textures for the model, for models with mutli-textures defined
    //Then actually generate the blockstate model, depending on whether the center is four-sided (rotating a model) or not (one singular model), plus handling different forms of toggle
    @Override
    public void generateBlockState(RegistryObject<Block> blockRegistryObject, MetroBlockStateProvider blockStateProvider) {
        Block block = blockRegistryObject.get();
        BlockModelBuilder modelItem;

        if (_independentModelsPerSide) {
            modelItem = blockStateProvider.getModelFilesWithTexture(block, "_standard_toggled", "blocks/decorative/" + _itemModelName + "_n", _resources[0].getTexture());
            BlockModelBuilder builders[] = applyTexturesToModels(
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_north", "blocks/decorative/" + _modelName + "_n", _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_east", "blocks/decorative/" + _modelName + "_e", _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_south", "blocks/decorative/" + _modelName + "_s", _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_west", "blocks/decorative/" + _modelName + "_w", _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_north", "blocks/decorative/" + _connectedModelName + "_n", _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_east", "blocks/decorative/" + _connectedModelName + "_e", _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_south", "blocks/decorative/" + _connectedModelName + "_s", _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_west", "blocks/decorative/" + _connectedModelName + "_w", _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_toggled_north", "blocks/decorative/" + _toggledModelName + "_n", _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_toggled_east", "blocks/decorative/" + _toggledModelName + "_e", _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_toggled_south", "blocks/decorative/" + _toggledModelName + "_s", _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_toggled_west", "blocks/decorative/" + _toggledModelName + "_w", _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_toggled_north", "blocks/decorative/" + _connectedToggledModelName + "_n", _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_toggled_east", "blocks/decorative/" + _connectedToggledModelName + "_e", _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_toggled_south", "blocks/decorative/" + _connectedToggledModelName + "_s", _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_toggled_west", "blocks/decorative/" + _connectedToggledModelName + "_w", _resources[0].getTexture())
            );

            getGeneratedBlockState(block, blockStateProvider, 0, 0, 0, 0,
                    builders[0],  builders[1],  builders[2],  builders[3],
                    builders[4],  builders[5],  builders[6],  builders[7],
                    builders[8],  builders[9],  builders[10], builders[11],
                    builders[12], builders[13], builders[14], builders[15]);
        } else {
            modelItem = blockStateProvider.getModelFilesWithTexture(block, "_standard_toggled", "blocks/decorative/" + _itemModelName + "_n", _resources[0].getTexture());
            BlockModelBuilder builders[] = applyTexturesToModels(
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_toggled", "blocks/decorative/" + _modelName, _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_toggled", "blocks/decorative/" + _connectedModelName, _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_standard", "blocks/decorative/" + _toggledModelName, _resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_toggled", "blocks/decorative/" + _connectedToggledModelName, _resources[0].getTexture())
            );

            getGeneratedBlockState(block, blockStateProvider, 0, 90, 180, 270,
                    builders[0], builders[0], builders[0], builders[0],
                    builders[1], builders[1], builders[1], builders[1],
                    builders[2], builders[2], builders[2], builders[2],
                    builders[3], builders[3], builders[3], builders[3]);
        }
        modelItem = applyTexturesToModel(modelItem);
        blockStateProvider.simpleBlockItem(block, modelItem);
    }

    private void getGeneratedBlockState(Block block, MetroBlockStateProvider blockStateProvider, int rotN, int rotE, int rotS, int rotW,
                               BlockModelBuilder modelNormalNorth, BlockModelBuilder modelNormalEast, BlockModelBuilder modelNormalSouth, BlockModelBuilder modelNormalWest,
                               BlockModelBuilder toggledModelNormalNorth, BlockModelBuilder toggledModelNormalEast, BlockModelBuilder toggledModelNormalSouth, BlockModelBuilder toggledModelNormalWest,
                               BlockModelBuilder modelConnectionNorth, BlockModelBuilder modelConnectionEast, BlockModelBuilder modelConnectionSouth, BlockModelBuilder modelConnectionWest,
                               BlockModelBuilder toggledModelConnectionNorth, BlockModelBuilder toggledModelConnectionEast, BlockModelBuilder toggledModelConnectionSouth, BlockModelBuilder toggledModelConnectionWest) {

        MultiPartBlockStateBuilder builder = blockStateProvider.getMultipartBuilder(block);

        if (_centerFourSided) {
            if (!_hideBaseOnToggle) {
                builder.part().modelFile(modelNormalNorth).uvLock(true).rotationY(rotN).addModel().condition(NORTH, false);
                builder.part().modelFile(modelNormalEast).uvLock(true).rotationY(rotE).addModel().condition(EAST, false);
                builder.part().modelFile(modelNormalSouth).uvLock(true).rotationY(rotS).addModel().condition(SOUTH, false);
                builder.part().modelFile(modelNormalWest).uvLock(true).rotationY(rotW).addModel().condition(WEST, false);
            } else {
                builder.part().modelFile(modelNormalNorth).uvLock(true).rotationY(rotN).addModel().condition(NORTH, false).condition(TOGGLED, false);
                builder.part().modelFile(modelNormalEast).uvLock(true).rotationY(rotE).addModel().condition(EAST, false).condition(TOGGLED, false);
                builder.part().modelFile(modelNormalSouth).uvLock(true).rotationY(rotS).addModel().condition(SOUTH, false).condition(TOGGLED, false);
                builder.part().modelFile(modelNormalWest).uvLock(true).rotationY(rotW).addModel().condition(WEST, false).condition(TOGGLED, false);
            }
        } else {
            builder.part().modelFile(modelNormalNorth).addModel().condition(TOGGLED, false);
        }

        if (_centerFourSidedToggled) {
            builder.part().modelFile(toggledModelNormalNorth).rotationY(rotN).uvLock(true).addModel().condition(NORTH, false).condition(TOGGLED, true);
            builder.part().modelFile(toggledModelNormalEast).rotationY(rotE).uvLock(true).addModel().condition(EAST, false).condition(TOGGLED, true);
            builder.part().modelFile(toggledModelNormalSouth).rotationY(rotS).uvLock(true).addModel().condition(SOUTH, false).condition(TOGGLED, true);
            builder.part().modelFile(toggledModelNormalWest).rotationY(rotW).uvLock(true).addModel().condition(WEST, false).condition(TOGGLED, true);
        } else {
            builder.part().modelFile(toggledModelNormalEast).rotationY(rotE).addModel().condition(TOGGLED, true).condition(NORTH, true).condition(SOUTH, true);
            builder.part().modelFile(toggledModelNormalNorth).addModel().condition(TOGGLED, true).condition(NORTH, true, false).condition(EAST, true, false).condition(SOUTH, false).condition(WEST, true, false);
            builder.part().modelFile(toggledModelNormalNorth).addModel().condition(TOGGLED, true).condition(NORTH, false).condition(EAST, true, false).condition(SOUTH, true, false).condition(WEST, true, false);
            builder.part().modelFile(toggledModelNormalNorth).addModel().condition(TOGGLED, true).condition(NORTH, false).condition(EAST, false).condition(SOUTH, false).condition(WEST, false);
        }

        if (!_hideBaseOnToggle) {
            builder.part().modelFile(modelConnectionNorth).rotationY(rotN).uvLock(true).addModel().condition(NORTH, true).end()
                    .part().modelFile(modelConnectionEast).rotationY(rotE).uvLock(true).addModel().condition(EAST, true).end()
                    .part().modelFile(modelConnectionSouth).rotationY(rotS).uvLock(true).addModel().condition(SOUTH, true).end()
                    .part().modelFile(modelConnectionWest).rotationY(rotW).uvLock(true).addModel().condition(WEST, true).end();
        } else {
            builder.part().modelFile(modelConnectionNorth).rotationY(rotN).uvLock(true).addModel().condition(NORTH, true).condition(TOGGLED, false).end()
                    .part().modelFile(modelConnectionEast).rotationY(rotE).uvLock(true).addModel().condition(EAST, true).condition(TOGGLED, false).end()
                    .part().modelFile(modelConnectionSouth).rotationY(rotS).uvLock(true).addModel().condition(SOUTH, true).condition(TOGGLED, false).end()
                    .part().modelFile(modelConnectionWest).rotationY(rotW).uvLock(true).addModel().condition(WEST, true).condition(TOGGLED, false).end()
                    .part().modelFile(toggledModelConnectionNorth).rotationY(rotN).uvLock(true).addModel().condition(NORTH, true).condition(TOGGLED, true).end()
                    .part().modelFile(toggledModelConnectionEast).rotationY(rotE).uvLock(true).addModel().condition(EAST, true).condition(TOGGLED, true).end()
                    .part().modelFile(toggledModelConnectionSouth).rotationY(rotS).uvLock(true).addModel().condition(SOUTH, true).condition(TOGGLED, true).end()
                    .part().modelFile(toggledModelConnectionWest).rotationY(rotW).uvLock(true).addModel().condition(WEST, true).condition(TOGGLED, true).end();
        }
    }
}
