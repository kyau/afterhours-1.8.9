package net.kyau.afterhours.init;

import java.util.ArrayList;
import java.util.List;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.items.Antenna;
import net.kyau.afterhours.items.BaseItem;
import net.kyau.afterhours.items.DarkMatter;
import net.kyau.afterhours.items.QRD;
import net.kyau.afterhours.items.QuantumRod;
import net.kyau.afterhours.items.Singularity;
import net.kyau.afterhours.items.StableCore;
import net.kyau.afterhours.items.UnstableCore;
import net.kyau.afterhours.items.UnstableDarkMatter;
import net.kyau.afterhours.items.VoidCrystal;
import net.kyau.afterhours.items.VoidJournal;
import net.kyau.afterhours.items.VoidPearl;
import net.kyau.afterhours.items.WormholeManipulator;
import net.kyau.afterhours.items.armor.DarkMatterArmor;
import net.kyau.afterhours.items.darkmatter.DarkMatterAxe;
import net.kyau.afterhours.items.darkmatter.DarkMatterPickaxe;
import net.kyau.afterhours.items.darkmatter.DarkMatterShovel;
import net.kyau.afterhours.items.darkmatter.DarkMatterSword;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ModInfo.MOD_ID)
public class ModItems {

  public static List<BaseItem> itemList = new ArrayList<BaseItem>();
  public static List<String> repairList = new ArrayList<String>();
  public static List<ItemArmor> armorList = new ArrayList<ItemArmor>();

  public static Item antenna;
  public static Item darkmatter;
  public static Item qrd;
  public static Item quantumrod;
  public static Item singularity;
  public static Item stablecore;
  public static Item unstablecore;
  public static Item unstable_darkmatter;
  public static Item voidcrystal;
  public static Item voidjournal;
  public static Item voidpearl;
  public static Item voidwell;

  // dark matter gear
  public static Item darkmatter_sword;
  public static Item darkmatter_shovel;
  public static Item darkmatter_pickaxe;
  public static Item darkmatter_axe;

  public static Item darkmatter_helmet;
  public static Item darkmatter_chestplate;
  public static Item darkmatter_leggings;
  public static Item darkmatter_boots;

  public static void registerItems() {

    voidjournal = new VoidJournal().register(Ref.ItemID.VOIDJOURNAL);
    antenna = new Antenna().register(Ref.ItemID.ANTENNA);
    qrd = new QRD().register(Ref.ItemID.QRD);
    unstable_darkmatter = new UnstableDarkMatter().register(Ref.ItemID.UNSTABLE_DARKMATTER);
    darkmatter = new DarkMatter().register(Ref.ItemID.DARKMATTER);
    quantumrod = new QuantumRod().register(Ref.ItemID.QUANTUMROD);
    singularity = new Singularity().register(Ref.ItemID.SINGULARITY);
    stablecore = new StableCore().register(Ref.ItemID.STABLECORE);
    unstablecore = new UnstableCore().register(Ref.ItemID.UNSTABLECORE);
    voidcrystal = new VoidCrystal().register(Ref.ItemID.VOIDCRYSTAL);
    voidpearl = new VoidPearl().register(Ref.ItemID.VOIDPEARL);
    voidwell = new WormholeManipulator().register(Ref.ItemID.WORMHOLE_MANIPULATOR);

    // dark matter gear
    darkmatter_sword = new DarkMatterSword(AfterHours.darkmatter).register(Ref.ItemID.DARKMATTER_SWORD);
    darkmatter_shovel = new DarkMatterShovel(AfterHours.darkmatter).register(Ref.ItemID.DARKMATTER_SHOVEL);
    darkmatter_pickaxe = new DarkMatterPickaxe(AfterHours.darkmatter).register(Ref.ItemID.DARKMATTER_PICKAXE);
    darkmatter_axe = new DarkMatterAxe(AfterHours.darkmatter).register(Ref.ItemID.DARKMATTER_AXE);

    GameRegistry.registerItem(darkmatter_helmet = new DarkMatterArmor("darkmatter_helmet", AfterHours.darkmatterArmor, 1, 0), "darkmatter_helmet");
    GameRegistry.registerItem(darkmatter_chestplate = new DarkMatterArmor("darkmatter_chestplate", AfterHours.darkmatterArmor, 1, 1), "darkmatter_chestplate");
    GameRegistry.registerItem(darkmatter_leggings = new DarkMatterArmor("darkmatter_leggings", AfterHours.darkmatterArmor, 2, 2), "darkmatter_leggings");
    GameRegistry.registerItem(darkmatter_boots = new DarkMatterArmor("darkmatter_boots", AfterHours.darkmatterArmor, 1, 3), "darkmatter_boots");
  }

  public static void registerRenders() {

    for (BaseItem item : itemList) {
      item.registerRender(item.getUnlocalizedName().substring(11));
    }
    ModelLoader.registerItemVariants(darkmatter_helmet);
    ModelLoader.setCustomModelResourceLocation(darkmatter_helmet, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":" + "darkmatter_helmet", "inventory"));
    ModelLoader.registerItemVariants(darkmatter_chestplate);
    ModelLoader.setCustomModelResourceLocation(darkmatter_chestplate, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":" + "darkmatter_chestplate", "inventory"));
    ModelLoader.registerItemVariants(darkmatter_leggings);
    ModelLoader.setCustomModelResourceLocation(darkmatter_leggings, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":" + "darkmatter_leggings", "inventory"));
    ModelLoader.registerItemVariants(darkmatter_boots);
    ModelLoader.setCustomModelResourceLocation(darkmatter_boots, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":" + "darkmatter_boots", "inventory"));
  }

}
