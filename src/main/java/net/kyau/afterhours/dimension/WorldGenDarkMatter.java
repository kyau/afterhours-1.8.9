package net.kyau.afterhours.dimension;

import java.util.Random;

import net.kyau.afterhours.init.ModBlocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenDarkMatter extends WorldGenerator {

  public WorldGenDarkMatter() {
  }

  public boolean generate(World world, Random rand, BlockPos pos) {
    int i = pos.getX(), j = pos.getY(), k = pos.getZ();

    BlockPos randPos = new BlockPos(i + rand.nextInt(12) - rand.nextInt(12), j + rand.nextInt(12) - rand.nextInt(12), k + rand.nextInt(12) - rand.nextInt(12));
    world.setBlockState(randPos, ModBlocks.darkmattercluster.getDefaultState());
    boolean switchGen = false;
    for (int incX = -1; incX < 2; incX++) {
      for (int incY = -1; incY < 2; incY++) {
        for (int incZ = -1; incZ < 2; incZ++) {
          BlockPos randPosInc = new BlockPos(randPos.getX() + incX, randPos.getY() + incY, randPos.getZ() + incZ);
          if (rand.nextFloat() < 0.3f) {
            if (switchGen) {
              world.setBlockState(randPosInc, ModBlocks.darkmattercluster.getDefaultState());
            } else {
              world.setBlockState(randPosInc, ModBlocks.voidstone.getDefaultState());
            }
            switchGen = !switchGen;
          }
        }
      }
    }

    return true;
  }
}
