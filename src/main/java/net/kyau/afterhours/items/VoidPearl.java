package net.kyau.afterhours.items;

import java.util.List;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.config.PlayerProperties;
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
import net.minecraft.nbt.NBTTagCompound;
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

  public static int cooldown = Ref.ItemCooldown.VOIDPEARL;

  public VoidPearl() {
    super();
    this.setUnlocalizedName(Ref.ItemID.VOIDPEARL);
    setCreativeTab(AfterHours.AfterHoursTab);
  }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
    return false;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    boolean firstUse = false;
    NBTTagCompound playerNBT = player.getEntityData();
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
    if (!playerNBT.hasKey(Ref.NBT.LASTUSE)) {
      playerNBT.setLong(Ref.NBT.LASTUSE, player.worldObj.getTotalWorldTime() - (cooldown + 10));
    }
    // if (!NBTHelper.hasTag(stack, Ref.NBT.LASTUSE)) {
    // NBTHelper.setLastUse(stack, player.worldObj.getTotalWorldTime() - (cooldown + 10));
    // }

    if (firstUse)
      return super.onItemRightClick(stack, world, player);

    final String owner = ItemHelper.getOwnerName(stack);
    // invalid ownership
    if (!owner.equals(player.getDisplayNameString())) {
      ChatUtil.sendNoSpam(player, Ref.Translation.IMPRINT_SCAN_FAILED);
      if (!world.isRemote) {
        player.playSound("afterhours:error", 0.5F, 1.0F);
      }
      return super.onItemRightClick(stack, world, player);
    }

    // long ticksSinceLastUse = player.worldObj.getTotalWorldTime() - playerNBT.getLong(Ref.NBT.LASTUSE);
    // long ticksSinceLastUse = player.worldObj.getTotalWorldTime() - NBTHelper.getLong(stack, Ref.NBT.LASTUSE);
    DimensionManager dm = new DimensionManager();
    World overworld = dm.getWorld(0);
    PlayerProperties props = PlayerProperties.get(player);
    long currentTime = player.worldObj.getTotalWorldTime();
    // long lastUse = playerNBT.getLong(Ref.NBT.LASTUSE);
    long lastUse = props.getCooldown();
    long ticksSinceLastUse = currentTime - lastUse;

    if (ModInfo.DEBUG)
      LogHelper.info("> DEBUG: ticksSinceLastUse: " + ticksSinceLastUse + "." + cooldown);
    if (ticksSinceLastUse > cooldown || ticksSinceLastUse < 0) {
      // Get the Overworld world object
      BlockPos playerHome = player.getBedLocation(0);
      boolean spawnpoint = false;

      // If player has no bed, set the destination to server spawn
      if (playerHome == null) {
        spawnpoint = true;
        if (!world.isRemote) {
          playerHome = overworld.getSpawnPoint();
        } else {
          playerHome = player.worldObj.getSpawnPoint();
        }
      }

      if (!world.isRemote) {
        IBlockState state = overworld.getBlockState(playerHome);
        Block block = (state == null) ? null : overworld.getBlockState(playerHome).getBlock();
        if (block != null && !spawnpoint) {
          if (block.equals(Blocks.bed) || block.isBed(overworld, playerHome, player)) {
            // Reposition player according to where bed wants the player to spawn
            playerHome = block.getBedSpawnPosition(overworld, playerHome, null);
          } else {
            player.addChatMessage(new ChatComponentTranslation("Your bed was missing or obstructed."));
            return super.onItemRightClick(stack, world, player);
          }
        } else if (block == null) {
          player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.RED + "ERROR: No spawn or bed location found!"));
          return super.onItemRightClick(stack, world, player);
        }

        // Trigger cooldown and send client packet
        long time = overworld.getTotalWorldTime();
        props.setCooldown(time);
        // IMessage msg = new SimplePacketClient.SimpleClientMessage(2, String.valueOf(time));
        // PacketHandler.net.sendTo(msg, (EntityPlayerMP) player);

        // Destination exists, teleport the player
        EntityPlayerMP playerMP = (EntityPlayerMP) player;
        // Make sure destination is clean
        while (overworld.getBlockState(playerHome).getBlock().isOpaqueCube())
          playerHome = playerHome.up(2);
        // Issue teleport
        if (!(playerMP.dimension == 1)) {
          if (playerMP.dimension != 0) {
            MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(playerMP, 0, new TeleporterVoid(playerMP.mcServer.worldServerForDimension(0)));
          }

          player.setPositionAndUpdate(playerHome.getX(), playerHome.getY(), playerHome.getZ());
          player.fallDistance = 0F;
          MinecraftServer.getServer().worldServerForDimension(playerMP.dimension).playSoundEffect(player.posX, player.posY, player.posZ, "mob.endermen.portal", 1.0F, 1.0F);
        }
      }
      return super.onItemRightClick(stack, world, player);
    } else {
      if (player.dimension == 1 && owner.equals(player.getDisplayNameString())) {
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
    if (!ItemHelper.hasOwner(stack)) {
      tooltip.add(StatCollector.translateToLocal(Ref.Translation.PREIMPRINT));
    }
    // Description
    if (Keyboard.isKeyDown(0x2A) || Keyboard.isKeyDown(0x36)) {
      tooltip.add(EnumChatFormatting.GRAY + "This device may be used to transport");
      tooltip.add(EnumChatFormatting.GRAY + "you across dimensions to your home.");
    } else {
      tooltip.add(StatCollector.translateToLocal(Ref.Translation.MORE_INFORMATION));
    }
    // Owner information
    if (ItemHelper.hasOwner(stack)) {
      final String owner = ItemHelper.getOwnerName(stack);

      if (owner.equals(player.getDisplayNameString())) {
        tooltip.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal(Ref.Translation.OWNER) + " " + owner);
        long lastUse = AfterHours.proxy.getVoidPearlLastUse();
        if (lastUse != -1) {
          long currentTime = player.worldObj.getTotalWorldTime();
          long ticksSinceLastUse = currentTime - lastUse;
          // LogHelper.info("Worldtime: " + currentTime + " / Last Use: " + lastUse);
          if (ticksSinceLastUse < cooldown) {
            long current = (cooldown / 20) - (ticksSinceLastUse / 20);
            String currentCooldown = ItemHelper.formatCooldown(current);
            tooltip.add(StatCollector.translateToLocal(Ref.Translation.COOLDOWN) + " " + currentCooldown);
          }
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

}
