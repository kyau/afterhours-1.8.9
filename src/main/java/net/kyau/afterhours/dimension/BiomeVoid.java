package net.kyau.afterhours.dimension;

import java.util.Random;

import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
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
    this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityEnderman.class, 100, 1, 4));
  }

  @Override
  public BiomeGenBase.TempCategory getTempCategory() {
    return BiomeGenBase.TempCategory.COLD;
  }

  @Override
  public final void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int p_180628_4_, int p_180628_5_, double p_180628_6_) {
    int seaLevel = 0;
    IBlockState iblockstate = this.topBlock;
    IBlockState iblockstate1 = this.fillerBlock;
    int j = -1;
    int k = (int) (p_180628_6_ / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
    int l = p_180628_4_ & 15;
    int i1 = p_180628_5_ & 15;
    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

    for (int j1 = 255; j1 >= 0; --j1) {
      IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);

      if (iblockstate2.getBlock().getMaterial() == Material.air) {
        j = -1;
      } else if (iblockstate2.getBlock() == Blocks.stone) {
        if (j == -1) {
          if (k <= 0) {
            iblockstate = null;
            iblockstate1 = Blocks.stone.getDefaultState();
          } else if (j1 >= seaLevel - 4 && j1 <= seaLevel + 1) {
            iblockstate = this.topBlock;
            iblockstate1 = this.fillerBlock;
          }

          if (j1 < seaLevel && (iblockstate == null || iblockstate.getBlock().getMaterial() == Material.air)) {
            if (this.getFloatTemperature(blockpos$mutableblockpos.set(p_180628_4_, j1, p_180628_5_)) < 0.15F) {
              iblockstate = Blocks.ice.getDefaultState();
            } else {
              iblockstate = Blocks.water.getDefaultState();
            }
          }

          j = k;

          if (j1 >= seaLevel - 1) {
            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
          } else if (j1 < seaLevel - 7 - k) {
            iblockstate = null;
            iblockstate1 = Blocks.stone.getDefaultState();
            chunkPrimerIn.setBlockState(i1, j1, l, Blocks.gravel.getDefaultState());
          } else {
            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
          }
        } else if (j > 0) {
          --j;
          chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);

          if (j == 0 && iblockstate1.getBlock() == Blocks.sand) {
            j = rand.nextInt(4) + Math.max(0, j1 - 63);
            iblockstate1 = iblockstate1.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? Blocks.red_sandstone.getDefaultState() : Blocks.sandstone.getDefaultState();
          }
        }
      }
    }
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
