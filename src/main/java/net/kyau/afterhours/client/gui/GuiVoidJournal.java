package net.kyau.afterhours.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;

public class GuiVoidJournal extends GuiScreen {

  static final ResourceLocation texture = new ResourceLocation("minecraft:textures/gui/book.png");
  private static final int BUTTON_NEXT = 0;
  private static final int BUTTON_PREV = 1;
  private int pageIndex = 0;
  private GuiButtonChangePage nextPage;
  private GuiButtonChangePage prevPage;
  private final List<String> pages = new ArrayList<String>();
  private static Minecraft minecraft = Minecraft.getMinecraft();
  private static EntityPlayer player = minecraft.thePlayer;

  public GuiVoidJournal() {
  }

  @Override
  public void initGui() {
    super.initGui();
    pages.add("\u00A71[ Day 3 ]\n\n\u00A70  This will sound completely crazy, but I think I am finally on to something, more to come after tonight's experiments...\n\n\u00A71[ Night 3 ]\n\n\u00A70\u00A7lIt worked!\u00A7r\n\n  I have successfully harnessed the \u00A75Void\u00A70 and in doing so I have unlocked something that has not been seen on Minecraftia for ages! And who would have thought, to combine something from the Nether with something from the End. Haha! It's so simple!\n\n\u00A71[ Day 6 ]\n\n\u00A70  After two days of hard contemplation I finally decided to brave using the item.\n\n  Typical... Nothing happened. But why would it have?\n\n I don't even know what I was expecting. Ender pearls and glowstone... \u00A7lHAH!\u00A7r I'm going to be the laughing stalk of the...");
    pages.add("\u00A70town. I am going to pack up and head home tomorrow, I guess this expedition ends early.\n\n\u00A71[ Night 8 ]\n\n\u00A70Where to begin..\n\nI used the \u00A75Voidstone\u00A70 again.\n\nThe initial use must have been some sort of binding ritual. Upon second use I was instantly transported back to my house. I would have written sooner but I used the item on an impulse and then had to hike all the way back to the expedition to retrieve my gear!\n\n  Upon arriving back at the expedition I collected all of my things and attempted to use the stone again but failed. I will have to do more research...\n\n\u00A71[ Day 9 ]\n\n\u00A70  I awoke this morning and decided to do two tests in one. A proximity test and another working test. I used the stone not");
    pages.add("\u00A7010 blocks from where I was teleported to the last time I used it.  I was instantly teleported 10 blocks behind myself. So I tried again this time closer, yet this time nothing happened. It is likely to conclude there is some sort of internal recharge or cooldown in effect after each use of the stone.\n\n\u00A71[ Day 12 ]\n\n\u00A70  After repeated use of the stone I have been having wierd dreams at night. It is almost as if the stone is attempting to communicate with me.\n\n\u00A71[ Day 15 ]\n\n\u00A70  So I finished the schematics exactly as I have been seeing them in my dreams. Tomorrow I start my expedition for materials.");
    pages.add("\u00A70page4");
    @SuppressWarnings("unchecked")
    List<GuiButton> buttons = buttonList;

    int bookXBegin = (width - 192) / 2;

    buttons.add(nextPage = new GuiButtonChangePage(BUTTON_NEXT, bookXBegin + 120, 2 + 154, false));
    buttons.add(prevPage = new GuiButtonChangePage(BUTTON_PREV, bookXBegin + 38, 2 + 154, true));
    updateButtonState();

  }

  @Override
  public boolean doesGuiPauseGame() {
    return false;
  }

  @Override
  protected void keyTyped(char c, int key) {
    char lowerCase = Character.toLowerCase(c);
    if (key == Keyboard.KEY_ESCAPE) {
      mc.displayGuiScreen(null);
    } else if (Character.getType(lowerCase) == Character.LOWERCASE_LETTER) {
      for (int i = 0, len = pages.size(); i < len; ++i) {
        String page = pages.get(i);
        if (Character.toLowerCase(page.charAt(0)) == c) {
          pageIndex = i;
          updateButtonState();
          break;
        }
      }
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float renderPartials) {
    int bookXStart = (width - 192) / 2;
    mc.renderEngine.bindTexture(texture);
    drawTexturedModalRect(bookXStart, 2, 0, 0, 192, 192);
    String page = pages.get(this.pageIndex);
    // Journal Title
    RenderUtils.renderText("\u00A7n\u00A7dVoid Journal", (long) bookXStart + 40 + 4 + 16, (long) 17, 0xffffff, 1, false);
    // FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    // Render the current page
    // fontRenderer.drawSplitString(page, bookXStart + 40, 17 + 15, 220, 0xffffff);
    RenderUtils.renderSplitText(page, bookXStart + 40, 17 + 15, 220, 0xffffff, 0);
    super.drawScreen(mouseX, mouseY, renderPartials);
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
    nextPage.visible = pageIndex < pages.size() - 1;
    prevPage.visible = pageIndex > 0;
  }
}
