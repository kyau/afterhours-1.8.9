package net.kyau.afterhours.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class SoundUtil {

  public static void playSound(String sound) {
    EntityPlayer player = Minecraft.getMinecraft().thePlayer;
    player.playSound(sound, 1.0F, 1.0F);
  }

}
