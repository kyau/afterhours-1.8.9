package net.kyau.afterhours.proxy;

import net.kyau.afterhours.client.gui.GuiHUD;
import net.kyau.afterhours.client.gui.GuiVoidJournal;
import net.kyau.afterhours.references.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

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

  }

  @Override
  public void registerModel(Item item, String name) {
    ModelLoader.registerItemVariants(item);
    ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":" + name, "inventory"));
    LogManager.getLogger(ModInfo.MOD_ID).log(Level.INFO, "ClientProxy: registerModel(): " + name + " SUCCESS!");
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
