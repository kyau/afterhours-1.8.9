package net.kyau.afterhours.items.darkmatter;

import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.items.BaseItem;
import net.kyau.afterhours.references.Ref;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Multimap;

public class DarkMatterSword extends BaseItem {

  private float attackDamage;
  private final BaseItem.ToolMaterial material;

  public DarkMatterSword(BaseItem.ToolMaterial material) {
    this.material = material;
    this.maxStackSize = 1;
    this.setMaxDamage(material.getMaxUses() - 250);
    this.attackDamage = 4.0F + material.getDamageVsEntity();
    this.setUnlocalizedName(Ref.ItemID.DARKMATTER_SWORD);
    ModItems.repairList.add(this.getUnlocalizedName());
  }

  public float getDamageVsEntity() {
    return this.material.getDamageVsEntity();
  }

  @Override
  public float getStrVsBlock(ItemStack stack, Block block) {
    if (block == Blocks.web) {
      return 15.0F;
    } else {
      Material material = block.getMaterial();
      return material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.gourd ? 1.0F : 1.5F;
    }
  }

  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    damageItem(stack, 1, attacker);
    return true;
  }

  @Override
  public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
    return false;
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
    if ((double) blockIn.getBlockHardness(worldIn, pos) != 0.0D) {
      damageItem(stack, 5, playerIn);
    }

    return true;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean isFull3D() {
    return true;
  }

  @Override
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.BLOCK;
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return 72000;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
    return itemStackIn;
  }

  @Override
  public boolean canHarvestBlock(Block blockIn) {
    return blockIn == Blocks.web;
  }

  @Override
  public int getItemEnchantability() {
    return this.material.getEnchantability();
  }

  public String getToolMaterialName() {
    return this.material.toString();
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return false;
  }

  @Override
  public Multimap<String, AttributeModifier> getItemAttributeModifiers() {
    Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();
    multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", (double) this.attackDamage, 0));
    return multimap;
  }

}
