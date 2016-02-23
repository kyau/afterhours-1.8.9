package net.kyau.afterhours.config;

import java.io.File;

import net.kyau.afterhours.init.ModDimensions;
import net.kyau.afterhours.init.ModEnchants;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

  private static Configuration config;
  // categories
  public static final String CATEGORY_GENERAL = StatCollector.translateToLocal("config." + ModInfo.MOD_ID + ".general");
  public static final String CATEGORY_HUD = StatCollector.translateToLocal("config." + ModInfo.MOD_ID + ".hud");
  public static final String CATEGORY_COOLDOWN = StatCollector.translateToLocal("config." + ModInfo.MOD_ID + ".cooldown");
  public static final String CATEGORY_ENERGY = StatCollector.translateToLocal("config." + ModInfo.MOD_ID + ".energy");
  public static final String CATEGORY_MACHINERY = StatCollector.translateToLocal("config." + ModInfo.MOD_ID + ".machinery");
  public static final String CATEGORY_VANILLA = StatCollector.translateToLocal("config." + ModInfo.MOD_ID + ".vanilla");
  // category: general
  public static int idDimension;
  public static int idBiome;
  public static int idEnchantAbsorption;
  public static int idEnchantEntanglement;
  public static int idEnchantGravitation;
  public static int idEnchantQuantumBoost;
  public static int idEnchantQuantumDisplay;
  public static boolean debugMode;
  // category: armor hud
  public static int hudNumberFormat;
  public static boolean hudShowArmor;
  public static boolean hudShowCharacter;
  public static boolean hudShowClock;
  public static boolean hudShowFps;
  public static boolean hudShowQuantumEnergy;
  public static boolean hudShowTemp;
  // category: cooldowns
  public static int cooldownVoidPearl;
  public static int cooldownWormholeManipulator;
  // category: quantum energy
  public static int darkmatterChestplateEnergyMax;
  public static int darkmatterChestplateEnergyOnUse;
  public static int wormholeEnergyMax;
  public static int wormholeEnergyOnUse;
  public static int wormholeEnergyPerTick;
  public static int energyRegenTheVoid;
  // category: quantum machinery
  public static int energyChargepadPerTick;
  public static int stabilizerDefaultTime;
  public static int stabilizerDarkMatter;
  public static int stabilizerQuantumRod;
  // category: vanilla tweaks
  public static boolean vanillaTooltip;
  public static boolean vanillaRecipes;

  public static Configuration getConfig() {
    return config;
  }

  public static void init(File file) {
    if (config == null)
      config = new Configuration(file, "0.13", true);
    config.addCustomCategoryComment(CATEGORY_GENERAL, "Biome, Dimension and Enchant IDs");
    config.addCustomCategoryComment(CATEGORY_HUD, "Armor HUD Tweaks");
    config.addCustomCategoryComment(CATEGORY_COOLDOWN, "Cooldown Tweaks");
    config.addCustomCategoryComment(CATEGORY_ENERGY, "Quantum Energy Tweaks");
    config.addCustomCategoryComment(CATEGORY_MACHINERY, "Quantum Machinery Tweaks");
    config.addCustomCategoryComment(CATEGORY_VANILLA, "Vanilla Minecraft Tweaks");
    loadConfig();
  }

  public static void loadConfig() {
    // Read in properties from config file
    //
    // Category: General
    idDimension = config.getInt("dimensionID", CATEGORY_GENERAL, ModDimensions.getEmptyDimensionId(), 2, 200, "The Dimension ID (DIM) for The Void.");
    Ref.Dimension.DIM = idDimension;
    idBiome = config.getInt("biomeID", CATEGORY_GENERAL, 222, 40, 256, "The Biome ID for The Void.");
    Ref.Dimension.BIOMEID = idBiome;
    idEnchantAbsorption = config.getInt("enchantAbsorptionID", CATEGORY_GENERAL, ModEnchants.getEmptyEnchantId(), 65, 255, "The Enchantment ID for Absorption.");
    Ref.Enchant.ABSORPTION_ID = idEnchantAbsorption;
    idEnchantEntanglement = config.getInt("enchantEntanglementID", CATEGORY_GENERAL, ModEnchants.getEmptyEnchantId(), 65, 255, "The Enchantment ID for Entanglement.");
    Ref.Enchant.ENTANGLEMENT_ID = idEnchantEntanglement;
    idEnchantGravitation = config.getInt("enchantGravitationID", CATEGORY_GENERAL, ModEnchants.getEmptyEnchantId(), 65, 255, "The Enchantment ID for Gravitation.");
    Ref.Enchant.GRAVITATION_ID = idEnchantGravitation;
    idEnchantQuantumBoost = config.getInt("enchantQuantumBoostID", CATEGORY_GENERAL, ModEnchants.getEmptyEnchantId(), 65, 255, "The Enchantment ID for Quantum Boost.");
    Ref.Enchant.QUANTUMBOOST_ID = idEnchantQuantumBoost;
    idEnchantQuantumDisplay = config.getInt("enchantQuantumDisplayID", CATEGORY_GENERAL, ModEnchants.getEmptyEnchantId(), 65, 255, "The Enchantment ID for Quantum Display.");
    Ref.Enchant.QUANTUMDISPLAY_ID = idEnchantQuantumDisplay;
    debugMode = config.getBoolean("debugMode", CATEGORY_GENERAL, false, "Enable developer debug mode. (console spam)");
    ModInfo.DEBUG = debugMode;
    // Category: Armor HUD
    hudShowArmor = config.getBoolean("showArmor", CATEGORY_HUD, true, "Display armor durability information.");
    Ref.ArmorHud.SHOW_ARMOR = hudShowArmor;
    hudNumberFormat = config.getInt("numberFormat", CATEGORY_HUD, 1, 0, 2, "Number format (0 = OFF, 1 = Percent, 2 = Numbers)");
    Ref.ArmorHud.NUMBER_FORMAT = hudNumberFormat;
    hudShowCharacter = config.getBoolean("showCharacter", CATEGORY_HUD, true, "Display the character portrait and day/biome info in the upper left.");
    Ref.ArmorHud.SHOW_CHARACTER = hudShowCharacter;
    hudShowClock = config.getBoolean("showClock", CATEGORY_HUD, true, "Display a real world clock on screen.");
    Ref.ArmorHud.SHOW_CLOCK = hudShowClock;
    hudShowFps = config.getBoolean("showFps", CATEGORY_HUD, true, "Display FPS and Ping metere on screen.");
    Ref.ArmorHud.SHOW_FPS = hudShowFps;
    hudShowQuantumEnergy = config.getBoolean("showQuantumEnergy", CATEGORY_HUD, true, "Display Quantum Energy reserve from Dark Matter Chestplate on screen.");
    Ref.ArmorHud.SHOW_QUANTUMENERGY = hudShowQuantumEnergy;
    hudShowTemp = config.getBoolean("showTemperature", CATEGORY_HUD, true, "Display the Biome Temperature on the screen.");
    Ref.ArmorHud.SHOW_TEMP = hudShowTemp;
    // Category: Cooldowns
    cooldownVoidPearl = config.getInt("cooldownVoidPearl", CATEGORY_COOLDOWN, 12000, 1200, 72000, "Void Pearl cooldown in ticks (divide by 20 for seconds).");
    Ref.ItemCooldown.VOIDPEARL = cooldownVoidPearl;
    cooldownWormholeManipulator = config.getInt("cooldownWormholeManipulator", CATEGORY_COOLDOWN, 160, 20, 72000, "Wormhole Manipulator cooldown in ticks (divide by 20 for seconds).");
    Ref.ItemCooldown.WORMHOLE_MANIPULATOR = cooldownWormholeManipulator;
    // Category: Quantum Energy
    darkmatterChestplateEnergyMax = config.getInt("darkmatterChestplateEnergyMax", CATEGORY_ENERGY, 5000, 1, 100000, "Maximum quantum energy stored in device.");
    Ref.ItemStat.ENERGY_DARKMATTER_CHESTPLATE = darkmatterChestplateEnergyMax;
    darkmatterChestplateEnergyOnUse = config.getInt("darkmatterChestplateEnergyOnUse", CATEGORY_ENERGY, 4, 1, 1000, "Energy per tick used while flying.");
    Ref.ItemStat.ENERGYONUSE_DARKMATTER_CHESTPLATE = darkmatterChestplateEnergyOnUse;
    wormholeEnergyMax = config.getInt("wormholeEnergyMax", CATEGORY_ENERGY, 500, 1, 10000, "Maximum quantum energy stored in device.");
    Ref.ItemStat.ENERGY_WORMHOLE_MANIPULATOR = wormholeEnergyMax;
    wormholeEnergyOnUse = config.getInt("wormholeEnergyOnUse", CATEGORY_ENERGY, 20, 1, 10000, "Quantum energy consumed on use.");
    Ref.ItemStat.ENERGYONUSE_WORMHOLE_MANIPULATOR = wormholeEnergyOnUse;
    wormholeEnergyPerTick = config.getInt("wormholeEnergyPerTick", CATEGORY_ENERGY, 5, 1, 10000, "Quantum energy regained per tick on recharge.");
    Ref.ItemStat.WORMHOLE_MANIPULATOR_PERTICK = wormholeEnergyPerTick;
    energyRegenTheVoid = config.getInt("energyRegenTheVoid", CATEGORY_ENERGY, 2, 1, 100, "Natural quantum energy regen while in The Void.");
    Ref.ItemStat.ENERGY_REGEN_VOID = energyRegenTheVoid;
    // Category: Quantum Machinery
    stabilizerDarkMatter = config.getInt("stabilizerDarkMatter", CATEGORY_MACHINERY, 6000, 200, 9999999, "Time in ticks it takes Dark Matter to stabilize.");
    Ref.BlockStat.STABILIZER_DARKMATTER_TIME = stabilizerDarkMatter;
    stabilizerQuantumRod = config.getInt("stabilizerQuantumRod", CATEGORY_MACHINERY, 12000, 200, 9999999, "Time in ticks it takes a Quantum Rod to stabilize.");
    Ref.BlockStat.STABILIZER_QUANTUMROD_TIME = stabilizerQuantumRod;
    energyChargepadPerTick = config.getInt("energyChargepadPerTick", CATEGORY_MACHINERY, 11, 1, 100, "Quantum energy gained per tick standing on the Quantum Chargepad.");
    Ref.BlockStat.CHARGEPAD_PERTICK = energyChargepadPerTick;
    // Category: Vanilla Tweaks
    vanillaRecipes = config.getBoolean("vanillaRecipes", CATEGORY_VANILLA, true, "Modify vanilla recipes to make the game more realistic.");
    Ref.Vanilla.RECIPES = vanillaRecipes;
    vanillaTooltip = config.getBoolean("vanillaTooltip", CATEGORY_VANILLA, true, "Modify vanilla tooltips to include more information.");
    Ref.Vanilla.TOOLTIP = vanillaTooltip;
    // Save the config file
    if (config.hasChanged())
      config.save();
  }
}
