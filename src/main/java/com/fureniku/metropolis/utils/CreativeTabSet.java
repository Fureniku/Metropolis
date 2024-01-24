package com.fureniku.metropolis.utils;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;

/**
 * Class to create a more traditional creative tab.
 * You can set an item for the icon, and register blocks/items to it later! No need to do it at construct time anymore.
 */
public class CreativeTabSet {

    private RegistryObject<CreativeModeTab> _creativeTab;
    private DeferredRegister<CreativeModeTab> _deferredRegistry;
    private String _name;
    private ArrayList<ItemStack> _items = new ArrayList<>();

    /**
     * Create the new creative tab
     * @param tab your tab registry. If you're using <code>RegistrationBase</code>, pass <code>creativeTabs</code>
     * @param name the unlocalized name for your tab
     * @param item the display item as a RegistryObject..
     */
    public CreativeTabSet(DeferredRegister<CreativeModeTab> tab, String name, RegistryObject<Item> item) {
        _deferredRegistry = tab;
        _name = name;
        _creativeTab = _deferredRegistry.register(_name, () -> CreativeModeTab.builder()
                .withTabsBefore(CreativeModeTabs.COMBAT)
                .icon(() -> item.get().getDefaultInstance())
                .build());
    }

    /**
     * Get the RegistryObject for this tab
     * @return the RegistryObject
     */
    public RegistryObject<CreativeModeTab> getTab() {
        return _creativeTab;
    }

    /**
     * Get a list of all the items registered to this tab.
     * Used by the event to register all items into the tab later.
     * @return
     */
    public ArrayList<ItemStack> getItems() {
        return _items;
    }

    /**
     * Adds a single item to the tab
     * @param stack the ItemStack to add
     */
    public void addItem(ItemStack stack) {
        _items.add(stack);
    }

    /**
     * Adds a series of items with different damage values to the tab
     * @param item the ItemLike of the base item
     * @param damages an array of all the damage values
     */
    public void addItems(ItemLike item, int[] damages) {
        for (int i = 0; i < damages.length; i++) {
            _items.add(SimpleUtils.createDamagedItemStack(item, damages[i]));
        }
    }
}
