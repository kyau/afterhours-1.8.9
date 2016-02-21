package net.kyau.afterhours.items;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.references.ModInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BaseItem extends Item {

  public BaseItem() {
    super();
    this.maxStackSize = 1;
    this.setCreativeTab(AfterHours.AfterHoursTab);
    this.setNoRepair();
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

  public static void damageItem(ItemStack stack, int amount, EntityLivingBase entityIn) {
    if (!(entityIn instanceof EntityPlayer) || !((EntityPlayer) entityIn).capabilities.isCreativeMode) {
      if (stack.isItemStackDamageable()) {
        if (stack.attemptDamageItem(amount, entityIn.getRNG())) {
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
