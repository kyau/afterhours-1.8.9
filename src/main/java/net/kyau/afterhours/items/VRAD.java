package net.kyau.afterhours.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.references.ItemTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class VRAD extends Item {

  ItemTypes type;

  public VRAD(ItemTypes type) {
    super();
    setUnlocalizedName("afterhours.vrad");
    maxStackSize = 1;
    setCreativeTab(AfterHours.AfterHoursTab);
    this.type = type;
  }

  public ItemTypes getType() {
    return type;
  }

  @Override
  public String getUnlocalizedName() {
    return "afterhours.vrad";
  }

  @Override
  public String getUnlocalizedName(ItemStack itemStack) {
    return "afterhours.vrad";
  }

  @Override
  public boolean getShareTag() {
    return true;
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
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
    // Description
    if (Keyboard.isKeyDown(0x2A) || Keyboard.isKeyDown(0x36)) {
      list.add(EnumChatFormatting.GRAY + "The VRAD or Void Remote Access Device");
      list.add(EnumChatFormatting.GRAY + "is used to remotely connect through");
      list.add(EnumChatFormatting.GRAY + "the void to secure storage containers.");
    } else {
      list.add(EnumChatFormatting.GRAY + "Hold " + EnumChatFormatting.WHITE + "SHIFT" + EnumChatFormatting.GRAY + " for more information.");
    }
    super.addInformation(stack, player, list, advanced);
  }
}
