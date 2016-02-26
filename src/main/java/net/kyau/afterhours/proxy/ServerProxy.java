package net.kyau.afterhours.proxy;

import java.util.Random;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ServerProxy extends CommonProxy {

  @Override
  public ClientProxy getClientProxy() {
    return null;
  }

  @Override
  public void registerKeybindings() {
    // NOOP

  }

  @Override
  public void initRenderingAndTextures() {
    // NOOP
  }

  @Override
  public boolean isSinglePlayer() {
    return false;
  }

  @Override
  public void openJournal() {
    // NOOP
  }

  @Override
  public void registerClientEventHandlers() {
    // NOOP
  }

  @Override
  public void generateQuantumParticles(World world, BlockPos pos, Random rand) {
    // NOOP
  }

  @Override
  public long getVoidPearlLastUse() {
    // NOOP
    return 0;
  }

  @Override
  public void setVoidPearlLastUse(long cooldown) {
    // NOOP
  }

}
