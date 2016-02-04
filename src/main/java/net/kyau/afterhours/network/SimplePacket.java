package net.kyau.afterhours.network;

import io.netty.buffer.ByteBuf;
import net.kyau.afterhours.network.SimplePacket.SimpleMessage;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SimplePacket implements IMessageHandler<SimpleMessage, IMessage> {

  @Override
  public IMessage onMessage(SimpleMessage message, MessageContext ctx) {
    // just to make sure that the side is correct
    if (ctx.side.isClient()) {
      System.out.println("AfterHours: recieved packet from server! (" + message.text + ")");
      String text = message.text;
      if (text == "test_packet") {
        // do this clientside;
      }
    }
    return null;
  }

  public static class SimpleMessage implements IMessage {

    private String text;
    private String simpleString;
    private boolean simpleBool;

    // this constructor is required otherwise you'll get errors (used
    // somewhere in fml through reflection)
    public SimpleMessage() {
    }

    public SimpleMessage(String text) {
      this.text = text;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
      this.text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf, this.text);
    }
  }
}