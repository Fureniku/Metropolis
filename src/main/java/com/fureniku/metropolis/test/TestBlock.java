package com.fureniku.metropolis.test;

import com.fureniku.metropolis.blocks.MetroBlockBase;
import com.fureniku.metropolis.datagen.MetroBlockStateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.RegistryObject;

public class TestBlock extends MetroBlockBase {

    public TestBlock() {
        super(BlockBehaviour.Properties.of().strength(1.0f).sound(SoundType.STONE));
    }

    @Override
    public void generateBlockState(RegistryObject<Block> blockRegistryObject, MetroBlockStateProvider blockStateProvider) {
        blockStateProvider.customRenderBlockTest(blockRegistryObject.get());
    }
}
