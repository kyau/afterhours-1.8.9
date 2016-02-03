package net.kyau.afterhours;

import net.kyau.afterhours.items.ModItems;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;


public class FMLEventHandler {

  @SubscribeEvent
  public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
    ItemStack item = event.crafting;
    EntityPlayer player = event.player;
    World world = player.worldObj;
    if (!world.isRemote) {
      if (item.getUnlocalizedName().equals(ModItems.voidstone.getUnlocalizedName())) {
        int count = countItems(player, ModItems.voidstone.getUnlocalizedName());
        if (count > 0) {
          if (ModInfo.DEBUG) player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.GREEN + "crafting successful!"));
          removeLimitedItem(player, item);
        }
      } else {
        if (ModInfo.DEBUG) player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.GREEN + "crafting successful!"));
      }
    }
  }

  public static int countItems(EntityPlayer player, String item) {
    int count = 0;
    for (int i = 0; i < player.inventory.mainInventory.length; ++i) {
      ItemStack stack = player.inventory.mainInventory[i];
      if (stack != null) {
        if (stack.getUnlocalizedName() == item) {
            count += stack.stackSize;
        }
      }
    }
    if (player.getInventoryEnderChest() != null) {
      for (int i = 0; i < player.getInventoryEnderChest().getSizeInventory(); ++i) {
        ItemStack stack = player.getInventoryEnderChest().getStackInSlot(i);
        if (stack != null) {
          if (stack.getUnlocalizedName() == item) {
              count += stack.stackSize;
          }
        }
      }
    }
    // very spammy due to playertick handler
    //if (ModInfo.DEBUG) player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.GRAY + item + " found: " + count));
    return count;
  }

  // remove limited items that do not belong to the player
  public static void removeLimitedItem(EntityPlayer player, ItemStack stack) {
    // check player inventory
    IInventory inv = player.inventory;
    searchInventory(inv, player, stack, 0);
    // check ender chest inventory
    if (player.getInventoryEnderChest() != null) {
      inv = player.getInventoryEnderChest();
      searchInventory(inv, player, stack, 1);
    }
  }

  public static void searchInventory(IInventory inv, EntityPlayer player, ItemStack stack, int chest) {
    String location = "inventory";
    if (chest == 1) location = "ender chest";
    for(int i=0; i < inv.getSizeInventory(); i++) {
      if(inv.getStackInSlot(i) != null) {
        ItemStack j = inv.getStackInSlot(i);
        if(j.getItem() != null && j.getItem() == stack.getItem()) {
          if (j.getTagCompound() != null) {
            if (j.getTagCompound().getString("Owner").equals(player.getDisplayNameString())) {
              if (ModInfo.DEBUG) player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.DARK_AQUA + "item found, saved!"));
            } else {
              if (ModInfo.DEBUG) player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.RED + stack.getUnlocalizedName().substring(11) + " removed from "+location+"!"));
              inv.setInventorySlotContents(i, null);
              Slot slot = player.openContainer.getSlotFromInventory(player.inventory, i);
              ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(player.openContainer.windowId, slot.slotNumber, null));
            }
          } else {
            if (ModInfo.DEBUG) player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.RED + stack.getUnlocalizedName().substring(11) + " removed from "+location+"!"));
            inv.setInventorySlotContents(i, null);
            Slot slot = player.openContainer.getSlotFromInventory(player.inventory, i);
            ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(player.openContainer.windowId, slot.slotNumber, null));
          }
        }
      }
    }
  }
}
