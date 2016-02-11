package net.kyau.afterhours.client.gui;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderUtils {

  private static Minecraft minecraft = Minecraft.getMinecraft();;
  private static EntityPlayer player = minecraft.thePlayer;
  private static FontRenderer fontRenderer = minecraft.fontRendererObj;
  private static RenderItem itemRenderer = minecraft.getRenderItem();
  protected float zLevel;

  public static void renderText(String text, long posX, long posY, int color, int textSize, boolean shadow) {
    glPush();
    if (textSize < 1) {
      GlStateManager.scale(0.50F, 0.50F, 0.50F);
      posX = posX * 2;
      posY = posY * 2;
    } else if (textSize > 1) {
      GlStateManager.scale(2.00F, 2.00F, 2.00F);
      posX = posX / 2;
      posY = posY / 2;
    }
    fontRenderer.drawString(text, posX, posY, color, shadow);
    glPop();
  }

  public static void renderSplitText(String text, int posX, int posY, int wrapWidth, int color, int textSize) {
    glPush();
    if (textSize < 1) {
      GlStateManager.scale(0.50F, 0.50F, 0.50F);
      posX = posX * 2;
      posY = posY * 2;
    } else if (textSize > 1) {
      GlStateManager.scale(2.00F, 2.00F, 2.00F);
      posX = posX / 2;
      posY = posY / 2;
    }
    GlStateManager.enableLighting();
    RenderHelper.disableStandardItemLighting();
    fontRenderer.drawSplitString(text, posX, posY, wrapWidth, color);
    glPop();
  }

  public static void renderTexture(String resourceDomainIn, String resourcePathIn, int posX, int posY, int textureX, int textureY, int width, int height, float zLevel) {
    glPush();
    // float zLevel = 0;
    minecraft.renderEngine.bindTexture(new ResourceLocation(resourceDomainIn, resourcePathIn));
    float f = 0.00390625F;
    float f1 = 0.00390625F;
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
    worldrenderer.pos((double) (posX + 0), (double) (posY + height), (double) zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1)).endVertex();
    worldrenderer.pos((double) (posX + width), (double) (posY + height), (double) zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1)).endVertex();
    worldrenderer.pos((double) (posX + width), (double) (posY + 0), (double) zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
    worldrenderer.pos((double) (posX + 0), (double) (posY + 0), (double) zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
    tessellator.draw();
    glPop();
  }

  public static void renderItemDurability(ItemStack stack, int posX, int posY) {
    RenderHelper.enableStandardItemLighting();
    RenderHelper.enableGUIStandardItemLighting();
    itemRenderer.renderItemAndEffectIntoGUI(stack, posX, posY);
    itemRenderer.renderItemOverlays(fontRenderer, stack, posX, posY);
  }

  public static void renderItem(ItemStack stack, int posX, int posY) {
    RenderHelper.enableStandardItemLighting();
    RenderHelper.enableGUIStandardItemLighting();
    itemRenderer.renderItemAndEffectIntoGUI(stack, posX, posY);
  }

  public static void renderItem(Block block, int posX, int posY) {
    RenderHelper.enableStandardItemLighting();
    RenderHelper.enableGUIStandardItemLighting();
    itemRenderer.renderItemAndEffectIntoGUI(new ItemStack(block), posX, posY);
  }

  public static void glPush() {
    GlStateManager.pushMatrix();
    GlStateManager.enableAlpha();
    GlStateManager.enableBlend();
    GlStateManager.disableLighting();
    RenderHelper.enableStandardItemLighting();
    RenderHelper.enableGUIStandardItemLighting();
  }

  public static void glPop() {
    GlStateManager.scale(1.00F, 1.00F, 1.00F);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.disableAlpha();
    GlStateManager.disableBlend();
    GlStateManager.enableLighting();
    RenderHelper.disableStandardItemLighting();
    GlStateManager.popMatrix();
  }
}
