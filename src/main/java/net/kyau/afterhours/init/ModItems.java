package net.kyau.afterhours.init;

import java.util.ArrayList;
import java.util.List;

import net.kyau.afterhours.items.Antenna;
import net.kyau.afterhours.items.BaseItem;
import net.kyau.afterhours.items.DarkMatter;
import net.kyau.afterhours.items.Singularity;
import net.kyau.afterhours.items.StableCore;
import net.kyau.afterhours.items.UnstableCore;
import net.kyau.afterhours.items.VRD;
import net.kyau.afterhours.items.VoidCrystal;
import net.kyau.afterhours.items.VoidJournal;
import net.kyau.afterhours.items.VoidPearl;
import net.kyau.afterhours.items.VoidWell;
import net.kyau.afterhours.items.darkmatter.DarkMatterAxe;
import net.kyau.afterhours.items.darkmatter.DarkMatterPickaxe;
import net.kyau.afterhours.items.darkmatter.DarkMatterShovel;
import net.kyau.afterhours.items.darkmatter.DarkMatterSword;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ModInfo.MOD_ID)
public class ModItems {

  public static List<BaseItem> itemList = new ArrayList<BaseItem>();
  public static List<String> repairList = new ArrayList<String>();

  public static BaseItem antenna;
  public static BaseItem darkmatter;
  public static BaseItem singularity;
  public static BaseItem stablecore;
  public static BaseItem unstablecore;
  public static BaseItem voidcrystal;
  public static BaseItem voidjournal;
  public static BaseItem voidpearl;
  public static BaseItem voidwell;
  public static BaseItem vrd;

  // dark matter gear
  public static BaseItem darkmatter_sword;
  public static BaseItem darkmatter_shovel;
  public static BaseItem darkmatter_pickaxe;
  public static BaseItem darkmatter_axe;

  public static void registerItems() {

    voidjournal = new VoidJournal().register(Ref.ItemID.VOIDJOURNAL);
    antenna = new Antenna().register(Ref.ItemID.ANTENNA);
    darkmatter = new DarkMatter().register(Ref.ItemID.DARKMATTER);
    singularity = new Singularity().register(Ref.ItemID.SINGULARITY);
    stablecore = new StableCore().register(Ref.ItemID.STABLECORE);
    unstablecore = new UnstableCore().register(Ref.ItemID.UNSTABLECORE);
    voidcrystal = new VoidCrystal().register(Ref.ItemID.VOIDCRYSTAL);
    voidpearl = new VoidPearl().register(Ref.ItemID.VOIDPEARL);
    voidwell = new VoidWell().register(Ref.ItemID.VOIDWELL);
    vrd = new VRD().register(Ref.ItemID.VRD);

    // dark matter gear
    darkmatter_sword = new DarkMatterSword(BaseItem.ToolMaterial.DARKMATTER).register(Ref.ItemID.DARKMATTER_SWORD);
    darkmatter_shovel = new DarkMatterShovel(BaseItem.ToolMaterial.DARKMATTER).register(Ref.ItemID.DARKMATTER_SHOVEL);
    darkmatter_pickaxe = new DarkMatterPickaxe(BaseItem.ToolMaterial.DARKMATTER).register(Ref.ItemID.DARKMATTER_PICKAXE);
    darkmatter_axe = new DarkMatterAxe(BaseItem.ToolMaterial.DARKMATTER).register(Ref.ItemID.DARKMATTER_AXE);
  }

  public static void registerRenders() {

    for (BaseItem item : itemList) {
      item.registerRender(item.getUnlocalizedName().substring(11));
    }
  }

}
