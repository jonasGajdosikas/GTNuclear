package com.jogaj.GTNuclear.common.data;

import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

import static com.jogaj.GTNuclear.GTNuclear.REGISTRATE;

public class GTNuclearCreativeModeTabs {
    public static RegistryEntry<CreativeModeTab> GTNuclear = REGISTRATE.defaultCreativeTab(com.jogaj.GTNuclear.GTNuclear.MOD_ID,
            builder -> builder.displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator(com.jogaj.GTNuclear.GTNuclear.MOD_ID, REGISTRATE))
                    .title(Component.literal("Greg Tech Nuclear"))
                    .build())
            .register();
    public static void init(){

    }
}
