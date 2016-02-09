package net.kyau.afterhours.utils;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Tim on 4/12/2015.
 */
public interface INBTTaggable {

  void readFromNBT(NBTTagCompound nbtTagCompound);

  void writeToNBT(NBTTagCompound nbtTagCompound);

  String getTagLabel();
}