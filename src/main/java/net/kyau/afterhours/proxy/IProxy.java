package net.kyau.afterhours.proxy;

public interface IProxy {

  public abstract ClientProxy getClientProxy();

  public abstract void registerEventHandlers();

  public abstract void registerKeybindings();

  public abstract void initRenderingAndTextures();
}
