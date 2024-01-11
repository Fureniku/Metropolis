package com.fureniku.metropolis.test;

import com.fureniku.metropolis.Metropolis;
import com.fureniku.metropolis.RegistrationBase;
import com.fureniku.metropolis.blockentity.MetroBlockEntity;
import com.fureniku.metropolis.blocks.decorative.MetroEntityBlockDecorative;
import com.fureniku.metropolis.blocks.decorative.builders.MetroBlockDecorativeBuilder;
import com.fureniku.metropolis.blocks.decorative.helpers.ConnectHorizontalHelper;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.enums.BlockConnectionType;
import com.fureniku.metropolis.utils.CreativeTabSet;
import com.fureniku.metropolis.utils.ShapeUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.RegistryObject;

import java.util.ArrayList;

/**
 * This class/package is used for testing and debugging Metropolis stuff. Use it for examples, but don't extend from it.
 */
public class RegistrationTest extends RegistrationBase {

    private CreativeTabSet _testTab;

    private ArrayList<String> blockNames = new ArrayList<>();

    public RegistrationTest(String modid, IEventBus modEventBus) {
        super(modid, modEventBus);
    }

    BlockBehaviour.Properties _props = BlockBehaviour.Properties.of().strength(1.0f).sound(SoundType.STONE);
    ResourceLocation TEST_TEXTURE_1 = new ResourceLocation(Metropolis.MODID, "block/test_texture_1");
    ResourceLocation TEST_TEXTURE_2 = new ResourceLocation(Metropolis.MODID, "block/test_texture_2");

    public RegistryObject<BlockEntityType<MetroBlockEntity>> TEST_BLOCK_ENTITY_DECORATIVE_ENTITY;

    @Override
    public void init(IEventBus modEventBus) {
        VoxelShape[] shapeA = ShapeUtils.makeShapes(4f, 4f);
        VoxelShape[] shapeB = ShapeUtils.makeShapes(2.5f, 4f, 16f);
        VoxelShape[] shapes = ShapeUtils.combineMultiShapes(shapeA, shapeB);

        //TODO re-test partials (not currently working)
        MetroBlockDecorativeBuilder partial = new MetroBlockDecorativeBuilder(_props).setModelDirectory("blocks/decorative/");

        blockNames.add(registerBlockSet("test_connecting_enum_same", () ->
                new MetroBlockDecorativeBuilder(_props)
                        .setModelDirectory("blocks/decorative/")
                        .setModelName("barrier_concrete_middle")
                        .setTextures(TEST_TEXTURE_1)
                        .setConnectHorizontalHelper(BlockConnectionType.SAME, shapes)
                        .build()));
        blockNames.add(registerBlockSet("test_connecting_enum_sameclass_a", () ->
                new MetroBlockDecorativeBuilder(_props)
                        .setModelDirectory("blocks/decorative/")
                        .setModelName("barrier_concrete_middle")
                        .setTextures(TEST_TEXTURE_1)
                        .setConnectHorizontalHelper(BlockConnectionType.SAMECLASS, shapes)
                        .build())); //Not really a valid test
        blockNames.add(registerBlockSet("test_connecting_enum_sameclass_b", () ->
                new MetroBlockDecorativeBuilder(_props)
                        .setModelDirectory("blocks/decorative/")
                        .setModelName("barrier_concrete_middle")
                        .setTextures(TEST_TEXTURE_1)
                        .setConnectHorizontalHelper(BlockConnectionType.SAMECLASS, shapes)
                        .build())); //Not really a valid test
        blockNames.add(registerBlockSet("test_connecting_enum_tag_a", () ->
                new MetroBlockDecorativeBuilder(_props)
                        .setModelDirectory("blocks/decorative/")
                        .setModelName("barrier_concrete_middle")
                        .setTextures(TEST_TEXTURE_1)
                        .setConnectHorizontalHelper(BlockConnectionType.TAG, shapes)
                        .setTag("tag_a")
                        .build()));
        blockNames.add(registerBlockSet("test_connecting_enum_tag_b", () ->
                new MetroBlockDecorativeBuilder(_props)
                        .setModelDirectory("blocks/decorative/")
                        .setModelName("barrier_concrete_middle")
                        .setTextures(TEST_TEXTURE_1)
                        .setConnectHorizontalHelper(BlockConnectionType.TAG, shapes)
                        .setTag("tag_b")
                        .build()));
        blockNames.add(registerBlockSet("test_connecting_enum_connecting", () ->
                new MetroBlockDecorativeBuilder(_props)
                        .setModelDirectory("blocks/decorative/")
                        .setModelName("barrier_concrete_middle")
                        .setTextures(TEST_TEXTURE_1)
                        .setConnectHorizontalHelper(BlockConnectionType.CONNECTING, shapes)
                        .build()));
        blockNames.add(registerBlockSet("test_connecting_enum_metro", () ->
                new MetroBlockDecorativeBuilder(_props)
                        .setModelDirectory("blocks/decorative/")
                        .setModelName("barrier_concrete_middle")
                        .setTextures(TEST_TEXTURE_1)
                        .setConnectHorizontalHelper(BlockConnectionType.METRO, shapes)
                        .build()));
        blockNames.add(registerBlockSet("test_connecting_enum_no_solid_same", () ->
                new MetroBlockDecorativeBuilder(_props)
                        .setModelDirectory("blocks/decorative/")
                        .setModelName("barrier_concrete_middle")
                        .setTextures(TEST_TEXTURE_1)
                        .setHelpers(new ConnectHorizontalHelper.Builder(BlockConnectionType.SAME, shapes).setDontConnectSolid().build())
                        .build()));
        blockNames.add(registerBlockSet("test_connecting_enum_no_solid_sameclass_a", () ->
                new MetroBlockDecorativeBuilder(_props)
                        .setModelDirectory("blocks/decorative/")
                        .setModelName("barrier_concrete_middle")
                        .setTextures(TEST_TEXTURE_1)
                        .setHelpers(new ConnectHorizontalHelper.Builder(BlockConnectionType.SAMECLASS, shapes).setDontConnectSolid().build())
                        .build())); //Not really a valid test
        blockNames.add(registerBlockSet("test_connecting_enum_no_solid_sameclass_b", () ->
                new MetroBlockDecorativeBuilder(_props)
                        .setModelDirectory("blocks/decorative/")
                        .setModelName("barrier_concrete_middle")
                        .setTextures(TEST_TEXTURE_1)
                        .setHelpers(new ConnectHorizontalHelper.Builder(BlockConnectionType.SAMECLASS, shapes).setDontConnectSolid().build())
                        .build())); //Not really a valid test
        blockNames.add(registerBlockSet("test_connecting_enum_no_solid_tag_a", () ->
                new MetroBlockDecorativeBuilder(_props)
                        .setModelDirectory("blocks/decorative/")
                        .setModelName("barrier_concrete_middle")
                        .setTextures(TEST_TEXTURE_1)
                        .setHelpers(new ConnectHorizontalHelper.Builder(BlockConnectionType.TAG, shapes).setDontConnectSolid().build())
                        .setTag("tag_a")
                        .build()));
        blockNames.add(registerBlockSet("test_connecting_enum_no_solid_tag_b", () ->
                new MetroBlockDecorativeBuilder(_props)
                        .setModelDirectory("blocks/decorative/")
                        .setModelName("barrier_concrete_middle")
                        .setTextures(TEST_TEXTURE_1)
                        .setHelpers(new ConnectHorizontalHelper.Builder(BlockConnectionType.TAG, shapes).setDontConnectSolid().build())
                        .setTag("tag_b")
                        .build()));
        blockNames.add(registerBlockSet("test_connecting_enum_no_solid_conecting", () ->
                new MetroBlockDecorativeBuilder(_props)
                        .setModelDirectory("blocks/decorative/")
                        .setModelName("barrier_concrete_middle")
                        .setTextures(TEST_TEXTURE_1)
                        .setHelpers(new ConnectHorizontalHelper.Builder(BlockConnectionType.CONNECTING, shapes).setDontConnectSolid().build())
                        .build()));
        blockNames.add(registerBlockSet("test_connecting_enum_no_solid_metro", () ->
                new MetroBlockDecorativeBuilder(_props)
                        .setModelDirectory("blocks/decorative/")
                        .setModelName("barrier_concrete_middle")
                        .setTextures(TEST_TEXTURE_1)
                        .setHelpers(new ConnectHorizontalHelper.Builder(BlockConnectionType.METRO, shapes).setDontConnectSolid().build())
                        .build()));
        blockNames.add(registerBlockSet("test_connecting_enum_all", () ->
                new MetroBlockDecorativeBuilder<MetroEntityBlockDecorative>(_props)
                        .setModelDirectory("blocks/decorative/")
                        .setModelName("barrier_concrete_middle")
                        .setTextures(TEST_TEXTURE_1)
                        .setConnectHorizontalHelper(BlockConnectionType.ALL, shapes)
                        .build()));


        blockNames.add("test_block_entity_decorative");
        /*RegistryObject<Block> blockEntity = retrieveRegisterBlockSet("test_block_entity_decorative", () ->
                new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE)
                        .setModelDirectory("blocks/decorative/")
                        .setModelName("barrier_concrete_middle")
                        .setTextures(TEST_TEXTURE_1)
                        .buildEntity());*/

        TEST_BLOCK_ENTITY_DECORATIVE_ENTITY = registerBlockEntityWithBlock("test_block_entity_decorative", () ->
                new MetroBlockDecorativeBuilder(_props)
                        .setModelDirectory("blocks/decorative/")
                        .setModelName("barrier_concrete_middle")
                        .setTextures(TEST_TEXTURE_1)
                        .setConnectHorizontalHelper(BlockConnectionType.ALL, shapes)
                        .build(), MetroBlockEntity::new);//Metropolis.registrationTest.getBlock("test_block_entity_decorative").get())

        _testTab = new CreativeTabSet(getCreativeTabDeferredRegister(), "tab_test", getItem(blockNames.get(0)));
    }

    @Override
    public void generateCreativeTabs() {
        for (int i = 0; i < blockNames.size(); i++) {
            _testTab.addItem(getItem(blockNames.get(i)).get().getDefaultInstance());
        }
    }

    @Override
    protected ArrayList<CreativeTabSet> getCreativeTabs() {
        ArrayList<CreativeTabSet> tabList = new ArrayList<>();
        tabList.add(_testTab);
        return tabList;
    }

    @Override
    protected void commonSetup(FMLCommonSetupEvent event) {}

    @Override
    protected void clientSetup(FMLClientSetupEvent event) {}

    @Override
    protected void modelSetup(ModelEvent.RegisterGeometryLoaders event) {}

    @Override
    protected void modifyBakingResult(ModelEvent.ModifyBakingResult event) {}

    @Override
    protected void bakingComplete(ModelEvent.BakingCompleted event) {}

    @Override
    protected void dataGen(GatherDataEvent event, DataGenerator gen, PackOutput packOutput, ExistingFileHelper efh) {
        gen.addProvider(event.includeClient(), new MetroBlockStateProvider(packOutput, Metropolis.MODID, efh, Metropolis.INSTANCE.registrationTest));
    }
}
