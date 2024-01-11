package com.fureniku.metropolis.blocks.decorative.builders;

import com.fureniku.metropolis.blocks.decorative.*;
import com.fureniku.metropolis.blocks.decorative.helpers.ConnectHorizontalHelper;
import com.fureniku.metropolis.blocks.decorative.helpers.HelperBase;
import com.fureniku.metropolis.blocks.decorative.helpers.OffsetHelper;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.BlockConnectionType;
import com.fureniku.metropolis.utils.SimpleUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.Arrays;

public class MetroBlockDecorativeBuilder<T extends MetroBlockDecorativeBase> {

    protected BlockBehaviour.Properties _props;
    protected VoxelShape _blockShape = Block.box(0, 0, 0, 16, 16, 16);
    protected String _modelDir = "blocks/";
    protected String _modelName = null;
    protected String _tag = "untagged";
    protected TextureSet[] _textures = null;
    protected ArrayList<HelperBase> _helpers = new ArrayList<>();

    public MetroBlockDecorativeBuilder(BlockBehaviour.Properties props) {
        _props = props;
    }

    public MetroBlockDecorativeBuilder(MetroBlockDecorativeBuilder partial) {
        _props = partial._props;
        _blockShape = partial._blockShape;
        _modelDir = partial._modelDir;
        _modelName = partial._modelName;
        _tag = partial._tag;
        _textures = partial._textures;
        _helpers = partial._helpers;
    }

    //region Normal shapes
    public MetroBlockDecorativeBuilder setHeight(float height) { return setShape(16, height, 16); }
    public MetroBlockDecorativeBuilder setWidth(float width) { return setShape(width, 16, width); }
    public MetroBlockDecorativeBuilder setShape(float width, float height) { return setShape(width, height, width); }

    public MetroBlockDecorativeBuilder setShape(float width, float height, float depth) {
        float insetX = (16-width)/2;
        float insetZ = (16-depth)/2;
        return setShape(Block.box(insetX, 0, insetZ, 16-insetX, height, 16-insetZ));
    }

    public MetroBlockDecorativeBuilder setShape(VoxelShape shape) {
        _blockShape = shape;
        return this;
    }
    //endregion

    public MetroBlockDecorativeBuilder setModelDirectory(String modelDir) {
        _modelDir = modelDir;
        return this;
    }

    public MetroBlockDecorativeBuilder setModelName(String modelName) {
        _modelName = modelName;
        return this;
    }

    public MetroBlockDecorativeBuilder setTag(String tag) {
        _tag = tag;
        return this;
    }

    public MetroBlockDecorativeBuilder setTextures(TextureSet... textures) {
        _textures = textures;
        return this;
    }

    public MetroBlockDecorativeBuilder setTextures(ResourceLocation resource) {
        return setTextures(new TextureSet("texture", resource));
    }

    public MetroBlockDecorativeBuilder setHelpers(HelperBase... helpers) {
        _helpers.addAll(Arrays.asList(helpers));
        return this;
    }

    public MetroBlockDecorativeBuilder addHelper(HelperBase helper) {
        _helpers.add(helper);
        return this;
    }

    public MetroBlockDecorativeBuilder setConnectHorizontalHelper(BlockConnectionType connectionType, VoxelShape[] shapes) {
        _helpers.add(new ConnectHorizontalHelper.Builder(connectionType, shapes).build());
        return this;
    }

    //TODO
    public MetroBlockDecorativeBuilder setConnectFullHelper() {
        return this;
    }

    public MetroBlockDecorativeBuilder get() { return this; }
    public BlockBehaviour.Properties getProps() { return _props; }
    public VoxelShape getShape() { return _blockShape; }
    public String getModelDirectory() { return _modelDir; }
    public String getModelName() { return _modelName; }
    public String getTag() { return _tag; }
    public TextureSet[] getTextures() { return _textures; }
    public ArrayList<HelperBase> getHelpers() { return _helpers; }

    /**
     * Generic build function. Creates a block instance of whatever object was specified - or MetroBlockDecorativeBase otherwise.
     * @return The created block instance
     */
    public T build() {
        HelperBase[] helpersArray = _helpers.toArray(new HelperBase[0]);
        MetroBlockDecorativeBase.MetroBlockStateFactory<T> factory = MetroBlockDecorativeBase.getBlockFactory(helpersArray);
        return factory.makeBlock(_props, _blockShape, _modelDir, _modelName, _tag, SimpleUtils.containsType(OffsetHelper.class, helpersArray), _textures);
    }
}