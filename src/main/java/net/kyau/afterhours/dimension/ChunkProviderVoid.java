package net.kyau.afterhours.dimension;

import java.util.List;
import java.util.Random;

import net.kyau.afterhours.init.ModBlocks;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenLakes;

public class ChunkProviderVoid implements IChunkProvider {

  /** RNG. */
  private Random rand;
  private NoiseGeneratorOctaves noiseGen1;
  private NoiseGeneratorOctaves noiseGen2;
  private NoiseGeneratorOctaves noiseGen3;
  private NoiseGeneratorPerlin perlinNoise;
  public NoiseGeneratorOctaves noiseGen5;
  public NoiseGeneratorOctaves noiseGen6;
  public NoiseGeneratorOctaves mobSpawnerNoise;
  private World worldObj;
  private final boolean mapFeaturesEnabled;
  private WorldType theWorldType;
  private final double[] field_147434_q;
  private final float[] parabolicField;
  private ChunkProviderSettings settings;
  private double[] stoneNoise;
  private BiomeGenBase[] biomesForGeneration;
  double[] mainNoiseArray;
  double[] lowerLimitNoiseArray;
  double[] upperLimitNoiseArray;
  double[] depthNoiseArray;
  private static final String __OBFID = "CL_00000396";

  public ChunkProviderVoid(World worldIn, long seed, boolean generateStructures, String structuresJson) {
    this.stoneNoise = new double[256];

    this.worldObj = worldIn;
    this.mapFeaturesEnabled = generateStructures;
    this.theWorldType = worldIn.getWorldInfo().getTerrainType();
    worldIn.setSeaLevel(0);
    this.rand = new Random(seed);
    this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
    this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
    this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
    this.perlinNoise = new NoiseGeneratorPerlin(this.rand, 4);
    this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
    this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
    this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
    this.field_147434_q = new double[825];
    this.parabolicField = new float[25];

    for (int j = -2; j <= 2; ++j) {
      for (int k = -2; k <= 2; ++k) {
        float f = 10.0F / MathHelper.sqrt_float((float) (j * j + k * k) + 0.2F);
        this.parabolicField[j + 2 + (k + 2) * 5] = f;
      }
    }

    if (structuresJson != null) {
      this.settings = ChunkProviderSettings.Factory.jsonToFactory(structuresJson).func_177864_b();
    }
  }

  public void setBlocksInChunk(int x, int z, ChunkPrimer primer) {
    this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, x * 4 - 2, z * 4 - 2, 10, 10);
    this.func_147423_a(x * 4, 0, z * 4);

    for (int k = 0; k < 4; ++k) {
      int l = k * 5;
      int i1 = (k + 1) * 5;

      for (int j1 = 0; j1 < 4; ++j1) {
        int k1 = (l + j1) * 33;
        int l1 = (l + j1 + 1) * 33;
        int i2 = (i1 + j1) * 33;
        int j2 = (i1 + j1 + 1) * 33;

        for (int k2 = 0; k2 < 32; ++k2) {
          double d0 = 0.125D;
          double d1 = this.field_147434_q[k1 + k2];
          double d2 = this.field_147434_q[l1 + k2];
          double d3 = this.field_147434_q[i2 + k2];
          double d4 = this.field_147434_q[j2 + k2];
          double d5 = (this.field_147434_q[k1 + k2 + 1] - d1) * d0;
          double d6 = (this.field_147434_q[l1 + k2 + 1] - d2) * d0;
          double d7 = (this.field_147434_q[i2 + k2 + 1] - d3) * d0;
          double d8 = (this.field_147434_q[j2 + k2 + 1] - d4) * d0;

          for (int l2 = 0; l2 < 8; ++l2) {
            double d9 = 0.25D;
            double d10 = d1;
            double d11 = d2;
            double d12 = (d3 - d1) * d9;
            double d13 = (d4 - d2) * d9;

            for (int i3 = 0; i3 < 4; ++i3) {
              double d14 = 0.25D;
              double d16 = (d11 - d10) * d14;
              double d15 = d10 - d16;

              for (int j3 = 0; j3 < 4; ++j3) {
                // if ((d15 += d16) > 0.0D) {
                primer.setBlockState(k * 4 + i3, k2 * 8 + l2, j1 * 4 + j3, Blocks.air.getDefaultState());
                // } else if (k2 * 8 + l2 < 127) {
                // primer.setBlockState(k * 4 + i3, k2 * 8 + l2, j1 * 4 + j3, this.oceanBlockTmpl.getDefaultState());
                // }
              }

              d10 += d12;
              d11 += d13;
            }

            d1 += d5;
            d2 += d6;
            d3 += d7;
            d4 += d8;
          }
        }
      }
    }
  }

  public void replaceBlocksForBiome(int x, int z, ChunkPrimer primer, BiomeGenBase[] biomeGens) {
    double d0 = 0.03125D;
    this.stoneNoise = this.perlinNoise.func_151599_a(this.stoneNoise, (double) (x * 16), (double) (z * 16), 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);

    for (int k = 0; k < 16; ++k) {
      for (int l = 0; l < 16; ++l) {
        BiomeGenBase biomegenbase = biomeGens[l + k * 16];
        biomegenbase.genTerrainBlocks(this.worldObj, this.rand, primer, x * 16 + k, z * 16 + l, this.stoneNoise[l + k * 16]);
      }
    }
  }

  private void func_147423_a(int x, int y, int z) {
    this.depthNoiseArray = this.noiseGen6.generateNoiseOctaves(this.depthNoiseArray, x, z, 5, 5, 200, 200, 0.5);
    this.mainNoiseArray = this.noiseGen3.generateNoiseOctaves(this.mainNoiseArray, x, y, z, 5, 33, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
    this.lowerLimitNoiseArray = this.noiseGen1.generateNoiseOctaves(this.lowerLimitNoiseArray, x, y, z, 5, 33, 5, 5000D, 2000D, 5000D);
    this.upperLimitNoiseArray = this.noiseGen2.generateNoiseOctaves(this.upperLimitNoiseArray, x, y, z, 5, 33, 5, 50D, 684.412D, 50D);
    boolean flag1 = false;
    boolean flag = false;
    int l = 0;
    int i1 = 0;

    for (int j1 = 0; j1 < 5; ++j1) {
      for (int k1 = 0; k1 < 5; ++k1) {
        float f2 = 0.0F;
        float f3 = 0.0F;
        float f4 = 0.0F;
        byte b0 = 2;
        BiomeGenBase biomegenbase = this.biomesForGeneration[j1 + 2 + (k1 + 2) * 10];

        for (int l1 = -b0; l1 <= b0; ++l1) {
          for (int i2 = -b0; i2 <= b0; ++i2) {
            BiomeGenBase biomegenbase1 = this.biomesForGeneration[j1 + l1 + 2 + (k1 + i2 + 2) * 10];
            float f5 = this.settings.biomeDepthOffSet + biomegenbase1.minHeight * this.settings.biomeDepthWeight;
            float f6 = this.settings.biomeScaleOffset + biomegenbase1.maxHeight * this.settings.biomeScaleWeight;

            if (this.theWorldType == WorldType.AMPLIFIED && f5 > 0.0F) {
              f5 = 1.0F + f5 * 2.0F;
              f6 = 1.0F + f6 * 4.0F;
            }

            float f7 = this.parabolicField[l1 + 2 + (i2 + 2) * 5] / (f5 + 2.0F);

            if (biomegenbase1.minHeight > biomegenbase.minHeight) {
              f7 /= 2.0F;
            }

            f2 += f6 * f7;
            f3 += f5 * f7;
            f4 += f7;
          }
        }

        f2 /= f4;
        f3 /= f4;
        f2 = f2 * 0.9F + 0.1F;
        f3 = (f3 * 4.0F - 1.0F) / 8.0F;
        double d7 = this.depthNoiseArray[i1] / 8000.0D;

        if (d7 < 0.0D) {
          d7 = -d7 * 0.3D;
        }

        d7 = d7 * 3.0D - 2.0D;

        if (d7 < 0.0D) {
          d7 /= 2.0D;

          if (d7 < -1.0D) {
            d7 = -1.0D;
          }

          d7 /= 1.4D;
          d7 /= 2.0D;
        } else {
          if (d7 > 1.0D) {
            d7 = 1.0D;
          }

          d7 /= 8.0D;
        }

        ++i1;
        double d8 = (double) f3;
        double d9 = (double) f2;
        d8 += d7 * 0.2D;
        d8 = d8 * (double) this.settings.baseSize / 8.0D;
        double d0 = (double) this.settings.baseSize + d8 * 4.0D;

        for (int j2 = 0; j2 < 33; ++j2) {
          double d1 = ((double) j2 - d0) * (double) this.settings.stretchY * 128.0D / 256.0D / d9;

          if (d1 < 0.0D) {
            d1 *= 4.0D;
          }

          double d2 = this.lowerLimitNoiseArray[l] / (double) this.settings.lowerLimitScale;
          double d3 = this.upperLimitNoiseArray[l] / (double) this.settings.upperLimitScale;
          double d4 = (this.mainNoiseArray[l] / 10.0D + 1.0D) / 2.0D;
          double d5 = MathHelper.denormalizeClamp(d2, d3, d4) - d1;

          if (j2 > 29) {
            double d6 = (double) ((float) (j2 - 29) / 3.0F);
            d5 = d5 * (1.0D - d6) + -10.0D * d6;
          }

          this.field_147434_q[l] = d5;
          ++l;
        }
      }
    }
  }

  @Override
  public boolean chunkExists(int x, int z) {
    return true;
  }

  @Override
  public Chunk provideChunk(int x, int z) {
    this.rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
    ChunkPrimer chunkprimer = new ChunkPrimer();
    this.setBlocksInChunk(x, z, chunkprimer);
    this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
    this.replaceBlocksForBiome(x, z, chunkprimer, this.biomesForGeneration);

    Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
    byte[] abyte = chunk.getBiomeArray();

    for (int k = 0; k < abyte.length; ++k) {
      abyte[k] = (byte) this.biomesForGeneration[k].biomeID;
    }

    chunk.generateSkylightMap();
    return chunk;
  }

  @Override
  public Chunk provideChunk(BlockPos blockPosIn) {

    return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
  }

  @Override
  public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
    BlockFalling.fallInstantly = true;
    int k = p_73153_2_ * 16;
    int l = p_73153_3_ * 16;
    BlockPos blockpos = new BlockPos(k, 0, l);
    BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos.add(16, 0, 16));
    this.rand.setSeed(this.worldObj.getSeed());
    long i1 = this.rand.nextLong() / 2L * 2L + 1L;
    long j1 = this.rand.nextLong() / 2L * 2L + 1L;
    this.rand.setSeed((long) p_73153_2_ * i1 + (long) p_73153_3_ * j1 ^ this.worldObj.getSeed());
    boolean flag = false;
    ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(p_73153_2_, p_73153_3_);

    int k1;
    int l1;
    int i2;

    k1 = this.rand.nextInt(16) + 8;
    l1 = this.rand.nextInt(256);
    i2 = this.rand.nextInt(16) + 8;
    (new WorldGenLakes(Blocks.ice)).generate(this.worldObj, this.rand, blockpos.add(k1, l1, i2));

    if (!flag && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes) {
      k1 = this.rand.nextInt(16) + 8;
      l1 = this.rand.nextInt(this.rand.nextInt(248) + 8);
      i2 = this.rand.nextInt(16) + 8;

      if (l1 < 63 || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0) {
        (new WorldGenLakes(Blocks.lava)).generate(this.worldObj, this.rand, blockpos.add(k1, l1, i2));
      }
    }

    biomegenbase.decorate(this.worldObj, this.rand, new BlockPos(k, 0, l));
    SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, k + 8, l + 8, 16, 16, this.rand);
    blockpos = blockpos.add(8, 0, 8);

    for (k1 = 0; k1 < 16; ++k1) {
      for (l1 = 0; l1 < 16; ++l1) {
        BlockPos blockpos1 = this.worldObj.getPrecipitationHeight(blockpos.add(k1, 0, l1));
        BlockPos blockpos2 = blockpos1.offset(EnumFacing.DOWN);

        if (this.worldObj.canBlockFreezeWater(blockpos2)) {
          this.worldObj.setBlockState(blockpos2, Blocks.ice.getDefaultState(), 2);
        }

        if (this.worldObj.canSnowAt(blockpos1, true)) {
          this.worldObj.setBlockState(blockpos1, Blocks.snow_layer.getDefaultState(), 2);
        }
      }
    }

    createSpawn(this.worldObj);

    BlockFalling.fallInstantly = false;
  }

  @Override
  public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_) {
    return false;
  }

  @Override
  public boolean saveChunks(boolean p_73151_1_, IProgressUpdate progressCallback) {
    return true;
  }

  @Override
  public boolean unloadQueuedChunks() {
    return false;
  }

  @Override
  public boolean canSave() {
    return true;
  }

  @Override
  public String makeString() {
    return "VoidRandomLevelSource";
  }

  @Override
  public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
    return null;
  }

  @Override
  public int getLoadedChunkCount() {
    return 0;
  }

  @Override
  public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {
  }

  private static void createSpawn(World world) {
    world.setBlockState(new BlockPos(0, 127, 0), ModBlocks.infused_voidstone.getDefaultState());
    world.setBlockState(new BlockPos(1, 127, 0), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(0, 127, 1), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(1, 127, 1), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(1, 127, -1), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(-1, 127, 0), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(0, 127, -1), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(-1, 127, -1), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(-1, 127, 1), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(2, 127, 0), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(0, 127, 1), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(0, 127, 2), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(-2, 127, 0), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(0, 127, -1), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(0, 127, -2), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(2, 127, 1), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(2, 127, -1), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(-2, 127, -1), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(-2, 127, 1), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(1, 127, 2), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(1, 127, -2), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(-1, 127, -2), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(-1, 127, 2), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(2, 127, 2), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(2, 127, -2), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(-2, 127, -2), ModBlocks.voidstone.getDefaultState());
    world.setBlockState(new BlockPos(-2, 127, 2), ModBlocks.voidstone.getDefaultState());
  }

  @Override
  public void saveExtraData() {
  }

  @Override
  public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
    BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(pos);
    return biomegenbase.getSpawnableList(creatureType);
  }

}
