package net.kyau.afterhours.items.armor;

import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

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

}
