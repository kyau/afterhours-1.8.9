package net.kyau.afterhours.items;

import java.util.List;

import net.kyau.afterhours.references.Ref;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StableCore extends BaseItem {

  public StableCore() {
    super();
    this.setUnlocalizedName(Ref.ItemID.STABLECORE);
    this.maxStackSize = 1;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    return super.onItemRightClick(stack, world, player);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
    super.addInformation(stack, player, tooltip, advanced);
  }
}
