package net.kyau.afterhours.items;

import java.util.List;

import net.kyau.afterhours.dimension.TeleporterVoid;
import net.kyau.afterhours.references.Ref;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

public class Singularity extends BaseItem {

  public Singularity() {
    super();
    this.setUnlocalizedName(Ref.ItemID.SINGULARITY);
    this.maxStackSize = 1;
  }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
    return false;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    if (!world.isRemote) {
      EntityPlayerMP playerMP = (EntityPlayerMP) player;
      if (playerMP.dimension != Ref.Dimension.DIM && playerMP.dimension != 1) {
        player.capabilities.isFlying = false;
        MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(playerMP, Ref.Dimension.DIM, new TeleporterVoid(playerMP.mcServer.worldServerForDimension(Ref.Dimension.DIM)));
        player.setPositionAndUpdate(0, 128, 0);
        MinecraftServer.getServer().worldServerForDimension(playerMP.dimension).playSoundEffect(player.posX, player.posY, player.posZ, "afterhours:singularity", 1.0F, 1.0F);
      }
    }
    return super.onItemRightClick(stack, world, player);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
    if (player.worldObj.getWorldTime() % 500 > 0 && player.worldObj.getWorldTime() % 500 < 60) {
      tooltip.add(EnumChatFormatting.DARK_RED + "Graviton awaits...");
    }
    // Description
    if (Keyboard.isKeyDown(0x2A) || Keyboard.isKeyDown(0x36)) {
      tooltip.add(EnumChatFormatting.GRAY + "Torn from space itself, this very");
      tooltip.add(EnumChatFormatting.GRAY + "massive, one-dimensional crystal");
      tooltip.add(EnumChatFormatting.GRAY + "seems to glow with an un-natural");
      tooltip.add(EnumChatFormatting.GRAY + "aura.");
    } else {
      tooltip.add(StatCollector.translateToLocal(Ref.Translation.MORE_INFORMATION));
    }
    super.addInformation(stack, player, tooltip, advanced);
  }
}
