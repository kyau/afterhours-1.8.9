package net.kyau.afterhours.utils;

import java.util.Comparator;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/*
 * Comparators.java
 * Source: Equivalent Exchange 3 by Pahimar
 * License: GPLv3
 * 
 */
public class Comparators {

  public static Comparator<String> stringComparator = new Comparator<String>() {

    @Override
    public int compare(String string1, String string2) {
      return string1.compareToIgnoreCase(string2);
    }
  };

  public static Comparator<ItemStack> idComparator = new Comparator<ItemStack>() {

    public int compare(ItemStack stack1, ItemStack stack2) {
      if (stack1 != null && stack2 != null) {
        // Sort on itemID
        if (Item.getIdFromItem(stack1.getItem()) - Item.getIdFromItem(stack2.getItem()) == 0) {
          // Sort on item
          if (stack1.getItem() == stack2.getItem()) {
            // Then sort on meta
            if ((stack1.getItemDamage() == stack2.getItemDamage()) || stack1.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack2.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
              // Then sort on NBT
              if (stack1.hasTagCompound() && stack2.hasTagCompound()) {
                // Then sort on stack size
                if (ItemStack.areItemStackTagsEqual(stack1, stack2)) {
                  return (stack1.stackSize - stack2.stackSize);
                } else {
                  return (stack1.getTagCompound().hashCode() - stack2.getTagCompound().hashCode());
                }
              } else if (!(stack1.hasTagCompound()) && stack2.hasTagCompound()) {
                return -1;
              } else if (stack1.hasTagCompound() && !(stack2.hasTagCompound())) {
                return 1;
              } else {
                return (stack1.stackSize - stack2.stackSize);
              }
            } else {
              return (stack1.getItemDamage() - stack2.getItemDamage());
            }
          } else {
            return stack1.getItem().getUnlocalizedName(stack1).compareToIgnoreCase(stack2.getItem().getUnlocalizedName(stack2));
          }
        } else {
          return Item.getIdFromItem(stack1.getItem()) - Item.getIdFromItem(stack2.getItem());
        }
      } else if (stack1 != null) {
        return -1;
      } else if (stack2 != null) {
        return 1;
      } else {
        return 0;
      }
    }
  };

  public static Comparator<ItemStack> reverseIdComparator = new Comparator<ItemStack>() {

    @Override
    public int compare(ItemStack stack1, ItemStack stack2) {
      return idComparator.compare(stack1, stack2) * -1;
    }
  };

  public static Comparator<ItemStack> displayNameComparator = new Comparator<ItemStack>() {

    public int compare(ItemStack stack1, ItemStack stack2) {
      if (stack1 != null && stack2 != null) {
        if (stack1.getDisplayName().equalsIgnoreCase(stack2.getDisplayName())) {
          return idComparator.compare(stack1, stack2);
        } else {
          return stack1.getDisplayName().compareToIgnoreCase(stack2.getDisplayName());
        }
      } else if (stack1 != null) {
        return -1;
      } else if (stack2 != null) {
        return 1;
      } else {
        return 0;
      }
    }
  };

  public static Comparator<ItemStack> reverseDisplayNameComparator = new Comparator<ItemStack>() {

    @Override
    public int compare(ItemStack stack1, ItemStack stack2) {
      return displayNameComparator.compare(stack1, stack2) * -1;
    }
  };

}