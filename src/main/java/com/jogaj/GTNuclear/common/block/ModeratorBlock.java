package com.jogaj.GTNuclear.common.block;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.jogaj.GTNuclear.GTNuclear;
import com.jogaj.GTNuclear.api.block.IModeratorType;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class ModeratorBlock extends ActiveBlock{
    public IModeratorType moderatorType;

    public ModeratorBlock(Properties properties, IModeratorType moderatorType) {
        super(properties);
        this.moderatorType = moderatorType;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (GTUtil.isShiftDown()) {
            int conversion = (int)(moderatorType.getFastNeutronConversion() * 100);
            int maxTemp = moderatorType.getMaxTemp();
            tooltip.add(Component.translatable("block.gtnuclear.moderator.tooltip_conversion", conversion));
            tooltip.add(Component.translatable("block.gtnuclear.moderator.tooltip_max_temp", maxTemp));
        } else {
            tooltip.add(Component.translatable("block.gtnuclear.moderator.tooltip_extended_info"));
        }
    }

    @Getter
    public enum ModeratorType implements StringRepresentable,IModeratorType{
        WATER("water", .1, 700 , GTNuclear.resourceLocation("block/casings/moderators/moderator_water"));

        @NotNull
        private final String name;

        private final double FastNeutronConversion;

        @NotNull
        private final ResourceLocation texture;

        private final int maxTemp;

        ModeratorType(@NotNull String name, double conversion, int maxTemp, @NotNull ResourceLocation texture){
            this.name = name;
            this.FastNeutronConversion = conversion;
            this.texture = texture;
            this.maxTemp = maxTemp;
        }

        @NotNull
        @Override
        public String toString(){
            return getName();
        }

        @Override
        @NotNull
        public String getSerializedName() {
            return name;
        }


    }
}
