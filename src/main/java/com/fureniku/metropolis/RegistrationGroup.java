package com.fureniku.metropolis;

import com.fureniku.metropolis.utils.CreativeTabSet;
import com.fureniku.metropolis.utils.Debug;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

/**
 * Class to keep RegistrationBase derivitive classes a bit cleaner. Group your blocks into these classes, if you want to.
 */
public abstract class RegistrationGroup {

    protected final RegistrationBase registration;

    /**
     * Constructor.
     * @param registrationBase the core registration class of your mod
     */
    public RegistrationGroup(RegistrationBase registrationBase) {
        registration = registrationBase;
    }

    /**
     * Initialize the registration, and register all your stuff. Cannot be called from the constructor!
     * Handle the creation of blocks in here - either creating blocksets, or call registerBlockSet(name, BlockClass::new)
     * Handle the creation of items
     * Handle the creation of creative tabs (new creativetabset, pass creativeTabs as first arg)
     * Do anything else you want at this stage.
     * @param modEventBus
     */
    public abstract void init(IEventBus modEventBus);

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
    public abstract ArrayList<CreativeTabSet> getCreativeTabs();

    /**
     * Passthrough to register a blockset
     * @param name blockset name
     * @param blockClass block's class
     */
    protected void registerBlockSet(String name, Supplier<Block> blockClass) {
        registration.registerBlockSet(name, blockClass);
    }

    /**
     * Passthrough to get a block
     * @param key block name
     * @return registry object of the block
     */
    protected RegistryObject<Block> getBlock(String key) {
        return registration.getBlock(key);
    }

    /**
     * Passthrough to get a item
     * @param key item name
     * @return registry object of the item
     */
    protected RegistryObject<Item> getItem(String key) {
        return registration.getItem(key);
    }
}
