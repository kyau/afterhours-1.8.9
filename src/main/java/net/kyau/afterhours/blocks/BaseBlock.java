package net.kyau.afterhours.blocks;

import net.kyau.afterhours.AfterHours;
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

}
