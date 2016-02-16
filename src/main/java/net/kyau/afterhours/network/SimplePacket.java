package net.kyau.afterhours.network;

import io.netty.buffer.ByteBuf;
import net.kyau.afterhours.network.SimplePacket.SimpleMessage;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.utils.LogHelper;
import net.kyau.afterhours.utils.SoundUtil;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SimplePacket implements IMessageHandler<SimpleMessage, IMessage> {

  @Override
  public IMessage onMessage(SimpleMessage message, MessageContext ctx) {
    // just to make sure that the side is correct
    if (ctx.side.isClient()) {
      if (ModInfo.DEBUG)
        LogHelper.info("recieved packet from server! (" + message.type + ": " + message.text + ")");

      if (message.type == 1) {
        SoundUtil.playSound(message.text);
      }
    }
    return null;
  }

  public static class SimpleMessage implements IMessage {

    private String text;
    private int type;
    private String simpleString;
    private boolean simpleBool;

    // this constructor is required otherwise you'll get errors (used
    // somewhere in fml through reflection)
    public SimpleMessage() {
    }

    public SimpleMessage(int type, String text) {
      this.text = text;
      this.type = type;
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