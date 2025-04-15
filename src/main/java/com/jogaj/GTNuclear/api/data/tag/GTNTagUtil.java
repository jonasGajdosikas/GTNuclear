package com.jogaj.GTNuclear.api.data.tag;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class GTNTagUtil {
    public static <T> TagKey<T> optionalTag(ResourceKey<? extends Registry<T>> registry, ResourceLocation id) {
        return TagKey.create(registry, id);
    }
    public static <T> TagKey<T> createTag(ResourceKey<? extends Registry<T>> registry, String path, boolean vanilla) {
        if (vanilla) return optionalTag(registry, new ResourceLocation("minecraft", path));
        return optionalTag(registry, new ResourceLocation("forge", path));
    }
    public static TagKey<Block> createBlockTag(String path){
        return createTag(Registries.BLOCK, path, false);
    }
}
