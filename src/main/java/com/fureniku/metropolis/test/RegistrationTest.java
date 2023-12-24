package com.fureniku.metropolis.test;

import com.fureniku.metropolis.Metropolis;
import com.fureniku.metropolis.RegistrationBase;
import com.fureniku.metropolis.blocks.MetroBlockBase;
import com.fureniku.metropolis.blocks.decorative.MetroBlockDecorative;
import com.fureniku.metropolis.blocks.decorative.builders.MetroBlockDecorativeBuilder;
import com.fureniku.metropolis.blocks.decorative.helpers.ConnectHorizontalHelper;
import com.fureniku.metropolis.blocks.decorative.helpers.HelperBase;
import com.fureniku.metropolis.blocks.decorative.helpers.OffsetHelper;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.enums.BlockConnectionType;
import com.fureniku.metropolis.enums.DecorativeBuilderType;
import com.fureniku.metropolis.utils.CreativeTabSet;
import com.fureniku.metropolis.utils.ShapeUtils;
import com.fureniku.metropolis.utils.SimpleUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

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

    @Override
    public void init(IEventBus modEventBus) {
        VoxelShape[] shapeA = ShapeUtils.makeShapes(4f, 4f);
        VoxelShape[] shapeB = ShapeUtils.makeShapes(2.5f, 4f, 16f);
        VoxelShape[] shapes = ShapeUtils.combineMultiShapes(shapeA, shapeB);

        //MetroBlockDecorativeBuilder connectingBuilder = new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1);
        //ConnectHorizontalHelper.Builder connectHelperBuilder = new ConnectHorizontalHelper.Builder().setShapes(shapes);

        ArrayList<HelperBase> helperList = new ArrayList<HelperBase>();
        helperList.add(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.SAME).build());
        MetroBlockDecorative.MetroBlockStateFactory factory =  MetroBlockDecorative.getBlockFactory(helperList);

        blockNames.add(registerBlockSet("test_connecting_enum_same", () -> factory.makeBlock(_props, shapes[0], "blocks/decorative/", "barrier_concrete_middle", "", SimpleUtils.containsType(helperList, OffsetHelper.class), new TextureSet("texture", TEST_TEXTURE_1))));//new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1).setHelpers(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.SAME).build()).build()));
        //blockNames.add(registerBlockSet("test_connecting_enum_same", () -> new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1).setHelpers(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.SAME).build()).build()));
        //blockNames.add(registerBlockSet("test_connecting_enum_sameclass_a", () -> new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1).setHelpers(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.SAMECLASS).build()).build())); //Not really a valid test
        //blockNames.add(registerBlockSet("test_connecting_enum_sameclass_b", () -> new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1).setHelpers(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.SAMECLASS).build()).build())); //Not really a valid test
        //blockNames.add(registerBlockSet("test_connecting_enum_tag_a", () -> new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1).setHelpers(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.TAG).build()).setTag("tag_a").build()));
        //blockNames.add(registerBlockSet("test_connecting_enum_tag_b", () -> new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1).setHelpers(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.TAG).build()).setTag("tag_b").build()));
        //blockNames.add(registerBlockSet("test_connecting_enum_connecting", () -> new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1).setHelpers(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.CONNECTING).build()).build()));
        //blockNames.add(registerBlockSet("test_connecting_enum_metro", () -> new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1).setHelpers(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.METRO).build()).build()));
        //blockNames.add(registerBlockSet("test_connecting_enum_solid", () -> new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1).setHelpers(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.SOLID).build()).build()));
        //blockNames.add(registerBlockSet("test_connecting_enum_solid_same", () -> new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1).setHelpers(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.SOLID_SAME).build()).build()));
        //blockNames.add(registerBlockSet("test_connecting_enum_solid_sameclass_a", () -> new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1).setHelpers(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.SOLID_SAMECLASS).build()).build())); //Not really a valid test
        //blockNames.add(registerBlockSet("test_connecting_enum_solid_sameclass_b", () -> new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1).setHelpers(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.SOLID_SAMECLASS).build()).build())); //Not really a valid test
        //blockNames.add(registerBlockSet("test_connecting_enum_solid_tag_a", () -> new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1).setHelpers(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.SOLID_TAG).build()).setTag("tag_a").build()));
        //blockNames.add(registerBlockSet("test_connecting_enum_solid_tag_b", () -> new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1).setHelpers(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.SOLID_TAG).build()).setTag("tag_b").build()));
        //blockNames.add(registerBlockSet("test_connecting_enum_solid_conecting", () -> new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1).setHelpers(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.SOLID_CONNECTING).build()).build()));
        //blockNames.add(registerBlockSet("test_connecting_enum_solid_metro", () -> new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1).setHelpers(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.SOLID_METRO).build()).build()));
        //blockNames.add(registerBlockSet("test_connecting_enum_all", () -> new MetroBlockDecorativeBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setModelName("barrier_concrete_middle").setModelDirectory("blocks/decorative/").setTextures(TEST_TEXTURE_1).setHelpers(new ConnectHorizontalHelper.Builder().setShapes(shapes).setConnectionType(BlockConnectionType.ALL).build()).build()));

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
