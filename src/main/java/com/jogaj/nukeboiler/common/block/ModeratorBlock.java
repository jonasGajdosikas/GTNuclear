package com.jogaj.nukeboiler.common.block;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.jogaj.nukeboiler.NukeBoiler;
import com.jogaj.nukeboiler.api.block.IModeratorType;
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

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class ModeratorBlock extends ActiveBlock {

    public IModeratorType moderatortype;

    public ModeratorBlock(Properties properties, IModeratorType moderatortype) {
        super(properties);
        this.moderatortype = moderatortype;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (GTUtil.isShiftDown()) {
            int conversion = (int)(moderatortype.getFastNeutronConversion() * 100);
            int maxTemp = moderatortype.getMaxTemp();
            tooltip.add(Component.translatable("block.nukeBoiler.moderator.tooltip_conversion", conversion));
            tooltip.add(Component.translatable("block.nukeBoiler.moderator.tooltip_max_temp", maxTemp));
        } else {
            tooltip.add(Component.translatable("block.nukeBoiler.moderator.tooltip_extended_info"));
        }
    }

    @Getter
    public enum ModeratorType implements StringRepresentable,IModeratorType{
        WATER("water", .1, 700 , NukeBoiler.resourceLocation("block/casings/moderator/moderator_water"));

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
