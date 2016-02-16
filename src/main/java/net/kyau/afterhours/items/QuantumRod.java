package net.kyau.afterhours.items;

import java.util.List;

import net.kyau.afterhours.references.Ref;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

public class QuantumRod extends BaseItem {

  public QuantumRod() {
    super();
    this.setUnlocalizedName(Ref.ItemID.QUANTUMROD);
    this.maxStackSize = 16;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    return super.onItemRightClick(stack, world, player);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
    // Description
    if (Keyboard.isKeyDown(0x2A) || Keyboard.isKeyDown(0x36)) {
      tooltip.add(EnumChatFormatting.GRAY + "Used to stabalize dark matter in");
      tooltip.add(EnumChatFormatting.GRAY + "order to harness the material.");
    } else {
      tooltip.add(StatCollector.translateToLocal(Ref.Translation.MORE_INFORMATION));
    }
    super.addInformation(stack, player, tooltip, advanced);
  }
}
