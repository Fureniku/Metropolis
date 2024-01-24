package com.fureniku.metropolis;

import com.fureniku.metropolis.blockentity.MetroBlockEntity;
import com.fureniku.metropolis.menus.MetroMenu;
import com.fureniku.metropolis.utils.CreativeTabSet;
import com.fureniku.metropolis.utils.Debug;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

/**
 * Class to handle all block and creative tab registration.
 * Will include other features as I need them
 */
public abstract class RegistrationBase {

    protected final DeferredRegister<Block> blockRegistry;
    protected final DeferredRegister<Item> itemRegistry;
    protected final DeferredRegister<CreativeModeTab> creativeTabs;
    protected final DeferredRegister<BlockEntityType<?>> blockEntityRegistry;
    protected final DeferredRegister<MenuType<?>> menuTypeRegistry;

    /**
     * Used by registration groups
     */
    public final String modid;

    private HashMap<String, RegistryObject<Block>> block_map = new HashMap<>();
    private HashMap<String, RegistryObject<Item>> item_map = new HashMap<>();

    /**
     * Constructor. This should be called from the mods init, with a public instance of registration always available from the mod instance.
     * @param modid the mod's ID
     * @param modEventBus the event bus
     */
    public RegistrationBase(String modid, IEventBus modEventBus) {
        this.modid = modid;
        blockRegistry = DeferredRegister.create(ForgeRegistries.BLOCKS, modid);
        itemRegistry = DeferredRegister.create(ForgeRegistries.ITEMS, modid);
        creativeTabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modid);
        blockEntityRegistry = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, modid);
        menuTypeRegistry = DeferredRegister.create(ForgeRegistries.MENU_TYPES, modid);
        modEventBus.addListener(this::common);
        modEventBus.addListener(this::client);
        modEventBus.addListener(this::buildCreativeTabs);
        modEventBus.addListener(this::modelInit);
        modEventBus.addListener(this::modelBakeComplete);
        modEventBus.addListener(this::modifyBake);
        modEventBus.addListener(this::generate);
        blockRegistry.register(modEventBus);
        itemRegistry.register(modEventBus);
        creativeTabs.register(modEventBus);
        blockEntityRegistry.register(modEventBus);
        menuTypeRegistry.register(modEventBus);
    }

    /**
     * Initialize the registration, and register all your stuff.
     * Handle the creation of blocks in here - either creating blocksets, or call registerBlockSet(name, BlockClass::new)
     * Handle the creation of items
     * Handle the creation of creative tabs (new creativetabset, pass creativeTabs as first arg)
     * Do anything else you want at this stage.
     * @param modEventBus
     */
    public abstract void init(IEventBus modEventBus);

    /**
     * Get the registryobject for a block by its name
     * @param key name of the block
     * @return the registryobject of the block
     */
    @Nullable
    public final RegistryObject<Block> getBlock(String key) {
        if (block_map.containsKey(key)) {
            return block_map.get(key);
        }

        Debug.LogError("Key [" + key + "] not found in block_map. Dump map info below.");
        Debug.Log(block_map.toString());
        return null;
    }

    /**
     * Get the registryobject for an item by its name
     * @param key name of the item
     * @return the registryobject of the item
     */
    @Nullable
    public final RegistryObject<Item> getItem(String key) {
        if (item_map.containsKey(key)) {
            return item_map.get(key);
        }

        Debug.LogError("Key [" + key + "] not found in item_map. Dump map info below.");
        Debug.Log(item_map.toString());
        return null;
    }

    /**
     * Get the hashmap of all registered block's registryobjects
     * @return HashMap of registryobjects
     */
    public final HashMap<String, RegistryObject<Block>> getBlockArray() {
        return block_map;
    }

    /**
     * Simple function to register a single block, with an itemblock.
     * The block can use any class and thus have pretty much any functionality. This should be used for the majority of blocks.
     * @param name The internal name for the block
     * @param blockClass a supplier for the block's class, for example <code>MetroBlockBase::new</code>
     * @return The passed name (to keep a list, if you want)
     */
    public String registerBlockSet(String name, Supplier<Block> blockClass) {
        retrieveRegisterBlockSet(name, blockClass);
        return name;
    }

    /**
     * Simple function to register a single block, with an itemblock.
     * The block can use any class and thus have pretty much any functionality. This should be used for the majority of blocks.
     * @param name The internal name for the block
     * @param blockClass a supplier for the block's class, for example <code>MetroBlockBase::new</code>
     * @return the created RegistryObject{@literal <Block>} for quick access
     */
    public RegistryObject<Block> retrieveRegisterBlockSet(String name, Supplier<Block> blockClass) {
        RegistryObject<Block> block = blockRegistry.register(name, blockClass);
        RegistryObject<Item> blockItem = itemRegistry.register(name, () -> new BlockItem(block.get(), new Item.Properties()));

        addBlock(name, block);
        addItem(name, blockItem);
        return block;
    }

    public <T extends MetroBlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> blockEntity, Block... validBlocks) {
        return blockEntityRegistry.register(name, () -> BlockEntityType.Builder.of(blockEntity, validBlocks).build(null));
    }

    public <T extends MetroBlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntityWithBlock(String name, Supplier<Block> blockClass, BlockEntityType.BlockEntitySupplier<T> blockEntity) {
        RegistryObject<Block> block = blockRegistry.register(name, blockClass);
        RegistryObject<Item> blockItem = itemRegistry.register(name, () -> new BlockItem(block.get(), new Item.Properties()));

        addBlock(name, block);
        addItem(name, blockItem);

        return blockEntityRegistry.register(name + "_entity", () -> BlockEntityType.Builder.of(blockEntity, block.get()).build(null));
    }

    public <T extends MetroMenu> RegistryObject<MenuType<T>> registerMenuType(String name, Supplier<MenuType<T>> menu) {
        return menuTypeRegistry.register(name, menu);
    }

    /**
     * Add all your blocks to the created creative tabs in here!
     * <p>It may be neater to create a function for each tab, and call those functions from here.</p>
     * <p>Tab registry is done using <code>yourTab.addItem(ItemStack)</code></p>
     * <p>A good example of achieving that here could be <code>yourTab.addItem(getItem("item_name").get().getDefaultInstance())</code></p>
     * <p>If you're using <code>BlockSet</code>s, you can instead call <code>blockSet.registerToCreativeTab(yourTab)</code> to add all blocks in the set to the tab.</p>
     */
    public abstract void generateCreativeTabs();

    /**
     * Create an ArrayList of all your creative tabs here.
     * @return an <code>ArrayList{@literal <CreativeTabSet>}</code> with all your creative tabs in
     */
    protected abstract ArrayList<CreativeTabSet> getCreativeTabs();

    /**
     * Builds all the creative tabs. You wont need to touch this if you've used the class properly.
     * @param event
     */
    @SubscribeEvent
    public void buildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        for (int i = 0; i < getCreativeTabs().size(); i++) {
            CreativeTabSet tab = getCreativeTabs().get(i);
            if (event.getTabKey() == tab.getTab().getKey()) {
                for (int j = 0; j < tab.getItems().size(); j++) {
                    event.accept(tab.getItems().get(j));
                }
            }
        }
    }

    /**
     * Common setup event. Handle setup stuff for client and server here.
     * @param event FMLCommonSetupEvent
     */
    protected abstract void commonSetup(final FMLCommonSetupEvent event);

    /**
     * Client setup event. Handle client-only setup here (e.g. rendering)
     * @param event FMLClientSetupEvent
     */
    protected abstract void clientSetup(final FMLClientSetupEvent event);

    /**
     * Model setup event. Register model overrides etc here. (Client only)
     * @param event ModelEvent.RegisterGeometryLoaders
     */
    protected abstract void modelSetup(ModelEvent.RegisterGeometryLoaders event);
    protected abstract void modifyBakingResult(ModelEvent.ModifyBakingResult event);
    protected abstract void bakingComplete(ModelEvent.BakingCompleted event);

    /**
     * Data generation event. Register data generations here.
     * @param event The parent event in case you need anything else from it
     * @param gen The data generator
     * @param packOutput Pack output
     * @param efh Existing file helper
     */
    protected abstract void dataGen(GatherDataEvent event, DataGenerator gen, PackOutput packOutput, ExistingFileHelper efh);

    /**
     * Getter for the block deferred register
     * @return
     */
    public DeferredRegister<Block> getBlockDeferredRegister() {
        return blockRegistry;
    }

    /**
     * Getter for the item deferred register
     * @return
     */
    public DeferredRegister<Item> getItemDeferredRegister() {
        return itemRegistry;
    }

    /**
     * Getter for the block entity deferred register
     * @return
     */
    public DeferredRegister<BlockEntityType<?>> getBlockEntityRegistry() {
        return blockEntityRegistry;
    }

    /**
     * Getter for the creative tab deferred register
     * @return
     */
    public DeferredRegister<CreativeModeTab> getCreativeTabDeferredRegister() {
        return creativeTabs;
    }

    //Setup generation and call the abstract function.
    private void generate(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper efh = event.getExistingFileHelper();
        dataGen(event, generator, packOutput, efh);
    }

    //Call anything we want on the common setup then offer it to end mods
    @SubscribeEvent
    protected void common(final FMLCommonSetupEvent event) {
        generateCreativeTabs();
        commonSetup(event);
    }

    //Call anything we want on the client setup then offer it to end mods
    @SubscribeEvent
    protected void client(final FMLClientSetupEvent event) {
        clientSetup(event);
    }

    //Call anything we want on the model setup then offer it to end mods
    @SubscribeEvent
    protected void modelInit(ModelEvent.RegisterGeometryLoaders event) {
        Debug.Log("EVENT CALL: Register Geometry Loaders");
        modelSetup(event);
    }

    @SubscribeEvent
    protected void modifyBake(ModelEvent.ModifyBakingResult event) {
        Debug.Log("EVENT CALL: Modify Baking Result");
        modifyBakingResult(event);
    }

    @SubscribeEvent
    protected void modelBakeComplete(ModelEvent.BakingCompleted event) {
        Debug.Log("EVENT CALL: Baking completed");
        bakingComplete(event);
    }

    //Add a block to the registry
    private void addBlock(String key, RegistryObject<Block> value) {
        block_map.put(key, value);
    }

    //Add an item to the registry
    private void addItem(String key, RegistryObject<Item> value) {
        item_map.put(key, value);
    }
}
