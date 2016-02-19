package net.kyau.afterhours.inventory;

import net.kyau.afterhours.inventory.qrd.InventoryQRDMain;
import net.kyau.afterhours.items.QRD;
import net.kyau.afterhours.items.VoidPearl;
import net.kyau.afterhours.items.WormholeManipulator;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerQS extends Container {

  public final InventoryQSMain inventoryQSMain;
  private static final int INV_START = InventoryQRDMain.INV_SIZE, INV_END = INV_START + 26, HOTBAR_START = INV_END + 1,
      HOTBAR_END = HOTBAR_START + 8;
  protected final int PLAYER_INVENTORY_ROWS = 3;
  protected final int PLAYER_INVENTORY_COLUMNS = 9;
  protected final int qsInventoryRows = Ref.Containers.QRD_MAIN_ROWS;
  protected final int qsInventoryColumns = Ref.Containers.QRD_MAIN_COLS;

  public ContainerQS(EntityPlayer player, InventoryQSMain inventoryQSMain) {
    this.inventoryQSMain = inventoryQSMain;
    for (int qsRowIndex = 0; qsRowIndex < qsInventoryRows; ++qsRowIndex) {
      for (int qsColumnIndex = 0; qsColumnIndex < qsInventoryColumns; ++qsColumnIndex) {
        this.addSlotToContainer(new SlotQSMain(this.inventoryQSMain, qsColumnIndex + qsRowIndex * qsInventoryColumns, 8 + qsColumnIndex * 18, qsRowIndex * 18 - 17));
        // LogHelper.info("index: " + (vrdColumnIndex + vrdRowIndex * vrdInventoryColumns));
      }
    }
    int i;
    // ITEM INVENTORY
    // You can make a custom Slot if you need different behavior,
    // such as only certain item types can be put into this slot
    // We made a custom slot to prevent our inventory-storing item
    // from being stored within itself, but if you want to allow that and
    // you followed my advice at the end of the above step, then you
    // could get away with using the vanilla Slot class
    // MOD SLOTS
    this.addSlotToContainer(new SlotQSMod1(this.inventoryQSMain, 45, 8, 83));
    this.addSlotToContainer(new SlotQSMod2(this.inventoryQSMain, 46, 26, 83));
    // int tmpX = 0;
    // for (i = 0; i < InventoryVRDMain.INV_SIZE; ++i) {
    // this.addSlotToContainer(new SlotModInv(this.inventoryVRDVoid, i, 8 + tmpX, 83));
    // tmpX = tmpX + 18;
    // }
    // ARMOR SLOTS, but you need to make a public version of SlotArmor.
    /*
    for (i = 0; i < 4; ++i) {
        // These are the standard positions for survival inventory layout
        this.addSlotToContainer(new SlotArmor(this.player, inventoryPlayer, inventoryPlayer.getSizeInventory() - 1 - i, 8, 8 + i * 18, i));
    } */
    // PLAYER INVENTORY
    for (int inventoryRowIndex = 0; inventoryRowIndex < PLAYER_INVENTORY_ROWS; ++inventoryRowIndex) {
      for (int inventoryColumnIndex = 0; inventoryColumnIndex < PLAYER_INVENTORY_COLUMNS; ++inventoryColumnIndex) {
        this.addSlotToContainer(new Slot(player.inventory, inventoryColumnIndex + inventoryRowIndex * 9 + 9, 8 + inventoryColumnIndex * 18, 117 + inventoryRowIndex * 18));
      }
    }
    // PLAYER ACTION BAR
    for (int actionBarSlotIndex = 0; actionBarSlotIndex < PLAYER_INVENTORY_COLUMNS; ++actionBarSlotIndex) {
      this.addSlotToContainer(new Slot(player.inventory, actionBarSlotIndex, 8 + actionBarSlotIndex * 18, 175));
    }
  }

  @Override
  public boolean canInteractWith(EntityPlayer player) {
    return true;
  }

  /**
   * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
   */

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

  /**
   * You should override this method to prevent the player from moving the stack that
   * opened the inventory, otherwise if the player moves it, the inventory will not
   * be able to save properly
   */
  @Override
  public ItemStack slotClick(int slot, int button, int flag, EntityPlayer player) {
    // this will prevent the player from interacting with the item that opened the inventory:
    if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == player.getHeldItem()) {
      return null;
    }
    super.detectAndSendChanges();
    return super.slotClick(slot, button, flag, player);
  }

  @Override
  public void onContainerClosed(EntityPlayer player) {
    super.onContainerClosed(player);

    if (!player.worldObj.isRemote) {
      InventoryPlayer invPlayer = player.inventory;
      for (ItemStack itemStack : invPlayer.mainInventory) {
        if (itemStack != null) {
          if (NBTHelper.hasTag(itemStack, Ref.NBT.QS_GUI_OPEN)) {
            NBTHelper.removeTag(itemStack, Ref.NBT.QS_GUI_OPEN);
          }
        }
      }

      saveInventory(player);
    }
  }

  public void saveInventory(EntityPlayer player) {
    inventoryQSMain.onGuiSaved(player);
  }

  public class SlotQSMain extends Slot {

    public SlotQSMain(IInventory inv, int index, int xPos, int yPos) {
      super(inv, index, xPos, yPos);
    }

    // Check if the stack is a valid item for this slot.
    @Override
    public boolean isItemValid(ItemStack itemstack) {
      // Everything returns true except an instance of our Item
      return !(itemstack.getItem() instanceof QRD || itemstack.getItem() instanceof VoidPearl || itemstack.getItem() instanceof WormholeManipulator);
    }

  }

  public class SlotQSMod1 extends Slot {

    public SlotQSMod1(IInventory inv, int index, int xPos, int yPos) {
      super(inv, index, xPos, yPos);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
      Item item = stack.getItem();
      if (item instanceof VoidPearl) {
        return true;
      }
      return false;
    }
  }

  public class SlotQSMod2 extends Slot {

    private final int slotIndex;

    public SlotQSMod2(IInventory inv, int index, int xPos, int yPos) {
      super(inv, index, xPos, yPos);
      this.slotIndex = index;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
      Item item = stack.getItem();
      if (item instanceof WormholeManipulator) {
        return true;
      }
      return false;
    }

    @Override
    public void putStack(ItemStack stack) {
      if (stack != null)
        if (stack.getItem() instanceof WormholeManipulator) {
          // TODO: implement client inventory sync
          // inventoryVRDVoid.voidSync = true;
        }
      this.inventory.setInventorySlotContents(this.slotIndex, stack);
      this.onSlotChanged();
    }

    @Override
    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
      if (stack != null)
        // if (stack.getItem() instanceof WormholeManipulator) {
        // inventoryQRDVoid.quantumSync = false;
        // }
        this.onSlotChanged();
    }
  }

}
