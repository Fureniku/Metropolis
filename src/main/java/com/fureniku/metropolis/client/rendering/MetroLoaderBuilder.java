package com.fureniku.metropolis.client.rendering;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MetroLoaderBuilder extends CustomLoaderBuilder<BlockModelBuilder> {

    public MetroLoaderBuilder(ResourceLocation loaderId, BlockModelBuilder parent, ExistingFileHelper existingFileHelper) {
        super(loaderId, parent, existingFileHelper);
    }
}
