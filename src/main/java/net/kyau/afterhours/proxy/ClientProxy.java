package net.kyau.afterhours.proxy;

import net.kyau.afterhours.event.GuiEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

  @Override
  public ClientProxy getClientProxy() {
    return this;
  }

  @Override
  public void registerEventHandlers() {
    super.registerEventHandlers();
    MinecraftForge.EVENT_BUS.register(new GuiEventHandler());
    // LogManager.getLogger(ModInfo.MOD_ID).log(Level.INFO, "ClientProxy: registerEventHandlers() SUCCESS!");
  }

  @Override
  public void registerKeybindings() {

  }

  @Override
  public void initRenderingAndTextures() {

  }
}
