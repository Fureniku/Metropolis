package com.fureniku.metropolis.blocks.decorative.helpers;

import com.fureniku.metropolis.blocks.MetroBlockBase;
import com.fureniku.metropolis.blocks.decorative.MetroBlockDecorative;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.HelperType;
import com.fureniku.metropolis.enums.ToggleType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

public class ToggleHelper extends HelperBlockstate {

    public static final BooleanProperty TOGGLED = BooleanProperty.create("toggled");
    private VoxelShape BLOCK_SHAPE_TOGGLED;
    protected String _toggledModelName;
    private Item _item;
    private ToggleType _type;

    public ToggleHelper(VoxelShape shape, String modelName, ToggleType type) {
        BLOCK_SHAPE_TOGGLED = shape;
        _toggledModelName = modelName;
        _type = type;
    }

    public ToggleHelper(VoxelShape shape, String modelName, Item item) {
        this(shape, modelName, ToggleType.ITEM);
        _item = item;
    }

    @Override
    public BlockState setDefaultState(BlockState state) {
        state.setValue(TOGGLED, false);
        return state;
    }

    @Override
    public HelperType getType() {
        return HelperType.TOGGLE;
    }

    @Override
    public Property getProperty() {
        return TOGGLED;
    }

    public void neighbourChanged(BlockState state, Level level, BlockPos pos, MetroBlockDecorative block) {
        if (!level.isClientSide && (_type == ToggleType.REDSTONE || _type == ToggleType.REDSTONE_INTERACT)) {
            block.setToggledState(level, pos, state, level.hasNeighborSignal(pos));
        }
    }

    public void rightClick(BlockState state, BlockPos pos, Player player, MetroBlockDecorative block) {
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
    public void generateBlockstate(TextureSet[] resources, String modelName, Block block, MetroBlockStateProvider blockStateProvider) {
        BlockModelBuilder modelNormal = blockStateProvider.getModelFilesWithTexture(block, "_standard", "blocks/decorative/" + modelName, resources[0].getTexture());
        BlockModelBuilder modelToggled = blockStateProvider.getModelFilesWithTexture(block, "_toggled", "blocks/decorative/" + _toggledModelName, resources[0].getTexture());

        if (resources.length > 1) {
            for (int i = 1; i < resources.length; i++) {
                modelNormal = modelNormal.texture(resources[i].getKey(), resources[i].getTexture());
                modelToggled = modelToggled.texture(resources[i].getKey(), resources[i].getTexture());
            }
        }

        BlockModelBuilder finalModelNormal = modelNormal;
        BlockModelBuilder finalModelToggled = modelToggled;
        blockStateProvider.getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(state.getValue(TOGGLED) ? finalModelToggled : finalModelNormal)
                        .build());

        blockStateProvider.simpleBlockItem(block, modelNormal);
    }
}
