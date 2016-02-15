package net.kyau.afterhours.proxy;

import net.kyau.afterhours.client.gui.GuiHUD;
import net.kyau.afterhours.client.gui.GuiVoidJournal;
import net.kyau.afterhours.init.ModBlocks;
import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.init.ModVanilla;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

  private static Minecraft minecraft = Minecraft.getMinecraft();

  @Override
  public ClientProxy getClientProxy() {
    return this;
  }

  @Override
  public void registerEventHandlers() {
    super.registerEventHandlers();
    MinecraftForge.EVENT_BUS.register(new GuiHUD(minecraft));
    // LogManager.getLogger(ModInfo.MOD_ID).log(Level.INFO, "ClientProxy: registerEventHandlers() SUCCESS!");
  }

  @Override
  public void registerKeybindings() {

  }

  @Override
  public void initRenderingAndTextures() {
    ModItems.registerRenders();
    ModBlocks.registerRenders();
    ModVanilla.registerRenders();
  }

  @Override
  public boolean isSinglePlayer() {
    return Minecraft.getMinecraft().isSingleplayer();
  }

  @Override
  public void openJournal() {
    Minecraft.getMinecraft().displayGuiScreen(new GuiVoidJournal());
  }

}
