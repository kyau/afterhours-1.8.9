package net.kyau.afterhours.config;

import java.io.File;

import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

  private static Configuration config;
  public static int idDimension;
  public static int idBiome;
  public static int cooldownVoidPearl;
  public static int cooldownWormholeManipulator;
  public static int wormholeEnergyMax;
  public static int wormholeEnergyOnUse;
  public static int wormholeEnergyPerTick;
  public static boolean vanillaTooltip;

  public static final String CATEGORY_GENERAL = StatCollector.translateToLocal("config." + ModInfo.MOD_ID + ".general");
  public static final String CATEGORY_VOIDPEARL = StatCollector.translateToLocal("config." + ModInfo.MOD_ID + ".void_pearl");
  public static final String CATEGORY_WORMHOLE = StatCollector.translateToLocal("config." + ModInfo.MOD_ID + ".wormhole");
  public static final String CATEGORY_VANILLA = StatCollector.translateToLocal("config." + ModInfo.MOD_ID + ".vanilla");

  public static Configuration getConfig() {
    return config;
  }

  public static void init(File file) {
    if (config == null)
      config = new Configuration(file, "0.12", true);
    config.addCustomCategoryComment(CATEGORY_GENERAL, "Dimension and Biome IDs");
    config.addCustomCategoryComment(CATEGORY_VOIDPEARL, "Cooldown Tweaks");
    config.addCustomCategoryComment(CATEGORY_WORMHOLE, "Cooldown & Energy Tweaks");
    config.addCustomCategoryComment(CATEGORY_VANILLA, "Vanilla Minecraft Tweaks");
    loadConfig();
  }

  public static void loadConfig() {
    // Read in properties from config file
    idDimension = config.getInt("dimensionID", CATEGORY_GENERAL, 5, 2, 200, "The Dimension ID (DIM) for The Void.");
    Ref.Dimension.DIM = idDimension;
    idBiome = config.getInt("biomeID", CATEGORY_GENERAL, 222, 40, 256, "The Biome ID for The Void.");
    Ref.Dimension.BIOMEID = idBiome;
    cooldownVoidPearl = config.getInt("cooldown", CATEGORY_VOIDPEARL, 12000, 1200, 72000, "Cooldown in ticks (divide by 20 for seconds)");
    Ref.ItemCooldown.VOIDPEARL = cooldownVoidPearl;
    cooldownWormholeManipulator = config.getInt("cooldown", CATEGORY_WORMHOLE, 160, 20, 72000, "Cooldown in ticks (divide by 20 for seconds)");
    Ref.ItemCooldown.WORMHOLE_MANIPULATOR = cooldownWormholeManipulator;
    wormholeEnergyMax = config.getInt("energyMax", CATEGORY_WORMHOLE, 100, 1, 1000, "Maximum quantum energy stored in device");
    Ref.ItemStat.ENERGY_WORMHOLE_MANIPULATOR = wormholeEnergyMax;
    wormholeEnergyOnUse = config.getInt("energyOnUse", CATEGORY_WORMHOLE, 10, 1, 1000, "Quantum energy consumed on use");
    Ref.ItemStat.ENERGYONUSE_WORMHOLE_MANIPULATOR = wormholeEnergyOnUse;
    wormholeEnergyPerTick = config.getInt("energyPerTick", CATEGORY_WORMHOLE, 2, 1, 1000, "Quantum energy regained per tick on recharge");
    Ref.ItemStat.WORMHOLE_MANIPULATOR_PERTICK = wormholeEnergyPerTick;
    vanillaTooltip = config.getBoolean("vanillaTooltip", CATEGORY_VANILLA, true, "Modify vanilla tooltips to include more information");
    Ref.Vanilla.TOOLTIP = vanillaTooltip;
    // Save the config file
    if (config.hasChanged())
      config.save();
  }
}
