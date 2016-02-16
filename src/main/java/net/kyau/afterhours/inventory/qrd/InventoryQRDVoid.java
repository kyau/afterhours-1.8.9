package net.kyau.afterhours.inventory.qrd;

import net.kyau.afterhours.items.QRD;
import net.kyau.afterhours.items.WormholeManipulator;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.ChatUtil;
import net.kyau.afterhours.utils.INBTTaggable;
import net.kyau.afterhours.utils.ItemHelper;
import net.kyau.afterhours.utils.LogHelper;
import net.kyau.afterhours.utils.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class InventoryQRDVoid implements IInventory, INBTTaggable {

  public static boolean quantumSync;
  public ItemStack parentItem;
  protected ItemStack[] inventory;
  protected String customName;
  public final static int INV_SIZE = 1;
  private ItemStack[] inventoryQRDMain;
  private EntityPlayer player;

  public InventoryQRDVoid(ItemStack stack, EntityPlayer player) {
    quantumSync = false;
    parentItem = stack;
    this.player = player;
    inventory = new ItemStack[this.getSizeInventory()];
    // readFromNBT(stack.getTagCompound());
  }

  @Override
  public String getName() {
    return this.hasCustomName() ? this.getCustomName() : StatCollector.translateToLocal(Ref.Containers.QRD_VOID);
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
    InventoryQRDMain qrdMainClass = new InventoryQRDMain(parentItem);
    this.inventoryQRDMain = qrdMainClass.getInventory();
    if (inventoryQRDMain != null) {
      for (int currentIndex = 0; currentIndex < inventoryQRDMain.length; ++currentIndex) {
        if (inventoryQRDMain[currentIndex] != null && inventoryQRDMain[currentIndex].getItem() instanceof WormholeManipulator) {
          this.quantumSync = true;
          ItemStack voidWell = inventoryQRDMain[currentIndex];
          if (ItemHelper.hasOwner(voidWell)) {
            if (NBTHelper.hasTag(voidWell, Ref.NBT.ENERGY_LEVEL) && NBTHelper.hasTag(voidWell, Ref.NBT.ENERGY_MAX)) {
              LogHelper.info("Void Well, with Energy!");
              int[] energy = NBTHelper.getEnergyLevels(voidWell);
              // String string = voidWell.getTagCompound().getString("Energy");
              // if (string.contains("#")) {
              // energy = string.split("#");
              // if (Integer.parseInt(energy[0]) < Integer.parseInt(energy[1])) {
              if (energy[0] < energy[1]) {
                if (stack != null && stack.stackSize > 62) {
                  int newEnergy = energy[0] + 2;
                  // voidWell.setTagInfo("Energy", new NBTTagString(newEnergy + "#" + energy[1]));
                  NBTHelper.setEnergyLevels(voidWell, newEnergy, energy[1]);
                  ChatUtil.sendNoSpam(player, EnumChatFormatting.GREEN + StatCollector.translateToLocal(Ref.Translation.ENERGY) + " " + EnumChatFormatting.GRAY + newEnergy + "/" + energy[1]);
                  qrdMainClass.setInventorySlotContents(currentIndex, voidWell);
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
    return !(stack.getItem() instanceof QRD);
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
