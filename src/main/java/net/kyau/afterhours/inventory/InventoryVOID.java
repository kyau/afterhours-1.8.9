package net.kyau.afterhours.inventory;

import net.kyau.afterhours.items.VRAD;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.utils.INBTTaggable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class InventoryVOID implements IInventory, INBTTaggable {

  public ItemStack parentItem;
  protected ItemStack[] inventory;
  protected String customName;
  public final static int INV_SIZE = 1;

  public InventoryVOID(ItemStack stack) {
    parentItem = stack;
    inventory = new ItemStack[this.getSizeInventory()];
    // readFromNBT(stack.getTagCompound());
  }

  @Override
  public String getName() {
    return this.hasCustomName() ? this.getCustomName() : StatCollector.translateToLocal(ModInfo.MOD_ID + ".container:void");
  }

  @Override
  public boolean hasCustomName() {
    return false;
  }

  @Override
  public IChatComponent getDisplayName() {
    return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
  }

  public String getCustomName() {
    return customName;
  }

  @Override
  public void readFromNBT(NBTTagCompound nbtTagCompound) {
    // TODO Auto-generated method stub

  }

  @Override
  public void writeToNBT(NBTTagCompound nbtTagCompound) {
    // TODO Auto-generated method stub

  }

  @Override
  public String getTagLabel() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getSizeInventory() {
    // TODO Auto-generated method stub
    return INV_SIZE;
  }

  @Override
  public ItemStack getStackInSlot(int index) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ItemStack decrStackSize(int index, int count) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ItemStack removeStackFromSlot(int index) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    inventory[index] = null;
  }

  @Override
  public int getInventoryStackLimit() {
    // TODO Auto-generated method stub
    return 64;
  }

  @Override
  public void markDirty() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer player) {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public void openInventory(EntityPlayer player) {
    // TODO Auto-generated method stub

  }

  @Override
  public void closeInventory(EntityPlayer player) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return !(stack.getItem() instanceof VRAD);
  }

  @Override
  public int getField(int id) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    // TODO Auto-generated method stub

  }

  @Override
  public int getFieldCount() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void clear() {
    // TODO Auto-generated method stub

  }

}
