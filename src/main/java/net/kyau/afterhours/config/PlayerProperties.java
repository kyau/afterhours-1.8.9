package net.kyau.afterhours.config;

import net.kyau.afterhours.network.PacketHandler;
import net.kyau.afterhours.network.SimplePacketClient;
import net.kyau.afterhours.references.Ref;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PlayerProperties implements IExtendedEntityProperties {

  public final static String IDENTIFIER = "VoidPearlCooldown";

  long voidPearlCooldown = 0;
  EntityPlayer player;

  public PlayerProperties(EntityPlayer player) {
    this.player = player;
    this.voidPearlCooldown = 0;
  }

  public static final void register(EntityPlayer player) {
    player.registerExtendedProperties(PlayerProperties.IDENTIFIER, new PlayerProperties(player));
  }

  public static final PlayerProperties get(EntityPlayer player) {
    return (PlayerProperties) player.getExtendedProperties(IDENTIFIER);
  }

  @Override
  public void saveNBTData(NBTTagCompound compound) {
    compound.setLong(Ref.NBT.LASTUSE, voidPearlCooldown);
  }

  @Override
  public void loadNBTData(NBTTagCompound compound) {
    if (compound.hasKey(Ref.NBT.LASTUSE)) {
      this.voidPearlCooldown = compound.getLong(Ref.NBT.LASTUSE);
    } else {
      this.voidPearlCooldown = 0;
    }
  }

  @Override
  public void init(Entity entity, World world) {
  }

  public void setCooldown(long cooldown) {
    this.voidPearlCooldown = cooldown;
    this.sync();
  }

  public long getCooldown() {
    return this.voidPearlCooldown;
  }

  public final void sync() {
    if (!player.worldObj.isRemote) {
      IMessage msg = new SimplePacketClient.SimpleClientMessage(2, String.valueOf(this.voidPearlCooldown));
      PacketHandler.net.sendTo(msg, (EntityPlayerMP) player);
    }
  }

}
