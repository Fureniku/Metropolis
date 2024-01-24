package com.fureniku.metropolis.datagen;

import com.fureniku.metropolis.RegistrationBase;
import com.fureniku.metropolis.blocks.MetroBlockBase;
import com.fureniku.metropolis.client.rendering.MetroLoaderBuilder;
import com.fureniku.metropolis.utils.Debug;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;

/**
 * Metropolis Block State Provider. Should be usable as-is without needing to subclass.
 * The individual blocks must implement generateBlockState, or default to simpleBlockWithItem for basic blocks.
 */
public class MetroBlockStateProvider extends BlockStateProvider {

    private RegistrationBase _registrationBase;
    /**
     * Constructor - for use in <code>generate(GatherDataEvent)</code> of your datagen class
     * @param output event.getGenerator().getPackOutput()
     * @param modid your modID (you can probably just pass this in your own super)
     * @param fileHelper event.getExistingFileHelper()
     * @param registrationBase your mods registration instance (so we can get the blocklist etc)
     */
    public MetroBlockStateProvider(PackOutput output, String modid, ExistingFileHelper fileHelper, RegistrationBase registrationBase) {
        super(output, modid, fileHelper);
        _registrationBase = registrationBase;
    }

    /**
     * Get a full-size block, with a matching itemblock. Uses your blocks name to get the matching texture.
     * @param block Your block
     */
    public void simpleBlockWithItem(Block block) {
        simpleBlockWithItem(block, cubeAll(block));
    }

    /**
     * A block which can rotate horizontally
     * @param block Your block
     * @param model The base model file (assumed facing north)
     */
    public void horizontalBlock(Block block, ModelFile model) {
        horizontalBlock(block, model, 180);
        simpleBlockItem(block, model);
    }

    /*public void horizontalFloorWallBlock(Block block, ModelFile model) {
        getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(model)
                        .rotationX(state.getValue(MetroBlockDecorativeRotatableFloorWall.DIRECTION).toXRot())
                        .rotationY(state.getValue(MetroBlockDecorativeRotatableFloorWall.DIRECTION).toYRot() + 180)
                        .build());
    }*/ //TODO

    /**
     * Get a modelled block, with a matching itemblock. Uses your blocks name to get the matching texture.
     * @param block Your block
     */
    public void blockWithItem(Block block, ResourceLocation loc) {
        simpleBlockWithItem(block, models().withExistingParent(name(block), loc));
    }

    /**
     * Get a block which connects on four horizontal sides with an idetical connecting model, and a central post (e.g. vanilla walls/fences)
     * @param block The block
     * @param post The central part, always rendered
     * @param side The connecting side, modelled for the NORTH side. Automatically rotated to the other three sides.
     */
    public void horizontalConnectingBlock(Block block, ModelFile post, ModelFile side) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block).part().modelFile(post).addModel().end();
        fourWayMultipart(builder, side);
    }

    public void customRenderBlockTest(Block block) {
        BlockModelBuilder model = models().getBuilder(name(block)).parent(models().getExistingFile(mcLoc("cube")))
                .customLoader((builder, helper) -> new MetroLoaderBuilder(modLoc(name(block)), builder, helper)).end();
        simpleBlockWithItem(block, model);
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
     * Get a list of RegistryObject{@literal <Block>} for the mod's blocks.
     * You want to return <code>registration.getBlockArray().values()</code> from your mods RegistrationBase instance.
     * @return all the blocks in the mod
     */
    protected Collection<RegistryObject<Block>> getBlocks() {
        return _registrationBase.getBlockArray().values();
    }

    public BlockModelBuilder prepareModels(Block block, String modelDir, String modelName, TextureSet[] resources) {
        return prepareModels(block, "", modelDir, modelName, resources);
    }

    public BlockModelBuilder prepareModels(Block block, String nameSuffix, String modelDir, String modelName, TextureSet[] resources) {
        BlockModelBuilder bmb;
        if (modelName == null || resources == null) {
            bmb = getModelFilesWithTexture(block, nameSuffix, modelDir + name(block), modLoc(modelDir + name(block)));
        } else {
            bmb = applyTexturesToModel(resources, getModelFilesWithTexture(block, nameSuffix, modelDir + modelName, resources[0].getTexture()));
            if (resources.length > 1) {
                for (int i = 1; i < resources.length; i++) {
                    bmb = bmb.texture(resources[i].getKey(), resources[i].getTexture());
                }
            }
        }
        return bmb;
    }

    public BlockModelBuilder applyTexturesToModel(TextureSet[] resources, BlockModelBuilder bmb) {
        if (resources.length > 1) {
            for (int i = 1; i < resources.length; i++) {
                bmb = bmb.texture(resources[i].getKey(), resources[i].getTexture());
            }
        }
        return bmb;
    }

    public BlockModelBuilder[] applyTexturesToModels(TextureSet[] resources, BlockModelBuilder... bmb) {
        if (resources.length > 1) {
            for (int i = 0; i < bmb.length; i++) {
                for (int j = 1; j < resources.length; j++) {
                    bmb[i] = bmb[i].texture(resources[j].getKey(), resources[j].getTexture());
                }
            }
        }
        return bmb;
    }

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