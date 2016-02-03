package net.kyau.afterhours.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.references.ItemTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class Antenna extends Item {

  ItemTypes type;

  public Antenna(ItemTypes type) {
    super();
    setUnlocalizedName("afterhours.antenna");
    maxStackSize = 16;
    setCreativeTab(AfterHours.AfterHoursTab);
    this.type = type;
  }

  public ItemTypes getType() {
    return type;
  }

  @Override
  public String getUnlocalizedName() {
    return "afterhours.antenna";
  }

  @Override
  public String getUnlocalizedName(ItemStack itemStack) {
    return "afterhours.antenna";
  }

  @Override
  public boolean getShareTag() {
    return true;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    return super.onItemRightClick(stack, world, player);
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
    // Description
    if (Keyboard.isKeyDown(0x2A) || Keyboard.isKeyDown(0x36)) {
      list.add(EnumChatFormatting.GRAY + "All devices that connect through the");
      list.add(EnumChatFormatting.GRAY + "void require an antenna for proper");
      list.add(EnumChatFormatting.GRAY + "communication.");
    } else {
      list.add(EnumChatFormatting.GRAY + "Hold " + EnumChatFormatting.WHITE + "SHIFT" + EnumChatFormatting.GRAY + " for more information.");
    }
    super.addInformation(stack, player, list, advanced);
  }
}
