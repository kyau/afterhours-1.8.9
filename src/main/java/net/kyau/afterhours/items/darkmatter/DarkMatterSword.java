package net.kyau.afterhours.items.darkmatter;

import net.kyau.afterhours.items.BaseItem;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Multimap;

public class DarkMatterSword extends BaseItem {

  private float attackDamage;
  private final BaseItem.ToolMaterial material;
  private boolean broken = false;

  public DarkMatterSword(BaseItem.ToolMaterial material) {
    this.material = material;
    this.maxStackSize = 1;
    this.setMaxDamage(material.getMaxUses());
    this.attackDamage = 4.0F + material.getDamageVsEntity();
    this.setUnlocalizedName(Ref.ItemID.DARKMATTER_SWORD);
  }

  /**
   * Returns the amount of damage this item will deal. One heart of damage is equal to 2 damage points.
   */
  public float getDamageVsEntity() {
    return this.material.getDamageVsEntity();
  }

  public float getStrVsBlock(ItemStack stack, Block block) {
    if (block == Blocks.web) {
      return 15.0F;
    } else {
      Material material = block.getMaterial();
      return material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.gourd ? 1.0F : 1.5F;
    }
  }

  /**
   * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
   * the damage on the stack.
   */
  public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    damageItem(stack, 1, attacker);
    return true;
  }

  @Override
  public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
    if (stack.getItemDamage() == stack.getMaxDamage()) {
      this.broken = true;
    } else {
      this.broken = false;
    }
    if (this.broken)
      return true;
    return false;
  }

  /**
   * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
   */
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
    if ((double) blockIn.getBlockHardness(worldIn, pos) != 0.0D) {
      damageItem(stack, 2, playerIn);
    }

    return true;
  }

  /**
   * Returns True is the item is renderer in full 3D when hold.
   */
  @SideOnly(Side.CLIENT)
  public boolean isFull3D() {
    return true;
  }

  /**
   * returns the action that specifies what animation to play when the items is being used
   */
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.BLOCK;
  }

  /**
   * How long it takes to use or consume an item
   */
  public int getMaxItemUseDuration(ItemStack stack) {
    return 72000;
  }

  /**
   * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
   */
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
    return itemStackIn;
  }

  /**
   * Check whether this Item can harvest the given Block
   */
  public boolean canHarvestBlock(Block blockIn) {
    return blockIn == Blocks.web;
  }

  /**
   * Return the enchantability factor of the item, most of the time is based on material.
   */
  public int getItemEnchantability() {
    return this.material.getEnchantability();
  }

  /**
   * Return the name for this tool's material.
   */
  public String getToolMaterialName() {
    return this.material.toString();
  }

  /**
   * Return whether this item is repairable in an anvil.
   */
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    ItemStack mat = this.material.getRepairItemStack();
    if (mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false))
      return true;
    return super.getIsRepairable(toRepair, repair);
  }

  public Multimap<String, AttributeModifier> getItemAttributeModifiers() {
    Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();
    multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", (double) this.attackDamage, 0));
    return multimap;
  }

  public void damageItem(ItemStack stack, int amount, EntityLivingBase entityIn) {
    if (!(entityIn instanceof EntityPlayer) || !((EntityPlayer) entityIn).capabilities.isCreativeMode) {
      if (stack.isItemStackDamageable()) {
        if (stack.attemptDamageItem(amount, entityIn.getRNG())) {
          LogHelper.info("SWORD BROKE!!!!!!!!!!!!!!!");
          entityIn.worldObj.playSoundAtEntity(entityIn, "random.break", 0.8F, 0.8F + entityIn.worldObj.rand.nextFloat() * 0.4F);
          for (int i = 0; i < 5; ++i) {
            Vec3 vec3 = new Vec3(((double) entityIn.worldObj.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            vec3 = vec3.rotatePitch(-entityIn.rotationPitch * (float) Math.PI / 180.0F);
            vec3 = vec3.rotateYaw(-entityIn.rotationYaw * (float) Math.PI / 180.0F);
            double d0 = (double) (-entityIn.worldObj.rand.nextFloat()) * 0.6D - 0.3D;
            Vec3 vec31 = new Vec3(((double) entityIn.worldObj.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
            vec31 = vec31.rotatePitch(-entityIn.rotationPitch * (float) Math.PI / 180.0F);
            vec31 = vec31.rotateYaw(-entityIn.rotationYaw * (float) Math.PI / 180.0F);
            vec31 = vec31.addVector(entityIn.posX, entityIn.posY + (double) entityIn.getEyeHeight(), entityIn.posZ);
            entityIn.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord + 0.05D, vec3.zCoord, new int[] { Item.getIdFromItem(stack.getItem()) });
          }

          if (entityIn instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) entityIn;
            entityplayer.triggerAchievement(StatList.objectBreakStats[Item.getIdFromItem(stack.getItem())]);
          }

          if (stack.stackSize < 0) {
            stack.stackSize = 0;
          }

          stack.setItemDamage(stack.getMaxDamage());
        }
      }
    }
  }

}
