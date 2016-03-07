package net.kyau.afterhours.init.recipes;

import net.kyau.afterhours.references.Ref;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ReciprocatorRecipes {

  private final static Item granite = new ItemStack(Blocks.stone, 1, 1).getItem();
  private final static Item diorite = new ItemStack(Blocks.stone, 1, 3).getItem();
  private final static Item andesite = new ItemStack(Blocks.stone, 1, 5).getItem();
  private final static ItemStack oak = new ItemStack(Blocks.sapling, 1, 0);
  private final static ItemStack spruce = new ItemStack(Blocks.sapling, 1, 1);
  private final static ItemStack birch = new ItemStack(Blocks.sapling, 1, 2);
  private final static ItemStack jungle = new ItemStack(Blocks.sapling, 1, 3);
  private final static ItemStack acacia = new ItemStack(Blocks.sapling, 1, 4);
  private final static ItemStack darkoak = new ItemStack(Blocks.sapling, 1, 5);
  private final static Item wheat_seeds = Items.wheat_seeds;
  private final static Item pumpkin_seeds = Items.pumpkin_seeds;
  private final static Item melon_seeds = Items.melon_seeds;
  private final static ItemStack coal = new ItemStack(Items.coal, 1, 0);
  private final static ItemStack charcoal = new ItemStack(Items.coal, 1, 1);

  public static int timeToReciprocate(ItemStack stack) {
    if (stack != null) {
      if (stack.getItem() == granite || stack.getItem() == diorite || stack.getItem() == andesite) {
        return Ref.BlockStat.RECIPROCATOR_STONE_TIME;
      }
      if (stack.getItem() == wheat_seeds || stack.getItem() == pumpkin_seeds || stack.getItem() == melon_seeds) {
        return Ref.BlockStat.RECIPROCATOR_PLANT_TIME;
      }
      if (stack.getUnlocalizedName().equals(oak.getUnlocalizedName()) || stack.getUnlocalizedName().equals(spruce.getUnlocalizedName()) || stack.getUnlocalizedName().equals(birch.getUnlocalizedName()) || stack.getUnlocalizedName().equals(jungle.getUnlocalizedName()) || stack.getUnlocalizedName().equals(acacia.getUnlocalizedName()) || stack.getUnlocalizedName().equals(darkoak.getUnlocalizedName())) {
        return Ref.BlockStat.RECIPROCATOR_PLANT_TIME;
      }
      if (stack.getUnlocalizedName().equals(coal.getUnlocalizedName()) || stack.getUnlocalizedName().equals(charcoal.getUnlocalizedName())) {
        return Ref.BlockStat.RECIPROCATOR_FUEL_TIME;
      }
    }
    return Ref.BlockStat.STABILIZER_TIME;
  }

  public static ItemStack getResult(ItemStack input) {
    ItemStack stack = null;
    // cobblestone
    if (input.getItem() == granite || input.getItem() == diorite || input.getItem() == andesite)
      stack = new ItemStack(Blocks.cobblestone, 1);
    // saplings
    if (input.getUnlocalizedName().equals(oak.getUnlocalizedName())) {
      stack = spruce;
    } else if (input.getUnlocalizedName().equals(spruce.getUnlocalizedName())) {
      stack = birch;
    } else if (input.getUnlocalizedName().equals(birch.getUnlocalizedName())) {
      stack = jungle;
    } else if (input.getUnlocalizedName().equals(jungle.getUnlocalizedName())) {
      stack = acacia;
    } else if (input.getUnlocalizedName().equals(acacia.getUnlocalizedName())) {
      stack = darkoak;
    } else if (input.getUnlocalizedName().equals(darkoak.getUnlocalizedName())) {
      stack = oak;
    }
    // seeds
    if (input.getItem() == wheat_seeds) {
      stack = new ItemStack(pumpkin_seeds);
    } else if (input.getItem() == pumpkin_seeds) {
      stack = new ItemStack(melon_seeds);
    } else if (input.getItem() == melon_seeds) {
      stack = new ItemStack(wheat_seeds);
    }
    // fuel
    if (input.getUnlocalizedName().equals(coal.getUnlocalizedName())) {
      stack = charcoal;
    } else if (input.getUnlocalizedName().equals(charcoal.getUnlocalizedName())) {
      stack = coal;
    }
    return stack;
  }

  public static float getExperience(ItemStack output) {
    if (output != null) {
      // cobblestone
      if (output.getItem() == granite || output.getItem() == diorite || output.getItem() == andesite) {
        return 0.1F;
      }
      // saplings
      if (output.getUnlocalizedName().equals(oak.getUnlocalizedName()) || output.getUnlocalizedName().equals(spruce.getUnlocalizedName()) || output.getUnlocalizedName().equals(birch.getUnlocalizedName()) || output.getUnlocalizedName().equals(jungle.getUnlocalizedName()) || output.getUnlocalizedName().equals(acacia.getUnlocalizedName()) || output.getUnlocalizedName().equals(darkoak.getUnlocalizedName())) {
        return 0.25F;
      }
      // seeds
      if (output.getItem() == wheat_seeds || output.getItem() == pumpkin_seeds || output.getItem() == melon_seeds) {
        return 0.2F;
      }
      // fuel
      if (output.getUnlocalizedName().equals(coal.getUnlocalizedName()) || output.getUnlocalizedName().equals(charcoal.getUnlocalizedName())) {
        return 0.1F;
      }
    }
    return 0F;
  }

  public static boolean isValid(Item item) {
    if (item == granite || item == diorite || item == andesite || item == oak.getItem() || item == spruce.getItem() || item == birch.getItem() || item == jungle.getItem() || item == acacia.getItem() || item == darkoak.getItem() || item == wheat_seeds || item == pumpkin_seeds || item == melon_seeds || item == coal.getItem() || item == charcoal.getItem()) {
      return true;
    }
    return false;
  }

}
