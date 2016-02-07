package net.kyau.afterhours.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class GuiButtonChangePage extends GuiButton {

  private final boolean previous;

  public GuiButtonChangePage(int id, int x, int y, boolean previous) {
    super(id, x, y, 23, 13, "");
    this.previous = previous;
  }

  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY) {
    if (visible) {
      boolean mouseOver = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
      mc.renderEngine.bindTexture(GuiVoidJournal.texture);
      int u = 0;
      int v = 192;

      if (mouseOver) {
        u += 23;
      }

      if (previous) {
        v += 13;
      }
      GlStateManager.pushMatrix();
      GlStateManager.enableAlpha();
      GlStateManager.enableBlend();
      GlStateManager.disableLighting();
      drawTexturedModalRect(xPosition, yPosition, u, v, 23, 13);
      GlStateManager.disableAlpha();
      GlStateManager.disableBlend();
      GlStateManager.enableLighting();
      GlStateManager.popMatrix();
    }
  }
}