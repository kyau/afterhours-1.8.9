package net.kyau.afterhours.items.darkmatter;

import java.util.Set;

import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.references.Ref;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.google.common.collect.Sets;

public class DarkMatterAxe extends DarkMatterTool {

  private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] {
      Blocks.planks,
      Blocks.bookshelf,
      Blocks.log,
      Blocks.log2,
      Blocks.chest,
      Blocks.pumpkin,
      Blocks.lit_pumpkin,
      Blocks.melon_block,
      Blocks.ladder });

  public DarkMatterAxe(ToolMaterial material) {
    super(3.0F, material, EFFECTIVE_ON);
    this.setUnlocalizedName(Ref.ItemID.DARKMATTER_AXE);
    ModItems.repairList.add(this.getUnlocalizedName());
  }

  @Override
  public float getStrVsBlock(ItemStack stack, Block block) {
    return block.getMaterial() != Material.wood && block.getMaterial() != Material.plants && block.getMaterial() != Material.vine ? super.getStrVsBlock(stack, block) : this.efficiencyOnProperMaterial;
  }

}
