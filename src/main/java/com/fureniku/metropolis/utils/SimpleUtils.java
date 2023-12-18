package com.fureniku.metropolis.utils;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;

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

    /**
     * Rotate a VoxelShape horizontally to the new direction. Assumes the passed one is facing north,
     * so east is 90, south 180 and west 270 degrees.
     * @param shape Original shape
     * @param newDirection Target direction
     * @return New shape
     */
    /*public static VoxelShape rotateVoxelShape(VoxelShape shape, Direction newDirection) {
        if (newDirection != Direction.NORTH) {
            double xMin = shape.min(Direction.Axis.X);
            double xMax = 1 - shape.max(Direction.Axis.X);
            double yMin = shape.min(Direction.Axis.Y);
            double yMax = 1 - shape.max(Direction.Axis.Y);
            double zMin = shape.min(Direction.Axis.Z);
            double zMax = 1 - shape.max(Direction.Axis.Z);
            switch (newDirection) {
                case EAST:
                    return createNewShape(zMax, yMin, xMin, zMin, yMax, xMax);
                case SOUTH:
                    return createNewShape(xMax, yMin, zMax, xMin, yMax, zMin);
                case WEST:
                    return createNewShape(zMin, yMin, xMax, zMax, yMax, xMin);
            }
        }
        return shape;
    }

    private static VoxelShape createNewShape(double xMin, double yMin, double zMin, double xMax, double yMax, double zMax) {
        return Block.box(xMin * 16, yMin * 16, zMin * 16, 16 - (xMax * 16), 16 - (yMax * 16), 16 - (zMax * 16));
    }
    */ //TODO moved to shape utils?

    /**
     * Get a VoxelShape and convert it to a printable string with the min/max points of each axis.
     * @param shape VoxelShape
     * @return A string to print with the logger
     */
    public static String getPrintableShape(VoxelShape shape) {
        return "X: " + shape.min(Direction.Axis.X) + " - " + shape.max(Direction.Axis.X) +
            " Y: " + shape.min(Direction.Axis.Y) + " - " + shape.max(Direction.Axis.Y) +
            " Z: " + shape.min(Direction.Axis.Z) + " - " + shape.max(Direction.Axis.Z);
    }

    /**
     * Check if an arraylist contains an object which is an instance of a subclass of the list type.
     * @param list The arraylist
     * @param type the type to check
     * @return whether an instance of that object exists in the list
     * @param <T>
     */
    public static <T>boolean containsType(ArrayList<? extends T> list, Class<? extends T> type) {
        for (T obj : list) {
            if (type.isInstance(obj)) {
                return true;
            }
        }
        return false;
    }
}
