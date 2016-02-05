package net.kyau.afterhours.init;

import javax.annotation.Nonnull;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.items.Antenna;
import net.kyau.afterhours.items.BaseItem;
import net.kyau.afterhours.items.Dough;
import net.kyau.afterhours.items.VRAD;
import net.kyau.afterhours.items.Voidstone;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Names;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

@GameRegistry.ObjectHolder(ModInfo.MOD_ID)
public class ModItems {

  public static final BaseItem antenna = new Antenna();
  public static final BaseItem dough = new Dough();
  public static final BaseItem voidstone = new Voidstone();
  public static final BaseItem vrad = new VRAD();

  public static void init(@Nonnull FMLPreInitializationEvent event) {

    GameRegistry.registerItem(antenna, Names.Items.ANTENNA);
    AfterHours.proxy.registerModel(antenna, Names.Items.ANTENNA);
    GameRegistry.registerItem(dough, Names.Items.DOUGH);
    AfterHours.proxy.registerModel(dough, Names.Items.DOUGH);
    GameRegistry.registerItem(voidstone, Names.Items.VOIDSTONE);
    AfterHours.proxy.registerModel(voidstone, Names.Items.VOIDSTONE);
    GameRegistry.registerItem(vrad, Names.Items.VRAD);
    AfterHours.proxy.registerModel(vrad, Names.Items.VRAD);
  }

  @SideOnly(Side.CLIENT)
  private static void registerModel(Item item, String name) {
    ModelLoader.registerItemVariants(item);
    ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":" + name, "inventory"));
    LogManager.getLogger(ModInfo.MOD_ID).log(Level.INFO, "ClientProxy: registerModel(): " + name + " SUCCESS!");
  }

}
