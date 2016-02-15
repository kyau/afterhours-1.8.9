package net.kyau.afterhours.event;

import javax.annotation.Nonnull;

import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.utils.InventoryHandler;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class FMLEventHandler {

  @SubscribeEvent
  public void onItemCrafted(@Nonnull PlayerEvent.ItemCraftedEvent event) {
    ItemStack item = event.crafting;
    EntityPlayer player = event.player;
    World world = player.worldObj;
    if (!world.isRemote) {
      if (event.craftMatrix.getStackInSlot(0).getUnlocalizedName().equals(ModItems.darkmatter_sword.getUnlocalizedName()) && event.craftMatrix.getStackInSlot(1).getUnlocalizedName().equals(ModItems.darkmatter.getUnlocalizedName())) {
        LogHelper.info("Found Sword in crafting table.");
        int newDamage = event.craftMatrix.getStackInSlot(0).getItemDamage();
        event.crafting.setItemDamage(newDamage + 750);
        LogHelper.info("New Damage: " + event.crafting.getItemDamage());
      }
      if (item.getUnlocalizedName().equals(ModItems.voidpearl.getUnlocalizedName())) {
        int count = InventoryHandler.countItems(player, ModItems.voidpearl);
        if (count > 1) {
          if (ModInfo.DEBUG)
            LogHelper.info("> DEBUG: crafting successful, deleting!");
          InventoryHandler.removeLimitedItem(player, item);
        }
      } else {
        if (ModInfo.DEBUG)
          LogHelper.info("> DEBUG: " + player.getDisplayNameString() + ": crafted:" + item.getDisplayName() + " (x" + item.stackSize + ")");
      }
    }
  }
}
