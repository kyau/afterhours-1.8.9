package net.kyau.afterhours.proxy;

import net.minecraft.item.Item;

public interface IProxy {

  public abstract ClientProxy getClientProxy();

  public abstract void registerEventHandlers();

  public abstract void registerKeybindings();

  public abstract void initRenderingAndTextures();

  public abstract void registerModel(Item item, String name);

  public abstract boolean isSinglePlayer();

  public abstract void openJournal();
}
