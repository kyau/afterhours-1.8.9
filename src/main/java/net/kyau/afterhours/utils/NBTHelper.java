package net.kyau.afterhours.utils;

import java.util.UUID;

import net.kyau.afterhours.references.Ref;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTHelper {

  public static void clearStatefulNBTTags(ItemStack itemStack) {
    if (NBTHelper.hasTag(itemStack, "craftingGuiOpen")) {
      NBTHelper.removeTag(itemStack, "craftingGuiOpen");
    } else if (NBTHelper.hasTag(itemStack, Ref.NBT.VRD_GUI_OPEN)) {
      NBTHelper.removeTag(itemStack, Ref.NBT.VRD_GUI_OPEN);
    }
  }

  public static boolean hasTag(ItemStack stack, String keyName) {
    return stack != null && stack.getTagCompound() != null && stack.getTagCompound().hasKey(keyName);
  }

  public static void removeTag(ItemStack stack, String keyName) {
    if (stack.getTagCompound() != null) {
      stack.getTagCompound().removeTag(keyName);
    }
  }

  public static boolean hasUUID(ItemStack stack) {
    return hasTag(stack, Ref.NBT.UUID_MOST_SIG) && hasTag(stack, Ref.NBT.UUID_LEAST_SIG);
  }

  public static void setUUID(ItemStack stack) {
    initNBTTagCompound(stack);

    // Set a UUID if one doesn't exist already
    if (!hasTag(stack, Ref.NBT.UUID_MOST_SIG) && !hasTag(stack, Ref.NBT.UUID_LEAST_SIG)) {
      UUID itemUUID = UUID.randomUUID();
      setLong(stack, Ref.NBT.UUID_MOST_SIG, itemUUID.getMostSignificantBits());
      setLong(stack, Ref.NBT.UUID_LEAST_SIG, itemUUID.getLeastSignificantBits());
    }
  }

  public static void setLastUse(ItemStack stack, long lastUse) {
    long ticksSinceLastUse;
    initNBTTagCompound(stack);

    setLong(stack, Ref.NBT.LASTUSE, lastUse);
  }

  /**
   * Initializes the NBT Tag Compound for the given ItemStack if it is null
   *
   * @param stack
   *          The ItemStack for which its NBT Tag Compound is being checked for initialization
   */
  private static void initNBTTagCompound(ItemStack stack) {
    if (stack.getTagCompound() == null) {
      stack.setTagCompound(new NBTTagCompound());
    }
  }

  public static void setLong(ItemStack stack, String keyName, long keyValue) {
    initNBTTagCompound(stack);

    stack.getTagCompound().setLong(keyName, keyValue);
  }

  // String
  public static String getString(ItemStack stack, String keyName) {
    initNBTTagCompound(stack);

    if (!stack.getTagCompound().hasKey(keyName)) {
      setString(stack, keyName, "");
    }

    return stack.getTagCompound().getString(keyName);
  }

  public static void setString(ItemStack stack, String keyName, String keyValue) {
    initNBTTagCompound(stack);

    stack.getTagCompound().setString(keyName, keyValue);
  }

  // boolean
  public static boolean getBoolean(ItemStack stack, String keyName) {
    initNBTTagCompound(stack);

    if (!stack.getTagCompound().hasKey(keyName)) {
      setBoolean(stack, keyName, false);
    }

    return stack.getTagCompound().getBoolean(keyName);
  }

  public static void setBoolean(ItemStack stack, String keyName, boolean keyValue) {
    initNBTTagCompound(stack);

    stack.getTagCompound().setBoolean(keyName, keyValue);
  }

  // byte
  public static byte getByte(ItemStack stack, String keyName) {
    initNBTTagCompound(stack);

    if (!stack.getTagCompound().hasKey(keyName)) {
      setByte(stack, keyName, (byte) 0);
    }

    return stack.getTagCompound().getByte(keyName);
  }

  public static void setByte(ItemStack stack, String keyName, byte keyValue) {
    initNBTTagCompound(stack);

    stack.getTagCompound().setByte(keyName, keyValue);
  }

  // short
  public static short getShort(ItemStack stack, String keyName) {
    initNBTTagCompound(stack);

    if (!stack.getTagCompound().hasKey(keyName)) {
      setShort(stack, keyName, (short) 0);
    }

    return stack.getTagCompound().getShort(keyName);
  }

  public static void setShort(ItemStack stack, String keyName, short keyValue) {
    initNBTTagCompound(stack);

    stack.getTagCompound().setShort(keyName, keyValue);
  }

  // int
  public static int getInt(ItemStack stack, String keyName) {
    initNBTTagCompound(stack);

    if (!stack.getTagCompound().hasKey(keyName)) {
      setInteger(stack, keyName, 0);
    }

    return stack.getTagCompound().getInteger(keyName);
  }

  public static void setInteger(ItemStack stack, String keyName, int keyValue) {
    initNBTTagCompound(stack);

    stack.getTagCompound().setInteger(keyName, keyValue);
  }

  // long
  public static long getLong(ItemStack stack, String keyName) {
    initNBTTagCompound(stack);

    if (!stack.getTagCompound().hasKey(keyName)) {
      setLong(stack, keyName, 0);
    }

    return stack.getTagCompound().getLong(keyName);
  }

  // float
  public static float getFloat(ItemStack stack, String keyName) {
    initNBTTagCompound(stack);

    if (!stack.getTagCompound().hasKey(keyName)) {
      setFloat(stack, keyName, 0);
    }

    return stack.getTagCompound().getFloat(keyName);
  }

  public static void setFloat(ItemStack stack, String keyName, float keyValue) {
    initNBTTagCompound(stack);

    stack.getTagCompound().setFloat(keyName, keyValue);
  }

  // double
  public static double getDouble(ItemStack stack, String keyName) {
    initNBTTagCompound(stack);

    if (!stack.getTagCompound().hasKey(keyName)) {
      setDouble(stack, keyName, 0);
    }

    return stack.getTagCompound().getDouble(keyName);
  }

  public static void setDouble(ItemStack itemStack, String keyName, double keyValue) {
    initNBTTagCompound(itemStack);

    itemStack.getTagCompound().setDouble(keyName, keyValue);
  }

  // tag list
  public static NBTTagList getTagList(ItemStack stack, String keyName, int nbtBaseType) {
    initNBTTagCompound(stack);

    if (!stack.getTagCompound().hasKey(keyName)) {
      setTagList(stack, keyName, new NBTTagList());
    }

    return stack.getTagCompound().getTagList(keyName, nbtBaseType);
  }

  public static void setTagList(ItemStack stack, String keyName, NBTTagList nbtTagList) {
    initNBTTagCompound(stack);

    stack.getTagCompound().setTag(keyName, nbtTagList);
  }

  // tag compound
  public static NBTTagCompound getTagCompound(ItemStack stack, String keyName) {
    initNBTTagCompound(stack);

    if (!stack.getTagCompound().hasKey(keyName)) {
      setTagCompound(stack, keyName, new NBTTagCompound());
    }

    return stack.getTagCompound().getCompoundTag(keyName);
  }

  public static void setTagCompound(ItemStack stack, String keyName, NBTTagCompound nbtTagCompound) {
    initNBTTagCompound(stack);

    stack.getTagCompound().setTag(keyName, nbtTagCompound);
  }
}