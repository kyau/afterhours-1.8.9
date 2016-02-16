package net.kyau.afterhours.items;

import java.util.List;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.dimension.TeleporterVoid;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.ChatUtil;
import net.kyau.afterhours.utils.ItemHelper;
import net.kyau.afterhours.utils.LogHelper;
import net.kyau.afterhours.utils.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

public class VoidPearl extends BaseItem {

  // int cooldown = 1200; // 1 minute
  // int cooldown = 36000; // 30 minutes
  // int cooldown = 72000; // 1 hour
  public static int cooldown = 12000; // 10 minutes

  public VoidPearl() {
    super();
    this.setUnlocalizedName(Ref.ItemID.VOIDPEARL);
    setCreativeTab(AfterHours.AfterHoursTab);
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    // Set an Owner on the Void Pearl, if one doesn't exist already
    if (!ItemHelper.hasOwnerUUID(stack)) {
      ItemHelper.setOwner(stack, player);
      if (!world.isRemote)
        ChatUtil.sendNoSpam(player, new ChatComponentTranslation("afterhours.msg.bound"));
    }
    // Set a UUID on the Void Pearl, if one doesn't exist already
    if (!NBTHelper.hasUUID(stack)) {
      NBTHelper.setUUID(stack);
    }
    // Set a LastUse on the Void Pearl, if one doesn't exist already
    if (!NBTHelper.hasTag(stack, Ref.NBT.LASTUSE)) {
      NBTHelper.setLastUse(stack, player.worldObj.getTotalWorldTime() - (cooldown + 10));
    }

    long ticksSinceLastUse = player.worldObj.getTotalWorldTime() - NBTHelper.getLong(stack, Ref.NBT.LASTUSE);

    if (!world.isRemote) {
      if (!ItemHelper.getOwnerName(stack).equals(player.getDisplayNameString())) {
        player.playSound("afterhours:error", 0.5F, 1.0F);
        return super.onItemRightClick(stack, world, player);
      }
      if (ModInfo.DEBUG)
        LogHelper.info("> DEBUG: ticksSinceLastUse: " + ticksSinceLastUse + "." + cooldown);
      if (ticksSinceLastUse > cooldown || ticksSinceLastUse < 0) {
        // get the overworld world object
        DimensionManager dm = new DimensionManager();
        World overworld = dm.getWorld(0);
        BlockPos playerHome = player.getBedLocation(0);
        boolean spawnpoint = false;

        // if player has no bed location, set to warp location to server spawn
        if (playerHome == null) {
          spawnpoint = true;
          playerHome = overworld.getSpawnPoint();
        }

        IBlockState state = overworld.getBlockState(playerHome);
        Block block = (state == null) ? null : overworld.getBlockState(playerHome).getBlock();
        if (block != null && !spawnpoint) {
          if (block.equals(Blocks.bed) || block.isBed(overworld, playerHome, player)) {
            // reposition player according to where bed wants the player to spawn
            playerHome = block.getBedSpawnPosition(overworld, playerHome, null);
          } else {
            player.addChatMessage(new ChatComponentTranslation("Your bed was missing or obstructed."));
            return super.onItemRightClick(stack, world, player);
          }
        } else if (block == null) {
          player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.RED + "ERROR: No spawn or bed location found!"));
          return super.onItemRightClick(stack, world, player);
        }

        // spawn exists, teleport the player
        EntityPlayerMP playerMP = (EntityPlayerMP) player;
        if (!(playerMP.dimension == 1)) {
          if (playerMP.dimension != 0) {
            MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(playerMP, 0, new TeleporterVoid(playerMP.mcServer.worldServerForDimension(0)));
          }
          player.setPositionAndUpdate(playerHome.getX(), playerHome.getY(), playerHome.getZ());
          while (player.getEntityBoundingBox() != null && world.getCollidingBoundingBoxes(player, player.getEntityBoundingBox()) != null && !world.getCollidingBoundingBoxes(player, player.getEntityBoundingBox()).isEmpty()) {
            player.setPositionAndUpdate(player.posX, player.posY + 1.0D, player.posZ);
          }
          MinecraftServer.getServer().worldServerForDimension(playerMP.dimension).playSoundEffect(player.posX, player.posY, player.posZ, "mob.endermen.portal", 1.0F, 1.0F);
          // trigger item cooldown
          NBTHelper.setLastUse(stack, overworld.getTotalWorldTime());
          return super.onItemRightClick(stack, world, player);
        }
        /*
         * Server->Client Packet Example if (player instanceof EntityPlayerMP) { IMessage msg = new
         * SimplePacket.SimpleMessage("voidstone_activate"); PacketHandler.net.sendTo(msg, (EntityPlayerMP)player); }
         */
      }
    } else {

      if (player.dimension == 1 && ItemHelper.getOwnerName(stack).equals(player.getDisplayNameString())) {
        player.playSound("afterhours:error", 0.5F, 1.0F);
      } else if (ticksSinceLastUse > 0 && ticksSinceLastUse < cooldown) {
        player.playSound("afterhours:error", 0.5F, 1.0F);
      }
    }
    return super.onItemRightClick(stack, world, player);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
    // Item Stats
    if (ItemHelper.hasOwner(stack)) {
      tooltip.add(EnumChatFormatting.DARK_PURPLE + Ref.ItemStat.BOUND + ", " + Ref.ItemStat.LIMITED);
    } else {
      tooltip.add(StatCollector.translateToLocal("afterhours.msg.prebound").trim());
    }
    // Description
    if (Keyboard.isKeyDown(0x2A) || Keyboard.isKeyDown(0x36)) {
      tooltip.add(EnumChatFormatting.GRAY + "This device may be used to transport");
      tooltip.add(EnumChatFormatting.GRAY + "you across dimensions to your home.");
    } else {
      tooltip.add(EnumChatFormatting.GRAY + "Hold " + EnumChatFormatting.WHITE + "SHIFT" + EnumChatFormatting.GRAY + " for more information.");
    }
    // Owner information
    if (ItemHelper.hasOwner(stack)) {
      String owner = ItemHelper.getOwnerName(stack);
      if (owner.equals(player.getDisplayNameString())) {
        tooltip.add(EnumChatFormatting.GREEN + "Owner: " + owner);
        if (NBTHelper.getLong(stack, Ref.NBT.LASTUSE) != -1) {
          long ticksSinceLastUse = player.worldObj.getTotalWorldTime() - NBTHelper.getLong(stack, Ref.NBT.LASTUSE);
          long current = (cooldown / 20) - (ticksSinceLastUse / 20);
          String currentCooldown = formatCooldown(current);
          tooltip.add("Cooldown: " + currentCooldown);
          if (ticksSinceLastUse > cooldown) {
            tooltip.remove(tooltip.size() - 1);
          }
        }
      } else {
        if (ModInfo.DEBUG) {
          tooltip.add(EnumChatFormatting.RED + "Owner: " + owner);
        } else {
          tooltip.add(EnumChatFormatting.RED + "Owner: " + EnumChatFormatting.OBFUSCATED + owner);
        }
      }
    }
    super.addInformation(stack, player, tooltip, advanced);
  }

  private String formatCooldown(long time) {
    long hours = time / 3600;
    long minutes = (time % 3600) / 60;
    long seconds = time % 60;
    String currentCooldown = "";
    if (hours > 0) {
      currentCooldown = String.format("%dh %dm %ds", hours, minutes, seconds);
    } else if (minutes > 0) {
      currentCooldown = String.format("%dm %ds", minutes, seconds);
    } else {
      currentCooldown = String.format("%ds", seconds);
    }
    return currentCooldown;
  }

}
