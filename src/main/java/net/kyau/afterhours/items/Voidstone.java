package net.kyau.afterhours.items;

import java.util.List;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.ModInfo;
import net.kyau.afterhours.references.ItemTypes;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Voidstone extends Item {

  //int cooldown = 1200; // 1 minute
  int cooldown = 36000; // 30 minutes
  //int cooldown = 72000; // 1 hour
  ItemTypes type;

  public Voidstone(ItemTypes type) {
    super();
    setUnlocalizedName("afterhours.voidstone");
    maxStackSize = 1;
    setCreativeTab(AfterHours.AfterHoursTab);
    this.type = type;
  }

  public ItemTypes getType() {
    return type;
  }

  @Override
  public String getUnlocalizedName() {
    return "afterhours.voidstone";
  }

  @Override
  public String getUnlocalizedName(ItemStack itemStack) {
    return "afterhours.voidstone";
  }

  @Override
  public boolean getShareTag() {
    return true;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    // do nothing if nbt tags don't exist
    if (stack.getTagCompound() == null) {
      return super.onItemRightClick(stack, world, player);
    }
    String owner = stack.getTagCompound().getString("Owner");
    long ticksSinceLastUse = player.worldObj.getTotalWorldTime() - getLastUseTime(stack);

    if (!world.isRemote) {
      // set a new owner if none exists
      if (owner.contains("###")) {
        if (ModInfo.DEBUG) player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.GREEN + "ownership set!"));
        setOwner(stack, player.getDisplayNameString());
        setLastUseTime(stack, (player.worldObj.getTotalWorldTime() - cooldown));
        return super.onItemRightClick(stack, world, player);
      } else {
        // invalid ownership
        if (!owner.contains(player.getDisplayNameString())) {
          if (ModInfo.DEBUG) player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.RED + "ownership invalid!"));
          if (ModInfo.DEBUG) player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.GRAY + "owner: '" + owner + "'"));
          player.playSound("afterhours:error", 0.5F, 1.0F);
          return super.onItemRightClick(stack, world, player);
        }
      }
      if (ModInfo.DEBUG) player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "> DEBUG: " + EnumChatFormatting.GRAY + "ticksSinceLastUse: " + ticksSinceLastUse + EnumChatFormatting.WHITE + "." + EnumChatFormatting.DARK_GRAY + cooldown));
      if (ticksSinceLastUse > cooldown || ticksSinceLastUse < 0) {
          // get the overworld world object
          DimensionManager dm = new DimensionManager();
          World overworld = dm.getWorld(0);
          BlockPos playerHome = player.getBedLocation(0);
          boolean spawnpoint = false;
  
          // if player has no bed location, set to warp location to server
          // spawn
          if (playerHome == null) {
            spawnpoint = true;
            playerHome = world.getSpawnPoint();
          }
  
          IBlockState state = world.getBlockState(playerHome);
          Block block = (state == null) ? null : world.getBlockState(playerHome).getBlock();
          if (block != null && !spawnpoint) {
            if (block.equals(Blocks.bed) || block.isBed(world, playerHome, player)) {
              // reposition player according to where bed wants the
              // player to spawn
              playerHome = block.getBedSpawnPosition(world, playerHome, null);
            } else {
              player.addChatMessage(new ChatComponentTranslation("Your bed was missing or obstructed."));
              return super.onItemRightClick(stack, world, player);
            }
          } else if (block == null) {
            player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.RED + "ERROR: No spawn or bed location found!"));
            return super.onItemRightClick(stack, world, player);
          }
  
          // trigger item cooldown
          setLastUseTime(stack, world.getTotalWorldTime());
          // spawn exists, teleport the player
          EntityPlayerMP playerMP = (EntityPlayerMP) player;
          MinecraftServer.getServer().worldServerForDimension(0).playSoundEffect(player.posX, player.posY, player.posZ, "mob.endermen.portal", 1.0F, 1.0F);
          if (playerMP.dimension != 0) {
            MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(playerMP, 0, new Teleporter(playerMP.mcServer.worldServerForDimension(0)));
          }
          player.setPositionAndUpdate(playerHome.getX(), playerHome.getY(), playerHome.getZ());
          while (player.getEntityBoundingBox() != null && world.getCollidingBoundingBoxes(player, player.getEntityBoundingBox()) != null && !world.getCollidingBoundingBoxes(player, player.getEntityBoundingBox()).isEmpty()) {
            player.setPositionAndUpdate(player.posX, player.posY + 1.0D, player.posZ);
          }
          /*
           * Server->Client Packet Example if (player instanceof EntityPlayerMP) {
           * IMessage msg = new SimplePacket.SimpleMessage("voidstone_activate");
           * PacketHandler.net.sendTo(msg, (EntityPlayerMP)player); }
           */
        world.playSoundAtEntity(player, "mob.endermen.portal", 1.0F, 1.0F);
        return super.onItemRightClick(stack, world, player);
      }
    } else {
      if (owner != "###" && (ticksSinceLastUse > 0 && ticksSinceLastUse < cooldown)) {
        player.playSound("afterhours:error", 0.5F, 1.0F);
      }
    }
    return super.onItemRightClick(stack, world, player);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
    super.addInformation(stack, player, list, advanced);
    String owner = getOwner(stack);
    if (!owner.contains("###")) {
      if (owner.contains(player.getDisplayNameString())) {
        list.add(EnumChatFormatting.GREEN + "Owner: " + owner);
        if (getLastUseTime(stack) != -1) {
          long ticksSinceLastUse = player.worldObj.getTotalWorldTime() - getLastUseTime(stack);
          long current = (cooldown / 20) - (ticksSinceLastUse / 20);
          String currentCooldown = formatCooldown(current);
          list.add("Cooldown: " + currentCooldown);
          if (ticksSinceLastUse > cooldown) {
            list.remove(list.size()-1);
          }
        }
      } else {
        if (ModInfo.DEBUG) {
          list.add(EnumChatFormatting.RED + "Owner: " + owner);
        } else {
          list.add(EnumChatFormatting.RED + "Owner: " + EnumChatFormatting.OBFUSCATED + owner);
        }
      }
    }
  }

  @Override
  public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
    // create nbt tags if none exist
    if (stack.getTagCompound() == null) {
      registerTags(stack);
    }
  }

  public String formatCooldown(long time) {
    long hours = time / 3600;
    long minutes = (time % 3600) / 60;
    long seconds = time % 60;
    String currentCooldown = "";
    if (hours > 0) {
      currentCooldown = String.format("%02dh %02dm %02ds", hours, minutes, seconds);
    } else if (minutes > 0) {
      currentCooldown = String.format("%02dm %02ds", minutes, seconds);
    } else {
      currentCooldown = String.format("%02ds", seconds);
    }
    return currentCooldown;
  }

  public void registerTags(ItemStack stack) {
    stack.setTagCompound(new NBTTagCompound());
    stack.getTagCompound().setLong("LastUse", -1);
    stack.getTagCompound().setString("Owner", "###");
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
}
