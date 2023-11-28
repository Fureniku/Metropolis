package com.fureniku.metropolis.datagen;

import com.fureniku.metropolis.blocks.MetroBlockBase;
import com.fureniku.metropolis.utils.Debug;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

import java.util.Collection;
import java.util.function.Function;

/**
 * Metropolis Block State Provider
 * Inherited by subclasses. Subclasses must provide a Collection of block RegistryObjects which should generate.
 * The individual blocks must also implement generateBlockState, or default to simpleBlockWithItem for basic blocks.
 */
public abstract class MetroBlockStateProvider extends BlockStateProvider {

    /**
     * Constructor - for use in <code>generate(GatherDataEvent)</code> of your datagen class
     * @param output event.getGenerator().getPackOutput()
     * @param modid your modID (you can probably just pass this in your own super)
     * @param fileHelper event.getExistingFileHelper()
     */
    public MetroBlockStateProvider(PackOutput output, String modid, ExistingFileHelper fileHelper) {
        super(output, modid, fileHelper);
    }

    /**
     * Get a full-size block, with a matching itemblock. Uses your blocks name to get the matching texture.
     * @param block Your block
     */
    public void simpleBlockWithItem(Block block) {
        simpleBlockWithItem(block, cubeAll(block));
    }

    public void horizontalBlock(Block block, ModelFile model) {
        horizontalBlock(block, model, 180);
    }

    /**
     * Get a modelled block, with a matching itemblock. Uses your blocks name to get the matching texture.
     * @param block Your block
     */
    public void blockWithItem(Block block, ResourceLocation loc) {
        simpleBlockWithItem(block, models().withExistingParent(name(block), loc));
    }

    /**
     * Get a model for a block with a single texture.
     * @param block your block
     * @param nameSuffix the suffix that should be appended to this block's modelname
     * @param parentModel the location of the model to parent from
     * @return the BlockModelBuilder for your block
     */
    public BlockModelBuilder getModelFiles(Block block, String nameSuffix, String parentModel) {
        return models().singleTexture(name(block) + nameSuffix, modLoc(parentModel), modLoc("block/" + name(block)));
    }

    /**
     * Get a model for a block, with a specific texture
     * @param block your block
     * @param nameSuffix the suffix that should be appended to this block's modelname
     * @param parentModel the location of the model to parent from
     * @param textureName The texture name - this will be in the textures/block/ folder. Subfolders are allowed!
     * @return the BlockModelBuilder for your block
     */
    public BlockModelBuilder getTexturedModelFiles(Block block, String nameSuffix, String parentModel, String textureName) {
        return models().singleTexture(name(block) + nameSuffix, modLoc(parentModel), modLoc("block/" + textureName));
    }

    /**
     * Get a model for a block, with a specific texture
     * @param block your block
     * @param nameSuffix the suffix that should be appended to this block's modelname
     * @param parentModel the location of the model to parent from
     * @param texture The full resource location of the texture to use (allowing for textures from other mods/vanilla)
     * @return the BlockModelBuilder for your block
     */
    public BlockModelBuilder getModelFilesWithTexture(Block block, String nameSuffix, String parentModel, ResourceLocation texture) {
        return models().singleTexture(name(block) + nameSuffix, modLoc(parentModel), texture);
    }

    /**
     * Get a model for the item of a block with a single texture.
     * @param block your block
     * @param nameSuffix the suffix that should be appended to this block's modelname
     * @param parentModel the location of the model to parent from
     * @return the ItemModelBuilder for your itemblock
     */
    public ItemModelBuilder getItemBlockModelFiles(Block block, String nameSuffix, String parentModel) {
        return itemModels().singleTexture(name(block) + nameSuffix, modLoc(parentModel), modLoc("block/" + name(block)));
    }

    /**
     * Get a list of RegistryObject<Block> for the mod's blocks.
     * You want to return <code>registration.getBlockArray().values()</code> from your mods RegistrationBase instance.
     * @return all the blocks in the mod
     */
    protected abstract Collection<RegistryObject<Block>> getBlocks();

    /**
     * Iterate through all the blocks registered in your mod, and generate their blockstates.
     * Generating blockstates are handled by the blocks, in their own block classes. Where it makes sense to do it.
     */
    @Override
    protected void registerStatesAndModels() {
        Collection<RegistryObject<Block>> blockValues = getBlocks();
        for (RegistryObject<Block> block : blockValues) {
            if (block.get() instanceof MetroBlockBase) {
                MetroBlockBase metroBlock = (MetroBlockBase) block.get();
                metroBlock.generateBlockState(block, this);
            } else {
                Debug.LogError(block.get().getName() + " was not a MetroBlockBase. This is an error!");
            }
        }
    }

    //Why are these private in the parent class? They're useful. FREE THE NAME FUNCTIONS.
    protected ResourceLocation key(Block block) { return ForgeRegistries.BLOCKS.getKey(block); }
    protected String name(Block block) { return key(block).getPath(); }
}