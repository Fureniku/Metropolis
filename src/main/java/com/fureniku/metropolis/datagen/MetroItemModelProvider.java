package com.fureniku.metropolis.datagen;

import com.fureniku.metropolis.Metropolis;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.RegistryObject;

/**
 * This class is a stub. It's not been used or tested yet.
 */
public abstract class MetroItemModelProvider extends ItemModelProvider {

    public MetroItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    protected ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(Metropolis.MODID, "item/" + item.getId().getPath()));
    }
}
