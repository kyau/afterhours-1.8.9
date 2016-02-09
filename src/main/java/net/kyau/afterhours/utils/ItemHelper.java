package net.kyau.afterhours.utils;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemHelper {

  public static ItemStack cloneItemStack(ItemStack stack, int stackSize) {

    ItemStack clonedStack = stack.copy();
    clonedStack.stackSize = stackSize;
    return clonedStack;
  }

  /**
   * Compares two ItemStacks for equality, testing itemID, metaData, stackSize, and their NBTTagCompounds (if they are
   * present)
   *
   * @param stack1
   *          The first ItemStack being tested for equality
   * @param stack2
   *          The second ItemStack being tested for equality
   * @return true if the two ItemStacks are equivalent, false otherwise
   */
  public static boolean equals(ItemStack stack1, ItemStack stack2) {
    return (Comparators.idComparator.compare(stack1, stack2) == 0);
  }

  public static boolean equalsIgnoreStackSize(ItemStack stack1, ItemStack stack2) {

    if (stack1 != null && stack2 != null) {
      // Sort on itemID
      if (Item.getIdFromItem(stack1.getItem()) - Item.getIdFromItem(stack2.getItem()) == 0) {
        // Sort on item
        if (stack1.getItem() == stack2.getItem()) {
          // Then sort on meta
          if (stack1.getItemDamage() == stack2.getItemDamage()) {
            // Then sort on NBT
            if (stack1.hasTagCompound() && stack2.hasTagCompound()) {
              // Then sort on stack size
              if (ItemStack.areItemStackTagsEqual(stack1, stack2)) {
                return true;
              }
            } else if (!stack1.hasTagCompound() && !stack2.hasTagCompound()) {
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  public static int compare(ItemStack stack1, ItemStack stack2) {
    return Comparators.idComparator.compare(stack1, stack2);
  }

  public static String toString(ItemStack stack) {

    if (stack != null) {
      if (stack.hasTagCompound()) {
        return String.format("%sxitemStack[%s@%s:%s]", stack.stackSize, stack.getUnlocalizedName(), stack.getItemDamage(), stack.getTagCompound());
      } else {
        return String.format("%sxitemStack[%s@%s]", stack.stackSize, stack.getUnlocalizedName(), stack.getItemDamage());
      }
    }

    return "null";
  }

  public static boolean hasOwner(ItemStack stack) {
    return (NBTHelper.hasTag(stack, "UUIDMostSig") && NBTHelper.hasTag(stack, "UUIDLeastSig")) || NBTHelper.hasTag(stack, "Owner");
  }

  public static boolean hasOwnerUUID(ItemStack stack) {
    return NBTHelper.hasTag(stack, "UUIDMostSig") && NBTHelper.hasTag(stack, "UUIDLeastSig");
  }

  public static boolean hasOwnerName(ItemStack stack) {
    return NBTHelper.hasTag(stack, "Owner");
  }

  public static String getOwnerName(ItemStack stack) {

    if (NBTHelper.hasTag(stack, "Owner")) {
      return NBTHelper.getString(stack, "Owner");
    }

    return null;
  }

  public static UUID getOwnerUUID(ItemStack stack) {

    if (NBTHelper.hasTag(stack, "UUIDMostSig") && NBTHelper.hasTag(stack, "UUIDLeastSig")) {
      return new UUID(NBTHelper.getLong(stack, "UUIDMostSig"), NBTHelper.getLong(stack, "UUIDLeastSig"));
    }

    return null;
  }

  public static void setOwner(ItemStack stack, EntityPlayer player) {
    setOwnerName(stack, player);
    setOwnerUUID(stack, player);
  }

  public static void setOwnerUUID(ItemStack stack, EntityPlayer player) {
    NBTHelper.setLong(stack, "UUIDMostSig", player.getUniqueID().getMostSignificantBits());
    NBTHelper.setLong(stack, "UUIDLeastSig", player.getUniqueID().getLeastSignificantBits());
  }

  public static void setOwnerName(ItemStack stack, EntityPlayer player) {
    NBTHelper.setString(stack, "Owner", player.getDisplayNameString());
  }

}