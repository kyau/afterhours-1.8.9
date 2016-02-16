package net.kyau.afterhours.init;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import net.kyau.afterhours.AfterHours;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeManager {

  @SuppressWarnings("unchecked")
  private static Collection<Item> itemVanillaRemoveSet = new HashSet<Item>();
  private static Collection<Block> blockVanillaRemoveSet = new HashSet<Block>();

  public static void init() {
    // Item recipes to remove from the game
    Collections.addAll(itemVanillaRemoveSet, new Item[] {
        Items.bread,
        Items.brewing_stand,
        Items.leather,
        Items.stone_axe,
        Items.stone_hoe,
        Items.stone_pickaxe,
        Items.stone_shovel,
        Items.stone_sword });
    removeVanillaRecipes();
    addVanillaRecipes();
    addRecipes();
  }

  public static void post() {
    changeRecipes();
  }

  private static void addRecipes() {
    GameRegistry.addRecipe(new ItemStack(ModItems.antenna, 1), "g", "s", 'g', Items.glowstone_dust, 's', Items.stick);
    GameRegistry.addRecipe(new ItemStack(ModVanilla.dough, 1), "www", 'w', Items.wheat);
    GameRegistry.addSmelting(ModVanilla.dough, new ItemStack(Items.bread, 1), 0.35F);
    GameRegistry.addShapelessRecipe(new ItemStack(ModVanilla.rawhide, 1), Items.rabbit_hide, Items.rabbit_hide, Items.rabbit_hide, Items.rabbit_hide);
    GameRegistry.addRecipe(new ItemStack(ModItems.singularity, 1), "iii", "iui", "iii", 'i', new ItemStack(ModBlocks.infused_voidstone, 1, 0), 'u', ModItems.unstablecore);
    GameRegistry.addRecipe(new ItemStack(ModItems.stablecore, 1), "ddd", "dud", "ddd", 'd', ModItems.darkmatter, 'u', ModItems.unstablecore);
    GameRegistry.addRecipe(new ItemStack(ModItems.unstablecore, 1), "ccc", "cnc", "ccc", 'c', ModItems.voidcrystal, 'n', Items.nether_star);
    GameRegistry.addRecipe(new ItemStack(ModItems.voidcrystal, 1), "ggg", "lel", "ggg", 'g', Items.glowstone_dust, 'e', Items.ender_pearl, 'l', Items.lava_bucket);
    GameRegistry.addShapelessRecipe(new ItemStack(ModItems.voidjournal, 1), Items.book, Items.ender_pearl, Items.glowstone_dust);
    GameRegistry.addRecipe(new ItemStack(ModItems.voidpearl, 1), " s ", "geg", " s ", 's', new ItemStack(Blocks.stone_slab, 1, 0), 'g', Items.glowstone_dust, 'e', ModItems.voidcrystal);
    GameRegistry.addSmelting(Blocks.end_stone, new ItemStack(ModBlocks.voidstone, 1), 1F);
    GameRegistry.addRecipe(new ItemStack(ModBlocks.infused_voidstone, 1), "ggg", "gvg", "ggg", 'g', Items.glowstone_dust, 'v', new ItemStack(ModBlocks.voidstone, 1, 0));
    GameRegistry.addRecipe(new ItemStack(ModItems.voidwell, 1), "mdm", "iui", "mdm", 'u', ModItems.stablecore, 'd', Items.diamond, 'i', new ItemStack(ModBlocks.infused_voidstone, 1, 0), 'm', ModItems.darkmatter);
    GameRegistry.addRecipe(new ItemStack(ModItems.qrd, 1), "sas", "ece", "ses", 's', new ItemStack(Blocks.stone_slab, 1, 0), 'a', ModItems.antenna, 's', Items.stick, 'c', Blocks.ender_chest, 'e', ModItems.voidcrystal);

    // dark matter gear
    GameRegistry.addRecipe(new ItemStack(ModItems.darkmatter_sword), " d ", " d ", " q ", 'd', ModItems.darkmatter, 'q', ModItems.quantumrod);
    GameRegistry.addRecipe(new ItemStack(ModItems.darkmatter_axe), "dd ", "dq ", " q ", 'd', ModItems.darkmatter, 'q', ModItems.quantumrod);
    GameRegistry.addRecipe(new ItemStack(ModItems.darkmatter_pickaxe), "ddd", " q ", " q ", 'd', ModItems.darkmatter, 'q', ModItems.quantumrod);
    GameRegistry.addRecipe(new ItemStack(ModItems.darkmatter_shovel), " d ", " q ", " q ", 'd', ModItems.darkmatter, 'q', ModItems.quantumrod);
    // dark matter repair recipes
    CraftingManager.getInstance().getRecipeList().add(new RecipeRepair(new ItemStack(ModItems.darkmatter_sword), new ItemStack(ModItems.darkmatter), (int) Math.round(AfterHours.darkmatter.getMaxUses() / 4.5)));
    CraftingManager.getInstance().getRecipeList().add(new RecipeRepair(new ItemStack(ModItems.darkmatter_shovel), new ItemStack(ModItems.darkmatter), (int) Math.round(AfterHours.darkmatter.getMaxUses() / 4.5)));
    CraftingManager.getInstance().getRecipeList().add(new RecipeRepair(new ItemStack(ModItems.darkmatter_pickaxe), new ItemStack(ModItems.darkmatter), (int) Math.round(AfterHours.darkmatter.getMaxUses() / 4.5)));
    CraftingManager.getInstance().getRecipeList().add(new RecipeRepair(new ItemStack(ModItems.darkmatter_axe), new ItemStack(ModItems.darkmatter), (int) Math.round(AfterHours.darkmatter.getMaxUses() / 4.5)));
  }

  private static void removeVanillaRecipes() {
    Iterator<IRecipe> iterator = CraftingManager.getInstance().getRecipeList().iterator();
    while (iterator.hasNext()) {
      IRecipe recipe = iterator.next();
      if (recipe == null)
        continue;
      ItemStack output = recipe.getRecipeOutput();
      if (output != null && output.getItem() != null) {
        if (itemVanillaRemoveSet.contains(output.getItem())) {
          iterator.remove();
        } else if (Block.getBlockFromItem(output.getItem()) == Blocks.sandstone && output.getItemDamage() == 1) {
          // chiseled sandstone
          iterator.remove();
        } else if (Block.getBlockFromItem(output.getItem()) == Blocks.red_sandstone && output.getItemDamage() == 1) {
          // chiseled red sandstone
          iterator.remove();
        } else if (Block.getBlockFromItem(output.getItem()) == Blocks.stonebrick && output.getItemDamage() == 3) {
          // chiseled stone bricks
          iterator.remove();
        } else if (Block.getBlockFromItem(output.getItem()) == Blocks.quartz_block && (output.getItemDamage() == 1 || output.getItemDamage() == 2)) {
          // chiseled quartz block & quartz pillar
          iterator.remove();
        }
      }
    }
  }

  private static void addVanillaRecipes() {
    // realistic recipes
    // stone tools actually made from stone
    GameRegistry.addRecipe(new ItemStack(Items.stone_axe, 1), "ss ", "st ", " t ", 's', new ItemStack(Blocks.stone, 1, 0), 't', Items.stick);
    GameRegistry.addRecipe(new ItemStack(Items.stone_hoe, 1), "ss ", " t ", " t ", 's', new ItemStack(Blocks.stone, 1, 0), 't', Items.stick);
    GameRegistry.addRecipe(new ItemStack(Items.stone_pickaxe, 1), "sss", " t ", " t ", 's', new ItemStack(Blocks.stone, 1, 0), 't', Items.stick);
    GameRegistry.addRecipe(new ItemStack(Items.stone_shovel, 1), " s ", " t ", " t ", 's', new ItemStack(Blocks.stone, 1, 0), 't', Items.stick);
    GameRegistry.addRecipe(new ItemStack(Items.stone_sword, 1), " s ", " s ", " t ", 's', new ItemStack(Blocks.stone, 1, 0), 't', Items.stick);
    // add stone slabs to the brewing station
    GameRegistry.addRecipe(new ItemStack(Items.brewing_stand, 1), " b ", "sss", 'b', Items.blaze_rod, 's', new ItemStack(Blocks.stone_slab, 1, 0));
    // flint from gravel
    GameRegistry.addShapelessRecipe(new ItemStack(Items.flint, 1), Blocks.gravel, Blocks.gravel, Blocks.gravel, Blocks.gravel);
    // allow crafting/smelting of armor back into 50% of the components to make them
    GameRegistry.addSmelting(Items.iron_helmet, new ItemStack(Items.iron_ingot, 2), 1f);
    GameRegistry.addSmelting(Items.iron_chestplate, new ItemStack(Items.iron_ingot, 4), 1f);
    GameRegistry.addSmelting(Items.iron_leggings, new ItemStack(Items.iron_ingot, 3), 1f);
    GameRegistry.addSmelting(Items.iron_boots, new ItemStack(Items.iron_ingot, 2), 1f);
    GameRegistry.addSmelting(Items.golden_helmet, new ItemStack(Items.gold_ingot, 2), 1f);
    GameRegistry.addSmelting(Items.golden_chestplate, new ItemStack(Items.gold_ingot, 4), 1f);
    GameRegistry.addSmelting(Items.golden_leggings, new ItemStack(Items.gold_ingot, 3), 1f);
    GameRegistry.addSmelting(Items.golden_boots, new ItemStack(Items.gold_ingot, 2), 1f);
    GameRegistry.addSmelting(Items.diamond_helmet, new ItemStack(Items.diamond, 2), 1f);
    GameRegistry.addSmelting(Items.diamond_chestplate, new ItemStack(Items.diamond, 4), 1f);
    GameRegistry.addSmelting(Items.diamond_leggings, new ItemStack(Items.diamond, 3), 1f);
    GameRegistry.addSmelting(Items.diamond_boots, new ItemStack(Items.diamond, 2), 1f);
    // wood slabs
    for (int i = 0; i < 6; i++) {
      GameRegistry.addShapelessRecipe(new ItemStack(Blocks.planks, 1, i), new ItemStack(Blocks.wooden_slab, 1, i), new ItemStack(Blocks.wooden_slab, 1, i));
    }
    // chiseled block fixes
    GameRegistry.addShapelessRecipe(new ItemStack(Blocks.sandstone, 4, 1), new ItemStack(Blocks.sandstone, 1, 2), new ItemStack(Blocks.sandstone, 1, 2), new ItemStack(Blocks.sandstone, 1, 2), new ItemStack(Blocks.sandstone, 1, 2));
    GameRegistry.addShapelessRecipe(new ItemStack(Blocks.red_sandstone, 4, 1), new ItemStack(Blocks.red_sandstone, 1, 2), new ItemStack(Blocks.red_sandstone, 1, 2), new ItemStack(Blocks.red_sandstone, 1, 2), new ItemStack(Blocks.red_sandstone, 1, 2));
    GameRegistry.addShapelessRecipe(new ItemStack(Blocks.stonebrick, 4, 3), new ItemStack(Blocks.stonebrick, 1, 0), new ItemStack(Blocks.stonebrick, 1, 0), new ItemStack(Blocks.stonebrick, 1, 0), new ItemStack(Blocks.stonebrick, 1, 0));
    GameRegistry.addShapelessRecipe(new ItemStack(Blocks.quartz_block, 4, 1), new ItemStack(Blocks.quartz_block, 1, 0), new ItemStack(Blocks.quartz_block, 1, 0), new ItemStack(Blocks.quartz_block, 1, 0), new ItemStack(Blocks.quartz_block, 1, 0));
    GameRegistry.addShapelessRecipe(new ItemStack(Blocks.quartz_block, 4, 2), new ItemStack(Blocks.quartz_block, 4, 1), new ItemStack(Blocks.quartz_block, 4, 1), new ItemStack(Blocks.quartz_block, 4, 1), new ItemStack(Blocks.quartz_block, 4, 1));
    // stone slabs
    GameRegistry.addShapelessRecipe(new ItemStack(Blocks.stone, 1, 0), new ItemStack(Blocks.stone_slab, 1, 0), new ItemStack(Blocks.stone_slab, 1, 0));
    GameRegistry.addShapelessRecipe(new ItemStack(Blocks.sandstone, 1, 0), new ItemStack(Blocks.stone_slab, 1, 1), new ItemStack(Blocks.stone_slab, 1, 1));
    GameRegistry.addShapelessRecipe(new ItemStack(Blocks.cobblestone, 1), new ItemStack(Blocks.stone_slab, 1, 3), new ItemStack(Blocks.stone_slab, 1, 3));
    GameRegistry.addShapelessRecipe(new ItemStack(Blocks.brick_block, 1), new ItemStack(Blocks.stone_slab, 1, 4), new ItemStack(Blocks.stone_slab, 1, 4));
    GameRegistry.addShapelessRecipe(new ItemStack(Blocks.stonebrick, 1, 0), new ItemStack(Blocks.stone_slab, 1, 5), new ItemStack(Blocks.stone_slab, 1, 5));
    GameRegistry.addShapelessRecipe(new ItemStack(Blocks.nether_brick, 1), new ItemStack(Blocks.stone_slab, 1, 6), new ItemStack(Blocks.stone_slab, 1, 6));
    GameRegistry.addShapelessRecipe(new ItemStack(Blocks.quartz_block, 1, 0), new ItemStack(Blocks.stone_slab, 1, 7), new ItemStack(Blocks.stone_slab, 1, 7));
    GameRegistry.addShapelessRecipe(new ItemStack(Blocks.red_sandstone, 1, 0), new ItemStack(Blocks.stone_slab2, 1, 0), new ItemStack(Blocks.stone_slab2, 1, 0));
    // smelting: rawhide -> leather
    GameRegistry.addSmelting(ModVanilla.rawhide, new ItemStack(Items.leather, 1), 0.25F);
  }

  private static void changeRecipes() {
    // TODO: change other mod recipes for balance
  }

}
