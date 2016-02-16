package net.kyau.afterhours.event;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.client.gui.GuiQRD;
import net.kyau.afterhours.inventory.ContainerQRD;
import net.kyau.afterhours.inventory.qrd.InventoryQRDMain;
import net.kyau.afterhours.inventory.qrd.InventoryQRDVoid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == AfterHours.GUI_VRD) {
      return new ContainerQRD(player, new InventoryQRDMain(player.getHeldItem()), new InventoryQRDVoid(player.getHeldItem(), player));
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == AfterHours.GUI_VRD) {
      // We have to cast the new container as our custom class
      // and pass in currently held item for the inventory
      return new GuiQRD(player, new InventoryQRDMain(player.getHeldItem()), new InventoryQRDVoid(player.getHeldItem(), player));
    }
    return null;
  }

}
