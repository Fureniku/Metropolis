package com.fureniku.metropolis.blocks.decorative;

import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.ToggleType;
import com.fureniku.metropolis.utils.Debug;
import com.fureniku.metropolis.utils.SimpleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.registries.RegistryObject;

/**
 * Decorative non-full block which can be rotated, and toggled. Toggle logic not implemented here!
 */
public class MetroBlockDecorativeRotatableToggle extends MetroBlockDecorativeRotatable {

    protected static final DirectionProperty DIRECTION = HorizontalDirectionalBlock.FACING;
    protected static final BooleanProperty TOGGLED = BooleanProperty.create("toggled");
    private final VoxelShape BLOCK_SHAPE_NORTH;
    private final VoxelShape BLOCK_SHAPE_EAST;
    private final VoxelShape BLOCK_SHAPE_SOUTH;
    private final VoxelShape BLOCK_SHAPE_WEST;

    private final VoxelShape BLOCK_SHAPE_NORTH_TOGGLED;
    private final VoxelShape BLOCK_SHAPE_EAST_TOGGLED;
    private final VoxelShape BLOCK_SHAPE_SOUTH_TOGGLED;
    private final VoxelShape BLOCK_SHAPE_WEST_TOGGLED;

    protected String _toggledModelName;

    private ToggleType _type = ToggleType.NONE;
    private Item _item;

    /**
     * Constructor for decorative blocks which have a specific shape. This shape is rotated automatically. Takes an item for item interactions.
     * @param props
     * @param shape
     * @param shapeToggled
     * @param modelName
     * @param toggledModelName
     * @param item
     * @param textures
     */
    public MetroBlockDecorativeRotatableToggle(Properties props, VoxelShape shape, VoxelShape shapeToggled, String modelName, String toggledModelName, Item item, TextureSet... textures) {
        this(props, shape, shapeToggled, modelName, toggledModelName, ToggleType.ITEM, textures);
        _item = item;
    }

    public MetroBlockDecorativeRotatableToggle(MetroBlockDecorativeBuilder builder, Item item) {
        this(builder.getProps(), builder.getShape(), builder.getShapeToggled(), builder.getModelName(), builder.getToggledModelName(), item, builder.getTextures());
    }

    public MetroBlockDecorativeRotatableToggle(MetroBlockDecorativeBuilder builder) {
        this(builder.getProps(), builder.getShape(), builder.getShapeToggled(), builder.getModelName(), builder.getToggledModelName(), builder.getToggleType(), builder.getTextures());
    }

    /**
     * Constructor for decorative blocks which have a specific shape. This shape is rotated automatically.
     * @param props
     * @param shape
     */
    public MetroBlockDecorativeRotatableToggle(Properties props, VoxelShape shape, VoxelShape shapeToggled, String modelName, String toggledModelName, ToggleType type, TextureSet... textures) {
        super(props, shape, modelName, textures);
        BLOCK_SHAPE_NORTH = shape;
        BLOCK_SHAPE_EAST = SimpleUtils.rotateVoxelShape(shape, Direction.EAST);
        BLOCK_SHAPE_SOUTH = SimpleUtils.rotateVoxelShape(shape, Direction.SOUTH);
        BLOCK_SHAPE_WEST = SimpleUtils.rotateVoxelShape(shape, Direction.WEST);

        BLOCK_SHAPE_NORTH_TOGGLED = shapeToggled;
        BLOCK_SHAPE_EAST_TOGGLED = SimpleUtils.rotateVoxelShape(shapeToggled, Direction.EAST);
        BLOCK_SHAPE_SOUTH_TOGGLED = SimpleUtils.rotateVoxelShape(shapeToggled, Direction.SOUTH);
        BLOCK_SHAPE_WEST_TOGGLED = SimpleUtils.rotateVoxelShape(shapeToggled, Direction.WEST);
        this.registerDefaultState(this.stateDefinition.any().setValue(DIRECTION, Direction.NORTH).setValue(TOGGLED, false));

        _toggledModelName = toggledModelName;
        _type = type;
    }

    protected void toggleBlock(Level level, BlockPos pos, BlockState state) {
        setToggledState(level, pos, state, !state.getValue(TOGGLED));
    }

    protected void setToggledState(Level level, BlockPos pos, BlockState state, boolean toggled) {
        setBlock(level, pos, state.setValue(TOGGLED, toggled));
    }

    @Override
    public void generateBlockState(RegistryObject<Block> blockRegistryObject, MetroBlockStateProvider blockStateProvider) {
        Block block = blockRegistryObject.get();
        BlockModelBuilder modelNormal = blockStateProvider.getModelFilesWithTexture(block, "_standard", "blocks/decorative/" + _modelName, _resources[0].getTexture());
        BlockModelBuilder modelToggled = blockStateProvider.getModelFilesWithTexture(block, "_toggled", "blocks/decorative/" + _toggledModelName, _resources[0].getTexture());

        if (_resources.length > 1) {
            for (int i = 1; i < _resources.length; i++) {
                modelNormal = modelNormal.texture(_resources[i].getKey(), _resources[i].getTexture());
                modelToggled = modelToggled.texture(_resources[i].getKey(), _resources[i].getTexture());
            }
        }

        Debug.Log("Normal: " + modelNormal + ", toggled: " + _toggledModelName);

        BlockModelBuilder finalModelNormal = modelNormal;
        BlockModelBuilder finalModelToggled = modelToggled;
        blockStateProvider.getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.builder()
                            .modelFile(state.getValue(TOGGLED) ? finalModelToggled : finalModelNormal)
                            .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                            .build());

        blockStateProvider.simpleBlockItem(block, modelNormal);
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState blockstate = this.defaultBlockState().setValue(DIRECTION, context.getHorizontalDirection());
        return blockstate;
    }

    @Override
    protected void createBlockState(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DIRECTION, TOGGLED);
    }

    @Override
    protected VoxelShape getShapeFromBlockState(BlockState state) {
        if (state.getValue(TOGGLED)) {
            switch (state.getValue(DIRECTION)) {
                case EAST:
                    return BLOCK_SHAPE_EAST_TOGGLED;
                case SOUTH:
                    return BLOCK_SHAPE_SOUTH_TOGGLED;
                case WEST:
                    return BLOCK_SHAPE_WEST_TOGGLED;
                default:
                    return BLOCK_SHAPE_NORTH_TOGGLED;
            }
        }
        switch (state.getValue(DIRECTION)) {
            case EAST:
                return BLOCK_SHAPE_EAST;
            case SOUTH:
                return BLOCK_SHAPE_SOUTH;
            case WEST:
                return BLOCK_SHAPE_WEST;
            default:
                return BLOCK_SHAPE_NORTH;
        }
    }

    @Override
    public void onNeighbourChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos) {
        if (!level.isClientSide && (_type == ToggleType.REDSTONE || _type == ToggleType.REDSTONE_INTERACT)) {
            setToggledState(level, pos, state, level.hasNeighborSignal(pos));
        }
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
}
