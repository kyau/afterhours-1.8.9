package net.kyau.afterhours.blocks;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.init.ModBlocks;
import net.kyau.afterhours.references.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BaseBlock extends Block {

  public BaseBlock() {
    this(Material.rock);
  }

  public BaseBlock(Material material) {
    super(material);
    this.setCreativeTab(AfterHours.AfterHoursTab);
  }

  public Block register(String name) {
    GameRegistry.registerBlock(this, name);
    ModBlocks.blockList.add(this);
    return this;
  }

  @Override
  public String getUnlocalizedName() {
    return String.format("%s.%s", ModInfo.MOD_ID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
  }

  protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
    return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
  }

}
