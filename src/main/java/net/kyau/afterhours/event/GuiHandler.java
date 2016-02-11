package net.kyau.afterhours.event;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.client.gui.GuiVRD;
import net.kyau.afterhours.inventory.ContainerVRD;
import net.kyau.afterhours.inventory.vrd.InventoryVRDMain;
import net.kyau.afterhours.inventory.vrd.InventoryVRDVoid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == AfterHours.GUI_VRD) {
      return new ContainerVRD(player, new InventoryVRDMain(player.getHeldItem()), new InventoryVRDVoid(player.getHeldItem(), player));
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == AfterHours.GUI_VRD) {
      // We have to cast the new container as our custom class
      // and pass in currently held item for the inventory
      return new GuiVRD(player, new InventoryVRDMain(player.getHeldItem()), new InventoryVRDVoid(player.getHeldItem(), player));
    }
    return null;
  }

}
