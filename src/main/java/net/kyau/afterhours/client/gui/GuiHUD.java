package net.kyau.afterhours.client.gui;

import java.awt.Color;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.utils.InventoryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
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
  private static UUID uuid;
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
  // player armour offset
  private static int armourPosX = 20;
  private static int armourIconSize = 22;

  public static void setArmourIconSize(int size) {
    armourIconSize = size;
  }

  // player armour
  private static Object[][] lastArmourSet = new Object[][] {
      new String[] {
          "",
          "",
          "",
          "" },
      new Integer[] {
          0,
          0,
          0,
          0 } };

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
    this.uuid = player.getGameProfile().getId();
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
      int countVRD = InventoryHandler.countItems(player, ModItems.vrd);
      if (countVRD > 0) {
        setClientPos();

        // render character
        renderCharacterOnScreen("LEFT", 14, scaled, minecraft.thePlayer);
        // player name
        RenderUtils.renderText(player.getDisplayNameString(), 48, 15, 0x3ffefe, 1, true);
        // ingame time + light level
        RenderUtils.renderText(getMinecraftTime() + " " + getLightLevel(), 48, 25, 0xffffff, 1, true);
        // biome
        ArrayList<Object> biomeInfo = getCurrentBiome();
        String biome = (String) biomeInfo.get(0);
        if (isSlimeChunk()) {
          biome = (String) biomeInfo.get(0) + EnumChatFormatting.GREEN + " (S)";
        }
        RenderUtils.renderText(biome, 48, 35, 0xbebebe, 1, true);

        // temperature
        String temperature = String.format("%.0f\u00b0", biomeInfo.get(1));
        RenderUtils.renderText(temperature, getScreenCoordinates()[0] - (fontRenderer.getStringWidth(temperature)) - 7, 8, (int) biomeInfo.get(3), 1, true);

        // inventory
        int invTotals[] = getInventorySize(inv);
        RenderUtils.renderText("" + (invTotals[0] - invTotals[1]), getScreenCoordinates()[0] - 16, fixedY, invTotals[2], 1, true);
        // drawTexture("afterhours", "textures/gui/afterhours_icons.png", getScreenCoordinates()[0] - 34, fixedY - 5, 0,
        // 0, 15, 16);
        RenderUtils.renderItem(Blocks.chest, getScreenCoordinates()[0] - 34, fixedY - 5);

        // fps / ping
        RenderUtils.renderText(getFPS(), centerLeftX, fixedY, 0xdcdcdc, 1, true);
        RenderUtils.renderTexture("minecraft", "textures/gui/icons.png", centerLeftX - 12, fixedY - 1, 0, 176 + getPingIndex() * 8, 10, 8, 0);

        // real clock
        RenderUtils.renderItem(new ItemStack(Items.clock), centerRightX, fixedY - 4);
        RenderUtils.renderText(getRealTime(), centerRightX + 18, fixedY, 0xdcdcdc, 1, true);
        // drawTexture("afterhours", "textures/gui/afterhours_icons.png", centerRightX, fixedY - 4, 16, 0, 16, 16);

        // render current armor and held item
        renderArmour();
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

  private static String getMinecraftTime() {
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

  private static ArrayList<Object> getCurrentBiome() {
    ArrayList<Object> weather = new ArrayList<Object>();
    weather.add((String) chunk.getBiome(new BlockPos(posX & 15, 0, posZ & 15), world.getWorldChunkManager()).biomeName);
    weather.add((float) world.getBiomeGenForCoords(playerPos).getFloatTemperature(playerPos) * 100);
    weather.add((float) world.getBiomeGenForCoords(playerPos).rainfall * 100);
    weather.add((int) getTempColor((float) weather.get(1)));
    return weather;
  }

  private static int getTempColor(float temperature) {
    int color = 0x000000;
    if (temperature >= 100)
      color = 0xa90303;
    else if (temperature >= 90)
      color = 0xcc0000;
    else if (temperature >= 80)
      color = 0xff4f00;
    else if (temperature >= 70)
      color = 0xff9900;
    else if (temperature >= 60)
      color = 0xffcc00;
    else if (temperature >= 50)
      color = 0xf7f705;
    else if (temperature >= 40)
      color = 0x7fff00;
    else if (temperature >= 30)
      color = 0x05f7f7;
    else if (temperature >= 20)
      color = 0x00ccff;
    else if (temperature >= 10)
      color = 0x007eff;
    else if (temperature >= 0)
      color = 0x0000ff;
    else if (temperature >= -10)
      color = 0x9e00ff;
    else if (temperature >= -20)
      color = 0xff00ff;
    return color;
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
    if (AfterHours.proxy.isSinglePlayer()) {
      return 5;
    }
    NetworkPlayerInfo playerInfo = minecraft.getMinecraft().getNetHandler().getPlayerInfo(uuid);
    int pingIndex = 0;
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
    } else {
      pingIndex = 4;
    }
    return pingIndex;
  }

  private static void renderArmour() {
    InventoryPlayer inv = player.inventory;
    // int overrideRenderCharacterTime = 0;
    ItemStack currentItem = inv.getCurrentItem();
    ItemStack boots = inv.armorInventory[0];
    ItemStack leggings = inv.armorInventory[1];
    ItemStack chestplate = inv.armorInventory[2];
    ItemStack helmet = inv.armorInventory[3];
    // Armour - Compare to Last Set
    String curHelmetName = "";
    String curChestplateName = "";
    String curLeggingsName = "";
    String curBootsName = "";
    Integer curHelmetDur = 0;
    Integer curChestplateDur = 0;
    Integer curLeggingsDur = 0;
    Integer curBootsDur = 0;
    if (helmet != null) {
      curHelmetName = helmet.getUnlocalizedName();
      curHelmetDur = helmet.getItemDamage();
    }
    if (chestplate != null) {
      curChestplateName = chestplate.getUnlocalizedName();
      curChestplateDur = chestplate.getItemDamage();
    }
    if (leggings != null) {
      curLeggingsName = leggings.getUnlocalizedName();
      curLeggingsDur = leggings.getItemDamage();
    }
    if (boots != null) {
      curBootsName = boots.getUnlocalizedName();
      curBootsDur = boots.getItemDamage();
    }
    String lastHelmetName = (String) lastArmourSet[0][0];
    String lastChestplateName = (String) lastArmourSet[0][1];
    String lastLeggingsName = (String) lastArmourSet[0][2];
    String lastBootsName = (String) lastArmourSet[0][3];
    lastArmourSet[0][0] = curHelmetName;
    lastArmourSet[0][1] = curChestplateName;
    lastArmourSet[0][2] = curLeggingsName;
    lastArmourSet[0][3] = curBootsName;
    lastArmourSet[1][0] = curHelmetDur;
    lastArmourSet[1][1] = curChestplateDur;
    lastArmourSet[1][2] = curLeggingsDur;
    lastArmourSet[1][3] = curBootsDur;

    int armorOffset = 16;
    int width = scaled.getScaledWidth() + armourPosX;
    int height = scaled.getScaledHeight();
    boolean armourAllNull = isArmourNull(boots, leggings, chestplate, helmet);

    int[] params = new int[] {
        width,
        height,
        armorOffset,
        armourAllNull ? 1 : 0 };

    if (!armourAllNull) {
      renderArmourSet(boots, 2, params, 2);
      renderArmourSet(leggings, 3, params, 2);
      renderArmourSet(chestplate, 4, params, 2);
      renderArmourSet(helmet, 5, params, 2);
    }
    renderArmourSet(currentItem, 1, params, 1);
  }

  public static boolean isArmourNull(ItemStack... stacks) {
    for (ItemStack s : stacks) {
      if (s != null)
        return false;
    }
    return true;
  }

  private static void renderArmourSet(ItemStack stack, int type, int[] params, int turn) {
    int width = params[0];
    int height = params[1];
    int armourOffset = params[2];
    if (stack != null) {
      int iconSize = 14; // height for each slot
      int x = 0 + (armourOffset - 16) - armourPosX;
      int y = (int) (((scaled.getScaledHeight() / 2 - ((5 * iconSize) / 2)) + ((5 - type) * iconSize)) + Math.round(scaled.getScaledHeight() * 0.1));
      String damage = String.valueOf(stack.getMaxDamage() - stack.getItemDamage());
      String damageText = "";
      if (stack.getMaxDamage() > 0) {
        int currentDamage = stack.getMaxDamage() - stack.getItemDamage();
        float percent = (float) currentDamage / (float) stack.getMaxDamage();
        damageText = MessageFormat.format("{0,number,#%}", percent);
        // DEBUG
        // System.out.println("float: " + String.valueOf(percent) + ", string: " + damage);
      } else {
        // Item does not have durability, show item count instead
        if (stack.stackSize > 1) {
          damageText = String.valueOf(stack.stackSize);
        }
      }
      int damageStringWidth = Math.max(fontRenderer.getStringWidth(damage) + 2, fontRenderer.getStringWidth("9999") + 2);
      x += damageStringWidth;
      RenderUtils.renderItemDurability(stack, x, y);
      if (minecraft.gameSettings.guiScale < 3) {
        if (stack.getMaxDamage() > 0) {
          int currentDamage = stack.getMaxDamage() - stack.getItemDamage();
          float percent = (float) currentDamage / (float) stack.getMaxDamage();
          if (percent > 0.65) {
            RenderUtils.renderText(String.valueOf(damageText), x + (damageStringWidth - 6), y + (fontRenderer.FONT_HEIGHT / 2) + 1, Color.GREEN.getRGB(), 1, true);
          } else if (percent > 0.45) {
            RenderUtils.renderText(String.valueOf(damageText), x + (damageStringWidth - 6), y + (fontRenderer.FONT_HEIGHT / 2) + 1, Color.YELLOW.getRGB(), 1, true);
          } else if (percent > 0.25) {
            RenderUtils.renderText(String.valueOf(damageText), x + (damageStringWidth - 6), y + (fontRenderer.FONT_HEIGHT / 2) + 1, Color.ORANGE.getRGB(), 1, true);
          } else {
            RenderUtils.renderText(String.valueOf(damageText), x + (damageStringWidth - 6), y + (fontRenderer.FONT_HEIGHT / 2) + 1, Color.RED.getRGB(), 1, true);
          }
        }
      }
    }
  }

}
