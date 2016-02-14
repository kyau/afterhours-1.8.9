package net.kyau.afterhours.dimension;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterVoid extends Teleporter {

  private final WorldServer worldServerInstance;
  private final Random random;
  private final LongHashMap destinationCoordinateCache = new LongHashMap();
  private final List destinationCoordinateKeys = new ArrayList();
  private BlockPos helperPos;

  public TeleporterVoid(WorldServer worldIn) {
    super(worldIn);
    this.worldServerInstance = worldIn;
    this.random = new Random(worldIn.getSeed());
  }

  @Override
  public void placeInPortal(Entity entityIn, float rotationYaw) {

  }

  @Override
  public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
    return false;
  }

  @Override
  public boolean makePortal(Entity p_85188_1_) {
    return true;
  }

}
