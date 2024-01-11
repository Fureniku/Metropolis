package com.fureniku.metropolis.test;

import com.fureniku.metropolis.blocks.decorative.MetroEntityBlockDecorative;
import com.fureniku.metropolis.datagen.TextureSet;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class MetroEntityBlockChildTest extends MetroEntityBlockDecorative {

    public MetroEntityBlockChildTest(Properties props, VoxelShape shape, String modelDir, String modelName, String tag, boolean dynamicShape, TextureSet... textures) {
        super(props, shape, modelDir, modelName, tag, dynamicShape, textures);
    }
}
