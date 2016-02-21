package net.kyau.afterhours.items.tools;

import java.util.List;
import java.util.Set;

import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.items.BaseItem;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Multimap;

public class DarkMatterTool extends ItemTool {

  private Set<Block> effectiveBlocks;
  protected float efficiencyOnProperMaterial = 4.0F;
  private float damageVsEntity;
  protected ToolMaterial toolMaterial;
  private String toolClass;

  protected DarkMatterTool(float attackDamage, ToolMaterial material, Set<Block> effectiveBlocks) {
    super(attackDamage, material, effectiveBlocks);
    this.toolMaterial = material;
    this.effectiveBlocks = effectiveBlocks;
    this.maxStackSize = 1;
    this.efficiencyOnProperMaterial = Ref.DarkMatter.EFFICIENCY;
    this.damageVsEntity = attackDamage + Ref.DarkMatter.DAMAGE - 4.0F;
    this.setCreativeTab(CreativeTabs.tabTools);
    if (this instanceof DarkMatterPickaxe) {
      this.setMaxDamage(Ref.DarkMatter.DURABILITY);
      this.efficiencyOnProperMaterial = Ref.DarkMatter.EFFICIENCY * 0.8F;
      toolClass = "pickaxe";
    } else if (this instanceof DarkMatterAxe) {
      this.setMaxDamage(Ref.DarkMatter.DURABILITY - 500);
      this.efficiencyOnProperMaterial = Ref.DarkMatter.EFFICIENCY;
      toolClass = "axe";
    } else if (this instanceof DarkMatterShovel) {
      this.efficiencyOnProperMaterial = Ref.DarkMatter.EFFICIENCY;
      this.setMaxDamage(Ref.DarkMatter.DURABILITY - 750);
      toolClass = "shovel";
    }
  }

  public Item register(String name) {
    GameRegistry.registerItem(this, name);
    ModItems.itemList.add((Item) this);
    return this;
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
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
    super.addInformation(stack, player, tooltip, advanced);
  }

  @Override
  public float getStrVsBlock(ItemStack stack, Block block) {
    return this.effectiveBlocks.contains(block) ? this.efficiencyOnProperMaterial : 1.0F;
  }

  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    BaseItem.damageItem(stack, 5, attacker);
    return true;
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
    if ((double) blockIn.getBlockHardness(worldIn, pos) != 0.0D) {
      BaseItem.damageItem(stack, 1, playerIn);
    }

    return true;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return false;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean isFull3D() {
    return true;
  }

  public Item.ToolMaterial getToolMaterial() {
    return this.toolMaterial;
  }

  public String getToolMaterialName() {
    return this.toolMaterial.toString();
  }

  @Override
  public Set<String> getToolClasses(ItemStack stack) {
    return toolClass != null ? com.google.common.collect.ImmutableSet.of(toolClass) : super.getToolClasses(stack);
  }

  @Override
  public float getDigSpeed(ItemStack stack, net.minecraft.block.state.IBlockState state) {
    for (String type : getToolClasses(stack)) {
      if (state.getBlock().isToolEffective(type, state))
        return efficiencyOnProperMaterial;
    }
    return super.getDigSpeed(stack, state);
  }

  @Override
  public int getItemEnchantability() {
    return this.toolMaterial.getEnchantability();
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return false;
  }

  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(ItemStack stack) {
    Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(stack);
    multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Tool modifier", (double) this.damageVsEntity, 0));
    return multimap;
  }

  @Override
  public int getHarvestLevel(ItemStack stack, String toolClass) {
    return this.toolMaterial.getHarvestLevel();
  }

  @Override
  public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
    ItemStack stack = new ItemStack(itemIn, 1, 0);
    // stack.addEnchantment(Enchantment.smite, 1);
    stack.addEnchantment(Enchantment.getEnchantmentById(Ref.Enchant.ENTANGLEMENT_ID), 1);
    subItems.add(stack);
  }
}
