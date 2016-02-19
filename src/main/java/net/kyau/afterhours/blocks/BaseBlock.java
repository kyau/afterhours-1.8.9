package net.kyau.afterhours.blocks;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.init.ModBlocks;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BaseBlock extends Block {

  public BaseBlock() {
    this(Material.rock);
  }

  public BaseBlock(Material material) {
    super(material);
    this.setCreativeTab(AfterHours.AfterHoursTab);
  }

  public BaseBlock register(String name) {
    GameRegistry.registerBlock(this, name);
    ModBlocks.blockList.add(this);
    return this;
  }

  public void registerRender(Block block) {
    Item item = Item.getItemFromBlock(block);
    // ModelLoader.registerItemVariants(item);
    ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":" + block.getUnlocalizedName().substring(11), "inventory"));
    if (ModInfo.DEBUG)
      LogHelper.info("ClientProxy: registerRender(): " + block.getUnlocalizedName().substring(11));
  }

  @Override
  public String getUnlocalizedName() {
    return String.format("%s.%s", ModInfo.MOD_ID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
  }

  protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
    return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
  }

}
