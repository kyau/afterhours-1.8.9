package net.kyau.afterhours.items;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BaseItem extends Item {

  public BaseItem() {
    super();
    this.maxStackSize = 1;
    this.setCreativeTab(AfterHours.AfterHoursTab);
    this.setNoRepair();
  }

  public BaseItem register(String name) {
    GameRegistry.registerItem(this, name);
    ModItems.itemList.add(this);
    return this;
  }

  public void registerRender(String name) {
    ModelLoader.registerItemVariants(this);
    ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":" + name, "inventory"));
    if (ModInfo.DEBUG)
      LogHelper.info("ClientProxy: registerRender(): " + name);
  }

  /*
    @Override
    public boolean getShareTag() {
      return true;
    }
  */

  public static enum ToolMaterial {
    WOOD(0, 59, 2.0F, 0.0F, 15, new ItemStack(Blocks.planks)),
    STONE(1, 131, 4.0F, 1.0F, 5, new ItemStack(Blocks.stone, 1, 0)),
    IRON(2, 250, 6.0F, 2.0F, 14, new ItemStack(Items.iron_ingot)),
    EMERALD(3, 1561, 8.0F, 3.0F, 10, new ItemStack(Items.diamond)),
    GOLD(0, 32, 12.0F, 0.0F, 22, new ItemStack(Items.gold_ingot)),
    DARKMATTER(4, 2431, 50.0F, 9.42F, 30, new ItemStack(ModItems.darkmatter));

    int harvestLevel;
    int uses;
    float efficiency;
    float damage;
    int enchantability;
    ItemStack repairItem;

    ToolMaterial(int harvestLevel, int uses, float efficiency, float damage, int enchantability, ItemStack repairItem) {
      this.harvestLevel = harvestLevel;
      this.uses = uses;
      this.efficiency = efficiency;
      this.damage = damage;
      this.enchantability = enchantability;
      this.repairItem = repairItem;
    }

    public float getEfficiency() {
      return this.efficiency;
    }

    public int getMaxUses() {
      return this.uses;
    }

    public float getDamageVsEntity() {
      return this.damage;
    }

    public int getEnchantability() {
      return this.enchantability;
    }

    public int getHarvestLevel() {
      return this.harvestLevel;
    }

    public ItemStack getRepairItemStack() {
      return this.repairItem;
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

  public void damageItem(ItemStack stack, int amount, EntityLivingBase entityIn) {
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
