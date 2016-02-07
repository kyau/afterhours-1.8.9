package net.kyau.afterhours.proxy;

import net.minecraft.item.Item;

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

  @Override
  public void registerModel(Item item, String name) {
    // NOOP
  }

  @Override
  public boolean isSinglePlayer() {
    return false;
  }

  @Override
  public void openJournal() {
    // NOOP
  }

}
