package com.fureniku.metropolis.test;

import com.fureniku.metropolis.Metropolis;
import com.fureniku.metropolis.RegistrationBase;
import com.fureniku.metropolis.blocks.MetroBlockBase;
import com.fureniku.metropolis.blocks.decorative.builders.MetroBlockDecorativeConnectingBuilder;
import com.fureniku.metropolis.client.rendering.MetroModelLoader;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.enums.BlockConnectionType;
import com.fureniku.metropolis.enums.DecorativeBuilderType;
import com.fureniku.metropolis.utils.CreativeTabSet;
import com.fureniku.metropolis.utils.Debug;
import com.fureniku.metropolis.utils.ShapeUtils;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.geometry.GeometryLoaderManager;
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
        blockNames.add(registerBlockSet("testblock", () -> new MetroBlockBase(_props)));

        VoxelShape[] shapeA = ShapeUtils.makeShapes(4f, 4f);
        VoxelShape[] shapeB = ShapeUtils.makeShapes(2.5f, 4f, 16f);

        MetroBlockDecorativeConnectingBuilder connectingBuilder = new MetroBlockDecorativeConnectingBuilder(_props, DecorativeBuilderType.DECORATIVE_CONNECT_HORIZONTAL).setShapes(ShapeUtils.combineMultiShapes(shapeA, shapeB)).setModelName("barrier_concrete_middle");//.setTextures(TEST_TEXTURE_1);

        blockNames.add(registerBlockSet("test_connecting_enum_same", () -> connectingBuilder.setConnectionType(BlockConnectionType.SAME).build()));
        blockNames.add(registerBlockSet("test_connecting_enum_sameclass_a", () -> connectingBuilder.setConnectionType(BlockConnectionType.SAMECLASS).build())); //Not really a valid test
        blockNames.add(registerBlockSet("test_connecting_enum_sameclass_b", () -> connectingBuilder.setConnectionType(BlockConnectionType.SAMECLASS).build())); //Not really a valid test
        blockNames.add(registerBlockSet("test_connecting_enum_tag_a", () -> connectingBuilder.setConnectionType(BlockConnectionType.TAG).setTag("tag_a").build()));
        blockNames.add(registerBlockSet("test_connecting_enum_tag_b", () -> connectingBuilder.setConnectionType(BlockConnectionType.TAG).setTag("tag_b").build()));
        blockNames.add(registerBlockSet("test_connecting_enum_connecting", () -> connectingBuilder.setConnectionType(BlockConnectionType.CONNECTING).build()));
        blockNames.add(registerBlockSet("test_connecting_enum_metro", () -> connectingBuilder.setConnectionType(BlockConnectionType.METRO).build()));
        blockNames.add(registerBlockSet("test_connecting_enum_solid", () -> connectingBuilder.setConnectionType(BlockConnectionType.SOLID).build()));
        blockNames.add(registerBlockSet("test_connecting_enum_solid_same", () -> connectingBuilder.setConnectionType(BlockConnectionType.SOLID_SAME).build()));
        blockNames.add(registerBlockSet("test_connecting_enum_solid_sameclass_a", () -> connectingBuilder.setConnectionType(BlockConnectionType.SOLID_SAMECLASS).build())); //Not really a valid test
        blockNames.add(registerBlockSet("test_connecting_enum_solid_sameclass_b", () -> connectingBuilder.setConnectionType(BlockConnectionType.SOLID_SAMECLASS).build())); //Not really a valid test
        blockNames.add(registerBlockSet("test_connecting_enum_solid_tag_a", () -> connectingBuilder.setConnectionType(BlockConnectionType.SOLID_TAG).setTag("tag_a").build()));
        blockNames.add(registerBlockSet("test_connecting_enum_solid_tag_b", () -> connectingBuilder.setConnectionType(BlockConnectionType.SOLID_TAG).setTag("tag_b").build()));
        blockNames.add(registerBlockSet("test_connecting_enum_solid_conecting", () -> connectingBuilder.setConnectionType(BlockConnectionType.SOLID_CONNECTING).build()));
        blockNames.add(registerBlockSet("test_connecting_enum_solid_metro", () -> connectingBuilder.setConnectionType(BlockConnectionType.SOLID_METRO).build()));
        blockNames.add(registerBlockSet("test_connecting_enum_all", () -> connectingBuilder.setConnectionType(BlockConnectionType.ALL).build()));

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
