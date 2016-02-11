package net.kyau.afterhours.items;

import java.util.List;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Names;
import net.kyau.afterhours.utils.ItemHelper;
import net.kyau.afterhours.utils.LogHelper;
import net.kyau.afterhours.utils.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

public class VRD extends BaseItem {

  public VRD() {
    super();
    this.setUnlocalizedName(Names.Items.VRD);
    this.maxStackSize = 1;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    if (!ItemHelper.hasOwnerUUID(stack)) {
      ItemHelper.setOwner(stack, player);
      if (!world.isRemote)
        player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.LIGHT_PURPLE + "Soulbound!"));
    }
    // Set a UUID on the Alchemical Bag, if one doesn't exist already
    if (!NBTHelper.hasUUID(stack)) {
      LogHelper.info("Setting UUID");
      NBTHelper.setUUID(stack);
    }
    if (!world.isRemote) {
      if (player.isSneaking()) {
        if (ItemHelper.getOwnerName(stack).equals(player.getDisplayNameString())) {
          InventoryEnderChest invEnderChest = player.getInventoryEnderChest();
          if (invEnderChest != null)
            player.displayGUIChest(invEnderChest);
        }
      } else {
        if (ItemHelper.getOwnerName(stack).equals(player.getDisplayNameString())) {
          // TODO Do a scan of inventory and if we find a bag with the same UUID, change it's UUID
          NBTHelper.setBoolean(stack, Names.NBT.VRD_GUI_OPEN, true);
          player.openGui(AfterHours.instance, AfterHours.GUI_VRD, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
          return super.onItemRightClick(stack, world, player);
        }
      }
    } else {
      if (!(ItemHelper.getOwnerName(stack).equals(player.getDisplayNameString()))) {
        player.playSound("afterhours:error", 0.5F, 1.0F);
      }
    }
    return super.onItemRightClick(stack, world, player);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
    // Soulbound status / item types
    if (ItemHelper.hasOwner(stack)) {
      tooltip.add(EnumChatFormatting.DARK_PURPLE + "Soulbound");
    } else {
      tooltip.add(EnumChatFormatting.DARK_PURPLE + "Right-click to soulbind item!");
    }
    // Description
    if (Keyboard.isKeyDown(0x2A) || Keyboard.isKeyDown(0x36)) {
      tooltip.add(EnumChatFormatting.GRAY + "The Void Resonance Device is");
      tooltip.add(EnumChatFormatting.GRAY + "used to directly commune with");
      tooltip.add(EnumChatFormatting.GRAY + "the void.");
    } else {
      tooltip.add(EnumChatFormatting.GRAY + "Hold " + EnumChatFormatting.WHITE + "SHIFT" + EnumChatFormatting.GRAY + " for more information.");
    }
    // Owner information
    if (ItemHelper.hasOwner(stack)) {
      String owner = ItemHelper.getOwnerName(stack);
      if (owner.equals(player.getDisplayNameString())) {
        tooltip.add(EnumChatFormatting.GREEN + "Owner: " + owner);
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
}
