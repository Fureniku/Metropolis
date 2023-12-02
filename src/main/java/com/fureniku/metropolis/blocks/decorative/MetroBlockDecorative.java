package com.fureniku.metropolis.blocks.decorative;

import com.fureniku.metropolis.blocks.MetroBlockBase;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.registries.RegistryObject;

public class MetroBlockDecorative extends MetroBlockBase {

    private final VoxelShape BLOCK_SHAPE;
    protected TextureSet[] _resources;
    protected String _modelName;

    public MetroBlockDecorative(Properties props, VoxelShape shape, String modelName, TextureSet... textures) {
        super(props);
        BLOCK_SHAPE = shape;
        _resources = textures;
        _modelName = modelName;
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
}
