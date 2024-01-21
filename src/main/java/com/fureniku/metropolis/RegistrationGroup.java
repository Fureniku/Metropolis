package com.fureniku.metropolis;

import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.utils.CreativeTabSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegistryObject;

import java.util.ArrayList;
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
     * @return (Optional) The passed-in name, to add to a list for creative registration etc
     */
    protected String registerBlockSet(String name, Supplier<Block> blockClass) {
        registration.registerBlockSet(name, blockClass);
        return name;
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

    /**
     * Get a resource location for the named resource. Override to provide subfolders, else it defaults to getLocFull.
     * @param name the name of the resource you're getting in the location.
     * @return resource location
     */
    protected ResourceLocation getLoc(String name) {
        return getLocFull(name);
    }

    /**
     * Get a resource location for the named resource. Always returns the contextual root (blocks/, models/ etc).
     * Override getLoc if you consistently want a subfolder.
     * @param name the name of the resource you're getting in the location.
     * @return resource location
     */
    protected final ResourceLocation getLocFull(String name) {
        return new ResourceLocation(registration.modid, name);
    }

    /**
     * Get a TextureSet for the texture name at the named resource locaation.
     * Calls getLoc(), so will use defined subfolders.
     * @param name The name of the texture for a face, passed to the model json
     * @param loc The name of the texture file (without png)
     * @return TextureSet
     */
    protected TextureSet texture(String name, String loc) {
        return texture(name, getLoc(loc));
    }

    /**
     * Get a TextureSet for the texture name at the named resource locaation.
     * @param name The name of the texture for a face, passed to the model json
     * @param loc ResourceLocation for the texture file
     * @return TextureSet
     */
    protected TextureSet texture(String name, ResourceLocation loc) {
        return new TextureSet(name, loc);
    }
}
