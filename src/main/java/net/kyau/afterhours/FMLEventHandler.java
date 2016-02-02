package net.kyau.afterhours;

import net.kyau.afterhours.items.ModItems;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;


public class FMLEventHandler {

  private static int maxItemAmount = 1;

  @SubscribeEvent
  public void onItemPickup(EntityItemPickupEvent event) {
    if (event.entity instanceof EntityPlayer) {
      ItemStack itemCurrent = event.item.getEntityItem();
      World world = event.entity.worldObj; 
      if (!world.isRemote) {
        if (itemCurrent.getUnlocalizedName().equals(ModItems.voidstone.getUnlocalizedName())) {
          String playerName = event.entity.getName();
          String itemOwner;
          if (itemCurrent.getTagCompound() == null) {
            itemOwner = "###";
          } else {
            itemOwner = itemCurrent.getTagCompound().getString("Owner");            
          }
          if (ModInfo.DEBUG) event.entity.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.GRAY + "itemPickup: " + itemCurrent.getUnlocalizedName() + EnumChatFormatting.DARK_GRAY + " (Owner=" + itemOwner + ")"));
          event.entity.addChatMessage(new ChatComponentTranslation(itemOwner+":"+playerName));
          if (playerName.equals(itemOwner)) {
            if (ModInfo.DEBUG) event.entity.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.GREEN + "owner valid!"));
          } else {
            if (ModInfo.DEBUG) event.entity.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.RED + "owner invalid!"));
          }
          int count = 0;
          for (int i = 0; i < event.entityPlayer.inventory.mainInventory.length; ++i) {
            ItemStack stack = event.entityPlayer.inventory.mainInventory[i];
            if (stack != null) {
              if (stack.getUnlocalizedName() == ModItems.voidstone.getUnlocalizedName()) {
                  count += stack.stackSize;
              }
            }
          }
          if (count > 0) {
            if (ModInfo.DEBUG) event.entity.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.RED + "itemPickup canceled!"));
            event.setCanceled(true);
          }
          //}
        }
      }
    }
  }

  @SubscribeEvent
  public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
    ItemStack item = event.crafting;
    EntityPlayer player = event.player;
    World world = player.worldObj;
    if (!world.isRemote) {
      if (ModInfo.DEBUG) player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.RED + "event fired!"));
      int count = countItems(player, ModItems.voidstone.getUnlocalizedName());
      if (ModInfo.DEBUG) player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.GRAY + "items found: " + count));
      if (count > 0) {
        if (ModInfo.DEBUG) player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.RED + "crafting canceled!"));
        removeLimitedItem(player, item);
      } else {
        if (ModInfo.DEBUG) player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.GREEN + "crafting successful!"));
      }
    }
  }

  public int countItems(EntityPlayer player, String item) {
    int count = 0;
    for (int i = 0; i < player.inventory.mainInventory.length; ++i) {
      ItemStack stack = player.inventory.mainInventory[i];
      if (stack != null) {
        if (stack.getUnlocalizedName() == item) {
            count += stack.stackSize;
        }
      }
    }
    return count;
  }

  // remove limited items that do not belong to the player
  public void removeLimitedItem(EntityPlayer player, ItemStack stack) {
    IInventory inv = player.inventory;
    for(int i=0; i < inv.getSizeInventory(); i++) {
      if(inv.getStackInSlot(i) != null) {
        ItemStack j = inv.getStackInSlot(i);
        if(j.getItem() != null && j.getItem() == stack.getItem()) {
          if (j.getTagCompound() != null) {
            if (!j.getTagCompound().getString("Owner").equals(player.getDisplayNameString())) {
              if (ModInfo.DEBUG) player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.RED + "item removed!"));
              inv.setInventorySlotContents(i, null);
            } else {
              if (ModInfo.DEBUG) player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.DARK_AQUA + "item found, saved!"));
            }
          } else {
            if (ModInfo.DEBUG) player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.RED + "item removed!"));
            inv.setInventorySlotContents(i, null);
          }
        }
      }
    }
}
  
}
