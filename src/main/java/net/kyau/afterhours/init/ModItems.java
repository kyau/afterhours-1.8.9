package net.kyau.afterhours.init;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.items.Antenna;
import net.kyau.afterhours.items.BaseItem;
import net.kyau.afterhours.items.Dough;
import net.kyau.afterhours.items.RawHide;
import net.kyau.afterhours.items.VRD;
import net.kyau.afterhours.items.VoidCrystal;
import net.kyau.afterhours.items.VoidJournal;
import net.kyau.afterhours.items.VoidWell;
import net.kyau.afterhours.items.VoidPearl;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

@GameRegistry.ObjectHolder(ModInfo.MOD_ID)
public class ModItems {

  public static final BaseItem antenna = new Antenna();
  public static final BaseItem dough = new Dough();
  public static final BaseItem rawhide = new RawHide();
  public static final BaseItem voidcrystal = new VoidCrystal();
  public static final BaseItem voidjournal = new VoidJournal();
  public static final BaseItem voidpearl = new VoidPearl();
  public static final BaseItem voidwell = new VoidWell();
  public static final BaseItem vrd = new VRD();

  public static void init() {

    GameRegistry.registerItem(antenna, Ref.ItemID.ANTENNA);
    AfterHours.proxy.registerModel(antenna, Ref.ItemID.ANTENNA);
    GameRegistry.registerItem(dough, Ref.ItemID.DOUGH);
    AfterHours.proxy.registerModel(dough, Ref.ItemID.DOUGH);
    GameRegistry.registerItem(rawhide, Ref.ItemID.RAWHIDE);
    AfterHours.proxy.registerModel(rawhide, Ref.ItemID.RAWHIDE);
    GameRegistry.registerItem(voidcrystal, Ref.ItemID.VOIDCRYSTAL);
    AfterHours.proxy.registerModel(voidcrystal, Ref.ItemID.VOIDCRYSTAL);
    GameRegistry.registerItem(voidjournal, Ref.ItemID.VOIDJOURNAL);
    AfterHours.proxy.registerModel(voidjournal, Ref.ItemID.VOIDJOURNAL);
    GameRegistry.registerItem(voidpearl, Ref.ItemID.VOIDPEARL);
    AfterHours.proxy.registerModel(voidpearl, Ref.ItemID.VOIDPEARL);
    GameRegistry.registerItem(voidwell, Ref.ItemID.VOIDWELL);
    AfterHours.proxy.registerModel(voidwell, Ref.ItemID.VOIDWELL);
    GameRegistry.registerItem(vrd, Ref.ItemID.VRD);
    AfterHours.proxy.registerModel(vrd, Ref.ItemID.VRD);
  }

  @SideOnly(Side.CLIENT)
  private static void registerModel(Item item, String name) {
    ModelLoader.registerItemVariants(item);
    ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":" + name, "inventory"));
    if (ModInfo.DEBUG)
      LogManager.getLogger(ModInfo.MOD_ID).log(Level.INFO, "ClientProxy: registerModel(): " + name + " SUCCESS!");
  }

}
