package net.kyau.afterhours.client.gui;

import java.io.IOException;
import java.util.List;

import net.kyau.afterhours.init.ModBlocks;
import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.ItemHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiVoidJournal extends GuiScreen {

  static final ResourceLocation book = new ResourceLocation("afterhours:textures/gui/book.png");
  static final ResourceLocation book_page1 = new ResourceLocation("afterhours:textures/gui/book_page1.png");
  static final ResourceLocation book_page3 = new ResourceLocation("afterhours:textures/gui/book_page3.png");
  static final ResourceLocation book_recipe = new ResourceLocation("afterhours:textures/gui/book_recipe.png");
  static final ResourceLocation book_recipes = new ResourceLocation("afterhours:textures/gui/book_recipes.png");
  static final ResourceLocation book_furnace_recipe = new ResourceLocation("afterhours:textures/gui/book_furnace1.png");
  static final ResourceLocation book_recipe_furnace = new ResourceLocation("afterhours:textures/gui/book_furnace2.png");
  static final ResourceLocation book_stabilizer_recipe = new ResourceLocation("afterhours:textures/gui/book_stabilizer1.png");
  static final ResourceLocation book_recipe_stabilizer = new ResourceLocation("afterhours:textures/gui/book_stabilizer2.png");
  static final ResourceLocation book_stabilizers = new ResourceLocation("afterhours:textures/gui/book_stabilizers.png");
  private static final int BUTTON_NEXT = 0;
  private static final int BUTTON_PREV = 1;
  private int pageIndex = 0;
  private int pageTotal = 12;
  private GuiButtonChangePage nextPage;
  private GuiButtonChangePage prevPage;
  private static Minecraft minecraft = Minecraft.getMinecraft();
  private static EntityPlayer player = minecraft.thePlayer;
  private static FontRenderer fontRenderer = minecraft.fontRendererObj;
  // private static ScaledResolution scaled = new ScaledResolution(minecraft);
  private static int guiHeight, bookXStart;

  public GuiVoidJournal() {
  }

  @Override
  public void initGui() {
    super.initGui();
    @SuppressWarnings("unchecked")
    List<GuiButton> buttons = buttonList;
    int bookXBegin = (width - 170) / 2;
    guiHeight = (height / 2) - 96;
    buttons.add(nextPage = new GuiButtonChangePage(BUTTON_NEXT, bookXBegin + 107, guiHeight + 154, false));
    buttons.add(prevPage = new GuiButtonChangePage(BUTTON_PREV, bookXBegin + 38, guiHeight + 154, true));
    updateButtonState();
  }

  @Override
  public boolean doesGuiPauseGame() {
    return false;
  }

  @Override
  protected void keyTyped(char c, int key) {
    if (key == Keyboard.KEY_ESCAPE) {
      mc.displayGuiScreen(null);
    } else if (Character.getType(c) == Character.DECIMAL_DIGIT_NUMBER) {
      int newPage = (int) (c - 49);
      if (newPage < pageTotal) {
        pageIndex = newPage;
        updateButtonState();
      }
    }
  }

  @Override
  public void handleMouseInput() throws IOException {
    int dWheel = Mouse.getDWheel();
    if (dWheel < 0 && ((pageIndex) < pageTotal)) {
      ++pageIndex;
      updateButtonState();
    } else if (dWheel > 0 && pageIndex > 0) {
      --pageIndex;
      updateButtonState();
    }
    super.handleMouseInput();
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float renderPartials) {
    bookXStart = (width - 256) / 2;
    switch (pageIndex) {
      case 0:
        renderBook(bookXStart, book_page1, "The Minecrafter's Guide to the Void");
        RenderUtils.renderSplitText("\u00A77  The Minecrafter's Guide to the Void is a wholly remarkable book. Perhaps the most remarkable, certainly the most successful book ever to come out of the great publishing corporations of Void Minor. More popular than \u00A7fThe Creeper Cleanup Omnibus\u00A77, better selling than \u00A7fFifty-three More Things to do in the Nether\u00A77, and more controversial than Herobrine's trilogy of philosophical blockbusters \u00A7fWhere Steve Went Wrong\u00A77, \u00A7fSome More of Steve's Greatest Mistakes\u00A77 and \u00A7fWho is this Steve Person Anyway?\u00A77 It's already supplanted the \u00A7fEncyclopedia Minecraftia\u00A77 as the standard repository of all knowledge and wisdom, for two important reasons. First, it's slightly cheaper to craft, and secondly it has the words \u00A72DON'T PANIC\u00A77 printed in large friendly letters on its cover.", bookXStart + 16, guiHeight + 106, 448, 0xffffff, 0);
        RenderUtils.renderText("\u00A73- The Guide", (long) bookXStart + 176, guiHeight + 149, 0xffffff, 0, false);
        super.drawScreen(mouseX, mouseY, renderPartials);
        break;
      case 1:
        renderBook(bookXStart, book, "Table of Contents");
        RenderUtils.renderSplitText("  \u00A771\u00A70..... \u00A7fCover\n  \u00A772\u00A70..... \u00A7fTable of Contents\n\n  \u00A773\u00A70..... \u00A7fThere's No Place Like Home\n  \u00A774\u00A70..... \u00A7fQuantum Energy & Imprinting\n  \u00A775\u00A70..... \u00A7fVoidstone\n  \u00A776\u00A70..... \u00A7fThe Singularity\n  \u00A777\u00A70..... \u00A7fEnter the Void\n  \u00A778\u00A70..... \u00A7fDark Matter\n  \u00A779\u00A70..... \u00A7fQuantum Components\n  \u00A7711\u00A70.. \u00A7fQuantum Devices\n  \u00A7712\u00A70.. \u00A7fQuantum Machinery\n\n\n\u00A78To navigate the guide use the buttons at the bottom of the screen, the mouse wheel, or typing in the page number works (up to page nine).", bookXStart + 70, guiHeight + 48, 240, 0xffffff, 0);
        super.drawScreen(mouseX, mouseY, renderPartials);
        break;
      case 2:
        renderBook(bookXStart, book_recipes, "There's No Place Like Home");
        // text
        RenderUtils.renderSplitText("\u00A7n\u00A7fVoid Crystal\n\n\u00A7r\u00A77  Because jumping straight into \u00A7fThe Void\u00A77 would generally be a bad idea, you should first start by educating yourself on the basics of quantum energy manipulation. The first ingredient you will need is the \u00A7fVoid Crystal\u00A77, these are used as a storage medium for quantum energy and will eventually be used as a power source for your quantum machinery.\n\n\u00A7fVoid Pearl\u00A77\n\n  Before you can begin your progress into quantum energy you must be certain that if anything goes wrong you always have a way back home. The \u00A7eVoid Pearl\u00A77 will provide you with this, on use it will utilize quantum tunelling via latent quantum energy in the environment to transport you to your position of origin. The \u00A7fVoid Crystal\u00A77 inside this device will require a recharge time of \u00A7f" + ItemHelper.formatCooldown(Ref.ItemCooldown.VOIDPEARL / 20) + "\u00A77 before it can be used again.", bookXStart + 16, guiHeight + 40, 240, 0xffffff, 0);
        // recipe #1
        RenderUtils.renderItem(new ItemStack(Items.glowstone_dust, 1, 0), bookXStart + 152, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(Items.glowstone_dust, 1, 0), bookXStart + 170, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(Items.glowstone_dust, 1, 0), bookXStart + 188, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(Items.lava_bucket, 1, 0), bookXStart + 152, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(Items.ender_pearl, 1, 0), bookXStart + 170, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(Items.water_bucket, 1, 0), bookXStart + 188, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(Items.glowstone_dust, 1, 0), bookXStart + 152, guiHeight + 70);
        RenderUtils.renderItem(new ItemStack(Items.glowstone_dust, 1, 0), bookXStart + 170, guiHeight + 70);
        RenderUtils.renderItem(new ItemStack(Items.glowstone_dust, 1, 0), bookXStart + 188, guiHeight + 70);
        // result #1
        RenderUtils.renderItem(new ItemStack(ModItems.voidcrystal, 1, 0), bookXStart + 217, guiHeight + 52);
        // recipe #2
        RenderUtils.renderItem(new ItemStack(Blocks.stone_slab, 1, 0), bookXStart + 170, guiHeight + 91);
        RenderUtils.renderItem(new ItemStack(Items.glowstone_dust, 1, 0), bookXStart + 152, guiHeight + 110);
        RenderUtils.renderItem(new ItemStack(ModItems.voidcrystal, 1, 0), bookXStart + 170, guiHeight + 110);
        RenderUtils.renderItem(new ItemStack(Items.glowstone_dust, 1, 0), bookXStart + 188, guiHeight + 110);
        RenderUtils.renderItem(new ItemStack(Blocks.stone_slab, 1, 0), bookXStart + 170, guiHeight + 127);
        // result #2
        RenderUtils.renderItem(new ItemStack(ModItems.voidpearl, 1, 0), bookXStart + 217, guiHeight + 110);
        super.drawScreen(mouseX, mouseY, renderPartials);
        // recipe #1
        placeTooltip(new ItemStack(Items.glowstone_dust, 1, 0), bookXStart + 152, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(Items.glowstone_dust, 1, 0), bookXStart + 170, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(Items.glowstone_dust, 1, 0), bookXStart + 188, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(Items.lava_bucket, 1, 0), bookXStart + 152, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(Items.ender_pearl, 1, 0), bookXStart + 170, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(Items.water_bucket, 1, 0), bookXStart + 188, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(Items.glowstone_dust, 1, 0), bookXStart + 152, guiHeight + 70, mouseX, mouseY);
        placeTooltip(new ItemStack(Items.glowstone_dust, 1, 0), bookXStart + 170, guiHeight + 70, mouseX, mouseY);
        placeTooltip(new ItemStack(Items.glowstone_dust, 1, 0), bookXStart + 188, guiHeight + 70, mouseX, mouseY);
        // result #1
        placeTooltip(new ItemStack(ModItems.voidcrystal, 1, 0), bookXStart + 217, guiHeight + 52, mouseX, mouseY);
        // recipe #2
        placeTooltip(new ItemStack(Blocks.stone_slab, 1, 0), bookXStart + 170, guiHeight + 91, mouseX, mouseY);
        placeTooltip(new ItemStack(Items.glowstone_dust, 1, 0), bookXStart + 152, guiHeight + 110, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.voidcrystal, 1, 0), bookXStart + 170, guiHeight + 110, mouseX, mouseY);
        placeTooltip(new ItemStack(Items.glowstone_dust, 1, 0), bookXStart + 188, guiHeight + 110, mouseX, mouseY);
        placeTooltip(new ItemStack(Blocks.stone_slab, 1, 0), bookXStart + 170, guiHeight + 127, mouseX, mouseY);
        // result #2
        placeTooltip(new ItemStack(ModItems.voidpearl, 1, 0), bookXStart + 217, guiHeight + 110, mouseX, mouseY);
        break;
      case 3:
        renderBook(bookXStart, book_page3, "Quantum Energy & Imprinting");
        RenderUtils.renderSplitText("\u00A7fQuantum Energy\u00A77\n\n  Despite the \u00A7fVoid Pearl\u00A77 not storing energy, it is the first device to harness it. Quantum energy, or what once was called Dark Energy, is the energy that powers the chunk-verse. It is the force that naturally expands and grows the chunk-verse itself. Given the extremely high levels of energy and unstable nature of quantum energy it can not be harnessed as a fuel source by conventional means. Devices that do store quantum energy can be replenished close to \u00A7fThe Void\u00A77, within 10 blocks of bedrock in the Overworld, or you can visit \u00A7fThe Void\u00A77 where your devices will naturally regenerate.\n\n\u00A7fQuantum Imprinting\u00A77\n\n  At this point you should begin to notice that items involving quantum energy will gain a quantum imprint of the user on first use. While this is needed for stabilization of the device, it also restricts access to the device via quantum imprint (ie. if you don't own it you can't use it).", bookXStart + 16, guiHeight + 34, 240, 0xffffff, 0);
        super.drawScreen(mouseX, mouseY, renderPartials);
        break;
      case 4:
        renderBook(bookXStart, book_furnace_recipe, "Voidstone");
        RenderUtils.renderSplitText("  \u00A77While \u00A7fVoidstone\u00A77 and \u00A7eInfused Voidstone\u00A77 can be used for building, both being extremely damage resistant and one giving off a modest amount of light, they are infact stabilization agents for manipulating quantum energy.\n\n\u00A7fVoidstone\u00A77\n\n  As the basic building block of all things related to \u00A7fThe Void\u00A77 this block will be key to you gaining access to and progressing further. Requiring a \u00A7bDiamond\u00A77 level tool to harvest it provides a blast resistace that is five times that of obsidian.\n\n\u00A7eInfused Voidstone\u00A77\n\n  By infusing \u00A7fVoidstone\u00A77 with a \u00A7fVoid Crystal\u00A77 and glowstone you will modify it's properties, removing the unstable nature and allowing it to become a conduit for quantum energy. This will in turn cause the block to give off a glow almost equivalent to glowstone itself.", bookXStart + 16, guiHeight + 37, 240, 0xffffff, 0);
        // furnace
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 208, guiHeight + 44);
        RenderUtils.renderItem(new ItemStack(Blocks.end_stone, 1, 0), bookXStart + 161, guiHeight + 44);
        // recipe
        RenderUtils.renderItem(new ItemStack(Items.glowstone_dust), bookXStart + 152, guiHeight + 83);
        RenderUtils.renderItem(new ItemStack(Items.glowstone_dust), bookXStart + 170, guiHeight + 83);
        RenderUtils.renderItem(new ItemStack(Items.glowstone_dust), bookXStart + 188, guiHeight + 83);
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone), bookXStart + 152, guiHeight + 102);
        RenderUtils.renderItem(new ItemStack(ModItems.voidcrystal), bookXStart + 170, guiHeight + 102);
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone), bookXStart + 188, guiHeight + 102);
        RenderUtils.renderItem(new ItemStack(Items.glowstone_dust), bookXStart + 152, guiHeight + 119);
        RenderUtils.renderItem(new ItemStack(Items.glowstone_dust), bookXStart + 170, guiHeight + 119);
        RenderUtils.renderItem(new ItemStack(Items.glowstone_dust), bookXStart + 188, guiHeight + 119);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 217, guiHeight + 102);
        super.drawScreen(mouseX, mouseY, renderPartials);
        // furnace
        placeTooltip(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 208, guiHeight + 44, mouseX, mouseY);
        placeTooltip(new ItemStack(Blocks.end_stone, 1, 0), bookXStart + 161, guiHeight + 44, mouseX, mouseY);
        // recipe
        placeTooltip(new ItemStack(Items.glowstone_dust), bookXStart + 152, guiHeight + 83, mouseX, mouseY);
        placeTooltip(new ItemStack(Items.glowstone_dust), bookXStart + 170, guiHeight + 83, mouseX, mouseY);
        placeTooltip(new ItemStack(Items.glowstone_dust), bookXStart + 188, guiHeight + 83, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.voidstone), bookXStart + 152, guiHeight + 102, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.voidcrystal), bookXStart + 170, guiHeight + 102, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.voidstone), bookXStart + 188, guiHeight + 102, mouseX, mouseY);
        placeTooltip(new ItemStack(Items.glowstone_dust), bookXStart + 152, guiHeight + 119, mouseX, mouseY);
        placeTooltip(new ItemStack(Items.glowstone_dust), bookXStart + 170, guiHeight + 119, mouseX, mouseY);
        placeTooltip(new ItemStack(Items.glowstone_dust), bookXStart + 188, guiHeight + 119, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 217, guiHeight + 102, mouseX, mouseY);
        break;
      case 5:
        renderBook(bookXStart, book_recipes, "The Singularity");
        RenderUtils.renderSplitText("\u00A7eUnstable Core\u00A77\n\n  In order to begin your journey to \u00A7fThe Void\u00A77 you have to learn to harness quantum energy. In order to fuel your journey you will need to build an \u00A7eUnstable Core\u00A77 in order to harness the quantum energy to its maximum potential which will provide you with enough energy to open a \u00A7bSingularity\u00A77. Due to the unstable nature of the core it cannot be used alone nor can it store said energy. \n\n\u00A7bSingularity\u00A77\n\n   In order to prevent the quantum energy that would be fully unleashed on the user if it were to be used by itself you must contain it. By surrounding it in a cube of \u00A7eInfused Voidstone\u00A77 you will have enough resistance to utilize the \u00A7eUnstable Core\u00A77. By creating such a device you will be able to open a \u00A7bSingularity\u00A77 to \u00A7fThe Void\u00A77.", bookXStart + 16, guiHeight + 40, 240, 0xffffff, 0);
        // recipe #1
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 152, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 170, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 188, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 152, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(Items.nether_star, 1, 0), bookXStart + 170, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 188, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 152, guiHeight + 70);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 170, guiHeight + 70);
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 188, guiHeight + 70);
        // result #1
        RenderUtils.renderItem(new ItemStack(ModItems.unstablecore, 1, 0), bookXStart + 217, guiHeight + 52);
        // recipe #2
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 152, guiHeight + 91);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 170, guiHeight + 91);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 188, guiHeight + 91);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 152, guiHeight + 110);
        RenderUtils.renderItem(new ItemStack(ModItems.unstablecore, 1, 0), bookXStart + 170, guiHeight + 110);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 188, guiHeight + 110);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 152, guiHeight + 127);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 170, guiHeight + 127);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 188, guiHeight + 127);
        // result #2
        RenderUtils.renderItem(new ItemStack(ModItems.singularity, 1, 0), bookXStart + 217, guiHeight + 110);
        super.drawScreen(mouseX, mouseY, renderPartials);
        // recipe #1
        placeTooltip(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 152, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 170, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 188, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 152, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(Items.nether_star, 1, 0), bookXStart + 170, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 188, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 152, guiHeight + 70, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 170, guiHeight + 70, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 188, guiHeight + 70, mouseX, mouseY);
        // result #1
        placeTooltip(new ItemStack(ModItems.unstablecore, 1, 0), bookXStart + 217, guiHeight + 52, mouseX, mouseY);
        // recipe #2
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 152, guiHeight + 91, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 170, guiHeight + 91, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 188, guiHeight + 91, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 152, guiHeight + 110, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.unstablecore, 1, 0), bookXStart + 170, guiHeight + 110, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 188, guiHeight + 110, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 152, guiHeight + 127, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 170, guiHeight + 127, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 188, guiHeight + 127, mouseX, mouseY);
        // result #2
        placeTooltip(new ItemStack(ModItems.singularity, 1, 0), bookXStart + 217, guiHeight + 110, mouseX, mouseY);
        break;
      case 6:
        renderBook(bookXStart, book, "Enter the Void");
        RenderUtils.renderSplitText("\u00A7fPreparations\u00A77\n\n\u00A77  Now that you have yourself a \u00A7bSingularity\u00A77 device you can access \u00A7fThe Void\u00A77 whenever you please by activating it. Before you leave on your journey there are yet a few things to take into account.\n\n  The first being your way home. While \u00A7fThe Void\u00A77 is more than happy to add your molecular energy to its collection by regenerating (dying), it would be much smarter to have a plan. If you do not yet possess a \u00A7fVoid Pearl\u00A77 now would be a good time to craft one, as without it you will have no way to escape \u00A7fThe Void\u00A77.\n\n  Upon entering \u00A7fThe Void\u00A77 you will notice very quickly that without carrying a metric ton of blocks with you, traversing \u00A7fThe Void\u00A77 will be vastly more difficult. You may imploy any means at your disposal to rectify this, but know this will get easier over time.\n\n  With \u00A7fThe Void\u00A77 now within your reach, you now have access to a new tier of material: \u00A7bDark Matter\u00A77. You will find this new material scattered through out \u00A7fThe Void\u00A77 in small clusters of \u00A7fVoidstone\u00A77.", bookXStart + 16, guiHeight + 46, 448, 0xffffff, 0);
        super.drawScreen(mouseX, mouseY, renderPartials);
        break;
      case 7:
        renderBook(bookXStart, book_recipe_stabilizer, "Dark Matter");
        RenderUtils.renderSplitText("\u00A7fQuantum Stabilizer\u00A77\n\n  In it's initial form \u00A7fUnstable Dark Matter\u00A77, as the name implies, cannot be used due to its unstable nature and must be processed through a \u00A7fQuantum Stabilizer\u00A77 in order to utilize it as a material. Power is provided via \u00A7fVoid Crystals\u00A77 in order to process the \u00A7fUnstable Dark Matter\u00A77 into \u00A7bDark Matter\u00A77. This machine is also utilized in the creation of \u00A7fQuantum Rods\u00A77.\n\n\u00A78Note: stabilization is a very slow process.\n\n\u00A7bDark Matter\u00A77\n\n  Setup to be a tier above \u00A7fDiamond\u00A77 this material can be used to make weapons, tools, armor, and even a few devices. Due to the quantum nature of \u00A7bDark Matter\u00A77 all items created with it will possess a quantum entanglement to their owner, meaning it is impossible to separate them (you don't loose them on death).", bookXStart + 16, guiHeight + 36, 240, 0xffffff, 0);
        // recipe #1
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 152, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 170, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 188, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 152, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(ModItems.unstablecore, 1, 0), bookXStart + 170, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 188, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 152, guiHeight + 70);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 170, guiHeight + 70);
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 188, guiHeight + 70);
        // result #1
        RenderUtils.renderItem(new ItemStack(ModBlocks.quantum_stabilizer, 1, 0), bookXStart + 217, guiHeight + 52);
        // stabilizer input
        RenderUtils.renderItem(new ItemStack(Items.gunpowder, 1, 0), bookXStart + 151, guiHeight + 119);
        RenderUtils.renderItem(new ItemStack(ModItems.unstable_darkmatter, 1, 0), bookXStart + 170, guiHeight + 119);
        // stabilizer input
        RenderUtils.renderItem(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 219, guiHeight + 119);
        super.drawScreen(mouseX, mouseY, renderPartials);
        // recipe #1
        placeTooltip(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 152, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 170, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 188, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 152, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.unstablecore, 1, 0), bookXStart + 170, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 188, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 152, guiHeight + 70, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 170, guiHeight + 70, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 188, guiHeight + 70, mouseX, mouseY);
        // result #1
        placeTooltip(new ItemStack(ModBlocks.quantum_stabilizer, 1, 0), bookXStart + 217, guiHeight + 52, mouseX, mouseY);
        // stabilizer input
        placeTooltip(new ItemStack(Items.gunpowder, 1, 0), bookXStart + 151, guiHeight + 119, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.unstable_darkmatter, 1, 0), bookXStart + 170, guiHeight + 119, mouseX, mouseY);
        // stabilizer input
        placeTooltip(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 219, guiHeight + 119, mouseX, mouseY);
        break;
      case 8:
        renderBook(bookXStart, book_stabilizer_recipe, "Quantum Components");
        RenderUtils.renderSplitText("\u00A7fQuantum Rod\u00A77\n\n  The first major component you will need to craft anything with \u00A7bDark Matter\u00A77 is the \u00A7fQuantum Rod\u00A77. This component is used to stabilize the \u00A7bDark Matter\u00A77 in a way which allows it to be utilized outside of \u00A7fThe Void\u00A77 and to its full potential. It also makes a great tool rod.\n\n\u00A7fQuantum Antenna\u00A77\n\n  All devices that require a connection to \u00A7fThe Void\u00A77 will require a \u00A7fQuantum Antenna\u00A77 in order to function properly. This component is used to allow devices to harness the Subspace Void Communications Network (or SVCN for short). The SVCN can be used to transmit statistical information about reality itself, if you are equipped with a piece of gear that is enchanted with \u00A75Quantum Display I\u00A77 you will be able to display this information on your in-helmet HUD. The SVCN can also function as a private storage if utilized with the proper device.", bookXStart + 16, guiHeight + 36, 240, 0xffffff, 0);
        // stabilizer input
        RenderUtils.renderItem(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 151, guiHeight + 40);
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 170, guiHeight + 40);
        // stabilizer input
        RenderUtils.renderItem(new ItemStack(ModItems.quantumrod, 1, 0), bookXStart + 219, guiHeight + 40);
        // recipe 2
        RenderUtils.renderItem(new ItemStack(Items.ender_pearl, 1, 0), bookXStart + 170, guiHeight + 87);
        RenderUtils.renderItem(new ItemStack(ModItems.quantumrod, 1, 0), bookXStart + 170, guiHeight + 106);
        // result 2
        RenderUtils.renderItem(new ItemStack(ModItems.antenna, 1, 0), bookXStart + 217, guiHeight + 106);
        super.drawScreen(mouseX, mouseY, renderPartials);
        // stabilizer input
        placeTooltip(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 151, guiHeight + 40, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 170, guiHeight + 40, mouseX, mouseY);
        // stabilizer input
        placeTooltip(new ItemStack(ModItems.quantumrod, 1, 0), bookXStart + 219, guiHeight + 40, mouseX, mouseY);
        // recipe 2
        placeTooltip(new ItemStack(Items.ender_pearl, 1, 0), bookXStart + 170, guiHeight + 91, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.quantumrod, 1, 0), bookXStart + 170, guiHeight + 110, mouseX, mouseY);
        // result 2
        placeTooltip(new ItemStack(ModItems.antenna, 1, 0), bookXStart + 217, guiHeight + 106, mouseX, mouseY);
        break;
      case 9:
        renderBook(bookXStart, book_recipe, "Quantum Components (cont.)");
        RenderUtils.renderSplitText("\u00A7bStable Core\u00A77\n\n  The last component you will need in the higher tier crafting recipes is the \u00A7bStable Core\u00A77 which is an \u00A7eUnstable Core\u00A77 that has been tamed by \u00A7bDark Matter\u00A77. This device will allow devices and gear to permanently store quantum energy inside the core. Stored inside the core it will be contained enough to not harm the player. For more information on quantum energy and how to replenish it, please refer to page 3 of this guide.", bookXStart + 16, guiHeight + 36, 240, 0xffffff, 0);
        // recipe #1
        RenderUtils.renderItem(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 152, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 170, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 188, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 152, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(ModItems.unstablecore, 1, 0), bookXStart + 170, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 188, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 152, guiHeight + 70);
        RenderUtils.renderItem(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 170, guiHeight + 70);
        RenderUtils.renderItem(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 188, guiHeight + 70);
        // result #1
        RenderUtils.renderItem(new ItemStack(ModItems.stablecore, 1, 0), bookXStart + 217, guiHeight + 52);
        super.drawScreen(mouseX, mouseY, renderPartials);
        // recipe #1
        placeTooltip(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 152, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 170, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 188, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 152, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.unstablecore, 1, 0), bookXStart + 170, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 188, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 152, guiHeight + 70, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 170, guiHeight + 70, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 188, guiHeight + 70, mouseX, mouseY);
        // result #1
        placeTooltip(new ItemStack(ModItems.stablecore, 1, 0), bookXStart + 217, guiHeight + 52, mouseX, mouseY);
        break;
      case 10:
        renderBook(bookXStart, book_recipes, "Quantum Devices");
        RenderUtils.renderSplitText("\u00A7bQuantum Resonance Device\u00A77\n\n  \u00A77The \u00A7bQRD\u00A77 utilizes quantum resonance in order to harness the internal quantum energy inside the \u00A7bStable Core\u00A77 to shift matter into qubits in order to be digitially stored in your personal SVCN (Subspace Void Communications Network) storage container. Items stored here will be accessible from anywhere provided you have your \u00A7bQRD\u00A77 with you. This device can access your Ender inventory by shift-right clicking. Inside you will also find slots for your other quantum devices and a Void slot.\n\n\u00A7bWormhole Manipulator\u00A77\n\n This device, similar to the \u00A7bQRD\u00A77 utilizes an internal core to manipulate subspace itself and open a wormhole suitable for instant matter transportation. In short you will be instantly transported to what you are looking at. Due to the massive amount of quantum energy required to accomplish this you are required to charge this device before use.", bookXStart + 16, guiHeight + 36, 240, 0xffffff, 0);
        // recipe #1
        RenderUtils.renderItem(new ItemStack(ModItems.quantumrod, 1, 0), bookXStart + 152, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(ModItems.antenna, 1, 0), bookXStart + 170, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(ModItems.quantumrod, 1, 0), bookXStart + 188, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 152, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(ModItems.stablecore, 1, 0), bookXStart + 170, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 188, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(ModItems.quantumrod, 1, 0), bookXStart + 152, guiHeight + 70);
        RenderUtils.renderItem(new ItemStack(Blocks.ender_chest, 1, 0), bookXStart + 170, guiHeight + 70);
        RenderUtils.renderItem(new ItemStack(ModItems.quantumrod, 1, 0), bookXStart + 188, guiHeight + 70);
        // result #1
        RenderUtils.renderItem(new ItemStack(ModItems.qrd, 1, 0), bookXStart + 217, guiHeight + 52);
        // recipe #2
        RenderUtils.renderItem(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 152, guiHeight + 91);
        RenderUtils.renderItem(new ItemStack(ModItems.antenna, 1, 0), bookXStart + 170, guiHeight + 91);
        RenderUtils.renderItem(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 188, guiHeight + 91);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 152, guiHeight + 110);
        RenderUtils.renderItem(new ItemStack(ModItems.stablecore, 1, 0), bookXStart + 170, guiHeight + 110);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 188, guiHeight + 110);
        RenderUtils.renderItem(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 152, guiHeight + 127);
        RenderUtils.renderItem(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 170, guiHeight + 127);
        RenderUtils.renderItem(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 188, guiHeight + 127);
        // result #2
        RenderUtils.renderItem(new ItemStack(ModItems.wormhole_manipulator, 1, 0), bookXStart + 217, guiHeight + 110);
        super.drawScreen(mouseX, mouseY, renderPartials);
        // recipe #1
        placeTooltip(new ItemStack(ModItems.quantumrod, 1, 0), bookXStart + 152, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.antenna, 1, 0), bookXStart + 170, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.quantumrod, 1, 0), bookXStart + 188, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 152, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.stablecore, 1, 0), bookXStart + 170, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 188, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.quantumrod, 1, 0), bookXStart + 152, guiHeight + 70, mouseX, mouseY);
        placeTooltip(new ItemStack(Blocks.ender_chest, 1, 0), bookXStart + 170, guiHeight + 70, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.quantumrod, 1, 0), bookXStart + 188, guiHeight + 70, mouseX, mouseY);
        // result #1
        placeTooltip(new ItemStack(ModItems.qrd, 1, 0), bookXStart + 217, guiHeight + 52, mouseX, mouseY);
        // recipe #2
        placeTooltip(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 152, guiHeight + 91, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.antenna, 1, 0), bookXStart + 170, guiHeight + 91, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 188, guiHeight + 91, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 152, guiHeight + 110, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.stablecore, 1, 0), bookXStart + 170, guiHeight + 110, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 188, guiHeight + 110, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 152, guiHeight + 127, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.infused_voidstone, 1, 0), bookXStart + 170, guiHeight + 127, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.darkmatter, 1, 0), bookXStart + 188, guiHeight + 127, mouseX, mouseY);
        // result #2
        placeTooltip(new ItemStack(ModItems.wormhole_manipulator, 1, 0), bookXStart + 217, guiHeight + 110, mouseX, mouseY);
        break;
      case 11:
        renderBook(bookXStart, book_recipe, "Quantum Devices");
        RenderUtils.renderSplitText("\u00A7bQuantum Chargepad\u00A77\n\n  \u00A77This device utilizes quantum tunneling in order for the internal \u00A7bStable Cores\u00A77 to pull energy from \u00A7fThe Void\u00A77 through the SVCN (Subspace Void Communications Network) via the \u00A7bSingularity\u00A77 in order to recharge quantum energy levels on gear. It also uses the internal cores in order to store energy for quick recharge. To utilize this device simple equip or hold any item you wish to charge and step onto the pad.", bookXStart + 16, guiHeight + 36, 240, 0xffffff, 0);
        // recipe #1
        RenderUtils.renderItem(new ItemStack(ModItems.stablecore, 1, 0), bookXStart + 152, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 170, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(ModItems.stablecore, 1, 0), bookXStart + 188, guiHeight + 34);
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 152, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(ModItems.singularity, 1, 0), bookXStart + 170, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 188, guiHeight + 52);
        RenderUtils.renderItem(new ItemStack(ModItems.stablecore, 1, 0), bookXStart + 152, guiHeight + 70);
        RenderUtils.renderItem(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 170, guiHeight + 70);
        RenderUtils.renderItem(new ItemStack(ModItems.stablecore, 1, 0), bookXStart + 188, guiHeight + 70);
        // result #1
        RenderUtils.renderItem(new ItemStack(ModBlocks.quantum_chargepad, 1, 0), bookXStart + 217, guiHeight + 52);
        super.drawScreen(mouseX, mouseY, renderPartials);
        // recipe #1
        placeTooltip(new ItemStack(ModItems.stablecore, 1, 0), bookXStart + 152, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 170, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.stablecore, 1, 0), bookXStart + 188, guiHeight + 34, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 152, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.singularity, 1, 0), bookXStart + 170, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 188, guiHeight + 52, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.stablecore, 1, 0), bookXStart + 152, guiHeight + 70, mouseX, mouseY);
        placeTooltip(new ItemStack(ModBlocks.voidstone, 1, 0), bookXStart + 170, guiHeight + 70, mouseX, mouseY);
        placeTooltip(new ItemStack(ModItems.stablecore, 1, 0), bookXStart + 188, guiHeight + 70, mouseX, mouseY);
        // result #1
        placeTooltip(new ItemStack(ModBlocks.quantum_chargepad, 1, 0), bookXStart + 217, guiHeight + 52, mouseX, mouseY);
        break;
      case 12:
        renderBook(bookXStart, book, "");
        RenderUtils.renderSplitText("\u00A72DON'T", bookXStart + 82, guiHeight + 56, 448, 0xffffff, 4);
        RenderUtils.renderSplitText("\u00A72PANIC", bookXStart + 78, guiHeight + 96, 448, 0xffffff, 4);
        super.drawScreen(mouseX, mouseY, renderPartials);
        break;
      default:
        renderBook(bookXStart, book, "\u00A7cError!");
        RenderUtils.renderSplitText("Could not find page data!", bookXStart + 64, guiHeight + 78, 448, 0x800000, 1);
        super.drawScreen(mouseX, mouseY, renderPartials);
    }
  }

  @Override
  protected void actionPerformed(GuiButton button) {
    switch (button.id) {
      case BUTTON_NEXT:
        ++pageIndex;
        break;
      case BUTTON_PREV:
        --pageIndex;
        break;
    }
    updateButtonState();
  }

  private void updateButtonState() {
    nextPage.visible = pageIndex < pageTotal;
    prevPage.visible = pageIndex > 0 && pageIndex != pageTotal;
  }

  private void renderBook(int bookXStart, ResourceLocation book, String title) {
    minecraft.renderEngine.bindTexture(book);
    drawTexturedModalRect(bookXStart, guiHeight, 0, 0, 256, 192);
    GlStateManager.resetColor();
    if (title != "") {
      RenderUtils.renderText("\u00A7n\u00A75" + title + "\u00A7r", (width / 2 - fontRenderer.getStringWidth(title) / 2), guiHeight + 15, 0xffffff, 1, true);
      RenderUtils.renderText("Page: " + (pageIndex + 1), (long) bookXStart + 16, guiHeight + 158, 0x191919, 0, false);
    }
  }

  private void placeTooltip(ItemStack stack, int itemX, int itemY, int mouseX, int mouseY) {
    if ((mouseX >= itemX && mouseX <= (itemX + 16)) && (mouseY >= itemY && mouseY <= (itemY + 16))) {
      RenderUtils.renderToolTip(stack, mouseX, mouseY);
    }
  }

}
