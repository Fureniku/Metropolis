package com.fureniku.metropolis.blocks;

import com.fureniku.metropolis.blocks.decorative.helpers.HelperBase;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Metropolis standard blocks
 * ALL blocks using Metropolis should derive from this, or they won't work with datagen and may break other things.
 * This abstracts all the forge calls into my own ones. In other words, when Forge updates and changes everything, changing this class SHOULD fix all my mods basic blocks.
 * Also provides some "events" and other base common functionality
 */
public abstract class MetroBlockBase extends Block {

    protected ArrayList<HelperBase> _helpers;

    private String _tag = "untagged";

    public MetroBlockBase(Properties props) {
        super(props);
    }

    /**
     * Get the state for block placement. Override when we need different blockstates on placement.
     * @param context BlockPlaceContext - has all the juicy info about the placement
     * @return the blockstate that should be used for this placement
     */
    protected BlockState getPlacementState(BlockPlaceContext context) {
        return this.defaultBlockState();
    }

    /**
     * Create the blockstate. Override whenever we have a custom blockstate available on a block.
     * @param builder
     */
    protected void createBlockState(StateDefinition.Builder<Block, BlockState> builder) {}

    /**
     * Get which shape should be used for the block. Override when blocks don't have a standard full cube.
     * @param blockState The current blockstate. Can be used to define shapes per blockstate, but not required.
     * @return a VoxelShape for the block (or its current state)
     */
    protected VoxelShape getShapeFromBlockState(BlockState blockState) { return Shapes.block(); }

    /**
     * Get an offset for the block which can be calculated from world positions.
     * Big example use case is most things that get placed onto Roads, which self-align downwards to appear on the road below.
     * @param blockState Blockstate
     * @param level BlockGetter acting as Level
     * @param pos BlockPos
     * @return Vector3 of the offset
     */
    protected Vec3 getOffset(BlockState blockState, BlockGetter level, BlockPos pos) {
        return Vec3.ZERO;
    }

    /**
     * Checks if the specified helper class exists in the list of helpers
     * @param helperClass A class of the type being searched for
     * @return True if found, false if not
     */
    public boolean hasHelper(Class<? extends HelperBase> helperClass) {
        for (int i = 0; i < _helpers.size(); i++) {
            if (_helpers.get(i).getClass() == helperClass) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the first entered helper of the defined class
     * @param helperClass A class of the type being searched for
     * @return The class if found, null if not.
     */
    @Nullable
    public HelperBase getHelper(Class<? extends HelperBase> helperClass) {
        for (int i = 0; i < _helpers.size(); i++) {
            if (_helpers.get(i).getClass() == helperClass) {
                return _helpers.get(i);
            }
        }
        return null;
    }

    protected void setTag(String tag) {
        _tag = tag;
    }

    public String getTag() {
        return _tag;
    }

    @Override
    public float getMaxHorizontalOffset() {
        return 0.25F;
    }

    public float getMaxVerticalOffset() {
        return 0.2F;
    }

    /**
     * Called when a block is right-clicked by a player
     * @param state The blockstate
     * @param level The current world
     * @param pos The position of the block
     * @param player The player who right-clicked it
     */
    protected InteractionResult onRightClick(BlockState state, Level level, BlockPos pos, Player player) { return InteractionResult.PASS; }

    /**
     * Called when a neighbouring block changes.
     * @param state The blockstate of this block
     * @param level The level
     * @param pos The position of this block
     * @param block The block instance of THE NEW block (Changed from Forge, which passed the OLD block in that space here - often air)
     * @param neighborPos The position of the block that changed
     */
    protected void onNeighbourChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos) {}

    /**
     * Use to update a block when needed.
     * @param level Level
     * @param pos Block position
     * @param state Blockstate
     */
    public void setBlock(Level level, BlockPos pos, BlockState state) {
        level.setBlockAndUpdate(pos, state);
    }

    /**
     * Called when a player interacts with a block.
     * @param state The blockstate
     * @param level The current world
     * @param pos The position of the block
     * @param player The player who right-clicked it
     * @param hand Which hand the player used to interact
     * @param result honestly no idea, its in the base function. Expected result maybe?
     * @return The result of the interaction (pass, fail etc)
     */
    protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) { return InteractionResult.PASS; }

    /**
     * Create the blockstate file for this block! Used by datagen. Override if the block is anything other than a normal, boring, full size block.
     * For blocks that you don't want to datagen, override this with no function body to skip.
     * @param blockRegistryObject the block's registryobject
     * @param blockStateProvider the blockstate provider class, for some helper functions.
     */
    public void generateBlockState(RegistryObject<Block> blockRegistryObject, MetroBlockStateProvider blockStateProvider) {
        blockStateProvider.simpleBlockWithItem(blockRegistryObject.get());
    }

    /**
     * Get a menu to open
     * @param state The blockstate
     * @param level The current level
     * @param pos The position of the block
     * @return The menu instance to open
     */
    public MenuProvider getMenu(BlockState state, Level level, BlockPos pos) {
        return null;
    }

    /*
     * FORGE START
     * Everything below here is a Minecraft/Forge function, which will call MY functions above.
     * My mods all use my functions. So on updates, I just change these, and everything is fixed... right?
     */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext pContext) {
        Vec3 offset = getOffset(state, level, pos);
        if (offset != Vec3.ZERO) {
            return getShapeFromBlockState(state).move(offset.x, offset.y, offset.z);
        }
        return getShapeFromBlockState(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getPlacementState(context);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (hand == InteractionHand.MAIN_HAND) {
            return onRightClick(state, level, pos, player);
        }
        return onUse(state, level, pos, player, hand, result);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        createBlockState(builder);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean p_55671_) {
        onNeighbourChanged(state, level, pos, state.getBlock(), neighborPos);
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return getMenu(state, level, pos);
    }
}
