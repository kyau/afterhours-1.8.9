package net.kyau.afterhours;

import javax.annotation.Nonnull;

import net.kyau.afterhours.config.AfterHoursTab;
import net.kyau.afterhours.items.ModItems;
import net.kyau.afterhours.items.RecipeChanges;
import net.kyau.afterhours.network.PacketHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModIdMappingEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = ModInfo.MOD_ID,
     name = ModInfo.MOD_NAME,
     version = ModInfo.MOD_VERSION,
     acceptedMinecraftVersions = "[1.8.9]",
     dependencies = "required-after:Forge@[11.15.1.1722,);")
public class AfterHours {

  @SidedProxy(clientSide = ModInfo.PROXY_CLIENT,
              serverSide = ModInfo.PROXY_COMMON)
  public static ProxyCommon proxy;

  public static ProxyCommon getProxy() {
    return proxy;
  }

  public static CreativeTabs AfterHoursTab = new AfterHoursTab(CreativeTabs.getNextID(), "AfterHours");

  @Mod.EventHandler
  public void preInit(@Nonnull FMLPreInitializationEvent event) {
    proxy.preInit(event);
    ModItems.init(event);
    if (event.getSide() == Side.CLIENT) {
      ModItems.registerModels();
    }
  }

  @Mod.EventHandler
  public void init(@Nonnull FMLInitializationEvent event) {
    proxy.init(event);
    PacketHandler.init();
  }

  @Mod.EventHandler
  public void postInit(@Nonnull FMLPostInitializationEvent event) {
    RecipeChanges.init();
  }

  @Mod.EventHandler
  public void startAfterHours(@Nonnull FMLModIdMappingEvent event) {
    if (!event.isFrozen) {
      proxy.startAfterHours();
    }
  }

}
