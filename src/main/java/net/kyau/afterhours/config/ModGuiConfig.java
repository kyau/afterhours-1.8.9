package net.kyau.afterhours.config;

import java.util.ArrayList;
import java.util.List;

import net.kyau.afterhours.references.ModInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ModGuiConfig extends GuiConfig {

  private static final String title1 = EnumChatFormatting.WHITE + "After" + EnumChatFormatting.AQUA + "Hours" + EnumChatFormatting.GRAY + " Config";
  private static final String title2 = EnumChatFormatting.DARK_GRAY + GuiConfig.getAbridgedConfigPath(ConfigHandler.getConfig().toString());

  public ModGuiConfig(GuiScreen parent) {
    super(parent, getConfigElements(), ModInfo.MOD_ID, false, false, title1, title2);
  }

  private static List<IConfigElement> getConfigElements() {
    ConfigCategory categoryGeneral = ConfigHandler.getConfig().getCategory(ConfigHandler.CATEGORY_GENERAL);
    ConfigCategory categoryArmorHud = ConfigHandler.getConfig().getCategory(ConfigHandler.CATEGORY_HUD);
    ConfigCategory categoryCooldown = ConfigHandler.getConfig().getCategory(ConfigHandler.CATEGORY_COOLDOWN);
    ConfigCategory categoryQuantumEnergy = ConfigHandler.getConfig().getCategory(ConfigHandler.CATEGORY_ENERGY);
    ConfigCategory categoryQuantumMachinery = ConfigHandler.getConfig().getCategory(ConfigHandler.CATEGORY_MACHINERY);
    ConfigCategory categoryVanilla = ConfigHandler.getConfig().getCategory(ConfigHandler.CATEGORY_VANILLA);

    List<IConfigElement> configElements = new ArrayList<>();
    configElements.add(new ConfigElement(categoryGeneral));
    configElements.add(new ConfigElement(categoryArmorHud));
    configElements.add(new ConfigElement(categoryCooldown));
    configElements.add(new ConfigElement(categoryQuantumEnergy));
    configElements.add(new ConfigElement(categoryQuantumMachinery));
    configElements.add(new ConfigElement(categoryVanilla));

    return configElements;
  }
}
