package net.kyau.afterhours.init;

import java.util.ArrayList;
import java.util.List;

import net.kyau.afterhours.blocks.BaseBlock;
import net.kyau.afterhours.blocks.DarkMatterCluster;
import net.kyau.afterhours.blocks.InfusedVoidstone;
import net.kyau.afterhours.blocks.QuantumStabilizer;
import net.kyau.afterhours.blocks.Voidstone;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ModBlocks {

  public static List<BaseBlock> blockList = new ArrayList<BaseBlock>();

  public static BaseBlock darkmattercluster;
  public static BaseBlock voidstone;
  public static BaseBlock infused_voidstone;
  public static BaseBlock quantum_stabilizer;

  public static void registerBlocks() {
    darkmattercluster = new DarkMatterCluster().register(Ref.BlockID.DARKMATTERCLUSTER);
    voidstone = new Voidstone().register(Ref.BlockID.VOIDSTONE);
    infused_voidstone = new InfusedVoidstone().register(Ref.BlockID.INFUSED_VOIDSTONE);
    quantum_stabilizer = new QuantumStabilizer().register(Ref.BlockID.QUANTUM_STABILIZER);
    // GameRegistry.registerBlock(darkmattercluster, Ref.BlockID.DARKMATTERCLUSTER);
    // GameRegistry.registerBlock(voidstone, Ref.BlockID.VOIDSTONE);
    // GameRegistry.registerBlock(infused_voidstone, Ref.BlockID.INFUSED_VOIDSTONE);
  }

  public static void registerRenders() {
    for (BaseBlock block : blockList) {
      block.registerRender(block);
    }

    // registerRender(darkmattercluster);
    // registerRender(voidstone);
    // registerRender(infused_voidstone);
  }

  private static void registerRender(Block block) {
    Item item = Item.getItemFromBlock(block);
    // ModelLoader.registerItemVariants(item);
    ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":" + block.getUnlocalizedName().substring(11), "inventory"));

    if (ModInfo.DEBUG)
      LogHelper.info("ClientProxy: registerRender(): " + block.getUnlocalizedName().substring(11));
  }
}
