package net.kyau.afterhours.network;

import net.kyau.afterhours.ModInfo;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

  public static SimpleNetworkWrapper net;

  public static void init() {
    net = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MOD_ID.toUpperCase());
    registerMessage(SimplePacket.class, SimplePacket.SimpleMessage.class);
  }

  private static int nextPacketId = 0;

  private static void registerMessage(Class packet, Class message) {
    net.registerMessage(packet, message, nextPacketId, Side.CLIENT);
    net.registerMessage(packet, message, nextPacketId, Side.SERVER);
    nextPacketId++;
  }
}