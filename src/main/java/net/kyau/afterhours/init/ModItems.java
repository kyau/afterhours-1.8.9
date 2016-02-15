package net.kyau.afterhours.init;

import net.kyau.afterhours.items.Antenna;
import net.kyau.afterhours.items.BaseItem;
import net.kyau.afterhours.items.DarkMatter;
import net.kyau.afterhours.items.Dough;
import net.kyau.afterhours.items.RawHide;
import net.kyau.afterhours.items.Singularity;
import net.kyau.afterhours.items.StableCore;
import net.kyau.afterhours.items.UnstableCore;
import net.kyau.afterhours.items.VRD;
import net.kyau.afterhours.items.VoidCrystal;
import net.kyau.afterhours.items.VoidJournal;
import net.kyau.afterhours.items.VoidPearl;
import net.kyau.afterhours.items.VoidWell;
import net.kyau.afterhours.items.darkmatter.DarkMatterSword;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ModInfo.MOD_ID)
public class ModItems {

  public static final BaseItem antenna = new Antenna();
  public static final BaseItem darkmatter = new DarkMatter();
  public static final BaseItem dough = new Dough();
  public static final BaseItem rawhide = new RawHide();
  public static final BaseItem singularity = new Singularity();
  public static final BaseItem stablecore = new StableCore();
  public static final BaseItem unstablecore = new UnstableCore();
  public static final BaseItem voidcrystal = new VoidCrystal();
  public static final BaseItem voidjournal = new VoidJournal();
  public static final BaseItem voidpearl = new VoidPearl();
  public static final BaseItem voidwell = new VoidWell();
  public static final BaseItem vrd = new VRD();

  // dark matter gear
  public static final BaseItem darkmatter_sword = new DarkMatterSword();

  public static void registerItems() {

    GameRegistry.registerItem(voidjournal, Ref.ItemID.VOIDJOURNAL);
    GameRegistry.registerItem(antenna, Ref.ItemID.ANTENNA);
    GameRegistry.registerItem(dough, Ref.ItemID.DOUGH);
    GameRegistry.registerItem(rawhide, Ref.ItemID.RAWHIDE);
    GameRegistry.registerItem(darkmatter, Ref.ItemID.DARKMATTER);
    GameRegistry.registerItem(singularity, Ref.ItemID.SINGULARITY);
    GameRegistry.registerItem(stablecore, Ref.ItemID.STABLECORE);
    GameRegistry.registerItem(unstablecore, Ref.ItemID.UNSTABLECORE);
    GameRegistry.registerItem(voidcrystal, Ref.ItemID.VOIDCRYSTAL);
    GameRegistry.registerItem(voidpearl, Ref.ItemID.VOIDPEARL);
    GameRegistry.registerItem(voidwell, Ref.ItemID.VOIDWELL);
    GameRegistry.registerItem(vrd, Ref.ItemID.VRD);

    // dark matter gear
    GameRegistry.registerItem(darkmatter_sword, Ref.ItemID.DARKMATTER_SWORD);
  }

  public static void registerRenders() {
    registerRender(antenna, Ref.ItemID.ANTENNA);
    registerRender(darkmatter, Ref.ItemID.DARKMATTER);
    registerRender(dough, Ref.ItemID.DOUGH);
    registerRender(rawhide, Ref.ItemID.RAWHIDE);
    registerRender(singularity, Ref.ItemID.SINGULARITY);
    registerRender(stablecore, Ref.ItemID.STABLECORE);
    registerRender(unstablecore, Ref.ItemID.UNSTABLECORE);
    registerRender(voidcrystal, Ref.ItemID.VOIDCRYSTAL);
    registerRender(voidjournal, Ref.ItemID.VOIDJOURNAL);
    registerRender(voidpearl, Ref.ItemID.VOIDPEARL);
    registerRender(voidwell, Ref.ItemID.VOIDWELL);
    registerRender(vrd, Ref.ItemID.VRD);

    registerRender(darkmatter_sword, Ref.ItemID.DARKMATTER_SWORD);
  }

  private static void registerRender(Item item, String name) {
    ModelLoader.registerItemVariants(item);
    ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":" + name, "inventory"));
    if (ModInfo.DEBUG)
      LogHelper.info("ClientProxy: registerRender(): " + name);
  }
}
