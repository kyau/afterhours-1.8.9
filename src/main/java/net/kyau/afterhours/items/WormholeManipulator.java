package net.kyau.afterhours.items;

import java.util.List;

import net.kyau.afterhours.network.PacketHandler;
import net.kyau.afterhours.network.SimplePacketClient;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.ChatUtil;
import net.kyau.afterhours.utils.ItemHelper;
import net.kyau.afterhours.utils.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

public class WormholeManipulator extends BaseItem {

  private int cooldown = Ref.ItemCooldown.WORMHOLE_MANIPULATOR;
  private int distance = 512; // max teleport distance (max: 64)

  public WormholeManipulator() {
    super();
    this.setUnlocalizedName(Ref.ItemID.WORMHOLE_MANIPULATOR);
    this.maxStackSize = 1;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return false;
  }

  @Override
  public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
    ItemStack stack = new ItemStack(itemIn, 1, 0);
    stack.addEnchantment(Enchantment.getEnchantmentById(Ref.Enchant.ENTANGLEMENT_ID), 1);
    subItems.add(stack);
  }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
    return false;
  }

  @Override
  public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
    World world = entityLiving.getEntityWorld();
    EntityPlayerMP player;
    // Do nothing if nbt tags don't exist
    if (!ItemHelper.hasOwnerUUID(stack)) {
      return false;
    }
    if (!world.isRemote) {

      if (entityLiving instanceof EntityPlayerMP) {
        player = (EntityPlayerMP) entityLiving;
        // invalid ownership
        if (!ItemHelper.getOwnerName(stack).equals(player.getDisplayNameString())) {
          ChatUtil.sendNoSpam(player, Ref.Translation.IMPRINT_SCAN_FAILED);
          player.playSound("afterhours:error", 0.5F, 1.0F);
          return false;
        }
        // check for valid dimension
        if (player.dimension == 1 || player.dimension == -1) {
          ChatUtil.sendNoSpam(player, Ref.Translation.INVALID_DIM);
          player.playSound("afterhours:error", 0.5F, 1.0F);
          return false;
        }
        // Retrieve NBT data
        long ticksSinceLastUse = player.worldObj.getTotalWorldTime() - NBTHelper.getLong(stack, Ref.NBT.LASTUSE);
        int energy[] = NBTHelper.getEnergyLevels(stack);
        if (ticksSinceLastUse > 100 && ItemHelper.getOwnerName(stack).equals(player.getDisplayNameString()) && energy[0] >= 10) {
          MovingObjectPosition mop = rayTrace(distance, 1.0F, player);
          BlockPos toPos = new BlockPos(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ());
          IBlockState state = world.getBlockState(toPos);
          Block block = (state == null) ? null : world.getBlockState(toPos).getBlock();
          if (block == null) {
            return false;
          }
          try {
            if (mop.hitVec != null) {
              // If a block is found, call the moveEntity below
              player.moveEntity(mop.hitVec.xCoord - player.posX, mop.hitVec.yCoord - player.posY + 1.1, mop.hitVec.zCoord - player.posZ);
            }
          } catch (NullPointerException npe) {
            player.moveEntity(-distance * Math.sin(Math.toRadians(player.rotationYawHead)) * Math.cos(Math.toRadians(player.rotationPitch)), -distance * Math.sin(Math.toRadians(player.rotationPitch)), distance * Math.cos(Math.toRadians(player.rotationYawHead)) * Math.cos(Math.toRadians(player.rotationPitch)));
          }
          player.setPositionAndUpdate(toPos.getX(), toPos.getY(), toPos.getZ());
          while (player.getEntityBoundingBox() != null && world.getCollidingBoundingBoxes(player, player.getEntityBoundingBox()) != null && !world.getCollidingBoundingBoxes(player, player.getEntityBoundingBox()).isEmpty()) {
            player.setPositionAndUpdate(player.posX, player.posY + 1.0D, player.posZ);
          }
          NBTHelper.setEnergyLevels(stack, energy[0] - Ref.ItemStat.ENERGYONUSE_WORMHOLE_MANIPULATOR, Ref.ItemStat.ENERGY_WORMHOLE_MANIPULATOR);
          NBTHelper.setLastUse(stack, player.worldObj.getTotalWorldTime());
          EntityPlayerMP playerMP = (EntityPlayerMP) player;
          MinecraftServer.getServer().worldServerForDimension(playerMP.dimension).playSoundEffect(player.posX, player.posY, player.posZ, "afterhours:singularity", 1.0F, 1.0F);
          ItemHelper.sendChatEnergy(player, stack);
        } else {
          if (player instanceof EntityPlayerMP) {
            IMessage msg = new SimplePacketClient.SimpleClientMessage(1, "afterhours:error");
            PacketHandler.net.sendTo(msg, (EntityPlayerMP) player);
          }
          return false;
        }
      } else {
        return false;
      }
      // MinecraftServer.getServer().worldServerForDimension(0).playSoundEffect(player.posX, player.posY, player.posZ,
      // "mob.endermen.portal", 1.0F, 1.0F);
    }
    return false;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    boolean firstUse = false;
    // Set an Owner, if one doesn't exist already
    if (!ItemHelper.hasOwnerUUID(stack)) {
      ItemHelper.setOwner(stack, player);
      if (!world.isRemote)
        ChatUtil.sendNoSpam(player, new ChatComponentTranslation(Ref.Translation.IMPRINT_SUCCESS));
      firstUse = true;
    }
    // Set a UUID, if one doesn't exist already
    if (!NBTHelper.hasUUID(stack)) {
      NBTHelper.setUUID(stack);
    }
    // Set a LastUse, if one doesn't exist already
    if (!NBTHelper.hasTag(stack, Ref.NBT.LASTUSE)) {
      NBTHelper.setLastUse(stack, player.worldObj.getTotalWorldTime() - (cooldown + 10));
    }
    // Set energy levels, if they don't exist already
    if (!NBTHelper.hasTag(stack, Ref.NBT.ENERGY_LEVEL) || !NBTHelper.hasTag(stack, Ref.NBT.ENERGY_MAX)) {
      NBTHelper.setEnergyLevels(stack, 0, Ref.ItemStat.ENERGY_WORMHOLE_MANIPULATOR);
    }

    if (firstUse)
      return stack;

    long ticksSinceLastUse = player.worldObj.getTotalWorldTime() - NBTHelper.getLong(stack, Ref.NBT.LASTUSE);
    if (!world.isRemote) {
      // invalid ownership
      if (!ItemHelper.getOwnerName(stack).equals(player.getDisplayNameString())) {
        player.playSound("afterhours:error", 0.5F, 1.0F);
        return super.onItemRightClick(stack, world, player);
      }
      int energy[] = NBTHelper.getEnergyLevels(stack);
      if (ticksSinceLastUse > 3 && (energy[0] < energy[1] && ((player.posY < 8 && player.dimension == 0) || player.dimension == Ref.Dimension.DIM))) {
        // Update energy levels
        NBTHelper.setEnergyLevels(stack, energy[0] + Ref.ItemStat.WORMHOLE_MANIPULATOR_PERTICK, Ref.ItemStat.ENERGY_WORMHOLE_MANIPULATOR);
        // Trigger cooldown
        NBTHelper.setLastUse(stack, player.worldObj.getTotalWorldTime());
        ItemHelper.sendChatEnergy(player, stack);
      }
    }
    return stack;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
    // Item Stats
    if (!ItemHelper.hasOwner(stack)) {
      tooltip.add(StatCollector.translateToLocal(Ref.Translation.PREIMPRINT));
    }
    // Description
    if (Keyboard.isKeyDown(0x2A) || Keyboard.isKeyDown(0x36)) {
      tooltip.add(EnumChatFormatting.GRAY + "This device can harness wormholes");
      tooltip.add(EnumChatFormatting.GRAY + "for instant matter teleportation at");
      tooltip.add(EnumChatFormatting.GRAY + "a quantum level.");
    } else {
      tooltip.add(StatCollector.translateToLocal(Ref.Translation.MORE_INFORMATION));
    }

    if (ItemHelper.hasOwner(stack)) {
      final String owner = ItemHelper.getOwnerName(stack);
      if (owner.equals(player.getDisplayNameString())) {
        // Owner information
        tooltip.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal(Ref.Translation.OWNER) + " " + owner);
        // Cooldown
        if (NBTHelper.getLong(stack, Ref.NBT.LASTUSE) != -1) {
          long ticksSinceLastUse = player.worldObj.getTotalWorldTime() - NBTHelper.getLong(stack, Ref.NBT.LASTUSE);
          long current = (cooldown / 20) - (ticksSinceLastUse / 20);
          String currentCooldown = ItemHelper.formatCooldown(current);
          tooltip.add(StatCollector.translateToLocal(Ref.Translation.COOLDOWN) + " " + currentCooldown);
          if (ticksSinceLastUse > cooldown) {
            tooltip.remove(tooltip.size() - 1);
          }
        }
        // Energy levels
        if (NBTHelper.hasTag(stack, Ref.NBT.ENERGY_LEVEL) && NBTHelper.hasTag(stack, Ref.NBT.ENERGY_MAX)) {
          int[] energy = NBTHelper.getEnergyLevels(stack);
          tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal(Ref.Translation.ENERGY) + " " + energy[0] + "/" + energy[1]);
        }
      } else {
        if (ModInfo.DEBUG) {
          tooltip.add(EnumChatFormatting.RED + StatCollector.translateToLocal(Ref.Translation.OWNER) + " " + owner);
        } else {
          tooltip.add(EnumChatFormatting.RED + StatCollector.translateToLocal(Ref.Translation.OWNER) + " " + EnumChatFormatting.OBFUSCATED + owner);
        }
      }
    }
    super.addInformation(stack, player, tooltip, advanced);
  }

  public Vec3 getPositionEyes(float partialTicks, Entity entity) {
    if (partialTicks == 1.0F) {
      return new Vec3(entity.posX, entity.posY + (double) entity.getEyeHeight(), entity.posZ);
    } else {
      double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
      double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks + (double) entity.getEyeHeight();
      double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;
      return new Vec3(d0, d1, d2);
    }
  }

  public MovingObjectPosition rayTrace(double blockReachDistance, float partialTicks, Entity entity) {
    Vec3 vec3 = getPositionEyes(partialTicks, entity);
    Vec3 vec31 = entity.getLook(partialTicks);
    Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);
    return entity.worldObj.rayTraceBlocks(vec3, vec32, false, false, true);
  }

}
