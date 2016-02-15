package net.kyau.afterhours.items;

import java.util.List;

import net.kyau.afterhours.dimension.TeleporterVoid;
import net.kyau.afterhours.references.Ref;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
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
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    if (!world.isRemote) {
      EntityPlayerMP playerMP = (EntityPlayerMP) player;
      if (playerMP.dimension != 5) {
        MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(playerMP, 5, new TeleporterVoid(playerMP.mcServer.worldServerForDimension(5)));
      }
      player.setPositionAndUpdate(0, 128, 0);
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
      tooltip.add(EnumChatFormatting.GRAY + "Hold " + EnumChatFormatting.WHITE + "SHIFT" + EnumChatFormatting.GRAY + " for more information.");
    }
    super.addInformation(stack, player, tooltip, advanced);
  }
}
