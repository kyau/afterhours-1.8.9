package net.kyau.afterhours.references;

public class ModInfo {

  private ModInfo() {

  }

  // Mod Information
  public static final String MOD_NAME = "AfterHours";
  public static final String MOD_ID = "afterhours";
  public static final String MOD_VERSION = "@VERSION@";
  public static final String MOD_MCVERSION = "@MCVERSION@";
  public static final String MOD_AUTHOR = "kyau";
  public static final String MOD_DESC = "Based on the original AfterHours support mod that was created for my custom modpack, AfterHours aims to be a mod that brings useful utilities to the multiplayer side of Minecraft.";
  public static final String MOD_URL = "https://github.com/kyau/afterhours-1.8.9";
  public static final String MOD_LOGO = "assets/afterhours/textures/logo.png";
  public static final String MOD_DEPENDENCIES = "";
  // Common/Client Proxy
  public static final String PROXY_CLIENT = "net.kyau." + MOD_ID + ".proxy.ClientProxy";
  public static final String PROXY_SERVER = "net.kyau." + MOD_ID + ".proxy.ServerProxy";
  // GUI Factory
  public static final String GUI_FACTORY = "net.kyau." + MOD_ID + ".client.gui.GuiFactory";
  // Resources
  public static final String TEXTURE_GUI_PATH = "textures/gui/";
  // Debug Switch
  public static final boolean DEBUG = true;
}
