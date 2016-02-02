package net.kyau.afterhours.items;

import javax.annotation.Nonnull;

import net.kyau.afterhours.ModInfo;
import net.kyau.afterhours.references.ItemTypes;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class ModItems {

  public static Item voidstone;

  public static void init(@Nonnull FMLPreInitializationEvent event) {
    voidstone = new Voidstone(ItemTypes.VOIDSTONE);
    registerItem(event, voidstone);
    // GameRegistry.registerItem(voidstone, "voidstone");
    GameRegistry.addRecipe(new ItemStack(ModItems.voidstone), " s ", "rer", " s ", 's', Blocks.stone, 'r', Items.redstone, 'e', Items.ender_pearl);
  }

  public static void registerModels() {
    // ModelLoader.registerItemVariants(voidstone);
    // ModelLoader.setCustomModelResourceLocation(voidstone, 0, new
    // ModelResourceLocation(ModInfo.MOD_ID + ":voidstone", "inventory"));
  }

  public static void registerItem(@Nonnull FMLPreInitializationEvent event, Item item) {
    String name = item.getUnlocalizedName().substring(11);
    GameRegistry.registerItem(item, name);
    if (event.getSide() == Side.CLIENT) {
      ModelLoader.registerItemVariants(item);
      ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":" + name, "inventory"));
    }
  }

}
