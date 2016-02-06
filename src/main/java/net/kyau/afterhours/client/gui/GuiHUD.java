package net.kyau.afterhours.client.gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.annotation.Nonnull;

import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.utils.InventoryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHUD extends Gui {

  private static Minecraft minecraft;
  private static EntityPlayer player;
  private static World world;
  private static ScaledResolution scaled;
  private static FontRenderer fontRenderer;
  private static RenderItem itemRenderer;
  private static IInventory inv;
  private static long worldSeed;
  protected float zLevel;
  // rendered character offset
  private static int charOffsetPosX = 20;
  // y position for bottom hud
  private static int hudYOffset = 14;
  private static int fixedY;
  private static int centerLeftX;
  private static int centerRightX;
  // current player position
  private static int posX;
  private static int posY;
  private static int posZ;
  private static BlockPos playerPos;
  private static Chunk chunk;

  public GuiHUD(Minecraft minecraft) {
    super();
    this.minecraft = minecraft;
  }

  public void setClientPos() {
    // update the player position
    this.posX = MathHelper.floor_double(player.posX);
    this.posY = MathHelper.floor_double(player.posY);
    this.posZ = MathHelper.floor_double(player.posZ);
    this.playerPos = new BlockPos(this.posX, this.posY, this.posZ);
    if (world != null && world.isBlockLoaded(this.playerPos)) {
      this.worldSeed = world.getSeed();
      this.chunk = world.getChunkFromBlockCoords(new BlockPos(this.posX, 0, this.posZ));
    }
  }

  @SubscribeEvent
  public void onGuiInit(@Nonnull GuiScreenEvent.InitGuiEvent.Post event) {
    this.player = minecraft.thePlayer;
    this.world = minecraft.theWorld;
    this.fontRenderer = minecraft.fontRendererObj;
    this.itemRenderer = minecraft.getRenderItem();
  }

  @SubscribeEvent(priority = EventPriority.NORMAL)
  public void onRenderGameOverlay(@Nonnull RenderGameOverlayEvent.Post event) {

    // assign
    this.inv = minecraft.thePlayer.inventory;
    this.scaled = new ScaledResolution(minecraft);
    this.fixedY = getScreenCoordinates()[1] - hudYOffset;
    this.centerLeftX = (getScreenCoordinates()[0] / 2) - 112;
    this.centerRightX = (getScreenCoordinates()[0] / 2) + 94;

    // RenderGameOverlayEvent is fired for every element type on every frame
    // this will limit rendering to only once per frame
    if (event.isCancelable() || event.type != ElementType.EXPERIENCE) {
      return;
    }

    if ((minecraft.inGameHasFocus || (minecraft.currentScreen != null && (minecraft.currentScreen instanceof GuiChat))) && !minecraft.gameSettings.showDebugInfo && !minecraft.ingameGUI.getChatGUI().getChatOpen()) {
      int countVRAD = InventoryHandler.countItems(player, ModItems.vrad);
      if (countVRAD > 0) {
        setClientPos();

        // render character
        renderCharacterOnScreen("LEFT", 14, scaled, minecraft.thePlayer);
        // player name
        renderShadowText(player.getDisplayNameString(), 48, 15, 0x3ffefe, 1);
        // ingame time + light level
        renderShadowText(getMinecraftTime() + " " + getLightLevel(), 48, 25, 0xffffff, 1);
        // biome
        String[] biomeInfo = getCurrentBiome();
        if (isSlimeChunk()) {
          biomeInfo[0] += EnumChatFormatting.GREEN + " (S)";
        }
        renderShadowText(biomeInfo[0], 48, 35, 0xbebebe, 1);

        // temperature
        String biome = biomeInfo[1] + "°";
        renderShadowText(biome, getScreenCoordinates()[0] - (fontRenderer.getStringWidth(biome)) - 7, 8, 0xffaa00, 1);

        // inventory
        int invTotals[] = getInventorySize(inv);
        renderShadowText("" + (invTotals[0] - invTotals[1]), getScreenCoordinates()[0] - 16, fixedY, invTotals[2], 1);
        // drawTexture("afterhours", "textures/gui/afterhours_icons.png", getScreenCoordinates()[0] - 34, fixedY - 5, 0,
        // 0, 15, 16);
        RenderHelper.enableGUIStandardItemLighting();
        itemRenderer.renderItemIntoGUI(new ItemStack(Blocks.chest), getScreenCoordinates()[0] - 34, fixedY - 5);

        // fps / ping
        renderShadowText(getFPS(), centerLeftX, fixedY, 0xdcdcdc, 1);
        drawTexture("minecraft", "textures/gui/icons.png", centerLeftX - 12, fixedY - 1, 0, 176 + getPingIndex() * 8, 10, 8);

        // real clock
        itemRenderer.renderItemIntoGUI(new ItemStack(Items.clock), centerRightX, fixedY - 4);
        renderShadowText(getRealTime(), centerRightX + 18, fixedY, 0xdcdcdc, 1);
        // drawTexture("afterhours", "textures/gui/afterhours_icons.png", centerRightX, fixedY - 4, 16, 0, 16, 16);
      }
    }
  }

  private int[] getScreenCoordinates() {
    int x = scaled.getScaledWidth();
    int y = scaled.getScaledHeight();
    return new int[] {
        x,
        y };
  }

  private void renderShadowText(String text, long posX, long posY, int color, int textSize) {
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
    fontRenderer.drawStringWithShadow(text, posX, posY, color);
    glPop();
  }

  private void drawTexture(String resourceDomainIn, String resourcePathIn, int posX, int posY, int textureX, int textureY, int width, int height) {
    glPush();
    minecraft.renderEngine.bindTexture(new ResourceLocation(resourceDomainIn, resourcePathIn));
    float f = 0.00390625F;
    float f1 = 0.00390625F;
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
    worldrenderer.pos((double) (posX + 0), (double) (posY + height), (double) this.zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1)).endVertex();
    worldrenderer.pos((double) (posX + width), (double) (posY + height), (double) this.zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1)).endVertex();
    worldrenderer.pos((double) (posX + width), (double) (posY + 0), (double) this.zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
    worldrenderer.pos((double) (posX + 0), (double) (posY + 0), (double) this.zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
    tessellator.draw();
    glPop();
  }

  private void glPush() {
    GlStateManager.pushMatrix();
    GlStateManager.enableAlpha();
    GlStateManager.enableBlend();
    GlStateManager.disableLighting();
  }

  private void glPop() {
    GlStateManager.scale(1.00F, 1.00F, 1.00F);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.disableAlpha();
    GlStateManager.disableBlend();
    GlStateManager.enableLighting();
    GlStateManager.popMatrix();
  }

  private static void renderCharacterOnScreen(String side, int posX, ScaledResolution scaled, EntityPlayer player) {
    if (side.contains("LEFT")) {
      GuiInventory.drawEntityOnScreen(24, 60, posX - (((posX / 2) * -1) * 2), -60, -player.rotationPitch, player);
    } else {
      GuiInventory.drawEntityOnScreen((scaled.getScaledWidth() - posX) + charOffsetPosX, scaled.getScaledHeight(), posX - (((posX / 2) * -1) * 2), 50, -player.rotationPitch, player);
    }
  }

  private static int[] getInventorySize(IInventory inv) {
    int invTotal = inv.getSizeInventory() - 4;
    int invCount = 0;
    for (int slot = 0; slot < invTotal; ++slot) {
      ItemStack itemStack = inv.getStackInSlot(slot);
      if (itemStack != null) {
        if (itemStack.stackSize > 0) {
          invCount++;
        }
      }
    }
    int color = 0xdcdcdc;
    if (player.inventory.getFirstEmptyStack() == -1) {
      color = 0xaa0000;
    }
    return new int[] {
        invTotal,
        invCount,
        color };
  }

  private static String getRealTime() {
    DateFormat df1 = new SimpleDateFormat("h:mm");
    DateFormat df2 = new SimpleDateFormat("a");
    Date dateobj = new Date();
    String suffix = "";
    if (df2.format(dateobj).contains("AM")) {
      suffix = "a";
    } else {
      suffix = "p";
    }
    return df1.format(dateobj) + suffix;
  }

  public static String getMinecraftTime() {
    String message;
    long time = minecraft.theWorld.getWorldTime();
    int day = (int) (time / 24000);
    int daynight = (int) (time % 24000);
    int hours24 = (int) (time / 1000L + 6L) % 24;
    int hours = hours24 % 12;
    int minutes = (int) ((float) time / 16.666666F % 60.0F);
    if (daynight > 12500) {
      message = "Night " + day;
    } else {
      message = "Day " + day;
    }
    message += String.format(" %d:%02d%s", new Object[] {
        Integer.valueOf(hours < 1 ? 12 : hours),
        Integer.valueOf(minutes),
        hours24 < 12 ? "a" : "p" });
    return message;
  }

  private static String getLightLevel() {
    String message = "";
    if (minecraft.theWorld != null && minecraft.theWorld.isBlockLoaded(playerPos)) {
      int light = chunk.getLightFor(EnumSkyBlock.BLOCK, playerPos);
      if (light < 8) {
        message = EnumChatFormatting.RED + "(" + light + ")";
      } else {
        message = EnumChatFormatting.YELLOW + "(" + light + ")";
      }
    }
    return message;
  }

  private static String[] getCurrentBiome() {
    return new String[] {
        chunk.getBiome(new BlockPos(posX & 15, 0, posZ & 15), world.getWorldChunkManager()).biomeName,
        String.format(Locale.ENGLISH, "%.0f", world.getBiomeGenForCoords(playerPos).getFloatTemperature(playerPos) * 100),
        String.format(Locale.ENGLISH, "%.0f", world.getBiomeGenForCoords(playerPos).rainfall * 100) };
  }

  private static boolean isSlimeChunk() {
    Random RANDOM = new Random();
    int x = posX >> 4;
    int z = posZ >> 4;
    RANDOM.setSeed(worldSeed + (x * x * 4987142) + (x * 5947611) + (z * z * 4392871) + (z * 389711) ^ 987234911);
    boolean slimeChunk = RANDOM.nextInt(10) == 0;
    if (slimeChunk || world.getBiomeGenForCoords(new BlockPos(posX, 0, posZ)).biomeID == BiomeGenBase.swampland.biomeID) {
      return true;
    }
    return false;
  }

  private static String getFPS() {
    return minecraft.debug.substring(0, minecraft.debug.indexOf(" fps"));
  }

  private static int getPingIndex() {
    NetworkPlayerInfo playerInfo = minecraft.getNetHandler().getPlayerInfo(player.getGameProfile().getId());
    int pingIndex = 5;
    if (playerInfo.getResponseTime() != 0) {
      if (playerInfo.getResponseTime() < 0) {
        pingIndex = 5;
      } else if (playerInfo.getResponseTime() < 150) {
        pingIndex = 0;
      } else if (playerInfo.getResponseTime() < 300) {
        pingIndex = 1;
      } else if (playerInfo.getResponseTime() < 600) {
        pingIndex = 2;
      } else if (playerInfo.getResponseTime() < 1000) {
        pingIndex = 3;
      }
    }
    return pingIndex;
  }
}
