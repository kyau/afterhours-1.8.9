package net.kyau.afterhours.event;

import java.util.Iterator;

import javax.annotation.Nonnull;

import net.kyau.afterhours.init.ModVanilla;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityLivingEvent {

  @SubscribeEvent
  public void onEntityDrop(@Nonnull LivingDropsEvent event) {
    if (event.entityLiving instanceof EntityCow || event.entityLiving instanceof EntityHorse) {
      ItemStack stack;
      Iterator drop = event.drops.iterator();
      while (drop.hasNext()) {
        stack = ((EntityItem) drop.next()).getEntityItem();
        if (stack != null) {
          Item item = stack.getItem();
          // LogHelper.info("Mob Drops: " + item.getUnlocalizedName());
          if (item.getUnlocalizedName().equals("item.leather")) {
            drop.remove();
          }
        }
      }
      // Drop rate (0.25d is 25% chance, 1D is a 100% chance)
      double rand = Math.random();
      if (rand < 0.40d) {
        if (event.entityLiving.isBurning()) {
          event.drops.add(new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, new ItemStack(Items.leather)));
        } else {
          event.drops.add(new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, new ItemStack(ModVanilla.rawhide)));
        }
      }
    }
  }
}
