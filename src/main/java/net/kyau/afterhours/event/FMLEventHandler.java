package net.kyau.afterhours.event;

import javax.annotation.Nonnull;

import net.kyau.afterhours.config.ConfigHandler;
import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.references.ModInfo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FMLEventHandler {

  @SubscribeEvent
  public void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.modID.equalsIgnoreCase(ModInfo.MOD_ID)) {
      // Resync config
      ConfigHandler.loadConfig();
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
