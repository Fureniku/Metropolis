package com.fureniku.metropolis.blocks;

import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.registries.RegistryObject;

public class MetroBlockDecorative extends MetroBlockBase {

    private final VoxelShape BLOCK_SHAPE;
    protected TextureSet[] _resources;
    protected String _modelName;

    public MetroBlockDecorative(Properties props, String modelName, ResourceLocation resource) {
        this(props, 16, 16, modelName, new TextureSet("texture", resource));
    }

    public MetroBlockDecorative(Properties props, float height, String modelName, ResourceLocation resource) {
        this(props, height, 16, modelName, new TextureSet("texture", resource));
    }

    public MetroBlockDecorative(Properties props, float height, float width, String modelName, ResourceLocation resource) {
        this(props, height, width, modelName, new TextureSet("texture", resource));
    }

    public MetroBlockDecorative(Properties props, VoxelShape shape, String modelName, ResourceLocation resource) {
        this(props, shape, modelName, new TextureSet("texture", resource));
    }

    public MetroBlockDecorative(Properties props, String modelName, TextureSet... textures) {
        this(props, 16, 16, modelName, textures);
    }

    public MetroBlockDecorative(Properties props, float height, String modelName, TextureSet... textures) {
        this(props, height, 16, modelName, textures);
    }

    public MetroBlockDecorative(Properties props, float height, float width, String modelName, TextureSet... textures) {
        super(props);
        float inset = (16-width)/2;
        BLOCK_SHAPE = Block.box(inset, 0, inset, 16-inset, height, 16-inset);
        _resources = textures;
        _modelName = modelName;
    }

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
