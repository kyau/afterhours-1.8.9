package net.kyau.afterhours.items;

import java.util.List;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Names;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
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

import org.lwjgl.input.Keyboard;

public class Voidstone extends BaseItem {

  // int cooldown = 1200; // 1 minute
  // int cooldown = 36000; // 30 minutes
  // int cooldown = 72000; // 1 hour
  int cooldown = 12000; // 10 minutes

  public Voidstone() {
    super();
    this.setUnlocalizedName(Names.Items.VOIDSTONE);
    setCreativeTab(AfterHours.AfterHoursTab);
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
      if (owner.equals("###")) {
        if (ModInfo.DEBUG)
          LogHelper.info("> DEBUG: ownership set!");
        player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "Soulbound!"));
        setOwner(stack, player.getDisplayNameString());
        setLastUseTime(stack, (player.worldObj.getTotalWorldTime() - cooldown));
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
      if (ModInfo.DEBUG)
        LogHelper.info("> DEBUG: ticksSinceLastUse: " + ticksSinceLastUse + "." + cooldown);
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
          playerHome = overworld.getSpawnPoint();
        }

        IBlockState state = overworld.getBlockState(playerHome);
        Block block = (state == null) ? null : overworld.getBlockState(playerHome).getBlock();
        if (block != null && !spawnpoint) {
          if (block.equals(Blocks.bed) || block.isBed(overworld, playerHome, player)) {
            // reposition player according to where bed wants the
            // player to spawn
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
            MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(playerMP, 0, new Teleporter(playerMP.mcServer.worldServerForDimension(0)));
          }
          player.setPositionAndUpdate(playerHome.getX(), playerHome.getY(), playerHome.getZ());
          while (player.getEntityBoundingBox() != null && world.getCollidingBoundingBoxes(player, player.getEntityBoundingBox()) != null && !world.getCollidingBoundingBoxes(player, player.getEntityBoundingBox()).isEmpty()) {
            player.setPositionAndUpdate(player.posX, player.posY + 1.0D, player.posZ);
          }
          MinecraftServer.getServer().worldServerForDimension(playerMP.dimension).playSoundEffect(player.posX, player.posY, player.posZ, "mob.endermen.portal", 1.0F, 1.0F);
          // trigger item cooldown
          setLastUseTime(stack, overworld.getTotalWorldTime());
          return super.onItemRightClick(stack, world, player);
        }
        /*
         * Server->Client Packet Example if (player instanceof EntityPlayerMP) { IMessage msg = new
         * SimplePacket.SimpleMessage("voidstone_activate"); PacketHandler.net.sendTo(msg, (EntityPlayerMP)player); }
         */
      }
    } else {
      if (player.dimension == 1 && owner.equals(player.getDisplayNameString())) {
        player.playSound("afterhours:error", 0.5F, 1.0F);
      } else if (!owner.equals("###") && (ticksSinceLastUse > 0 && ticksSinceLastUse < cooldown)) {
        player.playSound("afterhours:error", 0.5F, 1.0F);
      }
    }
    return super.onItemRightClick(stack, world, player);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
    String owner = getOwner(stack);
    // Soulbound status / item types
    if (!owner.equals("###")) {
      tooltip.add(EnumChatFormatting.DARK_PURPLE + "Soulbound, Limited");
    } else {
      tooltip.add(EnumChatFormatting.DARK_PURPLE + "Right-click to soulbind item!");
    }
    // Description
    if (Keyboard.isKeyDown(0x2A) || Keyboard.isKeyDown(0x36)) {
      tooltip.add(EnumChatFormatting.GRAY + "You may only own one instance of");
      tooltip.add(EnumChatFormatting.GRAY + "a limited item. Attempts to craft or");
      tooltip.add(EnumChatFormatting.GRAY + "obtain more will feed the void.");
    } else {
      tooltip.add(EnumChatFormatting.GRAY + "Hold " + EnumChatFormatting.WHITE + "SHIFT" + EnumChatFormatting.GRAY + " for more information.");
    }
    // Owner information
    if (!owner.equals("###")) {
      // list.add(EnumChatFormatting.DARK_PURPLE + "Soulbound, Limited");
      if (owner.equals(player.getDisplayNameString())) {
        tooltip.add(EnumChatFormatting.GREEN + "Owner: " + owner);
        if (getLastUseTime(stack) != -1) {
          long ticksSinceLastUse = player.worldObj.getTotalWorldTime() - getLastUseTime(stack);
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

  @Override
  public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
    // create nbt tags if none exist
    if (stack.getTagCompound() == null) {
      registerTags(stack);
    }
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

  private void registerTags(ItemStack stack) {
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
