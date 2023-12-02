package com.fureniku.metropolis.blocks.decorative;

import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.DecorativeBuilderType;
import com.fureniku.metropolis.enums.ToggleType;
import com.fureniku.metropolis.utils.Debug;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MetroBlockDecorativeBuilder {

    private BlockBehaviour.Properties _props;
    private VoxelShape _blockShape = Block.box(0, 0, 0, 16, 16, 16);
    private VoxelShape _toggledBlockShape = Block.box(0, 0, 0, 16, 16, 16);
    private String _modelName = null;
    private String _toggledModelName = null;
    private TextureSet[] _textures = null;
    private DecorativeBuilderType _type;
    private Item _toggleItem;
    private ToggleType _toggleType;

    public MetroBlockDecorativeBuilder(BlockBehaviour.Properties props) {
        this(props, DecorativeBuilderType.DECORATIVE);
    }

    public MetroBlockDecorativeBuilder(BlockBehaviour.Properties props, DecorativeBuilderType type) {
        _props = props;
        _type = type;
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

    //region Toggled shapes
    public MetroBlockDecorativeBuilder setToggledHeight(float height) { return setToggledShape(16, height, 16); }
    public MetroBlockDecorativeBuilder setToggledWidth(float width) { return setToggledShape(width, 16, width); }
    public MetroBlockDecorativeBuilder setToggledShape(float width, float height) { return setToggledShape(width, height, width); }

    public MetroBlockDecorativeBuilder setToggledShape(float width, float height, float depth) {
        float insetX = (16-width)/2;
        float insetZ = (16-depth)/2;
        return setToggledShape(Block.box(insetX, 0, insetZ, 16-insetX, height, 16-insetZ));
    }

    public MetroBlockDecorativeBuilder setToggledShape(VoxelShape shape) {
        _toggledBlockShape = shape;
        return this;
    }
    //endregion

    public MetroBlockDecorativeBuilder setModelName(String modelName) {
        _modelName = modelName;
        return this;
    }

    public MetroBlockDecorativeBuilder setToggleModelName(String modelName) {
        _toggledModelName = modelName;
        return this;
    }

    public MetroBlockDecorativeBuilder setToggleItem(Item toggleItem) {
        _toggleItem = toggleItem;
        return this;
    }

    public MetroBlockDecorativeBuilder setToggleType(ToggleType toggleType) {
        _toggleType = toggleType;
        return this;
    }

    public MetroBlockDecorativeBuilder setTextures(TextureSet... textures) {
        _textures = textures;
        return this;
    }

    public MetroBlockDecorativeBuilder setTextures(ResourceLocation resource) {
        return setTextures(new TextureSet("texture", resource));
    }

    public MetroBlockDecorativeBuilder setLit() {
        _props = _props.lightLevel(p_50874_ -> 15).noOcclusion();
        return this;
    }

    public MetroBlockDecorative build() {
        switch (_type) {
            case DECORATIVE:
                return new MetroBlockDecorative(_props, _blockShape, _modelName, _textures);

            case DECORATIVE_TOGGLE:
                if (_toggleItem != null) {
                    return new MetroBlockDecorativeToggle(_props, _blockShape, _toggledBlockShape, _modelName, _toggledModelName, _toggleItem, _textures);
                }
                return new MetroBlockDecorativeToggle(_props, _blockShape, _toggledBlockShape, _modelName, _toggledModelName, _toggleType, _textures);

            case DECORATIVE_ROTATABLE:
                return new MetroBlockDecorativeRotatable(_props, _blockShape, _modelName, _textures);

            case DECORATIVE_ROTATABLE_TOGGLE:
                if (_toggleItem != null) {
                    return new MetroBlockDecorativeRotatableToggle(_props, _blockShape, _toggledBlockShape, _modelName, _toggledModelName, _toggleItem, _textures);
                }
                return new MetroBlockDecorativeRotatableToggle(_props, _blockShape, _toggledBlockShape, _modelName, _toggledModelName, _toggleType, _textures);
        }
        return new MetroBlockDecorative(_props, _blockShape, _modelName, _textures);
    }
}