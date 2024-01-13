package com.fureniku.metropolis.blocks.decorative.helpers;

import com.fureniku.metropolis.blocks.decorative.MetroBlockDecorativeBase;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.HelperType;
import com.fureniku.metropolis.enums.ToggleType;
import com.fureniku.metropolis.utils.ShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;

public class ToggleHelper extends HelperBlockstate {

    public static final BooleanProperty TOGGLED = BooleanProperty.create("toggled");
    private VoxelShape BLOCK_SHAPE;
    private VoxelShape BLOCK_SHAPE_EAST;
    private VoxelShape BLOCK_SHAPE_SOUTH;
    private VoxelShape BLOCK_SHAPE_WEST;
    private VoxelShape BLOCK_SHAPE_TOGGLED;
    private VoxelShape BLOCK_SHAPE_TOGGLED_EAST;
    private VoxelShape BLOCK_SHAPE_TOGGLED_SOUTH;
    private VoxelShape BLOCK_SHAPE_TOGGLED_WEST;
    protected String _toggledModelName;
    private Item _item;
    private ToggleType _type;
    private boolean _rotatable = false;
    private boolean _hideBaseOnToggle = false;

    public ToggleHelper(boolean hideBaseOnToggle, VoxelShape shape, VoxelShape toggledShape, String modelName, ToggleType type) {
        BLOCK_SHAPE = shape;
        BLOCK_SHAPE_TOGGLED = toggledShape;
        _toggledModelName = modelName;
        _type = type;
        _hideBaseOnToggle = hideBaseOnToggle;
    }

    public ToggleHelper(boolean hideBaseOnTogggle, VoxelShape shape, VoxelShape toggledShape, String modelName, Item item) {
        this(hideBaseOnTogggle, shape, toggledShape, modelName, ToggleType.ITEM);
        _item = item;
    }

    @Override
    public BlockState setDefaultState(BlockState state) {
        state.setValue(TOGGLED, false);
        return state;
    }

    @Override
    public StateDefinition.Builder<Block, BlockState> addDefaultState(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TOGGLED);
        return builder;
    }

    @Override
    public HelperType getType() {
        return HelperType.TOGGLE;
    }

    public void setRotatable() {
        _rotatable = true;
        BLOCK_SHAPE_EAST = ShapeUtils.rotateVoxelShape(BLOCK_SHAPE, Direction.EAST);
        BLOCK_SHAPE_SOUTH = ShapeUtils.rotateVoxelShape(BLOCK_SHAPE, Direction.SOUTH);
        BLOCK_SHAPE_WEST = ShapeUtils.rotateVoxelShape(BLOCK_SHAPE, Direction.WEST);
        BLOCK_SHAPE_TOGGLED_EAST = ShapeUtils.rotateVoxelShape(BLOCK_SHAPE_TOGGLED, Direction.EAST);
        BLOCK_SHAPE_TOGGLED_SOUTH = ShapeUtils.rotateVoxelShape(BLOCK_SHAPE_TOGGLED, Direction.SOUTH);
        BLOCK_SHAPE_TOGGLED_WEST = ShapeUtils.rotateVoxelShape(BLOCK_SHAPE_TOGGLED, Direction.WEST);
    }

    public void neighbourChanged(BlockState state, Level level, BlockPos pos, MetroBlockDecorativeBase block) {
        if (!level.isClientSide && (_type == ToggleType.REDSTONE || _type == ToggleType.REDSTONE_INTERACT)) {
            block.setToggledState(level, pos, state, level.hasNeighborSignal(pos));
        }
    }

    public void rightClick(BlockState state, BlockPos pos, Player player, MetroBlockDecorativeBase block) {
        switch (_type) {
            case REDSTONE_INTERACT:
            case INTERACT:
                block.toggleBlock(player.level(), pos, state);
                return;
            case ITEM:
                if (_item != null) {
                    ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
                    if (heldItem.getItem() == _item) {
                        block.toggleBlock(player.level(), pos, state);
                    }
                }
        }
    }

    public BlockState toggleBlock(BlockState state) {
        return setToggledState(state, !state.getValue(TOGGLED));
    }

    public BlockState setToggledState(BlockState state, boolean toggled) {
        return state.setValue(TOGGLED, toggled);
    }

    @Override
    public void generateBlockState(TextureSet[] resources, String modelDir, String modelName, Block block, MetroBlockStateProvider blockStateProvider) {
        BlockModelBuilder modelNormal = blockStateProvider.prepareModels(block, "_standard", modelDir, modelName, resources);
        BlockModelBuilder modelToggled = blockStateProvider.prepareModels(block, "_toggled", modelDir, _toggledModelName, resources);

        blockStateProvider.getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(state.getValue(TOGGLED) ? modelToggled : modelNormal)
                        .build());

        blockStateProvider.simpleBlockItem(block, modelNormal);
    }

    public void generateBlockStateRotatable(TextureSet[] resources, String modelDir, String modelName, Block block, MetroBlockStateProvider blockStateProvider) {
        BlockModelBuilder modelNormal = blockStateProvider.prepareModels(block, "_standard", modelDir, modelName, resources);
        BlockModelBuilder modelToggled = blockStateProvider.prepareModels(block, "_toggled", modelDir, _toggledModelName, resources);

        blockStateProvider.getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(state.getValue(TOGGLED) ? modelToggled : modelNormal)
                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                        .build());

        blockStateProvider.simpleBlockItem(block, modelNormal);
    }

    //region Connecting Toggles
    //This isn't pretty, but this whole class is starting to get messy at this point.
    //First have to decide whether each connection has a unique model or not - required for anything with rotation within the model to avoid lighting issues.
    //Then update all textures for the model, for models with mutli-textures defined
    //Then actually generate the blockstate model, depending on whether the center is four-sided (rotating a model) or not (one singular model), plus handling different forms of toggle
    public void generateBlockStateConnectingToggle(TextureSet[] resources, String modelDir, String modelName, Block block, MetroBlockStateProvider blockStateProvider, ConnectHorizontalHelper connectHorizontalHelper) {
        String connectedModelName = connectHorizontalHelper._connectedModelName;
        BlockModelBuilder modelItem;

        if (connectHorizontalHelper._independentModelsPerSide) {
            modelItem = blockStateProvider.getModelFilesWithTexture(block, "_standard_toggled", modelDir + connectHorizontalHelper._itemModelName + "_n", resources[0].getTexture());
            BlockModelBuilder builders[] = blockStateProvider.applyTexturesToModels(resources,
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_north", modelDir + modelName + "_n", resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_east", modelDir + modelName + "_e", resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_south", modelDir + modelName + "_s", resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_west", modelDir + modelName + "_w", resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_north", modelDir + connectedModelName + "_n", resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_east", modelDir + connectedModelName + "_e", resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_south", modelDir + connectedModelName + "_s", resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_west", modelDir + connectedModelName + "_w", resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_toggled_north", modelDir + _toggledModelName + "_n", resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_toggled_east", modelDir + _toggledModelName + "_e", resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_toggled_south", modelDir + _toggledModelName + "_s", resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_toggled_west", modelDir + _toggledModelName + "_w", resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_toggled_north", modelDir + connectedModelName + "_n", resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_toggled_east", modelDir + connectedModelName + "_e", resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_toggled_south", modelDir + connectedModelName + "_s", resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_toggled_west", modelDir + connectedModelName + "_w", resources[0].getTexture())
            );

            getGeneratedBlockState(block, blockStateProvider, 0, 0, 0, 0, connectHorizontalHelper,
                    builders[0],  builders[1],  builders[2],  builders[3],
                    builders[4],  builders[5],  builders[6],  builders[7],
                    builders[8],  builders[9],  builders[10], builders[11],
                    builders[12], builders[13], builders[14], builders[15]);
        } else {
            modelItem = blockStateProvider.getModelFilesWithTexture(block, "_standard_toggled", modelDir + connectHorizontalHelper._itemModelName + "_n", resources[0].getTexture());
            BlockModelBuilder builders[] = blockStateProvider.applyTexturesToModels(resources,
                    blockStateProvider.getModelFilesWithTexture(block, "_standard_toggled", modelDir + modelName, resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_toggled", modelDir + connectedModelName, resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_standard", modelDir + _toggledModelName, resources[0].getTexture()),
                    blockStateProvider.getModelFilesWithTexture(block, "_connection_toggled", modelDir + _toggledModelName + "_connection", resources[0].getTexture())
            );

            getGeneratedBlockState(block, blockStateProvider, 0, 90, 180, 270, connectHorizontalHelper,
                    builders[0], builders[0], builders[0], builders[0],
                    builders[1], builders[1], builders[1], builders[1],
                    builders[2], builders[2], builders[2], builders[2],
                    builders[3], builders[3], builders[3], builders[3]);
        }
        modelItem = blockStateProvider.applyTexturesToModel(resources, modelItem);
        blockStateProvider.simpleBlockItem(block, modelItem);
    }

    private void getGeneratedBlockState(Block block, MetroBlockStateProvider blockStateProvider, int rotN, int rotE, int rotS, int rotW, ConnectHorizontalHelper connectHorizontalHelper,
                                        BlockModelBuilder modelNormalNorth, BlockModelBuilder modelNormalEast, BlockModelBuilder modelNormalSouth, BlockModelBuilder modelNormalWest,
                                        BlockModelBuilder toggledModelNormalNorth, BlockModelBuilder toggledModelNormalEast, BlockModelBuilder toggledModelNormalSouth, BlockModelBuilder toggledModelNormalWest,
                                        BlockModelBuilder modelConnectionNorth, BlockModelBuilder modelConnectionEast, BlockModelBuilder modelConnectionSouth, BlockModelBuilder modelConnectionWest,
                                        BlockModelBuilder toggledModelConnectionNorth, BlockModelBuilder toggledModelConnectionEast, BlockModelBuilder toggledModelConnectionSouth, BlockModelBuilder toggledModelConnectionWest) {

        MultiPartBlockStateBuilder builder = blockStateProvider.getMultipartBuilder(block);

        if (connectHorizontalHelper._centerFourSided) {
            builder.part().modelFile(toggledModelNormalNorth).rotationY(rotN).uvLock(true).addModel().condition(connectHorizontalHelper.NORTH, false).condition(TOGGLED, true);
            builder.part().modelFile(toggledModelNormalEast).rotationY(rotE).uvLock(true).addModel().condition(connectHorizontalHelper.EAST, false).condition(TOGGLED, true);
            builder.part().modelFile(toggledModelNormalSouth).rotationY(rotS).uvLock(true).addModel().condition(connectHorizontalHelper.SOUTH, false).condition(TOGGLED, true);
            builder.part().modelFile(toggledModelNormalWest).rotationY(rotW).uvLock(true).addModel().condition(connectHorizontalHelper.WEST, false).condition(TOGGLED, true);

            if (!_hideBaseOnToggle) {
                builder.part().modelFile(modelNormalNorth).uvLock(true).rotationY(rotN).addModel().condition(connectHorizontalHelper.NORTH, false);
                builder.part().modelFile(modelNormalEast).uvLock(true).rotationY(rotE).addModel().condition(connectHorizontalHelper.EAST, false);
                builder.part().modelFile(modelNormalSouth).uvLock(true).rotationY(rotS).addModel().condition(connectHorizontalHelper.SOUTH, false);
                builder.part().modelFile(modelNormalWest).uvLock(true).rotationY(rotW).addModel().condition(connectHorizontalHelper.WEST, false);
            } else {
                builder.part().modelFile(modelNormalNorth).uvLock(true).rotationY(rotN).addModel().condition(connectHorizontalHelper.NORTH, false).condition(TOGGLED, false);
                builder.part().modelFile(modelNormalEast).uvLock(true).rotationY(rotE).addModel().condition(connectHorizontalHelper.EAST, false).condition(TOGGLED, false);
                builder.part().modelFile(modelNormalSouth).uvLock(true).rotationY(rotS).addModel().condition(connectHorizontalHelper.SOUTH, false).condition(TOGGLED, false);
                builder.part().modelFile(modelNormalWest).uvLock(true).rotationY(rotW).addModel().condition(connectHorizontalHelper.WEST, false).condition(TOGGLED, false);
            }
        } else {
            builder.part().modelFile(modelNormalNorth).addModel().condition(TOGGLED, false);

            builder.part().modelFile(toggledModelNormalEast).rotationY(rotE).addModel().condition(TOGGLED, true).condition(connectHorizontalHelper.NORTH, true).condition(connectHorizontalHelper.SOUTH, true);
            builder.part().modelFile(toggledModelNormalNorth).addModel().condition(TOGGLED, true).condition(connectHorizontalHelper.NORTH, true, false).condition(connectHorizontalHelper.EAST, true, false).condition(connectHorizontalHelper.SOUTH, false).condition(connectHorizontalHelper.WEST, true, false);
            builder.part().modelFile(toggledModelNormalNorth).addModel().condition(TOGGLED, true).condition(connectHorizontalHelper.NORTH, false).condition(connectHorizontalHelper.EAST, true, false).condition(connectHorizontalHelper.SOUTH, true, false).condition(connectHorizontalHelper.WEST, true, false);
            builder.part().modelFile(toggledModelNormalNorth).addModel().condition(TOGGLED, true).condition(connectHorizontalHelper.NORTH, false).condition(connectHorizontalHelper.EAST, false).condition(connectHorizontalHelper.SOUTH, false).condition(connectHorizontalHelper.WEST, false);
        }

        if (!_hideBaseOnToggle) {
            builder.part().modelFile(modelConnectionNorth).rotationY(rotN).uvLock(true).addModel().condition(connectHorizontalHelper.NORTH, true).end()
                    .part().modelFile(modelConnectionEast).rotationY(rotE).uvLock(true).addModel().condition(connectHorizontalHelper.EAST, true).end()
                    .part().modelFile(modelConnectionSouth).rotationY(rotS).uvLock(true).addModel().condition(connectHorizontalHelper.SOUTH, true).end()
                    .part().modelFile(modelConnectionWest).rotationY(rotW).uvLock(true).addModel().condition(connectHorizontalHelper.WEST, true).end();
        } else {
            builder.part().modelFile(modelConnectionNorth).rotationY(rotN).uvLock(true).addModel().condition(connectHorizontalHelper.NORTH, true).condition(TOGGLED, false).end()
                    .part().modelFile(modelConnectionEast).rotationY(rotE).uvLock(true).addModel().condition(connectHorizontalHelper.EAST, true).condition(TOGGLED, false).end()
                    .part().modelFile(modelConnectionSouth).rotationY(rotS).uvLock(true).addModel().condition(connectHorizontalHelper.SOUTH, true).condition(TOGGLED, false).end()
                    .part().modelFile(modelConnectionWest).rotationY(rotW).uvLock(true).addModel().condition(connectHorizontalHelper.WEST, true).condition(TOGGLED, false).end()
                    .part().modelFile(toggledModelConnectionNorth).rotationY(rotN).uvLock(true).addModel().condition(connectHorizontalHelper.NORTH, true).condition(TOGGLED, true).end()
                    .part().modelFile(toggledModelConnectionEast).rotationY(rotE).uvLock(true).addModel().condition(connectHorizontalHelper.EAST, true).condition(TOGGLED, true).end()
                    .part().modelFile(toggledModelConnectionSouth).rotationY(rotS).uvLock(true).addModel().condition(connectHorizontalHelper.SOUTH, true).condition(TOGGLED, true).end()
                    .part().modelFile(toggledModelConnectionWest).rotationY(rotW).uvLock(true).addModel().condition(connectHorizontalHelper.WEST, true).condition(TOGGLED, true).end();
        }
    }
    //endregion

    public VoxelShape getShapeFromBlockState(BlockState state, Direction direction) {
        if (_rotatable) {
            if (state.getValue(TOGGLED)) {
                switch (direction) {
                    case EAST:
                        return BLOCK_SHAPE_TOGGLED_EAST;
                    case SOUTH:
                        return BLOCK_SHAPE_TOGGLED_SOUTH;
                    case WEST:
                        return BLOCK_SHAPE_TOGGLED_WEST;
                    default:
                        return BLOCK_SHAPE_TOGGLED;
                }
            }
            switch (direction) {
                case EAST:
                    return BLOCK_SHAPE_EAST;
                case SOUTH:
                    return BLOCK_SHAPE_SOUTH;
                case WEST:
                    return BLOCK_SHAPE_WEST;
                default:
                    return BLOCK_SHAPE;
            }
        }
        return state.getValue(TOGGLED) ? BLOCK_SHAPE_TOGGLED : BLOCK_SHAPE;
    }

    @Override
    public BlockState getPlacementState(BlockPlaceContext context, BlockState currentState, Block block) {
        return currentState;
    }
}
