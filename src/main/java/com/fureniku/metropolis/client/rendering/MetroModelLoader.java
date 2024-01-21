package com.fureniku.metropolis.client.rendering;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import java.util.function.Function;

public class MetroModelLoader implements IGeometryLoader<MetroModelLoader.MetroModelGeometry> {

    private BakedModel _modelIn;

    @Override
    public MetroModelGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        return new MetroModelGeometry(_modelIn);
    }

    public void setModel(BakedModel model) {
        _modelIn = model;
    }

    public static class MetroModelGeometry implements IUnbakedGeometry<MetroModelGeometry> {

        private BakedModel _model;

        public MetroModelGeometry(BakedModel model) {
            _model = model;
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
            return new MetroBakedModel(context, _model);
        }
    }
}
