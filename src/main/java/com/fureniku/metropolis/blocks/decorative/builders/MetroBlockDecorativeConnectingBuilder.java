package com.fureniku.metropolis.blocks.decorative.builders;

import com.fureniku.metropolis.blocks.decorative.MetroBlockConnectingHorizontal;
import com.fureniku.metropolis.blocks.decorative.MetroBlockConnectingHorizontalToggle;
import com.fureniku.metropolis.blocks.decorative.MetroBlockDecorative;
import com.fureniku.metropolis.enums.BlockConnectionType;
import com.fureniku.metropolis.enums.DecorativeBuilderType;
import com.fureniku.metropolis.enums.ToggleType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MetroBlockDecorativeConnectingBuilder extends MetroBlockDecorativeBuilder<MetroBlockDecorativeConnectingBuilder> {

    protected boolean _checkUp = false;
    protected boolean _checkDown = false;

    protected VoxelShape[] _shapes;
    protected String _tag = "untagged";
    protected BlockConnectionType _connectionType = BlockConnectionType.CONNECTING;
    protected VoxelShape[] _toggledShapes;

    protected VoxelShape _toggledBlockShape = Block.box(0, 0, 0, 16, 16, 16);
    protected String _toggledModelName = null;
    protected String _connectedModelName = null;
    protected String _connectedToggledModelName = null;
    protected String _itemModelName = null;
    protected boolean _centerFourSided = false;
    protected boolean _centerFourSidedToggled = false;
    protected boolean _hideBaseOnToggle = false;
    protected boolean _independentModelsPerSide = false;
    protected Item _toggleItem;
    protected ToggleType _toggleType = ToggleType.INTERACT;

    public MetroBlockDecorativeConnectingBuilder(BlockBehaviour.Properties props) {
        super(props);
    }

    public MetroBlockDecorativeConnectingBuilder(BlockBehaviour.Properties props, DecorativeBuilderType type) {
        super(props, type);
    }

    public MetroBlockDecorativeConnectingBuilder setCheckUp() {
        _checkUp = true;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setCheckDown() {
        _checkDown = true;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setCheck() {
        _checkUp = true;
        _checkDown = true;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setCenterFourSided() {
        _centerFourSided = true;
        _centerFourSidedToggled = true;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setHideBaseOnToggled() {
        _hideBaseOnToggle = true;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setCenterFourSided(boolean standard, boolean toggled) {
        _centerFourSided = standard;
        _centerFourSidedToggled = toggled;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setIndependentModelsPerSide() {
        _independentModelsPerSide = true;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setShapes(VoxelShape[] shapes) {
        _shapes = shapes;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setTag(String tag) {
        _tag = tag;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setConnectionType(BlockConnectionType connectionType) {
        _connectionType = connectionType;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setToggledShapes(VoxelShape[] shapes) {
        _toggledShapes = shapes;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setToggleModelName(String modelName) {
        _toggledModelName = modelName;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setConnectedModelName(String modelName) {
        _connectedModelName = modelName;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setConnectedToggledModelName(String modelName) {
        _connectedToggledModelName = modelName;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setItemModelName(String modelName) {
        _itemModelName = modelName;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setModelNames(String modelName, String toggledName) {
        _modelName = modelName;
        _toggledModelName = toggledName;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setConnectedModelNames(String modelName, String toggledName) {
        _connectedModelName = modelName;
        _connectedToggledModelName = toggledName;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setNoToggledConnectionNames(String modelName, String connectionName, String toggledName) {
        _modelName = modelName;
        _connectedModelName = connectionName;
        _toggledModelName = toggledName;
        _connectedToggledModelName = connectionName;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setAllModelNames(String modelName, String toggledName) {
        _modelName = modelName;
        _connectedModelName = modelName + "_connection";
        _toggledModelName = toggledName;
        _connectedToggledModelName = toggledName + "_connection";
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setAllModelNames(String modelName, String toggledName, String itemModel) {
        _modelName = modelName;
        _connectedModelName = modelName + "_connection";
        _toggledModelName = toggledName;
        _connectedToggledModelName = toggledName + "_connection";
        _itemModelName = itemModel;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setAllModelNames(String modelName, String connectedModelName, String toggledName, String connectedToggledModelName) {
        _modelName = modelName;
        _connectedModelName = connectedModelName;
        _toggledModelName = toggledName;
        _connectedToggledModelName = connectedToggledModelName;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setAllModelNames(String modelName, String connectedModelName, String toggledName, String connectedToggledModelName, String itemModel) {
        _modelName = modelName;
        _connectedModelName = connectedModelName;
        _toggledModelName = toggledName;
        _connectedToggledModelName = connectedToggledModelName;
        _itemModelName = itemModel;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setToggleItem(Item toggleItem) {
        _toggleItem = toggleItem;
        return this;
    }

    public MetroBlockDecorativeConnectingBuilder setToggleType(ToggleType toggleType) {
        _toggleType = toggleType;
        return this;
    }

    public VoxelShape getShapeToggled() { return _toggledBlockShape; }
    public String getToggledModelName() { return _toggledModelName; }
    public String getConnectedModelName() { return _connectedModelName; }
    public String getConnectedToggledModelName() { return _connectedToggledModelName; }
    public String getItemModelName() { return _itemModelName; }
    public ToggleType getToggleType() { return _toggleType; }
    public Item getToggleItem() { return _toggleItem; }
    public boolean getCenterFourSided() { return _centerFourSided; }
    public boolean getCenterFourSidedToggled() { return _centerFourSidedToggled; }
    public boolean getHideBaseOnToggled() { return _hideBaseOnToggle; }
    public boolean getCheckUp() { return _checkUp; }
    public boolean getCheckDown() { return _checkDown; }
    public boolean getIndependentModelsPerSide() { return _independentModelsPerSide; }
    public VoxelShape[] getShapes() { return _shapes; }
    public String getTag() { return _tag; }
    public BlockConnectionType getConnectionType() { return _connectionType; }

    public MetroBlockDecorative build() {
        if (_connectedModelName == null) {
            _connectedModelName = _modelName + "_connection";
        }
        if (_connectedToggledModelName == null) {
            _connectedToggledModelName = _toggledModelName + "_connection";
        }

        if (_itemModelName == null) {
            _itemModelName = _modelName;
        }

        switch (_type) {
            case DECORATIVE_CONNECT_HORIZONTAL:
                return new MetroBlockConnectingHorizontal(_props, _modelName, _connectedModelName, _itemModelName, _offsetDirection, _checkUp, _checkDown, _centerFourSided, _independentModelsPerSide, _shapes, _tag, _connectionType, _textures);


            case DECORATIVE_CONNECT_HORIZONTAL_TOGGLE:
                if (_toggleItem != null) {
                    return new MetroBlockConnectingHorizontalToggle(_props, _modelName, _connectedModelName, _toggledModelName, _connectedToggledModelName, _itemModelName,
                            _offsetDirection, _checkUp, _checkDown, _centerFourSided, _independentModelsPerSide, _centerFourSidedToggled, _hideBaseOnToggle, _shapes, _tag, _connectionType, _toggleItem, _textures);
                }
                return new MetroBlockConnectingHorizontalToggle(_props, _modelName, _connectedModelName, _toggledModelName, _connectedToggledModelName, _itemModelName,
                        _offsetDirection, _checkUp, _checkDown, _centerFourSided, _independentModelsPerSide, _centerFourSidedToggled, _hideBaseOnToggle, _shapes, _tag, _connectionType, _toggleType, _textures);
        }
        return super.build();
    }
}
