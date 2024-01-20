package com.fureniku.metropolis.blocks.decorative;

import com.fureniku.metropolis.blockentity.MetroBlockEntity;
import com.fureniku.metropolis.blocks.IToggleable;
import com.fureniku.metropolis.blocks.decorative.builders.MetroBlockDecorativeBuilder;
import com.fureniku.metropolis.blocks.decorative.helpers.HelperBase;
import com.fureniku.metropolis.blocks.decorative.helpers.OffsetHelper;
import com.fureniku.metropolis.datagen.TextureSet;
import com.fureniku.metropolis.utils.Debug;
import com.fureniku.metropolis.utils.SimpleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class MetroEntityBlockDecorative extends MetroBlockDecorativeBase implements IToggleable, EntityBlock {

    public MetroEntityBlockDecorative(Properties props, VoxelShape shape, String modelDir, String modelName, String tag, boolean dynamicShape, TextureSet... textures) {
        super(props, shape, modelDir, modelName, tag, dynamicShape, textures);
    }

    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        Debug.LogError("createBlockEntity must be overriden in EntityBlock class");
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return createBlockEntity(pos, state);
    }

    //Boilerplate function which every overriding class will need to use for construction because java's generics suck
    public static MetroBlockStateFactory getBlockFactory(HelperBase... helpersIn) {
        return (props, shape, modelDir, modelName, tag, dynamicShape, textures) -> new MetroEntityBlockDecorative(props, shape, modelDir, modelName, tag, SimpleUtils.containsType(OffsetHelper.class, helpersIn), textures) {
            @Override
            public ArrayList<HelperBase> getHelpers() {
                return new ArrayList<>(Arrays.asList(helpersIn));
            }
        };
    }
}