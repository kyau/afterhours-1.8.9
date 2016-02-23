package net.kyau.afterhours.items.armor;

import java.util.List;

import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.ItemHelper;
import net.kyau.afterhours.utils.LogHelper;
import net.kyau.afterhours.utils.NBTHelper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

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
    if (Keyboard.isKeyDown(0x2A) || Keyboard.isKeyDown(0x36)) {
      if (stack.getItem() == ModItems.darkmatter_boots) {
        tooltip.add(EnumChatFormatting.GRAY + "Provides you with increased jump");
        tooltip.add(EnumChatFormatting.GRAY + "distance, also mitigates fall damage");
        tooltip.add(EnumChatFormatting.GRAY + "with durability.");
      } else if (stack.getItem() == ModItems.darkmatter_chestplate) {
        tooltip.add(EnumChatFormatting.GRAY + "Grants the user the free flight ability");
        tooltip.add(EnumChatFormatting.GRAY + "given you have enough stored up");
        tooltip.add(EnumChatFormatting.GRAY + "energy in order to utilize it.");
      } else if (stack.getItem() == ModItems.darkmatter_helmet) {
        tooltip.add(EnumChatFormatting.GRAY + "Provides you with an in-helmet GUI");
        tooltip.add(EnumChatFormatting.GRAY + "that displays environmental stats");
        tooltip.add(EnumChatFormatting.GRAY + "along with chestplate energy values.");
      } else if (stack.getItem() == ModItems.darkmatter_leggings) {
        tooltip.add(EnumChatFormatting.GRAY + "Grants the user a boost to their");
        tooltip.add(EnumChatFormatting.GRAY + "movement via quantum phasing. You");
        tooltip.add(EnumChatFormatting.GRAY + "can stabilize the negative effects");
        tooltip.add(EnumChatFormatting.GRAY + "with a Dark Matter Helmet.");
      }
    } else {
      tooltip.add(StatCollector.translateToLocal(Ref.Translation.MORE_INFORMATION));
    }

    if (ItemHelper.hasOwner(stack)) {
      final String owner = ItemHelper.getOwnerName(stack);
      if (owner.equals(player.getDisplayNameString())) {
        // Owner information
        tooltip.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal(Ref.Translation.OWNER) + " " + owner);
        // Energy levels
        if (NBTHelper.hasTag(stack, Ref.NBT.ENERGY_LEVEL) && NBTHelper.hasTag(stack, Ref.NBT.ENERGY_MAX)) {
          int[] energy = NBTHelper.getEnergyLevels(stack);
          tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal(Ref.Translation.ENERGY) + " " + energy[0] + "/" + energy[1]);
        }
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

  @Override
  public void onUpdate(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) entityIn;
      // Set an Owner, if one doesn't exist already
      if (!ItemHelper.hasOwnerUUID(stack)) {
        ItemHelper.setOwner(stack, player);
      }
      // Set a UUID, if one doesn't exist already
      if (!NBTHelper.hasUUID(stack)) {
        NBTHelper.setUUID(stack);
      }
      if (stack.getItem() == ModItems.darkmatter_chestplate) {
        // Set energy levels, if they don't exist already
        if (!NBTHelper.hasTag(stack, Ref.NBT.ENERGY_LEVEL) || !NBTHelper.hasTag(stack, Ref.NBT.ENERGY_MAX)) {
          NBTHelper.setEnergyLevels(stack, 0, Ref.ItemStat.ENERGY_DARKMATTER_CHESTPLATE);
        }
      }
    }
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
