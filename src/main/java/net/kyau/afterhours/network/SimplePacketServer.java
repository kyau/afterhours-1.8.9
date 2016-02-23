package net.kyau.afterhours.network;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.kyau.afterhours.network.SimplePacketServer.SimpleServerMessage;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.utils.LogHelper;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SimplePacketServer implements IMessageHandler<SimpleServerMessage, IMessage> {

  @Override
  public IMessage onMessage(SimpleServerMessage message, MessageContext ctx) {
    // just to make sure that the side is correct
    if (ctx.side.isClient()) {
      if (ModInfo.DEBUG)
        LogHelper.info("recieved packet from server! (" + message.type + ": " + message.text + ")");

      if (message.type == 1) {
        // NOOP
      }
    }
    return null;
  }

  public static class SimpleServerMessage implements IMessage {

    private String text;
    private int type;
    private String simpleString;
    private boolean simpleBool;
    private UUID player;

    // this constructor is required otherwise you'll get errors (used
    // somewhere in fml through reflection)
    public SimpleServerMessage() {
    }

    public SimpleServerMessage(int type, String text, UUID player) {
      this.text = text;
      this.type = type;
      this.player = player;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
      type = buf.readInt();
      text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
      buf.writeInt(this.type);
      ByteBufUtils.writeUTF8String(buf, this.text);
    }
  }

}