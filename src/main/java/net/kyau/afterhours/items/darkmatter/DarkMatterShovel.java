package net.kyau.afterhours.items.darkmatter;

import java.util.Set;

import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.items.BaseItem;
import net.kyau.afterhours.references.Ref;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import com.google.common.collect.Sets;

public class DarkMatterShovel extends DarkMatterTool {

  private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] {
      Blocks.clay,
      Blocks.dirt,
      Blocks.farmland,
      Blocks.grass,
      Blocks.gravel,
      Blocks.mycelium,
      Blocks.sand,
      Blocks.snow,
      Blocks.snow_layer,
      Blocks.soul_sand });

  public DarkMatterShovel(BaseItem.ToolMaterial material) {
    super(1.0F, material, EFFECTIVE_ON);
    this.setUnlocalizedName(Ref.ItemID.DARKMATTER_SHOVEL);
    ModItems.repairList.add(this.getUnlocalizedName());
  }

  @Override
  public boolean canHarvestBlock(Block block) {
    return block == Blocks.snow_layer ? true : block == Blocks.snow;
  }

}
