package net.kyau.afterhours.tileentity;

import java.util.UUID;

import net.kyau.afterhours.references.Ref;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.UsernameCache;

public class TileEntityBase extends TileEntity implements ITickable {

  protected EnumFacing facing;
  protected int state;
  protected String customName;
  protected UUID ownerUUID;

  public TileEntityBase() {
    facing = EnumFacing.SOUTH;
    state = 0;
    customName = "";
    ownerUUID = null;
  }

  public EnumFacing getFacing() {
    return facing;
  }

  public void setFacing(EnumFacing orientation) {
    this.facing = orientation;
  }

  public void setFacing(int orientation) {
    this.facing = EnumFacing.getFront(orientation);
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public String getCustomName() {
    return customName;
  }

  public void setCustomName(String customName) {
    this.customName = customName;
  }

  public UUID getOwnerUUID() {
    return ownerUUID;
  }

  public void setOwnerUUID(UUID ownerUUID) {
    this.ownerUUID = ownerUUID;
  }

  public String getOwnerName() {
    if (ownerUUID != null) {
      return UsernameCache.getLastKnownUsername(ownerUUID);
    }

    return "Unknown";
  }

  public void setOwner(EntityPlayer entityPlayer) {
    this.ownerUUID = entityPlayer.getPersistentID();
  }

  @Override
  public void readFromNBT(NBTTagCompound nbtTagCompound) {
    super.readFromNBT(nbtTagCompound);

    if (nbtTagCompound.hasKey(Ref.NBT.FACING)) {
      this.facing = EnumFacing.getFront(nbtTagCompound.getByte(Ref.NBT.FACING));
    }

    if (nbtTagCompound.hasKey(Ref.NBT.STATE)) {
      this.state = nbtTagCompound.getInteger(Ref.NBT.STATE);
    }

    if (nbtTagCompound.hasKey(Ref.NBT.CUSTOM_NAME)) {
      this.customName = nbtTagCompound.getString(Ref.NBT.CUSTOM_NAME);
    }

    if (nbtTagCompound.hasKey(Ref.NBT.OWNER_UUID_MOST_SIG) && nbtTagCompound.hasKey(Ref.NBT.OWNER_UUID_LEAST_SIG)) {
      this.ownerUUID = new UUID(nbtTagCompound.getLong(Ref.NBT.OWNER_UUID_MOST_SIG), nbtTagCompound.getLong(Ref.NBT.OWNER_UUID_LEAST_SIG));
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound nbtTagCompound) {

    super.writeToNBT(nbtTagCompound);

    nbtTagCompound.setByte(Ref.NBT.FACING, (byte) facing.ordinal());
    nbtTagCompound.setInteger(Ref.NBT.STATE, state);

    if (this.hasCustomName()) {
      nbtTagCompound.setString(Ref.NBT.CUSTOM_NAME, customName);
    }

    if (this.hasOwner()) {
      nbtTagCompound.setLong(Ref.NBT.OWNER_UUID_MOST_SIG, ownerUUID.getMostSignificantBits());
      nbtTagCompound.setLong(Ref.NBT.OWNER_UUID_LEAST_SIG, ownerUUID.getLeastSignificantBits());
    }
  }

  public boolean hasCustomName() {
    return customName != null && customName.length() > 0;
  }

  public boolean hasOwner() {
    return ownerUUID != null;
  }

  @Override
  public Packet getDescriptionPacket() {
    // return NetworkRegistry.INSTANCE.getPacketFrom(new MessageTileEntity(this));
    NBTTagCompound nbt = new NBTTagCompound();
    writeToNBT(nbt);
    return new S35PacketUpdateTileEntity(getPos(), -999, nbt);
  }

  @Override
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    super.onDataPacket(net, pkt);
    readFromNBT(pkt.getNbtCompound());
  }

  @Override
  public void update() {
    // NOOP
  }

}
