package net.kyau.afterhours.client.gui;

import java.util.List;
import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderUtils {

  private static Minecraft minecraft = Minecraft.getMinecraft();;
  private static EntityPlayer player = minecraft.thePlayer;
  private static FontRenderer fontRenderer = minecraft.fontRendererObj;
  private static RenderItem itemRenderer = minecraft.getRenderItem();
  protected static float zLevel;

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
    GlStateManager.enableLighting();
    RenderHelper.disableStandardItemLighting();
    fontRenderer.drawString(text, posX, posY, color, shadow);
    glPop();
  }

  public static void renderSplitText(String text, int posX, int posY, int wrapWidth, int color, int textSize) {
    glPush();
    if (textSize < 1) {
      GlStateManager.scale(0.50F, 0.50F, 0.50F);
      posX = posX * 2;
      posY = posY * 2;
    } else if (textSize == 2) {
      GlStateManager.scale(2.00F, 2.00F, 2.00F);
      posX = posX / 2;
      posY = posY / 2;
    } else if (textSize == 4) {
      GlStateManager.scale(4.00F, 4.00F, 4.00F);
      posX = posX / 4;
      posY = posY / 4;
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

  public static void renderTextureHalf(String resourceDomainIn, String resourcePathIn, int posX, int posY, int textureX, int textureY, int width, int height, float zLevel) {
    glPush();
    // float zLevel = 0;
    minecraft.renderEngine.bindTexture(new ResourceLocation(resourceDomainIn, resourcePathIn));
    float f = 0.00390625F;
    float f1 = 0.00390625F;
    GlStateManager.scale(0.25D, 0.25D, 0.25D);
    posX = (int) (posX * 0.25);
    posY = (int) (posY * 0.25);
    ScaledResolution scaled = new ScaledResolution(minecraft);
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
    worldrenderer.pos((double) (posX + 0), (double) (posY + height), (double) zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1)).endVertex();
    worldrenderer.pos((double) (posX + width), (double) (posY + height), (double) zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1)).endVertex();
    worldrenderer.pos((double) (posX + width), (double) (posY + 0), (double) zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
    worldrenderer.pos((double) (posX + 0), (double) (posY + 0), (double) zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
    tessellator.draw();
    GlStateManager.scale(1D, 1D, 1D);
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
    renderItemAndEffectIntoGUI(stack, posX, posY);
  }

  public static void renderItem(Block block, int posX, int posY) {
    RenderHelper.enableStandardItemLighting();
    RenderHelper.enableGUIStandardItemLighting();
    renderItemAndEffectIntoGUI(new ItemStack(block), posX, posY);
  }

  public static void renderItemAndEffectIntoGUI(final ItemStack stack, int xPosition, int yPosition) {
    if (stack != null && stack.getItem() != null) {
      itemRenderer.zLevel = 1.0F;

      try {
        itemRenderer.renderItemIntoGUI(stack, xPosition, yPosition);
      } catch (Throwable throwable) {
        CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering item");
        CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
        crashreportcategory.addCrashSectionCallable("Item Type", new Callable<String>() {

          public String call() throws Exception {
            return String.valueOf((Object) stack.getItem());
          }
        });
        crashreportcategory.addCrashSectionCallable("Item Aux", new Callable<String>() {

          public String call() throws Exception {
            return String.valueOf(stack.getMetadata());
          }
        });
        crashreportcategory.addCrashSectionCallable("Item NBT", new Callable<String>() {

          public String call() throws Exception {
            return String.valueOf((Object) stack.getTagCompound());
          }
        });
        crashreportcategory.addCrashSectionCallable("Item Foil", new Callable<String>() {

          public String call() throws Exception {
            return String.valueOf(stack.hasEffect());
          }
        });
        throw new ReportedException(crashreport);
      }

      itemRenderer.zLevel = 0.0F;
    }
  }

  public static void renderToolTip(ItemStack stack, int x, int y) {
    List<String> list = stack.getTooltip(minecraft.thePlayer, minecraft.gameSettings.advancedItemTooltips);

    for (int i = 0; i < list.size(); ++i) {
      if (i == 0) {
        list.set(i, stack.getRarity().rarityColor + (String) list.get(i));
      } else {
        list.set(i, EnumChatFormatting.GRAY + (String) list.get(i));
      }
    }

    FontRenderer font = stack.getItem().getFontRenderer(stack);
    drawHoveringText(list, x, y, (font == null ? fontRenderer : font));
  }

  /**
   * Draws a List of strings as a tooltip. Every entry is drawn on a seperate line.
   */
  public static void drawHoveringText(List<String> textLines, int x, int y) {
    drawHoveringText(textLines, x, y, fontRenderer);
  }

  public static void drawHoveringText(List<String> textLines, int x, int y, FontRenderer font) {
    if (!textLines.isEmpty()) {
      GlStateManager.disableRescaleNormal();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      int i = 0;

      for (String s : textLines) {
        int j = font.getStringWidth(s);

        if (j > i) {
          i = j;
        }
      }

      int l1 = x + 12;
      int i2 = y - 12;
      int k = 8;

      if (textLines.size() > 1) {
        k += 2 + (textLines.size() - 1) * 10;
      }
      if (l1 + i > minecraft.currentScreen.width) {
        l1 -= 28 + i;
      }

      if (i2 + k + 6 > minecraft.currentScreen.height) {
        i2 = minecraft.currentScreen.height - k - 6;
      }
      int bgGradient = 0xff000000;
      drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, bgGradient, bgGradient);
      drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, bgGradient, bgGradient);
      drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, bgGradient, bgGradient);
      drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, bgGradient, bgGradient);
      drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, bgGradient, bgGradient);
      int borderGradient1 = 0xff191919;
      int borderGradient2 = 0xff080808;
      drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, borderGradient1, borderGradient2);
      drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, borderGradient1, borderGradient2);
      drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, borderGradient1, borderGradient1);
      drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, borderGradient2, borderGradient2);

      for (int k1 = 0; k1 < textLines.size(); ++k1) {
        String s1 = (String) textLines.get(k1);
        font.drawStringWithShadow(s1, (float) l1, (float) i2, -1);

        if (k1 == 0) {
          i2 += 2;
        }

        i2 += 10;
      }

      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      RenderHelper.enableStandardItemLighting();
      GlStateManager.enableRescaleNormal();
    }
  }

  public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
    float f = (float) (startColor >> 24 & 255) / 255.0F;
    float f1 = (float) (startColor >> 16 & 255) / 255.0F;
    float f2 = (float) (startColor >> 8 & 255) / 255.0F;
    float f3 = (float) (startColor & 255) / 255.0F;
    float f4 = (float) (endColor >> 24 & 255) / 255.0F;
    float f5 = (float) (endColor >> 16 & 255) / 255.0F;
    float f6 = (float) (endColor >> 8 & 255) / 255.0F;
    float f7 = (float) (endColor & 255) / 255.0F;
    GlStateManager.disableTexture2D();
    GlStateManager.enableBlend();
    GlStateManager.disableAlpha();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GlStateManager.shadeModel(7425);
    GL11.glTranslatef(0.0F, 0.0F, zLevel);
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
    worldrenderer.pos((double) right, (double) top, (double) zLevel).color(f1, f2, f3, f).endVertex();
    worldrenderer.pos((double) left, (double) top, (double) zLevel).color(f1, f2, f3, f).endVertex();
    worldrenderer.pos((double) left, (double) bottom, (double) zLevel).color(f5, f6, f7, f4).endVertex();
    worldrenderer.pos((double) right, (double) bottom, (double) zLevel).color(f5, f6, f7, f4).endVertex();
    tessellator.draw();
    GL11.glTranslatef(0.0F, 0.0F, 0.0F);
    GlStateManager.shadeModel(7424);
    GlStateManager.disableBlend();
    GlStateManager.enableAlpha();
    GlStateManager.enableTexture2D();
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
