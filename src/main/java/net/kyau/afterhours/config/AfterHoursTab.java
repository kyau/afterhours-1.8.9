package net.kyau.afterhours.config;

import net.kyau.afterhours.items.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class AfterHoursTab extends CreativeTabs {

  public AfterHoursTab(int index, String label) {
    super(index, label);
  }

  @Override
  public Item getTabIconItem() {
    return ModItems.voidstone;
  }

  public String getTranslatedTabLabel() {
    return "AfterHours";
  }

}
