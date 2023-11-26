package com.fureniku.metropolis.blocks;

import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.RegistryObject;

public class MetroBlockDecorativeRotatable extends MetroBlockBase {

    protected static final DirectionProperty DIRECTION = DirectionProperty.create("rotation", Direction.Plane.HORIZONTAL);
    private final VoxelShape BLOCK_SHAPE;

    public MetroBlockDecorativeRotatable(Properties props) {
        this(props, 16, 16);
    }

    public MetroBlockDecorativeRotatable(Properties props, float height) {
        this(props, height, 16);
    }

    public MetroBlockDecorativeRotatable(Properties props, float height, float width) {
        this(props, width, height, width);
    }

    public MetroBlockDecorativeRotatable(Properties props, VoxelShape shape) {
        super(props);
        BLOCK_SHAPE = shape;
        this.registerDefaultState(this.defaultBlockState().setValue(DIRECTION, Direction.NORTH));
    }

    public MetroBlockDecorativeRotatable(Properties props, float sizeX, float sizeY, float sizeZ) {
        super(props);
        float insetX = (16-sizeX)/2;
        float insetZ = (16-sizeZ)/2;
        BLOCK_SHAPE = Block.box(insetX, 0, insetZ, 16-insetX, 16-sizeY, 16-insetZ);
        this.registerDefaultState(this.defaultBlockState().setValue(DIRECTION, Direction.NORTH));
    }

    @Override
    public void generateBlockState(RegistryObject<Block> blockRegistryObject, MetroBlockStateProvider blockStateProvider) {
        blockStateProvider.simpleBlockWithItem(blockRegistryObject.get());
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState blockstate = this.defaultBlockState().setValue(DIRECTION, context.getHorizontalDirection());
        return blockstate;
    }

    @Override
    protected void createBlockState(StateDefinition.Builder builder) {
        builder.add(DIRECTION);
    }
}
