package com.jogaj.nukeboiler.common.data;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import org.jetbrains.annotations.NotNull;

public class NuclearModifiers {
    public static @NotNull ModifierFunction moderatorModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe){
        if (!(machine instanceof NuclearReactor reactor)){
            return RecipeModifier.nullWrongType(NuclearReactor.class, machine);
        }
        return null;
    }
}
