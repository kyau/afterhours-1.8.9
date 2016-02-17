package net.kyau.afterhours.dimension;

import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderVoid extends WorldProvider {

  @Override
  public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
    return 0.5f;
  }

  @Override
  public void registerWorldChunkManager() {
    this.worldChunkMgr = new WorldChunkManagerHell(DimensionHandler.voidBiome, 0.8F);
    this.dimensionId = Ref.Dimension.DIM;
    this.setSpawnPoint(new BlockPos(0, 128, 0));
  }

  public IChunkProvider createChunkGenerator() {
    return new ChunkProviderVoid(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled(), worldObj.getWorldInfo().getGeneratorOptions());
  }

  @SideOnly(Side.CLIENT)
  public float getCloudHeight() {
    return -512f;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public double getVoidFogYFactor() {
    return 0;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public float getStarBrightness(float par1) {
    return 1.0f;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public Vec3 drawClouds(float partialTicks) {
    return worldObj.drawCloudsBody(0.0f);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public Vec3 getSkyColor(net.minecraft.entity.Entity cameraEntity, float partialTicks) {
    return new Vec3(0, 0, 0);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
    return null;
  }

  public boolean renderClouds() {
    return false;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean isSkyColored() {
    return false;
  }

  @Override
  public boolean doesWaterVaporize() {
    return false;
  }

  @Override
  public float getSunBrightnessFactor(float par1) {
    return 1.0f;
  }

  @Override
  public String getDimensionName() {
    return StatCollector.translateToLocal(ModInfo.MOD_ID + ":dimension.name");
  }

  @Override
  public String getInternalNameSuffix() {
    return Ref.Dimension.INTERNAL_NAME;
  }

  @Override
  public boolean canRespawnHere() {
    return false;
  }

}
