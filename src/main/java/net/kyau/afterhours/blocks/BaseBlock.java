package net.kyau.afterhours.blocks;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.references.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BaseBlock extends Block {

  public BaseBlock() {
    this(Material.rock);
  }

  public BaseBlock(Material material) {
    super(material);
    this.setCreativeTab(AfterHours.AfterHoursTab);
  }

  @Override
  public String getUnlocalizedName() {
    return String.format("%s.%s", ModInfo.MOD_ID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
  }

  protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
    return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
  }

}
