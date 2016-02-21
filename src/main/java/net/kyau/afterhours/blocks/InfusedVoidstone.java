package net.kyau.afterhours.blocks;

import java.util.Random;

import net.kyau.afterhours.references.Ref;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InfusedVoidstone extends BaseBlock {

  public InfusedVoidstone() {
    // tool: "pickaxe", "axe", "shovel"
    // level: 0=wood; 1=stone; 2=iron; 3=diamond tool
    this.setHarvestLevel("pickaxe", 3);
    // stone:1.5F; obsidian:50.0F
    this.setHardness(25.0F);
    // stone:10.0F; obsidian:2000.0F; bedrock:6000000.0F
    this.setResistance(10000.0F);
    // default: 16 (completely opaque); maximum: 0 (100% translucent)
    this.setLightOpacity(0);
    // default: 0.0F (nothing); maximum: 1.0F (like full sunlight)
    // enderchest:0.5F; torch:0.9375F; fire/glowstone:1.0F
    this.setLightLevel(1.0F);
    // this.lightValue = 16;
    // sets the step sound of a block
    this.setStepSound(soundTypePiston);
    this.setUnlocalizedName(Ref.BlockID.INFUSED_VOIDSTONE);

  }

  @Override
  public boolean isOpaqueCube() {
    return true;
  }

  @Override
  public boolean isFullCube() {
    return true;
  }

  @Override
  public int getRenderType() {
    return 3;
  }

  @SideOnly(Side.CLIENT)
  public EnumWorldBlockLayer getBlockLayer() {
    return EnumWorldBlockLayer.SOLID;
  }

  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    double d0 = (double) ((float) pos.getX() + rand.nextFloat());
    double d1 = (double) ((float) pos.getY() + 0.8F);
    double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
    double d3 = 0.0D;
    double d4 = 0.0D;
    double d5 = 0.0D;
    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
  }

  @Override
  public float getBlockHardness(World worldIn, BlockPos pos) {
    // make spawn cube in the void unbreakable
    if (worldIn.provider.getDimensionId() == Ref.Dimension.DIM && pos.getY() == 127) {
      if ((pos.getX() > -3 && pos.getX() < 3) && (pos.getZ() > -3 && pos.getZ() < 3)) {
        return -1;
      }
    }
    return this.blockHardness;
  }
}
