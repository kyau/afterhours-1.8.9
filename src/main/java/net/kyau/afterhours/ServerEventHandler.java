package net.kyau.afterhours;

import net.kyau.afterhours.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class ServerEventHandler {

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
          if (ModInfo.DEBUG)
            event.entity.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.GRAY + "itemPickup: " + itemCurrent.getUnlocalizedName().substring(11) + EnumChatFormatting.DARK_GRAY + " (owner=" + itemOwner + ")"));
          if (playerName.equals(itemOwner)) {
            if (ModInfo.DEBUG)
              event.entity.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.GREEN + "owner valid!"));
          } else {
            if (ModInfo.DEBUG)
              event.entity.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.RED + "owner invalid!"));
          }
          int count = Utils.countItems(event.entityPlayer, ModItems.voidstone.getUnlocalizedName());
          if (count > 0) {
            if (ModInfo.DEBUG)
              event.entity.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.RED + "itemPickup canceled!"));
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
        int count = Utils.countItems(player, ModItems.voidstone.getUnlocalizedName());
        if (count > 1) {
          Utils.removeLimitedItem(player, item);
        }
      }
    }
  }
}
