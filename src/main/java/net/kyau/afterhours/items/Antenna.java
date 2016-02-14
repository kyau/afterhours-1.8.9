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

public class Antenna extends BaseItem {

  public Antenna() {
    super();
    this.setUnlocalizedName(Ref.ItemID.ANTENNA);
    this.maxStackSize = 16;
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
    // Description
    if (Keyboard.isKeyDown(0x2A) || Keyboard.isKeyDown(0x36)) {
      tooltip.add(EnumChatFormatting.GRAY + "All devices that connect through the");
      tooltip.add(EnumChatFormatting.GRAY + "void require an antenna for proper");
      tooltip.add(EnumChatFormatting.GRAY + "communication.");
    } else {
      tooltip.add(EnumChatFormatting.GRAY + "Hold " + EnumChatFormatting.WHITE + "SHIFT" + EnumChatFormatting.GRAY + " for more information.");
    }
    super.addInformation(stack, player, tooltip, advanced);
  }
}
