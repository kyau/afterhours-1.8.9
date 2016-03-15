package net.kyau.afterhours.event;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.kyau.afterhours.config.ConfigHandler;
import net.kyau.afterhours.config.PlayerProperties;
import net.kyau.afterhours.init.ModBlocks;
import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.network.PacketHandler;
import net.kyau.afterhours.network.SimplePacketClient;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

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

  @SubscribeEvent
  public void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
    if (!event.player.worldObj.isRemote) {
      PlayerProperties props = PlayerProperties.get(event.player);
      IMessage msg = new SimplePacketClient.SimpleClientMessage(2, String.valueOf(props.getCooldown()));
      PacketHandler.net.sendTo(msg, (EntityPlayerMP) event.player);
    }
  }

  @SubscribeEvent
  public void onBlockExplosion(ExplosionEvent.Detonate event) {
    List<BlockPos> blocks = event.getAffectedBlocks();
    List<BlockPos> newlist = new ArrayList<BlockPos>();
    for (BlockPos pos : blocks) {
      if (!(event.world.getBlockState(pos).getBlock() == ModBlocks.voidstone || event.world.getBlockState(pos).getBlock() == ModBlocks.infused_voidstone)) {
        newlist.add(pos);
      } else {
        LogHelper.info(event.world.getBlockState(pos).getBlock().getUnlocalizedName());
      }
    }
  }

  @SubscribeEvent
  public void onHarvest(HarvestDropsEvent event) {
    if (event.state.getBlock() == ModBlocks.voidstone || event.state.getBlock() == ModBlocks.infused_voidstone) {
      if (!(event.harvester instanceof EntityPlayer)) {
        event.drops.clear();
      }
    }
  }
}
