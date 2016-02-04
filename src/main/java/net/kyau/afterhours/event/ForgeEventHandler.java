package net.kyau.afterhours.event;

import javax.annotation.Nonnull;

import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.utils.InventoryHandler;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class ForgeEventHandler {

  @SubscribeEvent
  public void onItemPickup(@Nonnull EntityItemPickupEvent event) {
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
          if (ModInfo.DEBUG)
            LogHelper.info("> DEBUG: itemPickup: " + itemCurrent.getUnlocalizedName().substring(11) + " (owner=" + itemOwner + ")");
          if (playerName.equals(itemOwner)) {
            if (ModInfo.DEBUG)
              LogHelper.info("> DEBUG: owner valid!");
          } else {
            if (ModInfo.DEBUG)
              LogHelper.info("> DEBUG: owner invalid!");
          }
          int count = InventoryHandler.countItems(event.entityPlayer, ModItems.voidstone);
          if (count > 0) {
            if (ModInfo.DEBUG)
              LogHelper.info("> DEBUG: itemPickup canceled!");
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
        ItemStack item = new ItemStack(ModItems.voidstone);
        int count = InventoryHandler.countItems(player, ModItems.voidstone);
        if (count > 1) {
          InventoryHandler.removeLimitedItem(player, item);
        }
      }
    }
  }
}
