package net.kyau.afterhours.event;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Nonnull;

import net.kyau.afterhours.enchantment.EnchantmentEntanglement;
import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.utils.InventoryHandler;
import net.kyau.afterhours.utils.ItemHelper;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class ForgeEventHandler {

  public static List<String> tooltipList = new ArrayList<String>();

  @SubscribeEvent
  public void onItemPickup(@Nonnull EntityItemPickupEvent event) {
    if (event.entity instanceof EntityPlayer) {
      ItemStack itemCurrent = event.item.getEntityItem();
      World world = event.entity.worldObj;
      if (!world.isRemote) {
        if (itemCurrent.getUnlocalizedName().equals(ModItems.voidpearl.getUnlocalizedName())) {
          String playerName = event.entity.getName();
          String itemOwner;
          if (itemCurrent.getTagCompound() == null) {
            itemOwner = "null";
          } else {
            itemOwner = ItemHelper.getOwnerName(itemCurrent);
          }
          int count = InventoryHandler.countItems(event.entityPlayer, ModItems.voidpearl);
          if (count > 0) {
            if (ModInfo.DEBUG)
              LogHelper.info("> DEBUG: itemPickup: " + itemCurrent.getUnlocalizedName().substring(11) + " (owner=" + itemOwner + ") canceled!");
            event.setCanceled(true);
          }
        }
      }
    }
  }

  @SubscribeEvent
  public void playerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase == Phase.START) {
      EntityPlayer player = event.player;
      World world = player.worldObj;
      if (!world.isRemote) {
        ItemStack item = new ItemStack(ModItems.voidpearl);
        int count = InventoryHandler.countItems(player, ModItems.voidpearl);
        if (count > 1) {
          InventoryHandler.removeLimitedItem(player, item);
        }
      }
    }
  }

  @SubscribeEvent
  public void playerBreakSpeed(PlayerEvent.BreakSpeed event) {
    EntityPlayer player = event.entityPlayer;
    if (player.getHeldItem() != null) {
      ItemStack stack = player.getHeldItem();
      if (ModItems.repairList.contains(stack.getUnlocalizedName())) {
        if (stack.getItemDamage() == stack.getMaxDamage()) {
          event.newSpeed = 1.0F;
        }
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onPlayerDeath(@Nonnull PlayerDropsEvent event) {
    if (event.entityPlayer == null || event.entityPlayer instanceof FakePlayer || event.isCanceled()) {
      return;
    }
    if (event.entityPlayer.worldObj.getGameRules().getBoolean("keepInventory")) {
      return;
    }

    ListIterator<EntityItem> iterator = event.drops.listIterator();
    while (iterator.hasNext()) {
      EntityItem entityItem = iterator.next();
      ItemStack stack = entityItem.getEntityItem();
      if (EnchantmentEntanglement.isEntangled(stack)) {
        if (addToInventory(event.entityPlayer, stack, 0)) {
          iterator.remove();
        }
      }
    }
  }

  @SubscribeEvent
  public void onPlayerClone(@Nonnull PlayerEvent.Clone event) {
    if (!event.wasDeath || event.isCanceled()) {
      return;
    }
    if (event.original == null || event.entityPlayer == null || event.entityPlayer instanceof FakePlayer) {
      return;
    }
    if (event.entityPlayer.worldObj.getGameRules().getBoolean("keepInventory")) {
      return;
    }
    for (int i = 0; i < event.original.inventory.mainInventory.length; i++) {
      ItemStack stack = event.original.inventory.mainInventory[i];
      if (EnchantmentEntanglement.isEntangled(stack)) {
        if (addToInventory(event.entityPlayer, stack, i)) {
          event.original.inventory.mainInventory[i] = null;
        }
      }
    }
    for (int i = 0; i < event.original.inventory.armorInventory.length; i++) {
      ItemStack stack = event.original.inventory.armorInventory[i];
      if (EnchantmentEntanglement.isEntangled(stack)) {
        if (addToInventory(event.entityPlayer, stack, i)) {
          event.original.inventory.armorInventory[i] = null;
        }
      }
    }
  }

  private boolean addToInventory(EntityPlayer entityPlayer, ItemStack stack, int id) {
    if (stack == null || entityPlayer == null) {
      return false;
    }
    if (stack.getItem() instanceof ItemArmor) {
      ItemArmor arm = (ItemArmor) stack.getItem();
      int index = 3 - arm.armorType;
      if (entityPlayer.inventory.armorItemInSlot(index) == null) {
        entityPlayer.inventory.armorInventory[index] = stack;
        return true;
      }
    }

    InventoryPlayer inv = entityPlayer.inventory;
    if (inv.mainInventory[id] == null) {
      inv.mainInventory[id] = stack.copy();
      return true;
    } else {
      for (int i = 0; i < inv.mainInventory.length; i++) {
        if (inv.mainInventory[i] == null) {
          inv.mainInventory[i] = stack.copy();
          return true;
        }
      }
    }

    return false;
  }
}
