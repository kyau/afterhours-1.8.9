package net.kyau.afterhours.tileentity;

import net.kyau.afterhours.blocks.QuantumStabilizer;
import net.kyau.afterhours.init.ModBlocks;
import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class TileEntityQuantumStabilizer extends TileEntityBase implements IInventory {

  public enum slotEnum {
    INPUT_SLOT1,
    INPUT_SLOT2,
    FUEL_SLOT,
    OUTPUT_SLOT
  }

  private static final int[] slotsLeft = new int[] {
      slotEnum.INPUT_SLOT1.ordinal(),
      slotEnum.INPUT_SLOT2.ordinal() };
  private static final int[] slotsRight = new int[] { slotEnum.OUTPUT_SLOT.ordinal() };
  private static final int[] slotsBottom = new int[] { slotEnum.FUEL_SLOT.ordinal() };
  private static final int[] slotsSides = new int[] {};
  private ItemStack[] inventory = new ItemStack[4];
  private int timeCanStabilize;
  private int currentItemStabilizeTime;
  private int ticksStabilizeItemSoFar;
  private int ticksPerItem;
  private String stabilizerCustomName;
  public static boolean isStabilizing;

  public void dropItems() {
    InventoryHelper.dropInventoryItems(getWorld(), getPos(), this);
  }

  @Override
  public int getSizeInventory() {
    return inventory.length;
  }

  @Override
  public ItemStack getStackInSlot(int index) {
    return inventory[index];
  }

  @Override
  public ItemStack decrStackSize(int index, int count) {
    if (inventory[index] != null) {
      ItemStack stack;
      if (inventory[index].stackSize <= count) {
        stack = inventory[index];
        inventory[index] = null;
        return stack;
      } else {
        stack = inventory[index].splitStack(count);
        if (inventory[index].stackSize == 0) {
          inventory[index] = null;
        }
        return stack;
      }
    } else {
      return null;
    }
  }

  @Override
  public ItemStack removeStackFromSlot(int index) {
    if (inventory[index] != null) {
      ItemStack stack = inventory[index];
      inventory[index] = null;
      return stack;
    } else {
      return null;
    }
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    boolean isSameItemStackAlreadyInSlot = stack != null && stack.isItemEqual(inventory[index]) && ItemStack.areItemStackTagsEqual(stack, inventory[index]);
    inventory[index] = stack;
    if (stack != null && stack.stackSize > getInventoryStackLimit()) {
      stack.stackSize = getInventoryStackLimit();
    }

    // if input/fuel slot, reset the stabilizer times
    if ((index == slotEnum.INPUT_SLOT1.ordinal() || index == slotEnum.INPUT_SLOT2.ordinal() || index == slotEnum.FUEL_SLOT.ordinal()) && !isSameItemStackAlreadyInSlot) {
      ticksPerItem = timeToStabilizeItem(inventory[slotEnum.INPUT_SLOT1.ordinal()], inventory[slotEnum.INPUT_SLOT2.ordinal()]);
      ticksStabilizeItemSoFar = 0;
      markDirty();
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    NBTTagList nbttaglist = compound.getTagList("Items", 10);
    inventory = new ItemStack[getSizeInventory()];

    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
      NBTTagCompound nbtTagCompound = nbttaglist.getCompoundTagAt(i);
      byte b0 = nbtTagCompound.getByte("Slot");

      if (b0 >= 0 && b0 < inventory.length) {
        inventory[b0] = ItemStack.loadItemStackFromNBT(nbtTagCompound);
      }
    }

    timeCanStabilize = compound.getShort("StabilizeTime");
    ticksStabilizeItemSoFar = compound.getShort("CookTime");
    ticksPerItem = compound.getShort("CookTimeTotal");

    if (compound.hasKey("CustomName", 8)) {
      stabilizerCustomName = compound.getString("CustomName");
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setShort("StabilizeTime", (short) timeCanStabilize);
    compound.setShort("CookTime", (short) ticksStabilizeItemSoFar);
    compound.setShort("CookTimeTotal", (short) ticksPerItem);
    NBTTagList nbttaglist = new NBTTagList();

    for (int i = 0; i < inventory.length; ++i) {
      if (inventory[i] != null) {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setByte("Slot", (byte) i);
        inventory[i].writeToNBT(nbtTagCompound);
        nbttaglist.appendTag(nbtTagCompound);
      }
    }

    compound.setTag("Items", nbttaglist);

    if (hasCustomName()) {
      compound.setString("CustomName", stabilizerCustomName);
    }
  }

  @Override
  public int getInventoryStackLimit() {
    return 64;
  }

  public boolean stabilizeSomething() {
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
    return index == slotEnum.INPUT_SLOT1.ordinal() || index == slotEnum.INPUT_SLOT2.ordinal() || index == slotEnum.FUEL_SLOT.ordinal() ? true : false;
  }

  @Override
  public int getField(int id) {
    switch (id) {
      case 0:
        return timeCanStabilize;
      case 1:
        return currentItemStabilizeTime;
      case 2:
        return ticksStabilizeItemSoFar;
      case 3:
        return ticksPerItem;
      default:
        return 0;
    }
  }

  @Override
  public void setField(int id, int value) {
    switch (id) {
      case 0:
        timeCanStabilize = value;
        break;
      case 1:
        currentItemStabilizeTime = value;
        break;
      case 2:
        ticksStabilizeItemSoFar = value;
        break;
      case 3:
        ticksPerItem = value;
        break;
      default:
        break;
    }
  }

  @Override
  public int getFieldCount() {
    return 4;
  }

  @Override
  public void clear() {
    for (int i = 0; i < inventory.length; ++i) {
      inventory[i] = null;
    }
  }

  @Override
  public String getName() {
    return this.hasCustomName() ? this.getCustomName() : StatCollector.translateToLocal(Ref.Containers.QUANTUM_STABILIZER);
  }

  @Override
  public boolean hasCustomName() {
    return stabilizerCustomName != null && stabilizerCustomName.length() > 0;
  }

  public void setCustomInventoryName(String parCustomName) {
    stabilizerCustomName = parCustomName;
  }

  @Override
  public void update() {
    boolean hasBeenStabilizing = stabilizeSomething();
    boolean changedStabilizingState = false;
    boolean previousState = false;

    if (changedStabilizingState != previousState) {
      LogHelper.info(changedStabilizingState);
      previousState = changedStabilizingState;
    }

    if (ticksStabilizeItemSoFar > 0) {
      if (worldObj.getTileEntity(getPos()) != null) {
        ((QuantumStabilizer) getBlockType()).setState(worldObj, getPos(), worldObj.getBlockState(getPos()), true);
      }
    } else {
      if (worldObj.getTileEntity(getPos()) != null) {
        ((QuantumStabilizer) getBlockType()).setState(worldObj, getPos(), worldObj.getBlockState(getPos()), false);
      }
    }

    if (stabilizeSomething()) {
      --timeCanStabilize;
    }

    if (!worldObj.isRemote) {
      // if something is in the input slots
      if (inventory[slotEnum.INPUT_SLOT1.ordinal()] != null || inventory[slotEnum.INPUT_SLOT2.ordinal()] != null || inventory[slotEnum.FUEL_SLOT.ordinal()] != null) {
        // start stabilizing
        if (!stabilizeSomething() && canStabilize()) {
          timeCanStabilize = 150;
          if (stabilizeSomething()) {
            changedStabilizingState = true;
          }
        }

        // continue stabilizing
        if (stabilizeSomething() && canStabilize()) {
          if (hasBeenStabilizing) {
            isStabilizing = true;
          }
          ++ticksStabilizeItemSoFar;
          // check if completed stabilizing an item
          if (ticksStabilizeItemSoFar == ticksPerItem) {
            ticksStabilizeItemSoFar = 0;
            ticksPerItem = timeToStabilizeItem(inventory[0], inventory[1]);
            stabilizeItem();
            changedStabilizingState = true;
          }
        } else {
          ticksStabilizeItemSoFar = 0;
        }
      }

      // started or stopped stabilizing, update block to change to active
      // or inactive model
      if (hasBeenStabilizing != stabilizeSomething()) {
        changedStabilizingState = true;
        // QuantumStabilizer.changeBlockBasedOnStabilizeStatus(stabilizeSomething(), worldObj, pos);
      }

    }
    if (changedStabilizingState) {
      markDirty();
    }
  }

  public int timeToStabilizeItem(ItemStack stack1, ItemStack stack2) {
    Item voidstone = Item.getItemFromBlock(ModBlocks.voidstone);
    if (stack1 != null && stack2 != null) {
      if ((stack1.getItem() == Items.gunpowder && stack2.getItem() == ModItems.unstable_darkmatter) || (stack1.getItem() == ModItems.unstable_darkmatter && stack2.getItem() == Items.gunpowder)) {
        return Ref.BlockStat.STABILIZER_DARKMATTER_TIME;
      } else if ((stack1.getItem() == ModItems.darkmatter && stack2.getItem() == voidstone) || (stack1.getItem() == voidstone && stack2.getItem() == ModItems.darkmatter)) {
        return Ref.BlockStat.STABILIZER_QUANTUMROD_TIME;
      }
    }
    return Ref.BlockStat.STABILIZER_TIME;
  }

  private boolean canStabilize() {
    // if nothing in input slots
    if (inventory[slotEnum.INPUT_SLOT1.ordinal()] == null || inventory[slotEnum.INPUT_SLOT2.ordinal()] == null || inventory[slotEnum.FUEL_SLOT.ordinal()] == null) {
      return false;
    } else {
      // check if it has a grinding recipe
      ItemStack input1 = inventory[slotEnum.INPUT_SLOT1.ordinal()];
      ItemStack input2 = inventory[slotEnum.INPUT_SLOT2.ordinal()];
      ItemStack fuel = inventory[slotEnum.FUEL_SLOT.ordinal()];
      ItemStack stack = null;
      Item voidstone = Item.getItemFromBlock(ModBlocks.voidstone);
      if (((input1.getItem() == Items.gunpowder && input2.getItem() == ModItems.unstable_darkmatter) || (input1.getItem() == ModItems.unstable_darkmatter && input2.getItem() == Items.gunpowder)) && fuel.getItem() == ModItems.voidcrystal)
        stack = new ItemStack(ModItems.darkmatter, 1);
      // if (input1.getItem() == ModItems.darkmatter && input2.getItem() == ModItems.darkmatter && fuel.getItem() ==
      // ModItems.voidcrystal)
      if ((input1.getItem() == ModItems.darkmatter && input2.getItem() == voidstone) || (input1.getItem() == voidstone && input2.getItem() == ModItems.darkmatter) && fuel.getItem() == ModItems.voidcrystal)
        stack = new ItemStack(ModItems.quantumrod, 1);
      if (stack == null)
        return false;
      if (inventory[slotEnum.OUTPUT_SLOT.ordinal()] == null)
        return true;
      if (!inventory[slotEnum.OUTPUT_SLOT.ordinal()].isItemEqual(stack))
        return false;
      int result = inventory[slotEnum.OUTPUT_SLOT.ordinal()].stackSize + stack.stackSize;
      return result <= getInventoryStackLimit() && result <= inventory[slotEnum.OUTPUT_SLOT.ordinal()].getMaxStackSize();
    }
  }

  public void stabilizeItem() {
    if (canStabilize()) {
      ItemStack input1 = inventory[slotEnum.INPUT_SLOT1.ordinal()];
      ItemStack input2 = inventory[slotEnum.INPUT_SLOT2.ordinal()];
      ItemStack fuel = inventory[slotEnum.FUEL_SLOT.ordinal()];
      ItemStack stack = null;
      Item voidstone = Item.getItemFromBlock(ModBlocks.voidstone);
      if (((input1.getItem() == Items.gunpowder && input2.getItem() == ModItems.unstable_darkmatter) || (input1.getItem() == ModItems.unstable_darkmatter && input2.getItem() == Items.gunpowder)) && fuel.getItem() == ModItems.voidcrystal)
        stack = new ItemStack(ModItems.darkmatter, 1);
      // if (input1.getItem() == ModItems.darkmatter && input2.getItem() == ModItems.darkmatter && fuel.getItem() ==
      // ModItems.voidcrystal)
      if ((input1.getItem() == ModItems.darkmatter && input2.getItem() == voidstone) || (input1.getItem() == voidstone && input2.getItem() == ModItems.darkmatter) && fuel.getItem() == ModItems.voidcrystal)
        stack = new ItemStack(ModItems.quantumrod, 1);
      // check if output slot is empty
      if (inventory[slotEnum.OUTPUT_SLOT.ordinal()] == null) {
        inventory[slotEnum.OUTPUT_SLOT.ordinal()] = stack.copy();
      } else if (inventory[slotEnum.OUTPUT_SLOT.ordinal()].getItem() == stack.getItem()) {
        inventory[slotEnum.OUTPUT_SLOT.ordinal()].stackSize += stack.stackSize;
      }

      --inventory[slotEnum.INPUT_SLOT1.ordinal()].stackSize;
      --inventory[slotEnum.INPUT_SLOT2.ordinal()].stackSize;
      --inventory[slotEnum.FUEL_SLOT.ordinal()].stackSize;

      if (inventory[slotEnum.INPUT_SLOT1.ordinal()].stackSize <= 0) {
        inventory[slotEnum.INPUT_SLOT1.ordinal()] = null;
      }
      if (inventory[slotEnum.INPUT_SLOT2.ordinal()].stackSize <= 0) {
        inventory[slotEnum.INPUT_SLOT2.ordinal()] = null;
      }
      if (inventory[slotEnum.FUEL_SLOT.ordinal()].stackSize <= 0) {
        inventory[slotEnum.FUEL_SLOT.ordinal()] = null;
      }
      /*
      if (ModInfo.DEBUG)
        LogHelper.info("TileEntityQuantumStabilizer: Finished Stabilizing!");
      isStabilizing = false;
      ((QuantumStabilizer) getBlockType()).setState(worldObj, getPos(), worldObj.getBlockState(getPos()), isStabilizing);
      */
    }
  }

  @Override
  public IChatComponent getDisplayName() {
    return null;
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer player) {
    return true;
  }

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
    return oldState.getBlock() != newState.getBlock();
  }

  public boolean isStabilizing() {
    return this.currentItemStabilizeTime > 0;
  }
}
