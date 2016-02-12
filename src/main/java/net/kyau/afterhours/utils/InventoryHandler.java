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

  private static int ownerCount = 0;
  private static int unboundCount = 0;
  private static ArrayList<Integer> owner = new ArrayList<Integer>();
  private static ArrayList<IInventory> ownerInv = new ArrayList<IInventory>();
  private static ArrayList<Integer> unbound = new ArrayList<Integer>();
  private static ArrayList<IInventory> unboundInv = new ArrayList<IInventory>();

  public static int countItems(EntityPlayer player, Item item) {
    String itemName = item.getUnlocalizedName();
    int count = 0;
    for (int i = 0; i < player.inventory.mainInventory.length; ++i) {
      ItemStack stack = player.inventory.mainInventory[i];
      if (stack != null) {
        if (stack.getUnlocalizedName().equals(itemName)) {
          count += stack.stackSize;
        }
      }
    }
    if (player.getInventoryEnderChest() != null) {
      for (int i = 0; i < player.getInventoryEnderChest().getSizeInventory(); ++i) {
        ItemStack stack = player.getInventoryEnderChest().getStackInSlot(i);
        if (stack != null) {
          if (stack.getUnlocalizedName().equals(itemName)) {
            count += stack.stackSize;
          }
        }
      }
    }
    return count;
  }

  // remove limited items that do not belong to the player
  public static void removeLimitedItem(EntityPlayer player, ItemStack stack) {
    ownerCount = 0;
    unboundCount = 0;
    owner.clear();
    unbound.clear();
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
    for (int i = 0; i < inv.getSizeInventory(); i++) {
      if (inv.getStackInSlot(i) != null) {
        ItemStack stackFound = inv.getStackInSlot(i);
        if (stackFound.getItem() != null && stackFound.getItem().getUnlocalizedName().equals(stack.getItem().getUnlocalizedName())) {
          if (stackFound.getTagCompound() != null) {
            // if the voidstone belongs to the player
            if (ItemHelper.getOwnerName(stackFound).equals(player.getDisplayNameString()) && ownerCount < 1) {
              if (ModInfo.DEBUG)
                LogHelper.info("> DEBUG: item found (" + ItemHelper.getOwnerName(stackFound) + "), saved!");
              owner.add(ownerCount, i);
              ownerInv.add(ownerCount, inv);
              ownerCount++;
              if (unboundCount > 0) {
                for (int z = 0; z < unboundCount; z++) {
                  ItemStack tmpItem = unboundInv.get(z).getStackInSlot(unbound.get(z));
                  removeItemFromSlot(unboundInv.get(z), player, chest, unbound.get(z), tmpItem);
                }
              }
            } else if (ItemHelper.getOwnerName(stackFound).equals(player.getDisplayNameString()) && ownerCount == 1) {
              // remove the voidstone with the lowest cooldown
              if (ModInfo.DEBUG)
                LogHelper.info("> DEBUG: removing...");
              ItemStack prev = ownerInv.get(0).getStackInSlot(owner.get(0));
              if (NBTHelper.getLong(stackFound, "LastUse") > NBTHelper.getLong(prev, "LastUse")) {
                removeItemFromSlot(ownerInv.get(0), player, chest, owner.get(0), prev);
              } else {
                removeItemFromSlot(inv, player, chest, i, stack);
              }
              // remove the unbound voidstone
              if (unboundCount > 0) {
                for (int z = 0; z < unboundCount; z++) {
                  ItemStack tmpItem = unboundInv.get(z).getStackInSlot(unbound.get(z));
                  removeItemFromSlot(unboundInv.get(z), player, chest, unbound.get(z), tmpItem);
                }
              }
            } else {
              if (unboundCount > 0 || ownerCount > 0) {
                if (ModInfo.DEBUG)
                  LogHelper.info("> DEBUG: removing...");
                removeItemFromSlot(inv, player, chest, i, stack);
              }
              unbound.add(unboundCount, i);
              unboundInv.add(unboundCount, inv);
              unboundCount++;
            }
          } else {
            if (unboundCount > 0 || ownerCount > 0) {
              if (ModInfo.DEBUG)
                LogHelper.info("> DEBUG: removing...");
              removeItemFromSlot(inv, player, chest, i, stack);
            }
            unbound.add(unboundCount, i);
            unboundInv.add(unboundCount, inv);
            unboundCount++;
          }
          LogHelper.info("> DEBUG: ownerCount =  " + ownerCount + ", unboundCount = " + unboundCount);
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
