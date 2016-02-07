package net.kyau.afterhours.network;

import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.utils.ChatUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

  public static SimpleNetworkWrapper net;

  public static void init() {
    net = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MOD_ID);
    // net.registerMessage(SoundUtil.PacketPlaySound.Handler.class, SoundUtil.PacketPlaySound.class, 0, Side.CLIENT);
    net.registerMessage(ChatUtil.PacketNoSpamChat.Handler.class, ChatUtil.PacketNoSpamChat.class, 0, Side.CLIENT);
    net.registerMessage(SimplePacket.class, SimplePacket.SimpleMessage.class, 1, Side.CLIENT);
  }

  public static void sendToAllAround(IMessage message, TileEntity te, int range) {
    net.sendToAllAround(message, new NetworkRegistry.TargetPoint(te.getWorld().provider.getDimensionId(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), range));
  }

  public static void sendToAllAround(IMessage message, TileEntity te) {
    sendToAllAround(message, te, 64);
  }

  public static void sendTo(IMessage message, EntityPlayerMP player) {
    net.sendTo(message, player);
  }

  private static int nextPacketId = 0;

  @SuppressWarnings("unchecked")
  private static void registerMessage(Class packet, Class message) {
    net.registerMessage(packet, message, nextPacketId, Side.CLIENT);
    // net.registerMessage(packet, message, nextPacketId, Side.SERVER);
    nextPacketId++;
  }
}