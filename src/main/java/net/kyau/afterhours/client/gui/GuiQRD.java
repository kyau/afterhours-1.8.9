package net.kyau.afterhours.client.gui;

import net.kyau.afterhours.inventory.ContainerQRD;
import net.kyau.afterhours.inventory.qrd.InventoryQRDMain;
import net.kyau.afterhours.inventory.qrd.InventoryQRDVoid;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.kyau.afterhours.utils.NBTHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiQRD extends GuiContainer {

  private float xSize_lo;
  protected int ySize = 233;
  private float ySize_lo;
  private static final ResourceLocation iconLocation = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/qrd.png");
  private final ItemStack parentItem;
  private final InventoryQRDMain inventoryQRDMain;
  private final InventoryQRDVoid inventoryQRDVoid;

  public GuiQRD(EntityPlayer player, InventoryQRDMain inventoryQRDMain, InventoryQRDVoid inventoryQRDVoid) {
    super(new ContainerQRD(player, inventoryQRDMain, inventoryQRDVoid));
    // this.inventory = containerItem.inventory;
    this.parentItem = inventoryQRDMain.parentItem;
    this.inventoryQRDMain = inventoryQRDMain;
    this.inventoryQRDVoid = inventoryQRDVoid;
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
    String qrdMainName = this.inventoryQRDMain.hasCustomName() ? this.inventoryQRDMain.getName() : this.inventoryQRDMain.getName();
    String qrdVoidName = this.inventoryQRDVoid.hasCustomName() ? this.inventoryQRDVoid.getName() : this.inventoryQRDVoid.getName();
    String inventoryName = StatCollector.translateToLocal(Ref.Containers.INVENTORY);
    super.fontRendererObj.drawString(qrdMainName, 8, this.ySize - 262, 0x404040);
    super.fontRendererObj.drawString(qrdVoidName, 128, 87, 0x404040);
    super.fontRendererObj.drawString(inventoryName, 8, this.ySize - 128, 0x404040);
    if (this.inventoryQRDVoid.quantumSync) {
      super.fontRendererObj.drawString(Ref.Containers.VOID_SYNC_TRUE, 46, 87, 0x008000);
    } else {
      super.fontRendererObj.drawString(Ref.Containers.VOID_SYNC_FALSE, 46, 87, 0x800000);
    }
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
  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();

    if (mc.thePlayer != null) {
      for (ItemStack itemStack : mc.thePlayer.inventory.mainInventory) {
        if (itemStack != null) {
          if (NBTHelper.hasTag(itemStack, Ref.NBT.QRD_GUI_OPEN)) {
            NBTHelper.removeTag(itemStack, Ref.NBT.QRD_GUI_OPEN);
          }
        }
      }
    }
  }

  @Override
  protected boolean checkHotbarKeys(int key) {
    return false;
  }
}
