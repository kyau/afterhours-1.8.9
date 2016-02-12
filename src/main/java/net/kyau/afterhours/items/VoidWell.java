package net.kyau.afterhours.items;

import java.util.List;

import net.kyau.afterhours.network.PacketHandler;
import net.kyau.afterhours.network.SimplePacket;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.ChatUtil;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class VoidWell extends BaseItem {

  private int energyMax = 100; // max void energy
  private int distance = 64; // max teleport distance (max: 64)

  public VoidWell() {
    super();
    this.setUnlocalizedName(Ref.ItemID.VOIDWELL);
    this.maxStackSize = 1;
  }

  @Override
  public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
    // create nbt tags if none exist
    if (stack.getTagCompound() == null) {
      registerTags(stack);
    }
  }

  @Override
  public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
    World world = entityLiving.getEntityWorld();
    EntityPlayerMP player;
    // do nothing if nbt tags don't exist
    if (stack.getTagCompound() == null) {
      return false;
    }
    String owner = getOwner(stack);
    int[] energy = getVoidEnergy(stack);

    if (!world.isRemote) {
      if (entityLiving instanceof EntityPlayerMP) {
        player = (EntityPlayerMP) entityLiving;
        if (player.dimension != 0) {
          ChatUtil.sendNoSpam(player, EnumChatFormatting.RED + "Invalid dimension.");
          return false;
        }
        long ticksSinceLastUse = player.worldObj.getTotalWorldTime() - getLastUseTime(stack);
        if (ticksSinceLastUse > 100 && owner.equals(player.getDisplayNameString()) && energy[0] >= 10) {
          MovingObjectPosition mop = rayTrace(distance, 1.0F, player);
          BlockPos toPos = new BlockPos(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ());
          IBlockState state = world.getBlockState(toPos);
          Block block = (state == null) ? null : world.getBlockState(toPos).getBlock();
          if (block == null) {
            return false;
          }
          // LogHelper.info("Teleport (" + player.getDisplayNameString() + "): x: " + toPos.getX() + " y: " +
          // toPos.getY() + " z: " + toPos.getZ());
          player.setPositionAndUpdate(toPos.getX(), toPos.getY(), toPos.getZ());
          while (player.getEntityBoundingBox() != null && world.getCollidingBoundingBoxes(player, player.getEntityBoundingBox()) != null && !world.getCollidingBoundingBoxes(player, player.getEntityBoundingBox()).isEmpty()) {
            player.setPositionAndUpdate(player.posX, player.posY + 1.0D, player.posZ);
          }
          setVoidEnergy(stack, (energy[0] - 10));
          setLastUseTime(stack, (player.worldObj.getTotalWorldTime()));
          sendChatEnergy(player, stack);
        } else {
          if (player instanceof EntityPlayerMP) {
            IMessage msg = new SimplePacket.SimpleMessage(1, "afterhours:error");
            PacketHandler.net.sendTo(msg, (EntityPlayerMP) player);
          }
          return false;
        }
      } else {
        return false;
      }
      MinecraftServer.getServer().worldServerForDimension(0).playSoundEffect(player.posX, player.posY, player.posZ, "mob.endermen.portal", 1.0F, 1.0F);
    }
    return false;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    // do nothing if nbt tags don't exist
    if (stack.getTagCompound() == null) {
      return super.onItemRightClick(stack, world, player);
    }
    String owner = getOwner(stack);
    int[] energy = getVoidEnergy(stack);
    long ticksSinceLastUse = player.worldObj.getTotalWorldTime() - getLastUseTime(stack);

    if (!world.isRemote) {
      // set a new owner if none exists
      if (owner.equals("###")) {
        if (ModInfo.DEBUG)
          LogHelper.info("> DEBUG: ownership set!");
        player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "Soulbound!"));
        setOwner(stack, player.getDisplayNameString());
        setLastUseTime(stack, (player.worldObj.getTotalWorldTime()));
        return super.onItemRightClick(stack, world, player);
      } else {
        // invalid ownership
        if (!owner.equals(player.getDisplayNameString())) {
          if (ModInfo.DEBUG)
            LogHelper.info("> DEBUG: ownership invalid!");
          if (ModInfo.DEBUG)
            LogHelper.info("> DEBUG: owner: '" + owner + "'");
          player.playSound("afterhours:error", 0.5F, 1.0F);
          return super.onItemRightClick(stack, world, player);
        }
      }
      if (ticksSinceLastUse > 3 && (energy[0] < energy[1] && player.posY < 8 && player.dimension == 0)) {
        setVoidEnergy(stack, energy[0] + 2);
        setLastUseTime(stack, (player.worldObj.getTotalWorldTime()));
        sendChatEnergy(player, stack);
      }
    }
    // return super.onItemRightClick(stack, world, player);
    return stack;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
    String owner = getOwner(stack);
    int[] energy = getVoidEnergy(stack);
    // Soulbound status / item types
    if (!owner.equals("###")) {
      if (owner.equals(player.getDisplayNameString())) {
        tooltip.add(EnumChatFormatting.DARK_PURPLE + "Soulbound");
        tooltip.add(EnumChatFormatting.GREEN + "Owner: " + owner);
        tooltip.add(EnumChatFormatting.GRAY + "Energy: " + EnumChatFormatting.WHITE + energy[0] + "/" + energy[1]);
      } else {
        if (ModInfo.DEBUG) {
          tooltip.add(EnumChatFormatting.RED + "Owner: " + owner);
        } else {
          tooltip.add(EnumChatFormatting.RED + "Owner: " + EnumChatFormatting.OBFUSCATED + owner);
        }
      }
    } else {
      tooltip.add(EnumChatFormatting.DARK_PURPLE + "Right-click to soulbind item!");
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

  private void sendChatEnergy(EntityPlayer player, ItemStack stack) {
    ChatUtil.sendNoSpam(player, EnumChatFormatting.GREEN + "Void Energy: " + EnumChatFormatting.GRAY + getVoidEnergy(stack)[0] + "/" + getVoidEnergy(stack)[1]);
  }

  private void registerTags(ItemStack stack) {
    stack.setTagCompound(new NBTTagCompound());
    stack.getTagCompound().setLong("LastUse", -1);
    stack.getTagCompound().setString("Owner", "###");
    stack.getTagCompound().setString("Energy", "0#" + energyMax);
  }

  private String getOwner(ItemStack stack) {
    return stack.hasTagCompound() ? stack.getTagCompound().getString("Owner") : "###";
  }

  private void setOwner(ItemStack stack, String owner) {
    stack.setTagInfo("Owner", new NBTTagString(owner));
  }

  private long getLastUseTime(ItemStack stack) {
    return stack.hasTagCompound() ? stack.getTagCompound().getLong("LastUse") : 0;
  }

  private void setLastUseTime(ItemStack stack, long time) {
    stack.setTagInfo("LastUse", new NBTTagLong(time));
  }

  private int[] getVoidEnergy(ItemStack stack) {
    String[] energy = null;
    if (stack.hasTagCompound()) {
      String string = stack.getTagCompound().getString("Energy");
      if (string.contains("#")) {
        energy = string.split("#");
      } else {
        LogHelper.error("Void Well has invalid energy NBT!");
      }
    }
    return new int[] {
        energy == null ? 0 : Integer.parseInt(energy[0]),
        energy == null ? 100 : Integer.parseInt(energy[1]) };
  }

  private void setVoidEnergy(ItemStack stack, int energy) {
    stack.setTagInfo("Energy", new NBTTagString(energy + "#" + energyMax));
  }
}
