package com.fureniku.metropolis.utils;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SimpleUtils {

    //This class was technically the start of Metropolis.
    //I got frustrated with how every time I update my mods, all the function names have changed.
    //This class is going to just be a general set of functions I commonly use - mainly in debug but sometimes for other things too.
    //Then next time minecraft/forge/MCP/whoever renames it all, I only have to change it here instead of 20 different places.

    /**
     * Get the player's currently held item in their main hand
     * @param player
     * @return
     */
    public static ItemStack getHeldItem(Player player) {
        return player.getItemInHand(InteractionHand.MAIN_HAND);
    }

    /**
     * Gets a printable name for an item
     * @param item the ItemStack to get the name of
     * @return a printable String of the item's name
     */
    public static String getItemName(ItemStack item) {
        return item.getDisplayName().getString();
    }

    /**
     * Gets a printable name for an item
     * @param item the Item or ItemLike to get the name of
     * @return a printable String of the item's name
     */
    public static String getItemName(ItemLike item) {
        return new ItemStack(item).getDisplayName().getString();
    }

    /**
     * Create an ItemStack with a specific damage value
     * @param item the item or ItemLike to create an ItemStack of
     * @param damage the damage value to use
     * @return the created ItemStack
     */
    public static ItemStack createDamagedItemStack(ItemLike item, int damage) {
        ItemStack stack = new ItemStack(item);
        stack.setDamageValue(damage);
        return stack;
    }
}
