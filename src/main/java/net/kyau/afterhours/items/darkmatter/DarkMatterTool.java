package net.kyau.afterhours.items.darkmatter;

import java.util.Set;

import net.kyau.afterhours.items.BaseItem;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Multimap;

public class DarkMatterTool extends BaseItem {

  private Set<Block> effectiveBlocks;
  protected float efficiencyOnProperMaterial = 4.0F;
  private float damageVsEntity;
  protected BaseItem.ToolMaterial toolMaterial;
  private String toolClass;

  protected DarkMatterTool(float attackDamage, BaseItem.ToolMaterial material, Set<Block> effectiveBlocks) {
    this.toolMaterial = material;
    this.effectiveBlocks = effectiveBlocks;
    this.maxStackSize = 1;
    this.efficiencyOnProperMaterial = material.getEfficiency();
    this.damageVsEntity = attackDamage + material.getDamageVsEntity() - 4.0F;
    this.setCreativeTab(CreativeTabs.tabTools);
    if (this instanceof DarkMatterPickaxe) {
      this.setMaxDamage(material.getMaxUses());
      toolClass = "pickaxe";
    } else if (this instanceof DarkMatterAxe) {
      this.setMaxDamage(material.getMaxUses() - 500);
      toolClass = "axe";
    } else if (this instanceof DarkMatterShovel) {
      this.setMaxDamage(material.getMaxUses() - 750);
      toolClass = "shovel";
    }
  }

  @Override
  public float getStrVsBlock(ItemStack stack, Block block) {
    return this.effectiveBlocks.contains(block) ? this.efficiencyOnProperMaterial : 1.0F;
  }

  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    damageItem(stack, 5, attacker);
    return true;
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
    if ((double) blockIn.getBlockHardness(worldIn, pos) != 0.0D) {
      damageItem(stack, 1, playerIn);
    }

    return true;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean isFull3D() {
    return true;
  }

  public BaseItem.ToolMaterial getToolMaterial() {
    return this.toolMaterial;
  }

  @Override
  public int getItemEnchantability() {
    return this.toolMaterial.getEnchantability();
  }

  public String getToolMaterialName() {
    return this.toolMaterial.toString();
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return false;
  }

  @Override
  public Multimap<String, AttributeModifier> getItemAttributeModifiers() {
    Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();
    multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Tool modifier", (double) this.damageVsEntity, 0));
    return multimap;
  }

}
