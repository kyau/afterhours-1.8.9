package net.kyau.afterhours.dimension;

import java.util.Random;

import net.kyau.afterhours.references.Ref;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenerator implements IWorldGenerator {

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
    if (world.provider.getDimensionId() == Ref.Dimension.DIM) {
      generateVoid(world, random, chunkX * 16, chunkZ * 16);
    }
  }

  private void generateVoid(World world, Random random, int chunkX, int chunkZ) {
    for (int i = 0; i < 2; i++) {
      int structureX = chunkX + random.nextInt(16);
      int structureY = 63 + (int) (Math.random() * 116);
      int structureZ = chunkZ + random.nextInt(16);

      if (random.nextInt(75) == 42)
        new WorldGenDarkMatter().generate(world, random, new BlockPos(structureX, structureY, structureZ));
    }
  }

}
