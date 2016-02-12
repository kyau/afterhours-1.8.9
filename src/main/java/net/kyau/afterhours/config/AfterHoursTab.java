package net.kyau.afterhours.config;

import net.kyau.afterhours.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class AfterHoursTab extends CreativeTabs {

  public AfterHoursTab(int index, String label) {
    super(index, label);
  }

  @Override
  public Item getTabIconItem() {
    return ModItems.voidpearl;
  }

  public String getTranslatedTabLabel() {
    return "AfterHours";
  }

}
