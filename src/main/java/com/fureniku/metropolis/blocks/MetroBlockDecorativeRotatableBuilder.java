package com.fureniku.metropolis.blocks;

import com.fureniku.metropolis.datagen.TextureSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MetroBlockDecorativeRotatableBuilder {

    private BlockBehaviour.Properties props;
    private VoxelShape blockShape = Block.box(0, 0, 0, 16, 16, 16);
    private String modelName = null;
    private TextureSet[] textures = null;

    public MetroBlockDecorativeRotatableBuilder setProps(BlockBehaviour.Properties props) {
        this.props = props;
        return this;
    }

    public MetroBlockDecorativeRotatableBuilder setHeight(float height) {
        return setShape(16, height, 16);
    }

    public MetroBlockDecorativeRotatableBuilder setWidth(float width) {
        return setShape(width, 16, width);
    }

    public MetroBlockDecorativeRotatableBuilder setShape(float width, float height) {
        return setShape(width, height, width);
    }

    public MetroBlockDecorativeRotatableBuilder setShape(float width, float height, float depth) {
        float insetX = (16-width)/2;
        float insetZ = (16-depth)/2;
        return setShape(Block.box(insetX, 0, insetZ, 16-insetX, 16-height, 16-insetZ));
    }

    public MetroBlockDecorativeRotatableBuilder setShape(VoxelShape shape) {
        this.blockShape = shape;
        return this;
    }

    public MetroBlockDecorativeRotatableBuilder setModelName(String modelName) {
        this.modelName = modelName;
        return this;
    }

    public MetroBlockDecorativeRotatableBuilder setTextures(TextureSet... textures) {
        this.textures = textures;
        return this;
    }

    public MetroBlockDecorativeRotatableBuilder setTextures(ResourceLocation resource) {
        return setTextures(new TextureSet("texture", resource));
    }

    public MetroBlockDecorativeRotatable build() {
        return new MetroBlockDecorativeRotatable(props, blockShape, modelName, textures);
    }
}