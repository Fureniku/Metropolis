package com.fureniku.metropolis.blockentity;

import com.fureniku.metropolis.blocks.decorative.MetroBlockDecorative;
import com.fureniku.metropolis.blocks.decorative.helpers.HelperBase;
import com.fureniku.metropolis.datagen.TextureSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class MetroEntityBlockDecorative extends MetroBlockDecorative implements EntityBlock {

    Class<? extends MetroBlockEntity> _blockEntity;
    BlockEntityType<?> _blockEntityType;

    public MetroEntityBlockDecorative(Properties props, VoxelShape shape, String modelDir, String modelName, String tag, boolean dynamicShape, TextureSet... textures) {
        super(props, shape, modelDir, modelName, tag, dynamicShape, textures);
    }

    public void setBlockEntity(Class<? extends MetroBlockEntity> blockEntity, BlockEntityType<?> type) {
        _blockEntity = blockEntity;
        _blockEntityType = type;
    }

    @Nullable
    public MetroBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        try {
            return _blockEntity.getDeclaredConstructor(BlockEntityType.class, BlockPos.class, BlockState.class).newInstance(_blockEntityType, pos, state);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * FORGE START
     * Everything below here is a Minecraft/Forge function, which will call MY functions above.
     * My mods all use my functions. So on updates, I just change these, and everything is fixed... right?
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return createBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return EntityBlock.super.getTicker(p_153212_, p_153213_, p_153214_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> GameEventListener getListener(ServerLevel p_221121_, T p_221122_) {
        return EntityBlock.super.getListener(p_221121_, p_221122_);
    }
}
