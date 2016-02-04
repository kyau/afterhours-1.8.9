package net.kyau.afterhours.proxy;

public class ServerProxy extends CommonProxy {

  @Override
  public ClientProxy getClientProxy() {
    return null;
  }

  @Override
  public void registerKeybindings() {
    // NOOP

  }

  @Override
  public void initRenderingAndTextures() {
    // NOOP
  }

}
