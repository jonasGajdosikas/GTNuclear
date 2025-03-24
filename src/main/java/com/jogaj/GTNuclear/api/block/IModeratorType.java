package com.jogaj.GTNuclear.api.block;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;

public interface IModeratorType {
    /**
     * @return The Unique name of the moderator block
     */
    @NotNull
    String getName();
    /**
     * @return The factor of fast neutrons converted to thermal
     */
    double getFastNeutronConversion();

    /**
     * @return The max temperature the moderator can reach before failing
     */
    int getMaxTemp();

    /**
     * @return the {@link ResourceLocation} defining the base texture of the moderator block
     */
    ResourceLocation getTexture();
}
