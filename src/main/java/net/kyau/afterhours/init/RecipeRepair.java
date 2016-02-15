package net.kyau.afterhours.init;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeRepair implements IRecipe {

  private int repairAmount;
  private ItemStack toRepair;
  private ItemStack repairMaterial;
  private ItemStack returnItem;

  public RecipeRepair(ItemStack toRepair, ItemStack repairMaterial, int repairAmount) {
    this.toRepair = toRepair;
    this.repairMaterial = repairMaterial;
    this.repairAmount = repairAmount;
  }

  @Override
  public boolean matches(InventoryCrafting inventory, World world) {
    int count = 0;
    for (int i = 0; i < inventory.getSizeInventory(); i++) {
      ItemStack stack = inventory.getStackInSlot(i);
      if (stack != null) {
        if (stack.getUnlocalizedName().equals(this.toRepair.getUnlocalizedName())) {
          count++;
        }
        if (stack.getUnlocalizedName().equals(this.repairMaterial.getUnlocalizedName()) && stack.getItemDamage() == this.repairMaterial.getItemDamage()) {
          count++;
        }
      }
    }
    return count >= 2;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inventory) {
    ItemStack repair = null;
    int count = 0;
    int damage = 0;
    for (int i = 0; i < inventory.getSizeInventory(); i++) {
      ItemStack stack = inventory.getStackInSlot(i);
      if (stack != null) {
        if (stack.getUnlocalizedName().equals(this.toRepair.getUnlocalizedName())) {
          repair = stack;
          damage = repair.getItemDamage();
        }
        if (stack.getUnlocalizedName().equals(this.repairMaterial.getUnlocalizedName()) && stack.getItemDamage() == this.repairMaterial.getItemDamage()) {
          count++;
        }
      }
    }
    int rep = count * this.repairAmount;
    if (damage - rep < (-this.repairAmount + 1)) {
      return null;
    }
    ItemStack ret = this.toRepair.copy();
    ret.setItemDamage(damage - rep);
    // ItemStack ret = new ItemStack(PlunderRummageProxy.pulseShield, 1, damage - rep);
    this.returnItem = ret;
    return ret;
  }

  @Override
  public int getRecipeSize() {
    return 0;
  }

  @Override
  public ItemStack getRecipeOutput() {
    return this.returnItem;
  }

  @Override
  public ItemStack[] getRemainingItems(InventoryCrafting inv) {
    ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];

    for (int i = 0; i < aitemstack.length; ++i) {
      ItemStack itemstack = inv.getStackInSlot(i);
      aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
    }

    return aitemstack;
  }
}