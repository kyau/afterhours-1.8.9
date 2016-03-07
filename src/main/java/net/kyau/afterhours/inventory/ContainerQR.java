package net.kyau.afterhours.inventory;

import net.kyau.afterhours.init.recipes.ReciprocatorRecipes;
import net.kyau.afterhours.tileentity.TileEntityQuantumReciprocator;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerQR extends Container {

  private static final int INV_START = 2, INV_END = INV_START + 26, HOTBAR_START = INV_END + 1,
      HOTBAR_END = HOTBAR_START + 8;
  private final IInventory tileQR;
  private final int sizeInventory;
  private int ticksReciprocateItemSoFar;
  private int ticksPerItem;
  private int timeCanReciprocate;

  public ContainerQR(InventoryPlayer inventoryPlayer, IInventory inventoryQR) {

    tileQR = inventoryQR;
    sizeInventory = tileQR.getSizeInventory();

    addSlotToContainer(new SlotQRInput(tileQR, TileEntityQuantumReciprocator.slotEnum.INPUT_SLOT.ordinal(), 45, 35));
    addSlotToContainer(new SlotQROutput(inventoryPlayer.player, tileQR, TileEntityQuantumReciprocator.slotEnum.OUTPUT_SLOT.ordinal(), 116, 35));

    // add player inventory slots
    // note that the slot numbers are within the player inventory so can
    // be same as the tile entity inventory
    int i;
    for (i = 0; i < 3; ++i) {
      for (int j = 0; j < 9; ++j) {
        addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
      }
    }

    // add hotbar slots
    for (i = 0; i < 9; ++i) {
      addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
    }
  }

  @Override
  public void onCraftGuiOpened(ICrafting listener) {
    super.crafters.add(listener);
    listener.updateCraftingInventory(this, this.getInventory());
    this.detectAndSendChanges();
  }

  /**
   * Looks for changes made in the container, sends them to every listener.
   */
  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();

    for (int i = 0; i < crafters.size(); ++i) {
      ICrafting icrafting = (ICrafting) crafters.get(i);

      if (ticksReciprocateItemSoFar != tileQR.getField(2)) {
        icrafting.sendProgressBarUpdate(this, 2, tileQR.getField(2));
      }

      if (timeCanReciprocate != tileQR.getField(0)) {
        icrafting.sendProgressBarUpdate(this, 0, tileQR.getField(0));
      }

      if (ticksPerItem != tileQR.getField(3)) {
        icrafting.sendProgressBarUpdate(this, 3, tileQR.getField(3));
      }
    }

    ticksReciprocateItemSoFar = tileQR.getField(2);
    timeCanReciprocate = tileQR.getField(0);
    ticksPerItem = tileQR.getField(3);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void updateProgressBar(int id, int data) {
    tileQR.setField(id, data);
  }

  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    return tileQR.isUseableByPlayer(playerIn);
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int index) {
    ItemStack itemstack = null;
    Slot slot = (Slot) this.inventorySlots.get(index);

    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();

      // If item is in our custom Inventory or armor slot
      if (index < INV_START) {
        // try to place in player inventory / action bar
        if (!this.mergeItemStack(itemstack1, INV_START, HOTBAR_END + 1, true)) {
          return null;
        }

        slot.onSlotChange(itemstack1, itemstack);
      }
      // Item is in inventory / hotbar, try to place in custom inventory or armor slots
      else {
        if (index >= INV_START) {
          // place in custom inventory
          if (!this.mergeItemStack(itemstack1, 0, INV_START, false)) {
            return null;
          }
        }
        // item in action bar - place in player inventory
        else if (index >= HOTBAR_START && index < HOTBAR_END + 1) {
          if (!this.mergeItemStack(itemstack1, INV_START, INV_END + 1, false)) {
            return null;
          }
        }
      }

      if (itemstack1.stackSize == 0) {
        slot.putStack((ItemStack) null);
      } else {
        slot.onSlotChanged();
      }

      if (itemstack1.stackSize == itemstack.stackSize) {
        return null;
      }

      slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
    }

    return itemstack;
  }

  public class SlotQRInput extends Slot {

    public SlotQRInput(IInventory inv, int index, int xPos, int yPos) {
      super(inv, index, xPos, yPos);
    }

    // Check if the stack is a valid item for this slot.
    @Override
    public boolean isItemValid(ItemStack stack) {
      Item item = stack.getItem();
      return ReciprocatorRecipes.isValid(item);
    }

  }

  public class SlotQROutput extends Slot {

    /** The player that is using the GUI where this slot resides. */
    private final EntityPlayer thePlayer;
    private int numQROutput;

    public SlotQROutput(EntityPlayer player, IInventory inventory, int index, int xPos, int yPos) {
      super(inventory, index, xPos, yPos);
      thePlayer = player;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
      return false; // can't place anything into it
    }

    @Override
    public ItemStack decrStackSize(int amount) {
      if (getHasStack()) {
        numQROutput += Math.min(amount, getStack().stackSize);
      }

      return super.decrStackSize(amount);
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
      onCrafting(stack);
      super.onPickupFromSlot(player, stack);
    }

    @Override
    protected void onCrafting(ItemStack stack, int amountReciprocated) {
      numQROutput += amountReciprocated;
      onCrafting(stack);
    }

    @Override
    protected void onCrafting(ItemStack stack) {
      if (!thePlayer.worldObj.isRemote) {
        int expEarned = numQROutput;
        float expFactor = ReciprocatorRecipes.getExperience(stack);

        if (expFactor == 0.0F) {
          expEarned = 0;
        } else if (expFactor < 1.0F) {
          int possibleExpEarned = MathHelper.floor_float(expEarned * expFactor);

          if (possibleExpEarned < MathHelper.ceiling_float_int(expEarned * expFactor) && Math.random() < expEarned * expFactor - possibleExpEarned) {
            ++possibleExpEarned;
          }

          expEarned = possibleExpEarned;
        }

        // create experience orbs
        int expInOrb;
        while (expEarned > 0) {
          expInOrb = EntityXPOrb.getXPSplit(expEarned);
          expEarned -= expInOrb;
          thePlayer.worldObj.spawnEntityInWorld(new EntityXPOrb(thePlayer.worldObj, thePlayer.posX, thePlayer.posY + 0.5D, thePlayer.posZ + 0.5D, expInOrb));
        }
      }

      numQROutput = 0;

      // You can trigger achievements here based on output
      // E.g. if (parItemStack.getItem() == Items.grinded_fish)
      // {
      // thePlayer.triggerAchievement(AchievementList.grindFish);
      // }
    }
  }
}
