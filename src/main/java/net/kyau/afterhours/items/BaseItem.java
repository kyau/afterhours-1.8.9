package net.kyau.afterhours.items;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.references.ModInfo;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BaseItem extends Item {

  public BaseItem() {
    super();
    this.maxStackSize = 1;
    this.setCreativeTab(AfterHours.AfterHoursTab);
    this.setNoRepair();
  }

  @Override
  public boolean getShareTag() {
    return true;
  }

  @Override
  public String getUnlocalizedName() {
    return String.format("%s.%s", ModInfo.MOD_ID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
  }

  @Override
  public String getUnlocalizedName(ItemStack itemStack) {
    return String.format("%s.%s", ModInfo.MOD_ID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
  }

  protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
    return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
  }
}
