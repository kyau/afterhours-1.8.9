package net.kyau.afterhours.client.gui;

import net.kyau.afterhours.inventory.ContainerQR;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.NBTHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiQR extends GuiContainer {

  private float xSize_lo;
  protected int ySize = 166;
  private float ySize_lo;
  private static final ResourceLocation iconLocation = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/quantum_reciprocator.png");
  private final InventoryPlayer inventoryPlayer;
  private final IInventory tileQR;

  public GuiQR(InventoryPlayer inventoryPlayer, IInventory inventoryQR) {
    super(new ContainerQR(inventoryPlayer, inventoryQR));
    this.tileQR = inventoryQR;
    this.inventoryPlayer = inventoryPlayer;
  }

  /**
   * Draws the screen and all the components in it.
   */
  public void drawScreen(int par1, int par2, float par3) {
    super.drawScreen(par1, par2, par3);
    this.xSize_lo = (float) par1;
    this.ySize_lo = (float) par2;
  }

  /**
   * Draw the foreground layer for the GuiContainer (everything in front of the items)
   */
  protected void drawGuiContainerForegroundLayer(int par1, int par2) {
    String qrMainName = this.tileQR.hasCustomName() ? this.tileQR.getName() : this.tileQR.getName();
    String inventoryName = StatCollector.translateToLocal(Ref.Containers.INVENTORY);
    // this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
    super.fontRendererObj.drawString(qrMainName, this.xSize / 2 - this.fontRendererObj.getStringWidth(qrMainName) / 2, this.ySize - 160, 0x404040);
    super.fontRendererObj.drawString(inventoryName, 8, this.ySize - 94, 0x404040);
    int ticksReciprocatingItemSoFar = tileQR.getField(2);
    int ticksPerItem = tileQR.getField(3);
    // if (ticksPerItem != 0 && ticksReciprocatingItemSoFar != 0)
    // RenderUtils.renderText("Reciprocating...", 112, this.ySize - 94, 0x808080, 1, false);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(iconLocation);
    int x = (this.width - this.xSize) / 2;
    int y = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    // draw player on screen
    // GuiInventory.drawEntityOnScreen(k + 51, l + 75, 30, (float) (k + 51) - this.xSize_lo, (float) (l + 75 - 50) -
    // this.ySize_lo, this.mc.thePlayer);

    // draw progress indicator
    int marginHorizontal = (width - xSize) / 2;
    int marginVertical = (height - ySize) / 2;
    int progressLevel = getProgressLevel(24);
    drawTexturedModalRect(marginHorizontal + 74, marginVertical + 34, 176, 0, progressLevel + 1, 16);
  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();

    if (mc.thePlayer != null) {
      for (ItemStack itemStack : mc.thePlayer.inventory.mainInventory) {
        if (itemStack != null) {
          if (NBTHelper.hasTag(itemStack, Ref.NBT.QR_GUI_OPEN)) {
            NBTHelper.removeTag(itemStack, Ref.NBT.QR_GUI_OPEN);
          }
        }
      }
    }
  }

  @Override
  protected boolean checkHotbarKeys(int key) {
    return false;
  }

  private int getProgressLevel(int progressIndicatorPixelWidth) {
    int ticksReciprocateItemSoFar = tileQR.getField(2);
    int ticksPerItem = tileQR.getField(3);
    return ticksPerItem != 0 && ticksReciprocateItemSoFar != 0 ? ticksReciprocateItemSoFar * progressIndicatorPixelWidth / ticksPerItem : 0;
  }
}
