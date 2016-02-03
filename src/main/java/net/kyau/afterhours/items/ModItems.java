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

  public static Item antenna;
  public static Item dough;
  public static Item voidstone;
  public static Item vrad;

  public static void init(@Nonnull FMLPreInitializationEvent event) {
    antenna = new Antenna(ItemTypes.ANTENNA);
    dough = new Dough(ItemTypes.DOUGH);
    voidstone = new Voidstone(ItemTypes.VOIDSTONE);
    vrad = new VRAD(ItemTypes.VRAD);
    registerItem(event, antenna);
    registerItem(event, dough);
    registerItem(event, voidstone);
    registerItem(event, vrad);
    if (event.getSide() == Side.SERVER) {
      RecipeManager.init();
    }
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
