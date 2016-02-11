package net.kyau.afterhours.inventory.vrd;

import net.kyau.afterhours.items.VRD;
import net.kyau.afterhours.items.VoidWell;
import net.kyau.afterhours.references.Names;
import net.kyau.afterhours.utils.ChatUtil;
import net.kyau.afterhours.utils.INBTTaggable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class InventoryVRDVoid implements IInventory, INBTTaggable {

  public static boolean voidSync;
  public ItemStack parentItem;
  protected ItemStack[] inventory;
  protected String customName;
  public final static int INV_SIZE = 1;
  private ItemStack[] inventoryVRDMain;
  private EntityPlayer player;

  public InventoryVRDVoid(ItemStack stack, EntityPlayer player) {
    voidSync = false;
    parentItem = stack;
    this.player = player;
    inventory = new ItemStack[this.getSizeInventory()];
    // readFromNBT(stack.getTagCompound());
  }

  @Override
  public String getName() {
    return this.hasCustomName() ? this.getCustomName() : StatCollector.translateToLocal(Names.Containers.VRD_VOID);
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
    return null;
  }

  @Override
  public ItemStack removeStackFromSlot(int index) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    InventoryVRDMain vrdMainClass = new InventoryVRDMain(parentItem);
    this.inventoryVRDMain = vrdMainClass.getInventory();
    if (inventoryVRDMain != null) {
      for (int currentIndex = 0; currentIndex < inventoryVRDMain.length; ++currentIndex) {
        if (inventoryVRDMain[currentIndex] != null && inventoryVRDMain[currentIndex].getItem() instanceof VoidWell) {
          this.voidSync = true;
          ItemStack voidWell = inventoryVRDMain[currentIndex];
          String[] energy = null;
          if (voidWell.hasTagCompound()) {
            // LogHelper.info("Void Well, with Owner!");
            String string = voidWell.getTagCompound().getString("Energy");
            if (string.contains("#")) {
              energy = string.split("#");
              if (Integer.parseInt(energy[0]) < Integer.parseInt(energy[1])) {
                if (stack != null && stack.stackSize > 62) {
                  int newEnergy = Integer.parseInt(energy[0]) + 2;
                  voidWell.setTagInfo("Energy", new NBTTagString(newEnergy + "#" + energy[1]));
                  ChatUtil.sendNoSpam(player, EnumChatFormatting.GREEN + "Void Energy: " + EnumChatFormatting.GRAY + newEnergy + "/" + energy[1]);
                  vrdMainClass.setInventorySlotContents(currentIndex, voidWell);
                }
              }
            }
          }
        }
      }
    }
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
    return !(stack.getItem() instanceof VRD);
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
