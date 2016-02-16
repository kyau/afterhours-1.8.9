package net.kyau.afterhours;

import javax.annotation.Nonnull;

import net.kyau.afterhours.config.AfterHoursTab;
import net.kyau.afterhours.dimension.DimensionHandler;
import net.kyau.afterhours.dimension.WorldGenerator;
import net.kyau.afterhours.event.GuiHandler;
import net.kyau.afterhours.init.ModBlocks;
import net.kyau.afterhours.init.ModEnchants;
import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.init.ModVanilla;
import net.kyau.afterhours.init.RecipeManager;
import net.kyau.afterhours.network.PacketHandler;
import net.kyau.afterhours.proxy.IProxy;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = ModInfo.MOD_ID,
     name = ModInfo.MOD_NAME,
     version = ModInfo.MOD_VERSION,
     acceptedMinecraftVersions = "[1.8.9]",
     dependencies = "required-after:Forge@[11.15.1.1722,);")
public class AfterHours {

  @Mod.Instance(ModInfo.MOD_ID)
  public static AfterHours instance;

  @SidedProxy(clientSide = ModInfo.PROXY_CLIENT,
              serverSide = ModInfo.PROXY_SERVER)
  public static IProxy proxy;

  public static CreativeTabs AfterHoursTab = new AfterHoursTab(CreativeTabs.getNextID(), "AfterHours");

  public static int voidDimID = Ref.Dimension.DIM;

  public static ToolMaterial darkmatter;

  public static int guiIndex = 0;
  public static final int GUI_VOIDJOURNAL = guiIndex++;
  public static final int GUI_VRD = guiIndex++;

  @Mod.EventHandler
  public void preInit(@Nonnull FMLPreInitializationEvent event) {
    // hard-coded mcmod.info
    event.getModMetadata().autogenerated = false;
    event.getModMetadata().authorList.add(ModInfo.MOD_AUTHOR);
    event.getModMetadata().description = ModInfo.MOD_DESC;
    event.getModMetadata().url = ModInfo.MOD_URL;
    event.getModMetadata().logoFile = ModInfo.MOD_LOGO;
    DimensionHandler.init();
    GameRegistry.registerWorldGenerator(new WorldGenerator(), 1);
    PacketHandler.init();
    darkmatter = EnumHelper.addToolMaterial("DARKMATTER", Ref.DarkMatter.HARVEST_LEVEL, Ref.DarkMatter.DURABILITY, Ref.DarkMatter.EFFICIENCY, Ref.DarkMatter.DAMAGE, 0);
    proxy.registerEventHandlers();
    proxy.registerKeybindings();
    ModEnchants.registerEnchants();
    ModItems.registerItems();
    ModBlocks.registerBlocks();
    ModVanilla.registerItems();
    proxy.initRenderingAndTextures();
  }

  @Mod.EventHandler
  public void init(@Nonnull FMLInitializationEvent event) {
    RecipeManager.init();
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
  }

  @Mod.EventHandler
  public void postInit(@Nonnull FMLPostInitializationEvent event) {
    RecipeManager.post();
    proxy.registerClientEventHandlers();
  }
}
