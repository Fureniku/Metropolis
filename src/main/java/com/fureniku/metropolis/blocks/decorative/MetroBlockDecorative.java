package com.fureniku.metropolis.blocks.decorative;

import com.fureniku.metropolis.blocks.MetroBlockBase;
import com.fureniku.metropolis.blocks.decorative.builders.MetroBlockDecorativeBuilder;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.BlockOffsetDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.registries.RegistryObject;

import java.util.Optional;

public class MetroBlockDecorative extends MetroBlockBase {

    private final VoxelShape BLOCK_SHAPE;
    protected TextureSet[] _resources;
    protected String _modelName;
    private BlockOffsetDirection _offsetDirection = BlockOffsetDirection.NONE;

    public MetroBlockDecorative(Properties props, VoxelShape shape, String modelName, BlockOffsetDirection offsetDirection, TextureSet... textures) {
        super(offsetDirection == BlockOffsetDirection.NONE ? props : props.dynamicShape());
        BLOCK_SHAPE = shape;
        _resources = textures;
        _modelName = modelName;
        if (offsetDirection != BlockOffsetDirection.NONE) {
            _offsetDirection = offsetDirection;
        }
    }

    public MetroBlockDecorative(MetroBlockDecorativeBuilder builder) {
        this(builder.getProps(), builder.getShape(), builder.getModelName(), builder.getOffsetDirection(), builder.getTextures());
    }

    @Override
    protected Vec3 getOffset(BlockState blockState, BlockGetter level, BlockPos pos) {
        double x = 0;
        double y = 0;
        double z = 0;
        switch (_offsetDirection) {
            case NONE -> {
                return Vec3.ZERO;
            }
            case DOWN -> {
                y = getOffsetBlockPosValue(level, pos.below(), Direction.Axis.Y, true);
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
                y = getOffsetBlockPosValue(level, pos.above(), Direction.Axis.Y, false);
            }
        }
        double finalX = x;
        double finalY = y;
        double finalZ = z;
        blockState.offsetFunction = Optional.of((state, lvl, blockPos) -> new Vec3(finalX, finalY, finalZ));
        return new Vec3(x, y, z);
    }

    private double getOffsetBlockPosValue(BlockGetter level, BlockPos pos, Direction.Axis axis, boolean positive) {
        if (level.getBlockState(pos).getBlock() == Blocks.AIR) {
            return 0;
        }
        VoxelShape shape = level.getBlockState(pos).getShape(level, pos);
        return positive ? (1 - shape.max(axis)) * -1 : shape.min(axis);
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

        blockStateProvider.simpleBlockWithItem(block, applyTexturesToModels(bmb)[0]);
    }

    protected BlockModelBuilder applyTexturesToModel(BlockModelBuilder bmb) {
        if (_resources.length > 1) {
            for (int i = 1; i < _resources.length; i++) {
                bmb = bmb.texture(_resources[i].getKey(), _resources[i].getTexture());
            }
        }
        return bmb;
    }

    protected BlockModelBuilder[] applyTexturesToModels(BlockModelBuilder... bmb) {
        if (_resources.length > 1) {
            for (int i = 0; i < bmb.length; i++) {
                for (int j = 1; j < _resources.length; j++) {
                    bmb[i] = bmb[i].texture(_resources[j].getKey(), _resources[j].getTexture());
                }
            }
        }
        return bmb;
    }
}
