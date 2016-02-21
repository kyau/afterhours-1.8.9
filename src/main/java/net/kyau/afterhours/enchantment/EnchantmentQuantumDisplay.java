package net.kyau.afterhours.enchantment;

import net.kyau.afterhours.references.Ref;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentQuantumDisplay extends Enchantment {

  public EnchantmentQuantumDisplay(int id, int weight) {
    super(id, new ResourceLocation(Ref.Enchant.QUANTUMDISPLAY.toLowerCase().replaceAll("\\s", "")), weight, EnumEnchantmentType.ALL);
    setName(Ref.Enchant.QUANTUMDISPLAY.toLowerCase().replaceAll("\\s", ""));
    // addToBookList(this);
  }

  @Override
  public boolean canApply(ItemStack stack) {
    return false;
  }

  @Override
  public int getMaxEnchantability(int level) {
    return super.getMaxEnchantability(level) + 42;
  }

  @Override
  public int getMinEnchantability(int level) {
    return super.getMinEnchantability(level);
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  public static boolean hasQuantumDisplay(ItemStack stack) {
    return EnchantmentHelper.getEnchantmentLevel(Ref.Enchant.QUANTUMDISPLAY_ID, stack) > 0;
  }
}
