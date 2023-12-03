package com.fureniku.metropolis.blocks.decorative;

import com.fureniku.metropolis.blocks.MetroBlockBase;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.BlockOffsetDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.registries.RegistryObject;

public class MetroBlockDecorative extends MetroBlockBase {

    private final VoxelShape BLOCK_SHAPE;
    protected TextureSet[] _resources;
    protected String _modelName;
    private BlockOffsetDirection _offsetDirection = BlockOffsetDirection.NONE;

    public MetroBlockDecorative(Properties props, VoxelShape shape, String modelName, BlockOffsetDirection offsetDirection, TextureSet... textures) {
        super(props);
        BLOCK_SHAPE = shape;
        _resources = textures;
        _modelName = modelName;
        _offsetDirection = offsetDirection;
    }

    public MetroBlockDecorative(MetroBlockDecorativeBuilder builder) {
        this(builder.getProps(), builder.getShape(), builder.getModelName(), builder.getOffsetDirection(), builder.getTextures());
    }

    protected Vec3 getOffset(BlockState blockState, BlockGetter level, BlockPos pos) {
        double x = 0;
        double y = 0;
        double z = 0;
        switch (_offsetDirection) {
            case NONE -> {
                return Vec3.ZERO;
            }
            case DOWN -> {
                BlockPos offsetPos = pos.below();
                y = (1 - level.getBlockState(offsetPos).getShape(level, offsetPos).max(Direction.Axis.Y)) * -1;
            }
            case BACK -> {
            }
            case LEFT -> {
            }
            case FORWARD -> {
            }
            case RIGHT -> {
            }
            case UP -> {
                BlockPos offsetPos = pos.below();
                y = level.getBlockState(offsetPos).getShape(level, offsetPos).min(Direction.Axis.Y);
            }
        }
        return new Vec3(x, y, z);
    }

    @Override
    protected VoxelShape getShapeFromBlockState(BlockState pState) {
        return BLOCK_SHAPE;
    }

    @Override
    public void generateBlockState(RegistryObject<Block> blockRegistryObject, MetroBlockStateProvider blockStateProvider) {
        Block block = blockRegistryObject.get();
        BlockModelBuilder bmb = blockStateProvider.getModelFilesWithTexture(block, "", "blocks/decorative/" + _modelName, _resources[0].getTexture());
        if (_resources.length > 1) {
            for (int i = 1; i < _resources.length; i++) {
                bmb = bmb.texture(_resources[i].getKey(), _resources[i].getTexture());
            }
        }

        blockStateProvider.simpleBlockWithItem(block, bmb);
    }

    public Vec3 getOffset(BlockGetter p_60825_, BlockPos p_60826_) {
        return Vec3.ZERO;//this.offsetFunction.<Vec3>map(p_273089_ -> p_273089_.evaluate(this.asState(), p_60825_, p_60826_)).orElse(Vec3.ZERO);
    }
}
