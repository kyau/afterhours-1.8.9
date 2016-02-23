package net.kyau.afterhours.init;

import java.util.ArrayList;
import java.util.List;

import net.kyau.afterhours.enchantment.EnchantmentAbsorption;
import net.kyau.afterhours.enchantment.EnchantmentEntanglement;
import net.kyau.afterhours.enchantment.EnchantmentGravitation;
import net.kyau.afterhours.enchantment.EnchantmentQuantumBoost;
import net.kyau.afterhours.enchantment.EnchantmentQuantumDisplay;
import net.kyau.afterhours.references.Ref;
import net.minecraft.enchantment.Enchantment;

public class ModEnchants {

  private static List<Integer> idList = new ArrayList<Integer>();

  public static Enchantment absorption;
  public static Enchantment entanglement;
  public static Enchantment gravitation;
  public static Enchantment quantumBoost;
  public static Enchantment quantumDisplay;

  public static void registerEnchants() {
    absorption = new EnchantmentAbsorption(Ref.Enchant.ABSORPTION_ID, 4);
    entanglement = new EnchantmentEntanglement(Ref.Enchant.ENTANGLEMENT_ID, 4);
    gravitation = new EnchantmentGravitation(Ref.Enchant.GRAVITATION_ID, 4);
    quantumBoost = new EnchantmentQuantumBoost(Ref.Enchant.QUANTUMBOOST_ID, 4);
    quantumDisplay = new EnchantmentQuantumDisplay(Ref.Enchant.QUANTUMDISPLAY_ID, 4);
  }

  public static int getEmptyEnchantId() {
    for (int i = 65; i < 256; i++) {
      if (Enchantment.getEnchantmentById(i) == null) {
        boolean found = false;
        for (Integer used : idList) {
          if (i == used)
            found = true;
        }
        if (!found) {
          idList.add(i);
          return i;
        }
      }
    }
    return -1;
  }

}
