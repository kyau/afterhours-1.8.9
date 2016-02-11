package net.kyau.afterhours.items;

import java.util.List;

import net.kyau.afterhours.references.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RawHide extends BaseItem {

  public RawHide() {
    super();
    this.setUnlocalizedName(Names.Items.RAWHIDE);
    this.maxStackSize = 64;
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
