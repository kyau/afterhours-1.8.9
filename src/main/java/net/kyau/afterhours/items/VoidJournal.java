package net.kyau.afterhours.items;

import java.util.List;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.references.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class VoidJournal extends BaseItem {

  public VoidJournal() {
    super();
    this.setUnlocalizedName(Names.Items.VOIDJOURNAL);
    this.maxStackSize = 1;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    AfterHours.proxy.openJournal();
    return super.onItemRightClick(stack, world, player);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
    // Description
    tooltip.add(EnumChatFormatting.GRAY + "Written by: " + EnumChatFormatting.WHITE + player.getDisplayNameString());
    super.addInformation(stack, player, tooltip, advanced);
  }
}
