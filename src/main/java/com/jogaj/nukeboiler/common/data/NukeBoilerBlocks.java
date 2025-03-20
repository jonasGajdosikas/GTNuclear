package com.jogaj.nukeboiler.common.data;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;
import com.jogaj.nukeboiler.api.NukeBoilerAPI;
import net.minecraft.world.item.BlockItem;
import com.jogaj.nukeboiler.NukeBoiler;
import com.jogaj.nukeboiler.api.block.IModeratorType;
import com.jogaj.nukeboiler.common.block.ModeratorBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.ModelFile;

import static com.jogaj.nukeboiler.NukeBoiler.REGISTRATE;

@SuppressWarnings("removal")
public class NukeBoilerBlocks {

    static {
        REGISTRATE.creativeModeTab(() -> GTCreativeModeTabs.DECORATION);
    }
    // Moderator Blocks
    public static final BlockEntry<ModeratorBlock> MODERATOR_WATER = createModeratorBlock(ModeratorBlock.ModeratorType.WATER);




    private static BlockEntry<ModeratorBlock> createModeratorBlock(IModeratorType moderatorType){
        BlockEntry<ModeratorBlock> moderatorBlock = REGISTRATE
            .block("%s_moderator_block".formatted(moderatorType.getName()),p-> new ModeratorBlock(p, moderatorType))
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate(createModeratorModel("%s_moderator_block".formatted(moderatorType.getName()), moderatorType))
            .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
            .item(BlockItem::new)
            .build()
            .register();
        NukeBoilerAPI.MODERATORS.put(moderatorType, moderatorBlock);
        return moderatorBlock;
    }

    private static NonNullBiConsumer<DataGenContext<Block, ModeratorBlock>, RegistrateBlockstateProvider> createModeratorModel(
            String name,
            IModeratorType moderatorType
    ){
        return (ctx, prov) -> {
            ActiveBlock block = ctx.getEntry();
            ModelFile inactive = prov.models().cubeAll(name, moderatorType.getTexture());
            ModelFile active = prov.models().withExistingParent(name + "_active", NukeBoiler.resourceLocation("block/cube_2_layer/all"))
                    .texture("bot_all", moderatorType.getTexture())
                    .texture("top_all", moderatorType.getTexture().withSuffix("_bloom"));
            prov.getVariantBuilder(block)
                    .partialState().with(ActiveBlock.ACTIVE, false).modelForState().modelFile(inactive).addModel()
                    .partialState().with(ActiveBlock.ACTIVE, true).modelForState().modelFile(active).addModel();
        };
    }

    public static final void init() {}
}