package com.fureniku.metropolis.blocks.decorative;

import com.fureniku.metropolis.client.rendering.MetroModelLoader;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.BlockOffsetDirection;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Class for procedurally-generated blocks - blocks created on the mod launch without pre-existing files such as json and models.
 * This does require a model file to be passed in; but it can be overriden with a custom rendered model programmatically.
 * Primarily used in Roads for the custom paint system, but may be used by other mods too.
 */
public class MetroBlockGenerated extends MetroBlockDecorative {

    public MetroBlockGenerated(Properties props, VoxelShape shape, String modelDir, String modelName, BlockOffsetDirection offsetDirection, TextureSet... textures) {
        super(props, shape, modelDir, modelName, offsetDirection, textures);
    }

    public static void registerBlockStates() {

    }
}
