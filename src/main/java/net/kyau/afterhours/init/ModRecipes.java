package net.kyau.afterhours.init;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.references.Ref;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {

  @SuppressWarnings("unchecked")
  private static Collection<Item> itemVanillaRemoveSet = new HashSet<Item>();
  private static Collection<Block> blockVanillaRemoveSet = new HashSet<Block>();

  public static void init() {
    if (Ref.Vanilla.RECIPES) {
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
    }
    addRecipes();
  }

  public static void post() {
    changeRecipes();
  }

  private static void addRecipes() {
    GameRegistry.addRecipe(new ItemStack(ModItems.antenna, 1), "e", "q", 'e', Items.ender_pearl, 'q', ModItems.quantumrod);
    if (Ref.Vanilla.RECIPES) {
      GameRegistry.addRecipe(new ItemStack(ModVanilla.dough, 1), "www", 'w', Items.wheat);
      GameRegistry.addSmelting(ModVanilla.dough, new ItemStack(Items.bread, 1), 0.35F);
      GameRegistry.addShapelessRecipe(new ItemStack(ModVanilla.rawhide, 1), Items.rabbit_hide, Items.rabbit_hide, Items.rabbit_hide, Items.rabbit_hide);
    }
    GameRegistry.addRecipe(new ItemStack(ModItems.singularity, 1), "iii", "iui", "iii", 'i', new ItemStack(ModBlocks.infused_voidstone, 1, 0), 'u', ModItems.unstablecore);
    GameRegistry.addRecipe(new ItemStack(ModItems.stablecore, 1), "ddd", "dud", "ddd", 'd', ModItems.darkmatter, 'u', ModItems.unstablecore);
    GameRegistry.addRecipe(new ItemStack(ModItems.unstablecore, 1), "viv", "ini", "viv", 'v', new ItemStack(ModBlocks.voidstone, 1, 0), 'n', Items.nether_star, 'i', new ItemStack(ModBlocks.infused_voidstone, 1, 0));
    GameRegistry.addRecipe(new ItemStack(ModItems.voidcrystal, 1), "ggg", "lew", "ggg", 'g', Items.glowstone_dust, 'e', Items.ender_pearl, 'l', Items.lava_bucket, 'w', Items.water_bucket);
    GameRegistry.addShapelessRecipe(new ItemStack(ModItems.voidjournal, 1), Items.book, Items.ender_pearl, Items.glowstone_dust);
    GameRegistry.addRecipe(new ItemStack(ModItems.voidpearl, 1), " s ", "geg", " s ", 's', new ItemStack(Blocks.stone_slab, 1, 0), 'g', Items.glowstone_dust, 'e', ModItems.voidcrystal);
    GameRegistry.addSmelting(Blocks.end_stone, new ItemStack(ModBlocks.voidstone, 1), 1F);
    GameRegistry.addRecipe(new ItemStack(ModBlocks.infused_voidstone, 1), "ggg", "vcv", "ggg", 'g', Items.glowstone_dust, 'v', new ItemStack(ModBlocks.voidstone, 1, 0), 'c', ModItems.voidcrystal);
    GameRegistry.addRecipe(new ItemStack(ModItems.wormhole_manipulator, 1), "mam", "iui", "mim", 'a', ModItems.antenna, 'u', ModItems.stablecore, 'i', new ItemStack(ModBlocks.infused_voidstone, 1, 0), 'm', ModItems.darkmatter);
    GameRegistry.addRecipe(new ItemStack(ModItems.qrd, 1), "qaq", "ese", "qcq", 's', new ItemStack(Blocks.stone_slab, 1, 0), 'a', ModItems.antenna, 'q', ModItems.quantumrod, 'c', Blocks.ender_chest, 'e', new ItemStack(ModBlocks.infused_voidstone, 1, 0), 's', ModItems.stablecore);
    GameRegistry.addRecipe(new ItemStack(ModBlocks.quantum_stabilizer, 1), "vvv", "vuv", "viv", 'v', new ItemStack(ModBlocks.voidstone, 1, 0), 'u', ModItems.unstablecore, 'i', new ItemStack(ModBlocks.infused_voidstone, 1, 0));
    GameRegistry.addRecipe(new ItemStack(ModBlocks.quantum_reciprocator, 1), "vvv", "viv", "vgv", 'v', new ItemStack(ModBlocks.voidstone, 1, 0), 'i', new ItemStack(ModBlocks.infused_voidstone, 1, 0), 'g', Blocks.glass);
    GameRegistry.addRecipe(new ItemStack(ModBlocks.quantum_chargepad, 1), "svs", "vuv", "svs", 's', ModItems.stablecore, 'v', new ItemStack(ModBlocks.voidstone, 1, 0), 'u', ModItems.singularity);
    GameRegistry.addSmelting(ModBlocks.darkmattercluster, new ItemStack(ModItems.unstable_darkmatter), 1.0F);

    // dark matter gear
    ItemStack sword = new ItemStack(ModItems.darkmatter_sword);
    sword.addEnchantment(ModEnchants.entanglement, 1);
    GameRegistry.addRecipe(sword, " d ", " d ", " q ", 'd', ModItems.darkmatter, 'q', ModItems.quantumrod);
    CraftingManager.getInstance().getRecipeList().add(new RecipeRepair(sword, new ItemStack(ModItems.darkmatter), (int) Math.round(AfterHours.darkmatter.getMaxUses() / 4.5)));
    ItemStack axe = new ItemStack(ModItems.darkmatter_axe);
    GameRegistry.addRecipe(axe, "dd ", "dq ", " q ", 'd', ModItems.darkmatter, 'q', ModItems.quantumrod);
    axe.addEnchantment(ModEnchants.entanglement, 1);
    CraftingManager.getInstance().getRecipeList().add(new RecipeRepair(axe, new ItemStack(ModItems.darkmatter), (int) Math.round(AfterHours.darkmatter.getMaxUses() / 4.5)));
    ItemStack pickaxe = new ItemStack(ModItems.darkmatter_pickaxe);
    GameRegistry.addRecipe(pickaxe, "ddd", " q ", " q ", 'd', ModItems.darkmatter, 'q', ModItems.quantumrod);
    pickaxe.addEnchantment(ModEnchants.entanglement, 1);
    CraftingManager.getInstance().getRecipeList().add(new RecipeRepair(pickaxe, new ItemStack(ModItems.darkmatter), (int) Math.round(AfterHours.darkmatter.getMaxUses() / 4.5)));
    ItemStack shovel = new ItemStack(ModItems.darkmatter_shovel);
    shovel.addEnchantment(ModEnchants.entanglement, 1);
    GameRegistry.addRecipe(shovel, " d ", " q ", " q ", 'd', ModItems.darkmatter, 'q', ModItems.quantumrod);
    CraftingManager.getInstance().getRecipeList().add(new RecipeRepair(shovel, new ItemStack(ModItems.darkmatter), (int) Math.round(AfterHours.darkmatter.getMaxUses() / 4.5)));

    ItemStack boots = new ItemStack(ModItems.darkmatter_boots);
    boots.addEnchantment(ModEnchants.absorption, 1);
    boots.addEnchantment(ModEnchants.entanglement, 1);
    GameRegistry.addRecipe(boots, "   ", "dqd", "d d", 'd', ModItems.darkmatter, 'q', ModItems.quantumrod);
    CraftingManager.getInstance().getRecipeList().add(new RecipeRepair(boots, new ItemStack(ModItems.darkmatter), (int) Math.round(AfterHours.darkmatterArmor.getDurability(3) / 4.5)));
    ItemStack chestplate = new ItemStack(ModItems.darkmatter_chestplate);
    chestplate.addEnchantment(ModEnchants.entanglement, 1);
    chestplate.addEnchantment(ModEnchants.gravitation, 1);
    GameRegistry.addRecipe(chestplate, "d d", "qsq", "ddd", 'd', ModItems.darkmatter, 's', ModItems.stablecore, 'q', ModItems.quantumrod);
    CraftingManager.getInstance().getRecipeList().add(new RecipeRepair(chestplate, new ItemStack(ModItems.darkmatter), (int) Math.round(AfterHours.darkmatterArmor.getDurability(1) / 4.5)));
    ItemStack leggings = new ItemStack(ModItems.darkmatter_leggings);
    leggings.addEnchantment(ModEnchants.entanglement, 1);
    leggings.addEnchantment(ModEnchants.quantumBoost, 1);
    GameRegistry.addRecipe(leggings, "ddd", "dqd", "dqd", 'd', ModItems.darkmatter, 'q', ModItems.quantumrod);
    CraftingManager.getInstance().getRecipeList().add(new RecipeRepair(leggings, new ItemStack(ModItems.darkmatter), (int) Math.round(AfterHours.darkmatterArmor.getDurability(2) / 4.5)));
    ItemStack helmet = new ItemStack(ModItems.darkmatter_helmet);
    helmet.addEnchantment(ModEnchants.entanglement, 1);
    helmet.addEnchantment(ModEnchants.quantumDisplay, 1);
    GameRegistry.addRecipe(helmet, "   ", "dad", "dqd", 'd', ModItems.darkmatter, 'a', ModItems.antenna, 'q', ModItems.quantumrod);
    CraftingManager.getInstance().getRecipeList().add(new RecipeRepair(helmet, new ItemStack(ModItems.darkmatter), (int) Math.round(AfterHours.darkmatterArmor.getDurability(0) / 4.5)));
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
    GameRegistry.addSmelting(new ItemStack(Items.iron_helmet, 1, 0), new ItemStack(Items.iron_ingot, 2), 1f);
    GameRegistry.addSmelting(new ItemStack(Items.iron_chestplate, 1, 0), new ItemStack(Items.iron_ingot, 4), 1f);
    GameRegistry.addSmelting(new ItemStack(Items.iron_leggings, 1, 0), new ItemStack(Items.iron_ingot, 3), 1f);
    GameRegistry.addSmelting(new ItemStack(Items.iron_boots, 1, 0), new ItemStack(Items.iron_ingot, 2), 1f);
    GameRegistry.addSmelting(new ItemStack(Items.golden_helmet, 1, 0), new ItemStack(Items.gold_ingot, 2), 1f);
    GameRegistry.addSmelting(new ItemStack(Items.golden_chestplate, 1, 0), new ItemStack(Items.gold_ingot, 4), 1f);
    GameRegistry.addSmelting(new ItemStack(Items.golden_leggings, 1, 0), new ItemStack(Items.gold_ingot, 3), 1f);
    GameRegistry.addSmelting(new ItemStack(Items.golden_boots, 1, 0), new ItemStack(Items.gold_ingot, 2), 1f);
    GameRegistry.addSmelting(new ItemStack(Items.diamond_helmet, 1, 0), new ItemStack(Items.diamond, 2), 1f);
    GameRegistry.addSmelting(new ItemStack(Items.diamond_chestplate, 1, 0), new ItemStack(Items.diamond, 4), 1f);
    GameRegistry.addSmelting(new ItemStack(Items.diamond_leggings, 1, 0), new ItemStack(Items.diamond, 3), 1f);
    GameRegistry.addSmelting(new ItemStack(Items.diamond_boots, 1, 0), new ItemStack(Items.diamond, 2), 1f);
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
