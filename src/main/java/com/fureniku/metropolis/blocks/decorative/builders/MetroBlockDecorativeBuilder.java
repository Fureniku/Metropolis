package com.fureniku.metropolis.blocks.decorative.builders;

import com.fureniku.metropolis.blocks.decorative.*;
import com.fureniku.metropolis.blocks.decorative.helpers.HelperBase;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.DecorativeBuilderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.Arrays;

public class MetroBlockDecorativeBuilder<T extends MetroBlockDecorativeBuilder<T>> {

    protected BlockBehaviour.Properties _props;
    protected VoxelShape _blockShape = Block.box(0, 0, 0, 16, 16, 16);
    protected String _modelDir = "blocks/";
    protected String _modelName = null;
    protected String _tag = "untagged";
    protected TextureSet[] _textures = null;
    protected DecorativeBuilderType _type;
    protected ArrayList<HelperBase> _helpers = new ArrayList<>();

    public MetroBlockDecorativeBuilder(BlockBehaviour.Properties props) {
        this(props, DecorativeBuilderType.DECORATIVE);
    }

    public MetroBlockDecorativeBuilder(BlockBehaviour.Properties props, DecorativeBuilderType type) {
        _props = props;
        _type = type;
    }

    //region Normal shapes
    public T setHeight(float height) { return setShape(16, height, 16); }
    public T setWidth(float width) { return setShape(width, 16, width); }
    public T setShape(float width, float height) { return setShape(width, height, width); }

    public T setShape(float width, float height, float depth) {
        float insetX = (16-width)/2;
        float insetZ = (16-depth)/2;
        return setShape(Block.box(insetX, 0, insetZ, 16-insetX, height, 16-insetZ));
    }

    public T setShape(VoxelShape shape) {
        _blockShape = shape;
        return (T) this;
    }
    //endregion

    public T setModelDirectory(String modelDir) {
        _modelDir = modelDir;
        return (T) this;
    }

    public T setModelName(String modelName) {
        _modelName = modelName;
        return (T) this;
    }

    public T setTag(String tag) {
        _tag = tag;
        return (T) this;
    }

    public T setTextures(TextureSet... textures) {
        _textures = textures;
        return (T) this;
    }

    public T setTextures(ResourceLocation resource) {
        return setTextures(new TextureSet("texture", resource));
    }

    public T setHelpers(HelperBase... helpers) {
        _helpers.addAll(Arrays.asList(helpers));
        return (T) this;
    }

    public T get() { return (T) this; }
    public BlockBehaviour.Properties getProps() { return _props; }
    public VoxelShape getShape() { return _blockShape; }
    public String getModelDirectory() { return _modelDir; }
    public String getModelName() { return _modelName; }
    public String getTag() { return _tag; }
    public TextureSet[] getTextures() { return _textures; }
    public ArrayList<HelperBase> getHelpers() { return _helpers; }

    /**
     * Internal level build function. Fine to use for any blocks which can just use the metro base classes directly.
     * If you are making a derived block, create the builder instance as a separate object and pass it through the super
     * @return The created block instance
     */
    public MetroBlockDecorative build() {
        return new MetroBlockDecorative(_props, _blockShape, _modelDir, _modelName, _tag, _helpers, _textures);
    }
}