package net.kyau.afterhours.init;

import java.util.ArrayList;
import java.util.List;

import net.kyau.afterhours.blocks.DarkMatterCluster;
import net.kyau.afterhours.blocks.InfusedVoidstone;
import net.kyau.afterhours.blocks.QuantumChargepad;
import net.kyau.afterhours.blocks.QuantumReciprocator;
import net.kyau.afterhours.blocks.QuantumStabilizer;
import net.kyau.afterhours.blocks.Voidstone;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.tileentity.TileEntityQuantumReciprocator;
import net.kyau.afterhours.tileentity.TileEntityQuantumStabilizer;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

  public static List<Block> blockList = new ArrayList<Block>();

  public static Block darkmattercluster;
  public static Block voidstone;
  public static Block infused_voidstone;
  public static Block quantum_chargepad;
  public static Block quantum_reciprocator;
  public static Block quantum_stabilizer;

  public static void registerBlocks() {
    darkmattercluster = new DarkMatterCluster().register(Ref.BlockID.DARKMATTERCLUSTER);
    voidstone = new Voidstone().register(Ref.BlockID.VOIDSTONE);
    infused_voidstone = new InfusedVoidstone().register(Ref.BlockID.INFUSED_VOIDSTONE);
    quantum_chargepad = new QuantumChargepad().register(Ref.BlockID.QUANTUM_CHARGEPAD);
    quantum_reciprocator = new QuantumReciprocator().register(Ref.BlockID.QUANTUM_RECIPROCATOR);
    quantum_stabilizer = new QuantumStabilizer().register(Ref.BlockID.QUANTUM_STABILIZER);

    GameRegistry.registerTileEntity(TileEntityQuantumStabilizer.class, ModInfo.MOD_ID + "." + Ref.BlockID.QUANTUM_STABILIZER);
    GameRegistry.registerTileEntity(TileEntityQuantumReciprocator.class, ModInfo.MOD_ID + "." + Ref.BlockID.QUANTUM_RECIPROCATOR);
  }

  public static void registerRenders() {
    for (Block block : blockList) {
      registerRender(block);
    }
  }

  private static void registerRender(Block block) {
    Item item = Item.getItemFromBlock(block);
    ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":" + block.getUnlocalizedName().substring(11), "inventory"));

    if (ModInfo.DEBUG)
      LogHelper.info("ClientProxy: registerRender(): " + block.getUnlocalizedName().substring(11));
  }
}
