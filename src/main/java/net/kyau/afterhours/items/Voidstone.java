package net.kyau.afterhours.items;

import java.util.List;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.network.PacketHandler;
import net.kyau.afterhours.network.SimplePacket;
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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Voidstone extends Item {

  public static int cooldown = 0;
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
    if (cooldown == 0) {
      if (!world.isRemote) {
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
            return stack;
          }
        } else if (block == null) {
          player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.RED + "ERROR: No spawn or bed location found!"));
          return stack;
        }

        // trigger item cooldown
        stack.getTagCompound().setBoolean("timer", true);
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
      }
      world.playSoundAtEntity(player, "mob.endermen.portal", 1.0F, 1.0F);
      return stack;
    } else if (cooldown > 0 && cooldown != 1200) {
      player.playSound("afterhours:error", 0.5F, 1.0F);
      return stack;
    }
    return stack;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
    super.addInformation(stack, player, list, advanced);
    int current = cooldown / 20;
    list.add("Cooldown: " + current + "s");
    if (cooldown < 1) {
      list.remove(1);
    }
  }

  @Override
  public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
    if (stack.getTagCompound() == null) {
      stack.setTagCompound(new NBTTagCompound());
      stack.getTagCompound().setBoolean("timer", false);
    }
    if (stack.getTagCompound().getBoolean("timer")) {
      cooldown = 1200;
      stack.getTagCompound().setBoolean("timer", false);
    }
    if (cooldown > 0) {
      cooldown--;
    }
  }
}
