package com.fureniku.metropolis.test;

import com.fureniku.metropolis.Metropolis;
import com.fureniku.metropolis.RegistrationBase;
import com.fureniku.metropolis.blocks.MetroBlockBase;
import com.fureniku.metropolis.client.rendering.MetroModelLoader;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import com.fureniku.metropolis.utils.CreativeTabSet;
import com.fureniku.metropolis.utils.Debug;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
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

    private final String TEST_BLOCK = "testblock";
    private CreativeTabSet _testTab;

    public RegistrationTest(String modid, IEventBus modEventBus) {
        super(modid, modEventBus);
    }

    @Override
    public void init(IEventBus modEventBus) {
        registerBlockSet(TEST_BLOCK, () -> new TestBlock());
        _testTab = new CreativeTabSet(getCreativeTabDeferredRegister(), "tab_test", getItem(TEST_BLOCK));
    }

    @Override
    public void generateCreativeTabs() {
        _testTab.addItem(getItem(TEST_BLOCK).get().getDefaultInstance());
    }

    @Override
    protected ArrayList<CreativeTabSet> getCreativeTabs() {
        ArrayList<CreativeTabSet> tabList = new ArrayList<>();
        tabList.add(_testTab);
        return tabList;
    }

    @Override
    protected void commonSetup(FMLCommonSetupEvent event) {

    }

    @Override
    protected void clientSetup(FMLClientSetupEvent event) {

    }

    @Override
    protected void modelSetup(ModelEvent.RegisterGeometryLoaders event) {
        event.register(TEST_BLOCK, new MetroModelLoader());
    }

    @Override
    protected void modifyBakingResult(ModelEvent.ModifyBakingResult event) {

    }

    @Override
    protected void bakingComplete(ModelEvent.BakingCompleted event) {

    }

    @Override
    protected void dataGen(GatherDataEvent event, DataGenerator gen, PackOutput packOutput, ExistingFileHelper efh) {
        gen.addProvider(event.includeClient(), new MetroBlockStateProvider(packOutput, Metropolis.MODID, efh, Metropolis.INSTANCE.registrationTest));
    }
}
