package net.kyau.afterhours.init;

import net.kyau.afterhours.enchantment.EnchantmentEntanglement;
import net.minecraft.enchantment.Enchantment;

public class ModEnchants {

  public static Enchantment entanglement;

  public static void registerEnchants() {
    entanglement = new EnchantmentEntanglement(85, 4);
  }

  private static int getEmptyEnchantId() {
    for (int i = 0; i < Enchantment.enchantmentsBookList.length; i++) {
      if (Enchantment.enchantmentsBookList[i] == null) {
        return i;
      }
    }
    return -1;
  }

}
