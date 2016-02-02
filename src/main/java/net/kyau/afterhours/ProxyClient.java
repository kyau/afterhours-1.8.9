package net.kyau.afterhours;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ProxyClient extends ProxyCommon {

  private static boolean started = false;

  @Override
  public void preInit(@Nonnull FMLPreInitializationEvent event) {
    Minecraft minecraft = Minecraft.getMinecraft();
    IReloadableResourceManager reloadableResourceManager = (IReloadableResourceManager) minecraft.getResourceManager();
    reloadableResourceManager.registerReloadListener(new IResourceManagerReloadListener() {

      @Override
      public void onResourceManagerReload(IResourceManager resourceManager) {
        restartAfterHours();
      }
    });
  }

  @Override
  public void init(@Nonnull FMLInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(new GuiEventHandler());
    // MinecraftForge.EVENT_BUS.register(new GuiHelper());
  }

  @Override
  public void startAfterHours() {
    started = true;
  }

  @Override
  public void restartAfterHours() {
    if (started) {
      startAfterHours();
    }
  }
}
