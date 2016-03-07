package net.kyau.afterhours.tileentity;

import net.kyau.afterhours.blocks.QuantumReciprocator;
import net.kyau.afterhours.init.recipes.ReciprocatorRecipes;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class TileEntityQuantumReciprocator extends TileEntityBase implements IInventory {

  public enum slotEnum {
    INPUT_SLOT,
    OUTPUT_SLOT
  }

  private static final int[] slotsLeft = new int[] { slotEnum.INPUT_SLOT.ordinal() };
  private static final int[] slotsRight = new int[] { slotEnum.OUTPUT_SLOT.ordinal() };
  private static final int[] slotsBottom = new int[] {};
  private static final int[] slotsSides = new int[] {};
  private ItemStack[] inventory = new ItemStack[2];
  private int timeCanReciprocate;
  private int currentItemReciprocateTime;
  private int ticksReciprocateItemSoFar;
  private int ticksPerItem;
  private String reciprocatorCustomName;
  public static boolean isReciprocating;

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

    // if input/fuel slot, reset the reciprocator times
    if (index == slotEnum.INPUT_SLOT.ordinal() && !isSameItemStackAlreadyInSlot) {
      ticksPerItem = ReciprocatorRecipes.timeToReciprocate(inventory[slotEnum.INPUT_SLOT.ordinal()]);
      ticksReciprocateItemSoFar = 0;
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

    timeCanReciprocate = compound.getShort("ReciprocateTime");
    ticksReciprocateItemSoFar = compound.getShort("CookTime");
    ticksPerItem = compound.getShort("CookTimeTotal");

    if (compound.hasKey("CustomName", 8)) {
      reciprocatorCustomName = compound.getString("CustomName");
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setShort("ReciprocateTime", (short) timeCanReciprocate);
    compound.setShort("CookTime", (short) ticksReciprocateItemSoFar);
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
      compound.setString("CustomName", reciprocatorCustomName);
    }
  }

  @Override
  public int getInventoryStackLimit() {
    return 64;
  }

  public boolean reciprocateSomething() {
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
    return index == slotEnum.INPUT_SLOT.ordinal() ? true : false;
  }

  @Override
  public int getField(int id) {
    switch (id) {
      case 0:
        return timeCanReciprocate;
      case 1:
        return currentItemReciprocateTime;
      case 2:
        return ticksReciprocateItemSoFar;
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
        timeCanReciprocate = value;
        break;
      case 1:
        currentItemReciprocateTime = value;
        break;
      case 2:
        ticksReciprocateItemSoFar = value;
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
    return this.hasCustomName() ? this.getCustomName() : StatCollector.translateToLocal(Ref.Containers.QUANTUM_RECIPROCATOR);
  }

  @Override
  public boolean hasCustomName() {
    return reciprocatorCustomName != null && reciprocatorCustomName.length() > 0;
  }

  public void setCustomInventoryName(String parCustomName) {
    reciprocatorCustomName = parCustomName;
  }

  @Override
  public void update() {
    boolean hasBeenReciprocating = reciprocateSomething();
    boolean changedReciprocatingState = false;
    boolean previousState = false;

    if (changedReciprocatingState != previousState) {
      LogHelper.info(changedReciprocatingState);
      previousState = changedReciprocatingState;
    }

    if (ticksReciprocateItemSoFar > 0) {
      ((QuantumReciprocator) getBlockType()).setState(worldObj, getPos(), worldObj.getBlockState(getPos()), true);
    } else {
      ((QuantumReciprocator) getBlockType()).setState(worldObj, getPos(), worldObj.getBlockState(getPos()), false);
    }

    if (reciprocateSomething()) {
      --timeCanReciprocate;
    }

    if (!worldObj.isRemote) {
      // if something is in the input slots
      if (inventory[slotEnum.INPUT_SLOT.ordinal()] != null) {
        // start reciprocating
        if (!reciprocateSomething() && canReciprocate()) {
          timeCanReciprocate = 150;
          if (reciprocateSomething()) {
            changedReciprocatingState = true;
          }
        }

        // continue reciprocating
        if (reciprocateSomething() && canReciprocate()) {
          if (hasBeenReciprocating) {
            isReciprocating = true;
          }
          ++ticksReciprocateItemSoFar;
          // check if completed reciprocating an item
          if (ticksReciprocateItemSoFar == ticksPerItem) {
            ticksReciprocateItemSoFar = 0;
            ticksPerItem = ReciprocatorRecipes.timeToReciprocate(inventory[0]);
            reciprocateItem();
            changedReciprocatingState = true;
          }
        } else {
          ticksReciprocateItemSoFar = 0;
        }
      }

      // started or stopped stabilizing, update block to change to active
      // or inactive model
      if (hasBeenReciprocating != reciprocateSomething()) {
        changedReciprocatingState = true;
      }

    }
    if (changedReciprocatingState) {
      markDirty();
    }
  }

  /*
  public int timeToReciprocateItem(ItemStack stack1) {
    Item granite = new ItemStack(Blocks.stone, 1, 1).getItem();
    Item diorite = new ItemStack(Blocks.stone, 1, 3).getItem();
    Item andesite = new ItemStack(Blocks.stone, 1, 5).getItem();
    ItemStack oak = new ItemStack(Blocks.sapling, 1, 0);
    ItemStack spruce = new ItemStack(Blocks.sapling, 1, 1);
    ItemStack birch = new ItemStack(Blocks.sapling, 1, 2);
    ItemStack jungle = new ItemStack(Blocks.sapling, 1, 3);
    ItemStack acacia = new ItemStack(Blocks.sapling, 1, 4);
    ItemStack darkoak = new ItemStack(Blocks.sapling, 1, 5);
    if (stack1 != null) {
      if (stack1.getItem() == granite || stack1.getItem() == diorite || stack1.getItem() == andesite) {
        return Ref.BlockStat.RECIPROCATOR_STONE_TIME;
      }
      if (stack1.getUnlocalizedName().equals(oak.getUnlocalizedName()) || stack1.getUnlocalizedName().equals(spruce.getUnlocalizedName()) || stack1.getUnlocalizedName().equals(birch.getUnlocalizedName()) || stack1.getUnlocalizedName().equals(jungle.getUnlocalizedName()) || stack1.getUnlocalizedName().equals(acacia.getUnlocalizedName()) || stack1.getUnlocalizedName().equals(darkoak.getUnlocalizedName())) {
        return Ref.BlockStat.RECIPROCATOR_SAPLING_TIME;
      }
    }
    return Ref.BlockStat.STABILIZER_TIME;
  }
  */

  private boolean canReciprocate() {
    // if nothing in input slots
    if (inventory[slotEnum.INPUT_SLOT.ordinal()] == null) {
      return false;
    } else {
      // check if it has a reciprocator recipe
      ItemStack stack = ReciprocatorRecipes.getResult(inventory[slotEnum.INPUT_SLOT.ordinal()]);

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

  public void reciprocateItem() {
    if (canReciprocate()) {
      ItemStack stack = ReciprocatorRecipes.getResult(inventory[slotEnum.INPUT_SLOT.ordinal()]);

      // check if output slot is empty
      if (inventory[slotEnum.OUTPUT_SLOT.ordinal()] == null) {
        inventory[slotEnum.OUTPUT_SLOT.ordinal()] = stack.copy();
      } else if (inventory[slotEnum.OUTPUT_SLOT.ordinal()].getItem() == stack.getItem()) {
        inventory[slotEnum.OUTPUT_SLOT.ordinal()].stackSize += stack.stackSize;
      }

      --inventory[slotEnum.INPUT_SLOT.ordinal()].stackSize;

      if (inventory[slotEnum.INPUT_SLOT.ordinal()].stackSize <= 0) {
        inventory[slotEnum.INPUT_SLOT.ordinal()] = null;
      }
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

  public boolean isReciprocating() {
    return this.currentItemReciprocateTime > 0;
  }
}
