package com.jogaj.GTNuclear.common.data;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;

import com.jogaj.GTNuclear.api.GTNuclearAPI;
import com.jogaj.GTNuclear.GTNuclear;
import com.jogaj.GTNuclear.api.block.IModeratorType;
import com.jogaj.GTNuclear.common.block.ModeratorBlock;
import static com.jogaj.GTNuclear.GTNuclear.REGISTRATE;

import com.jogaj.GTNuclear.common.data.recipe.GTNTags;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.ModelFile;

@SuppressWarnings("removal")
public class GTNuclearBlocks {
    static {
        REGISTRATE.creativeModeTab(() -> GTNuclearCreativeModeTabs.GTNuclear);
    }

    public static final BlockEntry<ModeratorBlock> MODERATOR_WATER = createModeratorBlock(ModeratorBlock.ModeratorType.WATER);

    private static BlockEntry<ModeratorBlock> createModeratorBlock(@SuppressWarnings("SameParameterValue") IModeratorType moderatorType){
        BlockEntry<ModeratorBlock> moderatorBlock = REGISTRATE
                .block("%s_moderator_block".formatted(moderatorType.getName()),p-> new ModeratorBlock(p, moderatorType))
                .initialProperties(() -> Blocks.GLASS)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(createModeratorModel("%s_moderator_block".formatted(moderatorType.getName()), moderatorType))
                .tag(GTNTags.MINEABLE_WITH_WRENCH, BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .model(NonNullBiConsumer.noop())
                .build()
                .register();
        GTNuclearAPI.MODERATORS.put(moderatorType, moderatorBlock);
        return moderatorBlock;
    }

    private static NonNullBiConsumer<DataGenContext<Block, ModeratorBlock>, RegistrateBlockstateProvider> createModeratorModel(
            String name,
            IModeratorType moderatorType
    ){
        return (ctx, prov) -> {
            ActiveBlock block = ctx.getEntry();
            ModelFile inactive = prov.models().cubeAll(name, moderatorType.getTexture());
            ModelFile active = prov.models().withExistingParent(name + "_active", GTNuclear.resourceLocation("block/cube_2_layer/all"))
                    .texture("bot_all", moderatorType.getTexture())
                    .texture("top_all", moderatorType.getTexture().withSuffix("_bloom"));
            prov.getVariantBuilder(block)
                    .partialState().with(ActiveBlock.ACTIVE, false).modelForState().modelFile(inactive).addModel()
                    .partialState().with(ActiveBlock.ACTIVE, true).modelForState().modelFile(active).addModel();
        };
    }

    public static void init() {
        //GTNuclear.LOGGER.info(ModeratorBlock.ModeratorType.WATER.getTexture());
    }
}
