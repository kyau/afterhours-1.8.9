package net.kyau.afterhours.client.renderer;

import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.world.World;

public class EntityParticleFXQuantum extends EntityReddustFX {

  public EntityParticleFXQuantum(World parWorld, double parX, double parY, double parZ, float parMotionX, float parMotionY, float parMotionZ) {
    // new EntityReddustFX(worldIn, xCoordIn, yCoordIn, zCoordIn, (float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn)
    super(parWorld, parX, parY, parZ, parMotionX, parMotionY, parMotionZ);
    // setParticleTextureIndex(82); // same as happy villager
    particleScale = 0.25F;
    // setRBGColorF(0.545F, 0.741F, 0.749F);
    setRBGColorF(0.1F, 0.1F, 0.1F);
  }
}
