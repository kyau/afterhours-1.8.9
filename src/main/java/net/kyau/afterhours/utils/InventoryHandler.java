package net.kyau.afterhours.utils;

import java.util.ArrayList;

import net.kyau.afterhours.references.ModInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;

public class InventoryHandler {

  public static int countItems(EntityPlayer player, Item item) {
    String itemName = item.getUnlocalizedName();
    int count = 0;
    for (int i = 0; i < player.inventory.mainInventory.length; ++i) {
      ItemStack stack = player.inventory.mainInventory[i];
      if (stack != null) {
        if (stack.getUnlocalizedName().equals(itemName)) {
          // if (stack.getUnlocalizedName().equals("afterhours.voidstone"))
          // System.out.println("inventory slot: " + i);
          count += stack.stackSize;
        }
      }
    }
    if (player.getInventoryEnderChest() != null) {
      for (int i = 0; i < player.getInventoryEnderChest().getSizeInventory(); ++i) {
        ItemStack stack = player.getInventoryEnderChest().getStackInSlot(i);
        if (stack != null) {
          if (stack.getUnlocalizedName().equals(item)) {
            count += stack.stackSize;
          }
        }
      }
    }
    // very spammy due to playertick handler
    // if (ModInfo.DEBUG)
    // player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " +
    // EnumChatFormatting.GRAY + item + " found: " + count));
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
    ArrayList<Integer> owner = new ArrayList<Integer>();
    int ownerCount = 0;
    ArrayList<Integer> unboundOwner = new ArrayList<Integer>();
    int unboundCount = 0;
    for (int i = 0; i < inv.getSizeInventory(); i++) {
      if (inv.getStackInSlot(i) != null) {
        ItemStack j = inv.getStackInSlot(i);
        if (j.getItem() != null && j.getItem() == stack.getItem()) {
          if (j.getTagCompound() != null) {
            // if the voidstone belongs to the player
            if (j.getTagCompound().getString("Owner").equals(player.getDisplayNameString()) && ownerCount < 1) {
              if (ModInfo.DEBUG)
                LogHelper.info("> DEBUG: item found (" + j.getTagCompound().getString("Owner") + "), saved!");
              owner.add(ownerCount, i);
              ownerCount++;
              if (unboundCount > 0) {
                for (int z = 0; z < unboundCount; z++) {
                  ItemStack tmpItem = inv.getStackInSlot(unboundOwner.get(z));
                  removeItemFromSlot(inv, player, chest, unboundOwner.get(z), tmpItem);
                }
              }
            } else if (j.getTagCompound().getString("Owner").equals(player.getDisplayNameString()) && ownerCount == 1) {
              // remove the voidstone with the lowest cooldown
              if (ModInfo.DEBUG)
                LogHelper.info("> DEBUG: removing...");
              ItemStack prev = inv.getStackInSlot(owner.get(0));
              if (j.getTagCompound().getLong("LastUse") > prev.getTagCompound().getLong("LastUse")) {
                removeItemFromSlot(inv, player, chest, owner.get(0), prev);
              } else {
                removeItemFromSlot(inv, player, chest, i, stack);
              }
            } else {
              if (unboundCount > 0 || ownerCount > 0) {
                if (ModInfo.DEBUG)
                  LogHelper.info("> DEBUG: removing...");
                removeItemFromSlot(inv, player, chest, i, stack);
              }
              unboundOwner.add(unboundCount, i);
              unboundCount++;
            }
          } else {
            if (unboundCount > 0 || ownerCount > 0) {
              if (ModInfo.DEBUG)
                LogHelper.info("> DEBUG: removing...");
              removeItemFromSlot(inv, player, chest, i, stack);
            }
            unboundOwner.add(unboundCount, i);
            unboundCount++;
          }
        }
      }
    }
  }

  private static void removeItemFromSlot(IInventory inv, EntityPlayer player, int chest, int i, ItemStack stack) {
    String location = "inventory";
    if (chest == 1)
      location = "ender chest";
    if (ModInfo.DEBUG)
      LogHelper.info("> DEBUG: " + stack.getUnlocalizedName().substring(11) + " removed from " + location + "!");
    inv.setInventorySlotContents(i, null);
    Slot slot = player.openContainer.getSlotFromInventory(player.inventory, i);
    ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(player.openContainer.windowId, slot.slotNumber, null));
  }
}
