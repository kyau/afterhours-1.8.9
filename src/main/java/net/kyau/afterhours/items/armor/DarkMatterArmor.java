package net.kyau.afterhours.items.armor;

import java.util.List;

import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DarkMatterArmor extends ItemArmor {

  public DarkMatterArmor(String unlocalizedName, ArmorMaterial material, int renderIndex, int armorType) {
    super(material, renderIndex, armorType);
    this.setUnlocalizedName(unlocalizedName);
  }

  public ItemArmor register(String name) {
    GameRegistry.registerItem(this, name);
    ModItems.armorList.add(this);
    return this;
  }

  public void registerRender(String name) {
    ModelLoader.registerItemVariants(this);
    ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":" + name, "inventory"));
    if (ModInfo.DEBUG)
      LogHelper.info("ClientProxy: registerRender(): " + name);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
    /*    if (stack.getUnlocalizedName().equals(ModInfo.MOD_ID + "." + Ref.ItemID.DARKMATTER_BOOTS)) {
          tooltip.add(EnumChatFormatting.DARK_PURPLE + "Absorption I");
        } else if (stack.getUnlocalizedName().equals(ModInfo.MOD_ID + "." + Ref.ItemID.DARKMATTER_CHESTPLATE)) {
          tooltip.add(EnumChatFormatting.DARK_PURPLE + "Gravitation I");
        } else if (stack.getUnlocalizedName().equals(ModInfo.MOD_ID + "." + Ref.ItemID.DARKMATTER_LEGGINGS)) {
          tooltip.add(EnumChatFormatting.DARK_PURPLE + "Quantum Boost I");
        } else if (stack.getUnlocalizedName().equals(ModInfo.MOD_ID + "." + Ref.ItemID.DARKMATTER_HELMET)) {
          tooltip.add(EnumChatFormatting.DARK_PURPLE + "Quantum Display I");
        }*/
    super.addInformation(stack, player, tooltip, advanced);
  }

  @Override
  public String getUnlocalizedName() {
    return String.format("%s.%s", ModInfo.MOD_ID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
  }

  @Override
  public String getUnlocalizedName(ItemStack itemStack) {
    return String.format("%s.%s", ModInfo.MOD_ID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
  }

  protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
    return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
  }

  @Override
  public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
    ItemStack stack = new ItemStack(itemIn, 1, 0);
    if (stack.getUnlocalizedName().equals(ModInfo.MOD_ID + "." + Ref.ItemID.DARKMATTER_BOOTS)) {
      stack.addEnchantment(Enchantment.getEnchantmentById(Ref.Enchant.ABSORPTION_ID), 1);
      stack.addEnchantment(Enchantment.getEnchantmentById(Ref.Enchant.ENTANGLEMENT_ID), 1);
    } else if (stack.getUnlocalizedName().equals(ModInfo.MOD_ID + "." + Ref.ItemID.DARKMATTER_CHESTPLATE)) {
      stack.addEnchantment(Enchantment.getEnchantmentById(Ref.Enchant.ENTANGLEMENT_ID), 1);
      stack.addEnchantment(Enchantment.getEnchantmentById(Ref.Enchant.GRAVITATION_ID), 1);
    } else if (stack.getUnlocalizedName().equals(ModInfo.MOD_ID + "." + Ref.ItemID.DARKMATTER_LEGGINGS)) {
      stack.addEnchantment(Enchantment.getEnchantmentById(Ref.Enchant.ENTANGLEMENT_ID), 1);
      stack.addEnchantment(Enchantment.getEnchantmentById(Ref.Enchant.QUANTUMBOOST_ID), 1);
    } else if (stack.getUnlocalizedName().equals(ModInfo.MOD_ID + "." + Ref.ItemID.DARKMATTER_HELMET)) {
      stack.addEnchantment(Enchantment.getEnchantmentById(Ref.Enchant.ENTANGLEMENT_ID), 1);
      stack.addEnchantment(Enchantment.getEnchantmentById(Ref.Enchant.QUANTUMDISPLAY_ID), 1);
    }
    subItems.add(stack);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return false;
  }

}
