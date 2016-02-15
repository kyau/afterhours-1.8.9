package net.kyau.afterhours.config;

import java.util.List;

import net.kyau.afterhours.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AfterHoursTab extends CreativeTabs {

  public AfterHoursTab(int index, String label) {
    super(index, label);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public Item getTabIconItem() {
    return ModItems.darkmatter;
  }

  public String getTranslatedTabLabel() {
    // return "AfterHours";
    return StatCollector.translateToLocal("afterhours.creativetab");
  }

  @Override
  public void displayAllReleventItems(List itemsToShowOnTab) {
    for (Object itemObject : Item.itemRegistry) {
      Item item = (Item) itemObject;
      if (item != null) {
        if (item.getUnlocalizedName().contains("afterhours.")) {
          item.getSubItems(item, this, itemsToShowOnTab); // add all sub items to the list
        }
      }
    }
  }

}
