package net.kyau.afterhours.items;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import net.kyau.afterhours.init.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeManager {

  @SuppressWarnings("unchecked")
  private static Collection<Item> removeSet = new HashSet();

  public static void init() {
    // Item recipes to remove from the game
    Collections.addAll(removeSet, new Item[] { Items.bread, });
    removeRecipes();
    addRecipes();
  }

  public static void post() {
    changeRecipes();
  }

  private static void addRecipes() {
    GameRegistry.addRecipe(new ItemStack(ModItems.antenna, 1), "g", "s", 'g', Items.glowstone_dust, 's', Items.stick);
    GameRegistry.addRecipe(new ItemStack(ModItems.dough, 1), "www", 'w', Items.wheat);
    GameRegistry.addSmelting(ModItems.dough, new ItemStack(Items.bread, 1), 0.35F);
    GameRegistry.addRecipe(new ItemStack(ModItems.voidstone, 1), " s ", "geg", " s ", 's', new ItemStack(Blocks.stone_slab, 1, 0), 'g', Items.glowstone_dust, 'e', Items.ender_pearl);
    GameRegistry.addRecipe(new ItemStack(ModItems.voidwell, 1), "ege", "cmp", "ege", 'c', Items.clock, 'e', Items.ender_pearl, 'g', Items.glowstone_dust, 'm', Items.map, 'p', Items.compass);
    GameRegistry.addRecipe(new ItemStack(ModItems.vrad, 1), "lal", "ses", "lcl", 'l', new ItemStack(Blocks.stone_slab, 1, 0), 'a', ModItems.antenna, 's', Items.stick, 'e', Blocks.ender_chest, 'c', Blocks.chest);
  }

  private static void removeRecipes() {
    Iterator<IRecipe> iterator = CraftingManager.getInstance().getRecipeList().iterator();
    while (iterator.hasNext()) {
      IRecipe recipe = iterator.next();
      if (recipe == null)
        continue;
      ItemStack output = recipe.getRecipeOutput();
      if (output != null && output.getItem() != null && removeSet.contains(output.getItem()))
        iterator.remove();
    }
  }

  private static void changeRecipes() {
    // TODO: change other mod recipes for balance
  }

}
