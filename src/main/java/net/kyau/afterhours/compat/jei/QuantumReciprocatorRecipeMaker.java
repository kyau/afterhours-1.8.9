package net.kyau.afterhours.compat.jei;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class QuantumReciprocatorRecipeMaker {

  @Nonnull
  public static List<QuantumReciprocatorRecipe> getRecipes(IJeiHelpers jeiHelpers) {
    IStackHelper stackHelper = jeiHelpers.getStackHelper();
    List<QuantumReciprocatorRecipe> recipes = new ArrayList<QuantumReciprocatorRecipe>();
    List<ItemStack> inputs = new ArrayList<ItemStack>();
    inputs.add(new ItemStack(Blocks.stone, 1, 1));
    inputs.add(new ItemStack(Blocks.stone, 1, 3));
    inputs.add(new ItemStack(Blocks.stone, 1, 5));
    recipes.add(new QuantumReciprocatorRecipe(inputs, new ItemStack(Blocks.cobblestone, 1, 0)));
    inputs = new ArrayList<ItemStack>();
    inputs.add(new ItemStack(Blocks.sapling, 1, 0));
    recipes.add(new QuantumReciprocatorRecipe(inputs, new ItemStack(Blocks.sapling, 1, 1)));
    inputs = new ArrayList<ItemStack>();
    inputs.add(new ItemStack(Blocks.sapling, 1, 1));
    recipes.add(new QuantumReciprocatorRecipe(inputs, new ItemStack(Blocks.sapling, 1, 2)));
    inputs = new ArrayList<ItemStack>();
    inputs.add(new ItemStack(Blocks.sapling, 1, 2));
    recipes.add(new QuantumReciprocatorRecipe(inputs, new ItemStack(Blocks.sapling, 1, 3)));
    inputs = new ArrayList<ItemStack>();
    inputs.add(new ItemStack(Blocks.sapling, 1, 3));
    recipes.add(new QuantumReciprocatorRecipe(inputs, new ItemStack(Blocks.sapling, 1, 4)));
    inputs = new ArrayList<ItemStack>();
    inputs.add(new ItemStack(Blocks.sapling, 1, 4));
    recipes.add(new QuantumReciprocatorRecipe(inputs, new ItemStack(Blocks.sapling, 1, 5)));
    inputs = new ArrayList<ItemStack>();
    inputs.add(new ItemStack(Blocks.sapling, 1, 5));
    recipes.add(new QuantumReciprocatorRecipe(inputs, new ItemStack(Blocks.sapling, 1, 0)));
    inputs = new ArrayList<ItemStack>();
    inputs.add(new ItemStack(Items.wheat_seeds, 1));
    recipes.add(new QuantumReciprocatorRecipe(inputs, new ItemStack(Items.pumpkin_seeds, 1)));
    inputs = new ArrayList<ItemStack>();
    inputs.add(new ItemStack(Items.pumpkin_seeds, 1));
    recipes.add(new QuantumReciprocatorRecipe(inputs, new ItemStack(Items.melon_seeds, 1)));
    inputs = new ArrayList<ItemStack>();
    inputs.add(new ItemStack(Items.melon_seeds, 1));
    recipes.add(new QuantumReciprocatorRecipe(inputs, new ItemStack(Items.wheat_seeds, 1)));
    inputs = new ArrayList<ItemStack>();
    inputs.add(new ItemStack(Items.coal, 1, 0));
    recipes.add(new QuantumReciprocatorRecipe(inputs, new ItemStack(Items.coal, 1, 1)));
    inputs = new ArrayList<ItemStack>();
    inputs.add(new ItemStack(Items.coal, 1, 1));
    recipes.add(new QuantumReciprocatorRecipe(inputs, new ItemStack(Items.coal, 1, 0)));
    return recipes;
  }
}
