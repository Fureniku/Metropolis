package com.fureniku.metropolis.blocks.decorative.builders;

import com.fureniku.metropolis.blocks.decorative.*;
import com.fureniku.metropolis.enums.DecorativeBuilderType;
import com.fureniku.metropolis.enums.ToggleType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MetroBlockDecorativeToggleBuilder extends MetroBlockDecorativeBuilder<MetroBlockDecorativeToggleBuilder> {

    protected VoxelShape _toggledBlockShape = Block.box(0, 0, 0, 16, 16, 16);
    protected String _toggledModelName = null;
    protected Item _toggleItem;
    protected ToggleType _toggleType;

    public MetroBlockDecorativeToggleBuilder(BlockBehaviour.Properties props) {
        super(props);
    }

    public MetroBlockDecorativeToggleBuilder(BlockBehaviour.Properties props, DecorativeBuilderType type) {
        super(props, type);
    }

    //region Toggled shapes
    public MetroBlockDecorativeToggleBuilder setToggledHeight(float height) { return setToggledShape(16, height, 16); }
    public MetroBlockDecorativeToggleBuilder setToggledWidth(float width) { return setToggledShape(width, 16, width); }
    public MetroBlockDecorativeToggleBuilder setToggledShape(float width, float height) { return setToggledShape(width, height, width); }

    public MetroBlockDecorativeToggleBuilder setToggledShape(float width, float height, float depth) {
        float insetX = (16-width)/2;
        float insetZ = (16-depth)/2;
        return setToggledShape(Block.box(insetX, 0, insetZ, 16-insetX, height, 16-insetZ));
    }

    public MetroBlockDecorativeToggleBuilder setToggledShape(VoxelShape shape) {
        _toggledBlockShape = shape;
        return this;
    }
    //endregion

    public MetroBlockDecorativeToggleBuilder setToggleModelName(String modelName) {
        _toggledModelName = modelName;
        return this;
    }

    public MetroBlockDecorativeToggleBuilder setModelNames(String modelName, String toggledName) {
        _modelName = modelName;
        _toggledModelName = toggledName;
        return this;
    }

    public MetroBlockDecorativeToggleBuilder setToggleItem(Item toggleItem) {
        _toggleItem = toggleItem;
        return this;
    }

    public MetroBlockDecorativeToggleBuilder setToggleType(ToggleType toggleType) {
        _toggleType = toggleType;
        return this;
    }

    public VoxelShape getShapeToggled() { return _toggledBlockShape; }
    public String getToggledModelName() { return _toggledModelName; }
    public ToggleType getToggleType() { return _toggleType; }
    public Item getToggleItem() { return _toggleItem; }

    @Override
    public MetroBlockDecorative build() {
        switch (_type) {
            case DECORATIVE_TOGGLE:
                if (_toggleItem != null) {
                    return new MetroBlockDecorativeToggle(_props, _blockShape, _toggledBlockShape, _modelName, _toggledModelName, _toggleItem, _offsetDirection, _textures);
                }
                return new MetroBlockDecorativeToggle(_props, _blockShape, _toggledBlockShape, _modelName, _toggledModelName, _toggleType, _offsetDirection, _textures);
            case DECORATIVE_ROTATABLE_TOGGLE:
                if (_toggleItem != null) {
                    return new MetroBlockDecorativeRotatableToggle(_props, _blockShape, _toggledBlockShape, _modelName, _toggledModelName, _toggleItem, _offsetDirection, _textures);
                }
                return new MetroBlockDecorativeRotatableToggle(_props, _blockShape, _toggledBlockShape, _modelName, _toggledModelName, _toggleType, _offsetDirection, _textures);
            default:
                return super.build();
        }
    }
}
