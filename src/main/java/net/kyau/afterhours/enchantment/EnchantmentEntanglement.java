package net.kyau.afterhours.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentEntanglement extends Enchantment {

  public EnchantmentEntanglement(int id, int weight) {
    super(id, new ResourceLocation("entanglement"), weight, EnumEnchantmentType.ALL);
    setName("entanglement");
    addToBookList(this);
  }

  @Override
  public boolean canApply(ItemStack stack) {
    return true;
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

  public static boolean isEntangled(ItemStack stack) {
    return EnchantmentHelper.getEnchantmentLevel(85, stack) > 0;
  }
}
