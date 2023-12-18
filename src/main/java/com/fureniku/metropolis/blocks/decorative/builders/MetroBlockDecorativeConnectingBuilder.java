package com.fureniku.metropolis.blocks.decorative.builders;

import com.fureniku.metropolis.blocks.decorative.MetroBlockConnectingHorizontal;
import com.fureniku.metropolis.blocks.decorative.MetroBlockDecorative;
import com.fureniku.metropolis.enums.DecorativeBuilderType;
import com.fureniku.metropolis.utils.Debug;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class MetroBlockDecorativeConnectingBuilder extends MetroBlockDecorativeBuilder<MetroBlockDecorativeConnectingBuilder> {

    protected boolean _checkUp = false;
    protected boolean _checkDown = false;

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

    public boolean getCheckUp() { return _checkUp; }
    public boolean getCheckDown() { return _checkDown; }

    public MetroBlockDecorative build() {
        switch (_type) {
            case DECORATIVE_CONNECT_HORIZONTAL:
                return new MetroBlockConnectingHorizontal(_props, _blockShape, _modelDir, _modelName, _offsetDirection, _checkUp, _checkDown, _textures);
        }
        return super.build();
    }
}
