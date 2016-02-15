package net.kyau.afterhours.items.darkmatter;

import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.items.BaseItem;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
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
  private ItemStack repairMaterial;

  // (int harvestLevel, int maxUses, float efficiency, float damageVsEntity, int enchantability)
  // Diamond: (3, 1561, 8.0F, 3.0F, 10)
  public DarkMatterSword() {
    super();
    this.setUnlocalizedName(Ref.ItemID.DARKMATTER_SWORD);
    this.maxStackSize = 1;
    this.setMaxDamage(2341);
    this.attackDamage = 13.666F;
    this.repairMaterial = new ItemStack(ModItems.darkmatter, 0, 1);
  }

  @Override
  public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    stack.addEnchantment(Enchantment.unbreaking, 5);
  }

  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    damageItem(stack, 1000, attacker);
    return true;
  }

  public void damageItem(ItemStack stack, int amount, EntityLivingBase entityIn) {
    if (!(entityIn instanceof EntityPlayer) || !((EntityPlayer) entityIn).capabilities.isCreativeMode) {
      if (stack.isItemStackDamageable()) {
        if (stack.attemptDamageItem(amount, entityIn.getRNG())) {
          LogHelper.info("SWORD BROKE!!!!!!!!!!!!!!!");
          entityIn.playSound("random.break", 0.8F, 0.8F + entityIn.worldObj.rand.nextFloat() * 0.4F);
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
          // --stack.stackSize;

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

  // Tells the game the item has a container item
  @Override
  public boolean hasContainerItem(ItemStack stack) {
    return false;
  }

  // Sets the container item
  @Override
  public ItemStack getContainerItem(ItemStack itemStack) {
    ItemStack stack = itemStack.copy();
    LogHelper.info("REMOVED!");
    if (itemStack.getItemDamage() >= 0) {
      LogHelper.info("FOUND!" + itemStack.getItemDamage());
      int newDamage = itemStack.getItemDamage() - 750;
      if (newDamage > itemStack.getMaxDamage()) {
        itemStack.setItemDamage(itemStack.getMaxDamage());
      } else {
        itemStack.setItemDamage(newDamage);
      }
      stack.stackSize = 1;
    }
    // itemStack.attemptDamageItem(1, itemRand);

    return stack;
  }

  @Override
  public float getStrVsBlock(ItemStack stack, Block block) {
    if (block == Blocks.web) {
      return 5.0F;
    } else {
      Material material = block.getMaterial();
      return material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.gourd ? 1.0F : 1.5F;
    }
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
    if ((double) blockIn.getBlockHardness(worldIn, pos) != 0.0D) {
      stack.damageItem(2, playerIn);
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
    // gold level
    return 22;
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    ItemStack mat = getRepairItemStack();
    // if (mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) return true;
    return super.getIsRepairable(toRepair, repair);
  }

  @Override
  public Multimap<String, AttributeModifier> getItemAttributeModifiers() {
    Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();
    multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", (double) this.attackDamage, 0));
    return multimap;
  }

  public ItemStack getRepairItemStack() {
    return repairMaterial;
  }

  public String getToolMaterialName() {
    return "Dark Matter";
  }
}
