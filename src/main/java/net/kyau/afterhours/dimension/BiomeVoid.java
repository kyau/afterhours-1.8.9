package net.kyau.afterhours.dimension;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeVoid extends BiomeGenBase {

  public BiomeVoid(int id) {
    super(id);
    this.enableRain = false;
    this.enableSnow = false;
    this.spawnableMonsterList.clear();
    this.spawnableCreatureList.clear();
    this.spawnableWaterCreatureList.clear();
    this.spawnableWaterCreatureList.clear();
  }

  @Override
  public BiomeGenBase.TempCategory getTempCategory() {
    return BiomeGenBase.TempCategory.COLD;
  }

  @Override
  public final void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int p_180628_4_, int p_180628_5_, double p_180628_6_) {
  }

  public BiomeVoid setColor(int color) {
    return (BiomeVoid) super.setColor(color);
  }

  public BiomeVoid setBiomeName(String name) {
    return (BiomeVoid) super.setBiomeName(name);
  }

  public BiomeVoid setTheHeight(BiomeGenBase.Height h) {
    return (BiomeVoid) super.setHeight(h);
  }

}
