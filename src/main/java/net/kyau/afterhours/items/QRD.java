package net.kyau.afterhours.items;

import java.util.List;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.ChatUtil;
import net.kyau.afterhours.utils.ItemHelper;
import net.kyau.afterhours.utils.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

public class QRD extends BaseItem {

  public QRD() {
    super();
    this.setUnlocalizedName(Ref.ItemID.QRD);
    this.maxStackSize = 1;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    // Set an Owner, if one doesn't exist already
    if (!ItemHelper.hasOwnerUUID(stack)) {
      ItemHelper.setOwner(stack, player);
      if (!world.isRemote)
        ChatUtil.sendNoSpam(player, new ChatComponentTranslation(Ref.Translation.IMPRINT_SUCCESS));
    }
    // Set a UUID, if one doesn't exist already
    if (!NBTHelper.hasUUID(stack)) {
      NBTHelper.setUUID(stack);
    }
    final String owner = ItemHelper.getOwnerName(stack);
    if (!world.isRemote) {
      if (player.isSneaking()) {
        if (owner.equals(player.getDisplayNameString())) {
          InventoryEnderChest invEnderChest = player.getInventoryEnderChest();
          if (invEnderChest != null)
            player.displayGUIChest(invEnderChest);
        }
      } else {
        if (owner.equals(player.getDisplayNameString())) {
          // TODO Do a scan of inventory and if we find a bag with the same UUID, change it's UUID
          NBTHelper.setBoolean(stack, Ref.NBT.QRD_GUI_OPEN, true);
          player.openGui(AfterHours.instance, AfterHours.GUI_VRD, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
          return super.onItemRightClick(stack, world, player);
        }
      }
    } else {
      // invalid ownership
      if (!(owner.equals(player.getDisplayNameString()))) {
        ChatUtil.sendNoSpam(player, Ref.Translation.IMPRINT_SCAN_FAILED);
        player.playSound("afterhours:error", 0.5F, 1.0F);
        return super.onItemRightClick(stack, world, player);
      }
    }
    return super.onItemRightClick(stack, world, player);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
    // Item Stats
    if (ItemHelper.hasOwner(stack)) {
      tooltip.add(StatCollector.translateToLocal(Ref.Translation.IMPRINTED));
    } else {
      tooltip.add(StatCollector.translateToLocal(Ref.Translation.PREIMPRINT));
    }
    // Description
    if (Keyboard.isKeyDown(0x2A) || Keyboard.isKeyDown(0x36)) {
      tooltip.add(EnumChatFormatting.GRAY + "This device uses quantum resonance");
      tooltip.add(EnumChatFormatting.GRAY + "to shift matter into qubits to digitally");
      tooltip.add(EnumChatFormatting.GRAY + "store them.");
      tooltip.add(EnumChatFormatting.BLUE + "Upgrades Available!");
    } else {
      tooltip.add(StatCollector.translateToLocal(Ref.Translation.MORE_INFORMATION));
    }
    // Owner information
    if (ItemHelper.hasOwner(stack)) {
      final String owner = ItemHelper.getOwnerName(stack);
      if (owner.equals(player.getDisplayNameString())) {
        tooltip.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal(Ref.Translation.OWNER) + " " + owner);
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
