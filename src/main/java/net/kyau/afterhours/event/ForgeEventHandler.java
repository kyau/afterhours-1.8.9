package net.kyau.afterhours.event;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.kyau.afterhours.enchantment.EnchantmentEntanglement;
import net.kyau.afterhours.init.ModBlocks;
import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.InventoryHandler;
import net.kyau.afterhours.utils.ItemHelper;
import net.kyau.afterhours.utils.LogHelper;
import net.kyau.afterhours.utils.NBTHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class ForgeEventHandler {

  public static List<String> tooltipList = new ArrayList<String>();
  public static int tick = 0;

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
  public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
    if (event.entityLiving instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.entityLiving;
      World world = player.worldObj;
      if (!player.capabilities.isCreativeMode) {
        if (player.inventory.armorInventory[2] == null) {
          player.capabilities.allowFlying = false;
          player.capabilities.disableDamage = false;
        } else {
          if (player.inventory.armorInventory[2].getUnlocalizedName().equals(ModItems.darkmatter_chestplate.getUnlocalizedName())) {
            if (player.inventory.armorInventory[2].hasTagCompound()) {
              String owner = ItemHelper.getOwnerName(player.inventory.armorInventory[2]);
              int energy[] = NBTHelper.getEnergyLevels(player.inventory.armorInventory[2]);
              if (player.getDisplayNameString().equals(owner) && energy[0] > 0) {
                player.capabilities.allowFlying = true;
                player.capabilities.disableDamage = false;
              }
            }
          }
        }
        if (player.inventory.armorInventory[1] == null) {
          changeSpeed(player, 1D, "speedMod");
        } else {
          if (player.inventory.armorInventory[1].getUnlocalizedName().equals(ModItems.darkmatter_leggings.getUnlocalizedName())) {
            if (player.inventory.armorInventory[1].hasTagCompound()) {
              String owner = ItemHelper.getOwnerName(player.inventory.armorInventory[1]);
              if (player.getDisplayNameString().equals(owner)) {
                changeSpeed(player, 2.2D, "speedMod");
              }
            }
          } else {
            changeSpeed(player, 1D, "speedMod");
          }
        }
      }
    }
  }

  @SubscribeEvent
  public void onFOVChange(FOVUpdateEvent event) {
    if (event.entity instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.entity;
      if (!(player.inventory.armorInventory[3] == null)) {
        if (player.inventory.armorInventory[3].getUnlocalizedName().equals(ModItems.darkmatter_helmet.getUnlocalizedName())) {
          if (player.inventory.armorInventory[3].hasTagCompound()) {
            String owner = ItemHelper.getOwnerName(player.inventory.armorInventory[1]);
            if (player.getDisplayNameString().equals(owner)) {
              event.newfov = 1.0F;
            }
          }
        }
      }
    }
  }

  @SubscribeEvent
  public void onPlayerFall(LivingFallEvent event) {
    if (event.entity instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.entityLiving;
      if (player.inventory.armorInventory[0] != null) {
        if (player.inventory.armorInventory[0].getUnlocalizedName().equals(ModItems.darkmatter_boots.getUnlocalizedName())) {
          if (player.inventory.armorInventory[0].hasTagCompound()) {
            String owner = ItemHelper.getOwnerName(player.inventory.armorInventory[0]);
            if (player.getDisplayNameString().equals(owner)) {
              int damage = (int) (1.5 * event.distance * event.damageMultiplier);
              String soundName = damage > 10 ? "game.player.hurt.fall.big" : "game.player.hurt.fall.small";
              player.inventory.armorInventory[0].attemptDamageItem(damage, new Random());
              event.damageMultiplier = 0;
              event.entityLiving.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, soundName, 1.0F, 1.0F);
            }
          }
        }
      }
    }
  }

  @SubscribeEvent
  public void onPlayerFallFlying(PlayerFlyableFallEvent event) {
    boolean boots = false;
    if (event.entity instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.entityLiving;
      int damage = (int) (0.4 * event.distance * event.multipler);
      if (player.inventory.armorInventory[0] != null) {
        if (player.inventory.armorInventory[0].getUnlocalizedName().equals(ModItems.darkmatter_boots.getUnlocalizedName())) {
          if (player.inventory.armorInventory[0].hasTagCompound()) {
            String owner = ItemHelper.getOwnerName(player.inventory.armorInventory[0]);
            if (player.getDisplayNameString().equals(owner)) {
              if (event.distance > 10) {
                String soundName = damage > 5 ? "game.player.hurt.fall.big" : "game.player.hurt.fall.small";
                player.inventory.armorInventory[0].attemptDamageItem(damage, new Random());
                player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, soundName, 1.0F, 1.0F);
                boots = true;
              }
              event.multipler = 0;
            }
          }
        }
      }
      if (!boots) {
        if (event.distance > 3) {
          damage = (int) (event.distance * event.multipler) / 2;
          player.attackEntityFrom(DamageSource.fall, damage);
        }
      }
    }
  }

  @SubscribeEvent
  public void onPlayerJump(LivingJumpEvent event) {
    if (event.entityLiving instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.entityLiving;
      World world = player.worldObj;
      if (!(player.inventory.armorInventory[0] == null)) {
        if (player.inventory.armorInventory[0].getUnlocalizedName().equals(ModItems.darkmatter_boots.getUnlocalizedName())) {
          if (player.inventory.armorInventory[0].hasTagCompound()) {
            String owner = ItemHelper.getOwnerName(player.inventory.armorInventory[0]);
            if (player.getDisplayNameString().equals(owner)) {
              float f = player.rotationYaw * 0.017453292F;
              if (player.motionX > 0 || player.motionX < 0) {
                player.motionX -= (double) (MathHelper.sin(f) * 1.2F);
              }
              if (player.motionZ > 0 || player.motionZ < 0) {
                player.motionZ += (double) (MathHelper.cos(f) * 1.2F);
              }
              player.motionY = (double) 1.0F;
            }
          }
        }
      }
    }
  }

  private void playerTickEvent(EntityPlayer player) {
    final ItemStack equippedChestplate = player.inventory.armorInventory[2];
    if (equippedChestplate != null) {
      if (equippedChestplate.getUnlocalizedName().equals(ModItems.darkmatter_chestplate.getUnlocalizedName())) {
        if (equippedChestplate.hasTagCompound()) {
          if (NBTHelper.hasTag(equippedChestplate, Ref.NBT.ENERGY_LEVEL) && NBTHelper.hasTag(equippedChestplate, Ref.NBT.ENERGY_MAX)) {
            BlockPos underPlayer = new BlockPos(player.posX, MathHelper.floor_double(player.posY - 0.1D), player.posZ);
            if (player.worldObj.getBlockState(underPlayer).getBlock() == ModBlocks.quantum_chargepad) {
              int energy[] = NBTHelper.getEnergyLevels(equippedChestplate);
              if ((energy[0] + Ref.BlockStat.CHARGEPAD_PERTICK) <= energy[1]) {
                NBTHelper.setEnergyLevels(equippedChestplate, (energy[0] + Ref.BlockStat.CHARGEPAD_PERTICK), energy[1]);
              } else {
                NBTHelper.setEnergyLevels(equippedChestplate, energy[1], energy[1]);
              }
              if (player.getHeldItem() != null) {
                Item heldItem = player.getHeldItem().getItem();
                ItemStack heldStack = player.getHeldItem();
                if (heldItem == ModItems.wormhole_manipulator && heldStack.hasTagCompound()) {
                  if (NBTHelper.hasTag(heldStack, Ref.NBT.ENERGY_LEVEL) && NBTHelper.hasTag(heldStack, Ref.NBT.ENERGY_MAX)) {
                    int heldEnergy[] = NBTHelper.getEnergyLevels(heldStack);
                    if ((heldEnergy[0] + (Ref.BlockStat.CHARGEPAD_PERTICK) / 2) <= heldEnergy[1]) {
                      NBTHelper.setEnergyLevels(heldStack, (heldEnergy[0] + (Ref.BlockStat.CHARGEPAD_PERTICK / 2)), heldEnergy[1]);
                    } else {
                      NBTHelper.setEnergyLevels(heldStack, heldEnergy[1], heldEnergy[1]);
                    }
                  }
                }
              }
            }
            if (player.dimension == Ref.Dimension.DIM) {
              int energy[] = NBTHelper.getEnergyLevels(equippedChestplate);
              if ((energy[0] + 1) <= energy[1]) {
                NBTHelper.setEnergyLevels(equippedChestplate, (energy[0] + 1), energy[1]);
              }
            }
            if (player.capabilities.isFlying) {
              int energy[] = NBTHelper.getEnergyLevels(equippedChestplate);
              int newEnergy = energy[0] - 3;
              if (newEnergy > 0) {
                NBTHelper.setEnergyLevels(equippedChestplate, newEnergy, energy[1]);
              } else {
                NBTHelper.setEnergyLevels(equippedChestplate, 0, energy[1]);
              }
            }
          }
        }
      }
    }
    ItemStack item = new ItemStack(ModItems.voidpearl);
    int count = InventoryHandler.countItems(player, ModItems.voidpearl);
    if (count > 1) {
      InventoryHandler.removeLimitedItem(player, item);
    }
  }

  @SubscribeEvent
  public void playerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase == Phase.START) {
      EntityPlayer player = event.player;
      World world = player.worldObj;
      if (!world.isRemote) {
        if (tick >= 10) {
          playerTickEvent(player);
          tick = 0;
        } else {
          tick++;
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

  public void changeSpeed(EntityLivingBase entity, double modifier, String name) {
    final UUID speedModifierUUID = UUID.fromString("c5595a67-9410-4fb2-826a-bcaf432c6a6f");
    AttributeModifier speedModifier = (new AttributeModifier(speedModifierUUID, name, modifier - 1, 2)).setSaved(false);
    IAttributeInstance iattributeinstance = entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed);

    if (iattributeinstance.getModifier(speedModifierUUID) != null) {
      iattributeinstance.removeModifier(speedModifier);
    }
    iattributeinstance.applyModifier(speedModifier);
  }

}
