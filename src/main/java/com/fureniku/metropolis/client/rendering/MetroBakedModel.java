package com.fureniku.metropolis.client.rendering;

import com.fureniku.metropolis.utils.Debug;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MetroBakedModel implements IDynamicBakedModel {

    protected final BakedModel _originalModel;
    private final IGeometryBakingContext _context;

    public MetroBakedModel(IGeometryBakingContext context, BakedModel originalModel) {
        _context = context;
        _originalModel = originalModel;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
        Debug.Log("Getting quads");
        //BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(state);
        List<BakedQuad> quads = new ArrayList<>();

        //quads.addAll(model.getQuads(state, side, rand));
        return quads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return _context.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return _context.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return _context.useBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply((new ResourceLocation("minecraft", "missingno")));
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Nonnull
    @Override
    public ItemTransforms getTransforms() {
        return _context.getTransforms();
    }
}
