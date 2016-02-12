package net.kyau.afterhours.init;

import net.kyau.afterhours.blocks.BaseBlock;
import net.kyau.afterhours.blocks.InfusedVoidstone;
import net.kyau.afterhours.blocks.Voidstone;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

  public static final BaseBlock voidstone = new Voidstone();
  public static final BaseBlock infused_voidstone = new InfusedVoidstone();

  public static void registerBlocks() {
    GameRegistry.registerBlock(voidstone, Ref.BlockID.VOIDSTONE);
    GameRegistry.registerBlock(infused_voidstone, Ref.BlockID.INFUSED_VOIDSTONE);
  }

  public static void registerRenders() {
    registerRender(voidstone);
    registerRender(infused_voidstone);
  }

  private static void registerRender(Block block) {
    Item item = Item.getItemFromBlock(block);
    // ModelLoader.registerItemVariants(item);
    ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":" + block.getUnlocalizedName().substring(11), "inventory"));

    if (ModInfo.DEBUG)
      LogHelper.info("ClientProxy: registerRender(): " + block.getUnlocalizedName().substring(11));
  }
}
