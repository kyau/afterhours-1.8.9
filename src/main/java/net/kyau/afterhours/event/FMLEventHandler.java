package net.kyau.afterhours.event;

import javax.annotation.Nonnull;

import net.kyau.afterhours.config.ConfigHandler;
import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.utils.InventoryHandler;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class FMLEventHandler {

  @SubscribeEvent
  public void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.modID.equalsIgnoreCase(ModInfo.MOD_ID)) {
      // Resync config
      ConfigHandler.loadConfig();
    }
  }

  @SubscribeEvent
  public void onItemCrafted(@Nonnull PlayerEvent.ItemCraftedEvent event) {
    ItemStack item = event.crafting;
    EntityPlayer player = event.player;
    World world = player.worldObj;
    if (!world.isRemote) {
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

  @SubscribeEvent
  public void onEntityAttacked(@Nonnull LivingHurtEvent event) {
    // Only do 0.5 damage if our undestroyable tool out of durability
    if (event.entityLiving instanceof EntityLiving) {
      EntityLiving attackedEnt = (EntityLiving) event.entityLiving;
      DamageSource attackSource = event.source;
      if (attackSource.getSourceOfDamage() != null) {
        if (attackSource.getSourceOfDamage() instanceof EntityPlayer) {
          EntityPlayer player = (EntityPlayer) attackSource.getSourceOfDamage();
          if (player.getHeldItem() != null) {
            ItemStack stack = player.getHeldItem();
            if (ModItems.repairList.contains(stack.getUnlocalizedName())) {
              if (stack.getItemDamage() == stack.getMaxDamage()) {
                event.ammount = 0.5F;
              }
            }
          }
        }
      }
    }
  }
}
