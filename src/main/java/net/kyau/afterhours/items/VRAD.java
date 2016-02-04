package net.kyau.afterhours.items;

import java.util.List;

import net.kyau.afterhours.references.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

public class VRAD extends BaseItem {

  public VRAD() {
    super();
    this.setUnlocalizedName(Names.Items.VRAD);
    this.maxStackSize = 1;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    if (!world.isRemote) {
      if (player.isSneaking()) {
        InventoryEnderChest inventoryenderchest = player.getInventoryEnderChest();
        if (inventoryenderchest != null)
          player.displayGUIChest(inventoryenderchest);
      }
    }
    return super.onItemRightClick(stack, world, player);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
    // Description
    if (Keyboard.isKeyDown(0x2A) || Keyboard.isKeyDown(0x36)) {
      tooltip.add(EnumChatFormatting.GRAY + "The VRAD or Void Remote Access Device");
      tooltip.add(EnumChatFormatting.GRAY + "is used to remotely connect through");
      tooltip.add(EnumChatFormatting.GRAY + "the void to secure storage containers.");
    } else {
      tooltip.add(EnumChatFormatting.GRAY + "Hold " + EnumChatFormatting.WHITE + "SHIFT" + EnumChatFormatting.GRAY + " for more information.");
    }
    super.addInformation(stack, player, tooltip, advanced);
  }
}
