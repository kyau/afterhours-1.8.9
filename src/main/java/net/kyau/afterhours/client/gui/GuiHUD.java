package net.kyau.afterhours.client.gui;

import javax.annotation.Nonnull;

import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.utils.InventoryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHUD {

  private static Minecraft minecraft;
  private static ScaledResolution scaled;
  private static FontRenderer fontRenderer;
  private static EntityPlayer player;
  // rendered character offset
  private static int charOffsetPosX = 20;

  public GuiHUD(Minecraft minecraft) {
    super();
    this.minecraft = minecraft;
  }

  @SubscribeEvent
  public void onGuiInit(@Nonnull GuiScreenEvent.InitGuiEvent.Post event) {
    this.scaled = new ScaledResolution(minecraft);
    this.fontRenderer = minecraft.fontRendererObj;
    this.player = minecraft.thePlayer;
  }

  @SubscribeEvent(priority = EventPriority.NORMAL)
  public void onRenderGameOverlay(@Nonnull RenderGameOverlayEvent.Post event) {

    // RenderGameOverlayEvent is fired for every element type on every frame
    // this will limit rendering to only once per frame
    if (event.isCancelable() || event.type != ElementType.EXPERIENCE || player.inventory == null) {
      return;
    }

    int countVRAD = InventoryHandler.countItems(player, ModItems.vrad);
    if (countVRAD > 0) {
      if ((minecraft.inGameHasFocus || (minecraft.currentScreen != null && (minecraft.currentScreen instanceof GuiChat))) && !minecraft.gameSettings.showDebugInfo && !minecraft.ingameGUI.getChatGUI().getChatOpen()) {
        // start gl updates
        GlStateManager.pushAttrib();
        // enable transparency for textures
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        // reset color so texture isnt the color of previous text
        fontRenderer.drawStringWithShadow(player.getDisplayNameString(), 48, 15, 0x3ffefe);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        renderCharacterOnScreen("LEFT", 14, scaled, minecraft.thePlayer);
        GlStateManager.resetColor();
        // push gl updates
        GlStateManager.popAttrib();
      }
    }
  }

  public static void renderCharacterOnScreen(String side, int posX, ScaledResolution scaled, EntityPlayer player) {
    if (side.contains("LEFT")) {
      GuiInventory.drawEntityOnScreen(24, 60, posX - (((posX / 2) * -1) * 2), -60, -player.rotationPitch, player);
    } else {
      GuiInventory.drawEntityOnScreen((scaled.getScaledWidth() - posX) + charOffsetPosX, scaled.getScaledHeight(), posX - (((posX / 2) * -1) * 2), 50, -player.rotationPitch, player);
    }
  }
}
