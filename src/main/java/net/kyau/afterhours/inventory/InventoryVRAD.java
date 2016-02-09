package net.kyau.afterhours.inventory;

import java.util.UUID;

import net.kyau.afterhours.items.VRAD;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.utils.INBTTaggable;
import net.kyau.afterhours.utils.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class InventoryVRAD implements IInventory, INBTTaggable {

  private String name = "VRAD";
  public final static int INV_SIZE = 9 * 5;
  public ItemStack parentItem;
  protected ItemStack[] inventory;
  protected String customName;

  public InventoryVRAD(ItemStack stack) {
    parentItem = stack;
    inventory = new ItemStack[this.getSizeInventory()];
    readFromNBT(stack.getTagCompound());
  }

  @Override
  public String getName() {
    return this.hasCustomName() ? this.getCustomName() : StatCollector.translateToLocal(ModInfo.MOD_ID + ".container:" + parentItem.getUnlocalizedName().substring(ModInfo.MOD_ID.length() + 1));
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
  public int getSizeInventory() {
    // return inventory.length;
    return INV_SIZE;
  }

  @Override
  public ItemStack getStackInSlot(int index) {
    return inventory[index];
  }

  @Override
  public ItemStack decrStackSize(int index, int count) {
    ItemStack itemStack = getStackInSlot(index);
    if (itemStack != null) {
      if (itemStack.stackSize <= count) {
        setInventorySlotContents(index, null);
      } else {
        itemStack = itemStack.splitStack(count);
        if (itemStack.stackSize == 0) {
          setInventorySlotContents(index, null);
        }
      }
    }

    return itemStack;
  }

  @Override
  public ItemStack removeStackFromSlot(int index) {
    if (inventory[index] != null) {
      ItemStack itemStack = inventory[index];
      inventory[index] = null;
      return itemStack;
    } else {
      return null;
    }
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    inventory[index] = stack;
  }

  @Override
  public int getInventoryStackLimit() {
    return 64;
  }

  @Override
  public void markDirty() {
    // NOOP
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer player) {
    return true;
  }

  @Override
  public void openInventory(EntityPlayer player) {
  }

  @Override
  public void closeInventory(EntityPlayer player) {
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return !(stack.getItem() instanceof VRAD);
  }

  @Override
  public int getField(int id) {
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    // NOOP
  }

  @Override
  public int getFieldCount() {
    return 0;
  }

  @Override
  public void clear() {
    for (int i = 0; i < this.getSizeInventory(); i++)
      this.setInventorySlotContents(i, null);
  }

  @Override
  public void readFromNBT(NBTTagCompound nbtTagCompound) {
    if (nbtTagCompound != null && nbtTagCompound.hasKey("Items")) {
      // Read in the ItemStacks in the inventory from NBT
      if (nbtTagCompound.hasKey("Items")) {
        NBTTagList tagList = nbtTagCompound.getTagList("Items", 10);
        inventory = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < tagList.tagCount(); ++i) {
          NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
          byte slotIndex = tagCompound.getByte("Slot");
          if (slotIndex >= 0 && slotIndex < inventory.length) {
            inventory[slotIndex] = ItemStack.loadItemStackFromNBT(tagCompound);
          }
        }
      }

      // Read in any custom name for the inventory
      if (nbtTagCompound.hasKey("display") && nbtTagCompound.getTag("display").getClass().equals(NBTTagCompound.class)) {
        if (nbtTagCompound.getCompoundTag("display").hasKey("Name")) {
          customName = nbtTagCompound.getCompoundTag("display").getString("Name");
        }
      }
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound nbtTagCompound) {
    // Write the ItemStacks in the inventory to NBT
    NBTTagList tagList = new NBTTagList();
    for (int currentIndex = 0; currentIndex < inventory.length; ++currentIndex) {
      if (inventory[currentIndex] != null) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setByte("Slot", (byte) currentIndex);
        inventory[currentIndex].writeToNBT(tagCompound);
        tagList.appendTag(tagCompound);
      }
    }
    nbtTagCompound.setTag("Items", tagList);
  }

  public void save() {
    NBTTagCompound nbtTagCompound = parentItem.getTagCompound();

    if (nbtTagCompound == null) {
      nbtTagCompound = new NBTTagCompound();

      UUID uuid = UUID.randomUUID();
      nbtTagCompound.setLong("UUIDMostSig", uuid.getMostSignificantBits());
      nbtTagCompound.setLong("UUIDLeastSig", uuid.getLeastSignificantBits());
    }

    writeToNBT(nbtTagCompound);
    parentItem.setTagCompound(nbtTagCompound);
  }

  public void onGuiSaved(EntityPlayer player) {
    parentItem = findParentItem(player);

    if (parentItem != null) {
      save();
    }
  }

  public ItemStack findParentItem(EntityPlayer player) {
    if (NBTHelper.hasUUID(parentItem)) {
      UUID parentItemStackUUID = new UUID(parentItem.getTagCompound().getLong("UUIDMostSig"), parentItem.getTagCompound().getLong("UUIDLeastSig"));
      for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
        ItemStack itemStack = player.inventory.getStackInSlot(i);

        if (NBTHelper.hasUUID(itemStack)) {
          if (itemStack.getTagCompound().getLong("UUIDMostSig") == parentItemStackUUID.getMostSignificantBits() && itemStack.getTagCompound().getLong("UUIDLeastSig") == parentItemStackUUID.getLeastSignificantBits()) {
            return itemStack;
          }
        }
      }
    }

    return null;
  }

  public boolean matchesUUID(UUID uuid) {
    return NBTHelper.hasUUID(parentItem) && parentItem.getTagCompound().getLong("UUIDLeastSig") == uuid.getLeastSignificantBits() && parentItem.getTagCompound().getLong("UUIDMostSig") == uuid.getMostSignificantBits();
  }

  @Override
  public String getTagLabel() {
    return "InventoryVRAD";
  }
}
