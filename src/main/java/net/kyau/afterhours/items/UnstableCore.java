package net.kyau.afterhours.items;

import java.util.List;

import net.kyau.afterhours.references.Ref;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UnstableCore extends BaseItem {

  public UnstableCore() {
    super();
    this.setUnlocalizedName(Ref.ItemID.UNSTABLECORE);
    this.maxStackSize = 1;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    return super.onItemRightClick(stack, world, player);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
    tooltip.add(EnumChatFormatting.GRAY + "" + EnumChatFormatting.OBFUSCATED + "Unstable Core");
    super.addInformation(stack, player, tooltip, advanced);
  }
}
