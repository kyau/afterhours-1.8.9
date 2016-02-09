package net.kyau.afterhours.client.gui;

import net.kyau.afterhours.inventory.ContainerItem;
import net.kyau.afterhours.inventory.InventoryVOID;
import net.kyau.afterhours.inventory.InventoryVRAD;
import net.kyau.afterhours.utils.NBTHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiVRADInventory extends GuiContainer {

  private float xSize_lo;
  protected int ySize = 233;
  private float ySize_lo;
  private static final ResourceLocation iconLocation = new ResourceLocation("afterhours", "textures/gui/vrad-1.png");
  private final ItemStack parentItem;
  private final InventoryVRAD inventoryVRAD;
  private final InventoryVOID inventoryVOID;

  public GuiVRADInventory(EntityPlayer player, InventoryVRAD inventoryVRAD, InventoryVOID inventoryVOID) {
    super(new ContainerItem(player, inventoryVRAD, inventoryVOID));
    // this.inventory = containerItem.inventory;
    this.parentItem = inventoryVRAD.parentItem;
    this.inventoryVRAD = inventoryVRAD;
    this.inventoryVOID = inventoryVOID;
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
    String vradName = this.inventoryVRAD.hasCustomName() ? this.inventoryVRAD.getName() : this.inventoryVRAD.getName();
    String voidName = this.inventoryVOID.hasCustomName() ? this.inventoryVOID.getName() : this.inventoryVOID.getName();
    super.fontRendererObj.drawString(vradName, 8, this.ySize - 262, 0x404040);
    super.fontRendererObj.drawString(voidName, 128, 90, 0x404040);
    super.fontRendererObj.drawString("Inventory", 8, this.ySize - 128, 0x404040);
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
          if (NBTHelper.hasTag(itemStack, "vradGuiOpen")) {
            NBTHelper.removeTag(itemStack, "vradGuiOpen");
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
