package com.fureniku.metropolis.blocks.decorative;

import com.fureniku.metropolis.blocks.MetroBlockBase;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.ToggleType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.registries.RegistryObject;

public class MetroBlockDecorativeToggle extends MetroBlockDecorative {

    protected static final BooleanProperty TOGGLED = BooleanProperty.create("toggled");

    private final VoxelShape BLOCK_SHAPE;
    private final VoxelShape BLOCK_SHAPE_TOGGLED;
    protected String _toggledModelName;
    private Item _item;
    private ToggleType _type;

    public MetroBlockDecorativeToggle(Properties props, VoxelShape shape, VoxelShape toggledShape, String modelName, String toggledModelName, Item item, TextureSet... textures) {
        this(props, shape, toggledShape, modelName, toggledModelName, ToggleType.ITEM, textures);
        _item = item;
    }

    public MetroBlockDecorativeToggle(Properties props, VoxelShape shape, VoxelShape toggledShape, String modelName, String toggledModelName, ToggleType type, TextureSet... textures) {
        super(props, shape, modelName, textures);
        BLOCK_SHAPE = shape;
        BLOCK_SHAPE_TOGGLED = toggledShape;
        _toggledModelName = toggledModelName;
        _type = type;
        this.registerDefaultState(this.stateDefinition.any().setValue(TOGGLED, false));
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

        BlockModelBuilder finalModelNormal = modelNormal;
        BlockModelBuilder finalModelToggled = modelToggled;
        blockStateProvider.getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(state.getValue(TOGGLED) ? finalModelToggled : finalModelNormal)
                        .build());

        blockStateProvider.simpleBlockItem(block, modelNormal);
    }

    protected void toggleBlock(Level level, BlockPos pos, BlockState state) {
        setToggledState(level, pos, state, !state.getValue(TOGGLED));
    }

    protected void setToggledState(Level level, BlockPos pos, BlockState state, boolean toggled) {
        setBlock(level, pos, state.setValue(TOGGLED, toggled));
    }

    @Override
    protected void createBlockState(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TOGGLED);
    }

    @Override
    protected VoxelShape getShapeFromBlockState(BlockState state) {
        return state.getValue(TOGGLED) ? BLOCK_SHAPE_TOGGLED : BLOCK_SHAPE;
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
