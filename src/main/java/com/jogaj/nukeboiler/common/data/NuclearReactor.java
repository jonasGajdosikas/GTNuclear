package com.jogaj.nukeboiler.common.data;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IRotorHolderMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.jogaj.nukeboiler.api.block.IModeratorType;
import com.jogaj.nukeboiler.common.block.ModeratorBlock;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import lombok.Getter;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;


public class NuclearReactor extends WorkableElectricMultiblockMachine implements IExplosionMachine, IDisplayUIMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(NuclearReactor.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    public static final int MIN_DURABILITY_TO_WARN = 10;

    private final long BASE_EU_OUTPUT;

    public NuclearReactor(IMachineBlockEntity holder,
                          Object... args) {
        super(holder, args);
        this.BASE_EU_OUTPUT = 1;
    }

    @Nullable
    private IRotorHolderMachine getRotorHolder(){
        for (IMultiPart part : getParts()){
            if (part instanceof IRotorHolderMachine rotorHolder) return rotorHolder;
        }
        return null;
    }

    protected double rotorBoost(){
        var rotorHolder = getRotorHolder();
        if (rotorHolder != null && rotorHolder.hasRotor()){
            int maxSpeed = rotorHolder.getMaxRotorHolderSpeed();
            int currentSpeed = rotorHolder.getRotorSpeed();
            if (currentSpeed >= maxSpeed) return 1;
            return Math.pow(1.0 * currentSpeed / maxSpeed, 2);
        }
        return 0;
    }

    @Override
    public long getOverclockVoltage() {
        var rotorHolder = getRotorHolder();
        if (rotorHolder != null && rotorHolder.hasRotor()){
            return BASE_EU_OUTPUT * rotorHolder.getTotalPower() / 100;
        }
        return 0;
    }
    //////////////////////////////////////
    // ********* Recipe Logic ********* //
    //////////////////////////////////////

    public static ModifierFunction recipeModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe) {
        if (!(machine instanceof NuclearReactor reactor)) {
            return RecipeModifier.nullWrongType(NuclearReactor.class, machine);
        }

        var rotorHolder = reactor.getRotorHolder();
        if (rotorHolder == null) return ModifierFunction.NULL;

        long EUt = RecipeHelper.getOutputEUt(recipe);
        long turbineMaxVoltage = reactor.getOverclockVoltage();
        double holderEfficiency = rotorHolder.getTotalEfficiency() / 100.0;

        if (EUt <= 0 || turbineMaxVoltage <= EUt || holderEfficiency <= 0) return ModifierFunction.NULL;

        // get the amount of parallel required to match the desired output voltage
        int maxParallel = (int) (turbineMaxVoltage / EUt);
        int actualParallel = ParallelLogic.getParallelAmountFast(reactor, recipe, maxParallel);
        double eutMultiplier = reactor.rotorBoost() * actualParallel;

        return ModifierFunction.builder()
                .inputModifier(ContentModifier.multiplier(actualParallel))
                .outputModifier(ContentModifier.multiplier(actualParallel))
                .eutMultiplier(eutMultiplier)
                .parallels(actualParallel)
                .durationMultiplier(holderEfficiency)
                .build();
    }


    @Override
    public boolean dampingWhenWaiting() {
        return false;
    }

    @Override
    public boolean canVoidRecipeOutputs(RecipeCapability<?> capability) {
        return capability != EURecipeCapability.CAP;
    }

    //////////////////////////////////////
    // *************  GUI **************//
    //////////////////////////////////////

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            var rotorHolder = getRotorHolder();

            if (rotorHolder != null && rotorHolder.getRotorEfficiency() > 0) {
                textList.add(Component.translatable("gtceu.multiblock.turbine.rotor_speed",
                        FormattingUtil.formatNumbers(rotorHolder.getRotorSpeed()),
                        FormattingUtil.formatNumbers(rotorHolder.getMaxRotorHolderSpeed())));
                textList.add(Component.translatable("gtceu.multiblock.turbine.efficiency",
                        rotorHolder.getTotalEfficiency()));

                long maxProduction = getOverclockVoltage();
                long currentProduction = isActive() && recipeLogic.getLastRecipe() != null ?
                        RecipeHelper.getOutputEUt(recipeLogic.getLastRecipe()) : 0;
                String voltageName = GTValues.VNF[GTUtil.getTierByVoltage(currentProduction)];

                if (isActive()) {
                    textList.add(3, Component.translatable("gtceu.multiblock.turbine.energy_per_tick",
                            FormattingUtil.formatNumbers(currentProduction),
                            FormattingUtil.formatNumbers(maxProduction)));
                }

                int rotorDurability = rotorHolder.getRotorDurabilityPercent();
                if (rotorDurability > MIN_DURABILITY_TO_WARN) {
                    textList.add(Component.translatable("gtceu.multiblock.turbine.rotor_durability", rotorDurability));
                } else {
                    textList.add(Component.translatable("gtceu.multiblock.turbine.rotor_durability", rotorDurability)
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                }
            }
        }
    }

    //////////////////////////////////////
    // ************ MULTI ************* //
    //////////////////////////////////////

    @Getter
    private IModeratorType moderatorType = ModeratorBlock.ModeratorType.WATER;

    @Override
    public void onStructureFormed(){
        super.onStructureFormed();

    }
}
