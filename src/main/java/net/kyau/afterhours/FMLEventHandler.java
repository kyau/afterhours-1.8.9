package net.kyau.afterhours;

import net.kyau.afterhours.items.ModItems;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class FMLEventHandler {

  @SubscribeEvent
  public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
    ItemStack item = event.crafting;
    EntityPlayer player = event.player;
    World world = player.worldObj;
    if (!world.isRemote) {
      if (item.getUnlocalizedName().equals(ModItems.voidstone.getUnlocalizedName())) {
        int count = Utils.countItems(player, ModItems.voidstone.getUnlocalizedName());
        if (count > 0) {
          if (ModInfo.DEBUG)
            player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.GREEN + "crafting successful!"));
          Utils.removeLimitedItem(player, item);
        }
      } else {
        if (ModInfo.DEBUG)
          player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.GREEN + "crafting successful!"));
      }
    }
  }
}
