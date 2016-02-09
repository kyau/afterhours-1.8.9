package net.kyau.afterhours.event;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.client.gui.GuiVRADInventory;
import net.kyau.afterhours.inventory.ContainerItem;
import net.kyau.afterhours.inventory.InventoryVOID;
import net.kyau.afterhours.inventory.InventoryVRAD;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == AfterHours.GUI_VRAD) {
      return new ContainerItem(player, new InventoryVRAD(player.getHeldItem()), new InventoryVOID(player.getHeldItem()));
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == AfterHours.GUI_VRAD) {
      // We have to cast the new container as our custom class
      // and pass in currently held item for the inventory
      // return new GuiVRADInventory((ContainerItem) new ContainerItem(player, new
      // InventoryVRAD(player.getHeldItem())));
      return new GuiVRADInventory(player, new InventoryVRAD(player.getHeldItem()), new InventoryVOID(player.getHeldItem()));
    }
    return null;
  }

}
