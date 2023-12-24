/*package com.fureniku.metropolis.blocks.decorative;

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


}
*/