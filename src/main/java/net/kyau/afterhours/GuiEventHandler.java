package net.kyau.afterhours;

import javax.annotation.Nonnull;

import net.kyau.afterhours.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiEventHandler extends Gui {

  private static Minecraft minecraft = Minecraft.getMinecraft();
  private static int pingIndex = 0;
  protected float zLevel;

  @SubscribeEvent
  public void onGuiInit(@Nonnull GuiScreenEvent.InitGuiEvent.Post event) {
    // GUI Initialization
  }

  @SubscribeEvent
  public void onRenderGameOverlay(@Nonnull RenderGameOverlayEvent event) {
    if (event.isCancelable() || event.type != ElementType.EXPERIENCE) {
      return;
    }

    FontRenderer fontRenderer = minecraft.fontRendererObj;
    EntityPlayer player = minecraft.thePlayer;
    int count = Utils.countItems(player, ModItems.vrad.getUnlocalizedName());
    if (count > 0) {
      if ((minecraft.inGameHasFocus || (minecraft.currentScreen != null && (minecraft.currentScreen instanceof GuiChat))) && !minecraft.gameSettings.showDebugInfo && !minecraft.ingameGUI.getChatGUI().getChatOpen()) {
        String msg = player.getDisplayNameString();
        int color = 0x3ffefe;
        fontRenderer.drawStringWithShadow(msg, 48, 15, color);
      }
    }
  }
}
