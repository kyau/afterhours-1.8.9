package net.kyau.afterhours.proxy;

import net.kyau.afterhours.event.EntityLivingEvent;
import net.kyau.afterhours.event.FMLEventHandler;
import net.kyau.afterhours.event.ForgeEventHandler;
import net.minecraftforge.common.MinecraftForge;

public abstract class CommonProxy implements IProxy {

  @Override
  public void registerEventHandlers() {
    MinecraftForge.EVENT_BUS.register(new FMLEventHandler());
    MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
    MinecraftForge.EVENT_BUS.register(new EntityLivingEvent());
    // LogManager.getLogger(ModInfo.MOD_ID).log(Level.INFO, "CommonProxy: registerEventHandlers() SUCCESS!");
  }

}
