package net.kyau.afterhours.compat.jei;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.kyau.afterhours.init.ModBlocks;
import net.kyau.afterhours.init.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class QuantumStabilizerRecipeMaker {

  @Nonnull
  public static List<QuantumStabilizerRecipe> getRecipes(IJeiHelpers jeiHelpers) {
    IStackHelper stackHelper = jeiHelpers.getStackHelper();
    List<QuantumStabilizerRecipe> recipes = new ArrayList<QuantumStabilizerRecipe>();
    List<ItemStack> inputs1 = new ArrayList<ItemStack>();
    List<ItemStack> inputs2 = new ArrayList<ItemStack>();
    inputs1.add(new ItemStack(ModItems.voidcrystal));
    inputs1.add(new ItemStack(ModItems.unstable_darkmatter));
    inputs1.add(new ItemStack(Items.gunpowder));
    inputs2.add(new ItemStack(ModItems.voidcrystal));
    inputs2.add(new ItemStack(ModBlocks.voidstone));
    inputs2.add(new ItemStack(ModItems.darkmatter));
    recipes.add(new QuantumStabilizerRecipe(inputs1, new ItemStack(ModItems.darkmatter)));
    recipes.add(new QuantumStabilizerRecipe(inputs2, new ItemStack(ModItems.quantumrod)));
    return recipes;
  }
}
