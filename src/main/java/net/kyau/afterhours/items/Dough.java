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


public class Dough extends Item {

  ItemTypes type;

  public Dough(ItemTypes type) {
    super();
    setUnlocalizedName("afterhours.dough");
    maxStackSize = 64;
    setCreativeTab(AfterHours.AfterHoursTab);
    this.type = type;
  }

  public ItemTypes getType() {
    return type;
  }

  @Override
  public String getUnlocalizedName() {
    return "afterhours.dough";
  }

  @Override
  public String getUnlocalizedName(ItemStack itemStack) {
    return "afterhours.dough";
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
    super.addInformation(stack, player, list, advanced);
  }}
