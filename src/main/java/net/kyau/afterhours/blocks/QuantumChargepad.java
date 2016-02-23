package net.kyau.afterhours.blocks;

import java.util.Random;

import net.kyau.afterhours.AfterHours;
import net.kyau.afterhours.references.Ref;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class QuantumChargepad extends BaseBlock {

  public QuantumChargepad() {
    // tool: "pickaxe", "axe", "shovel"
    // level: 0=wood; 1=stone; 2=iron; 3=diamond tool
    this.setHarvestLevel("pickaxe", 3);
    // stone:1.5F; obsidian:50.0F
    this.setHardness(1.5F);
    // stone:10.0F; obsidian:2000.0F; bedrock:6000000.0F
    this.setResistance(10000.0F);
    // default: 16 (completely opaque); maximum: 0 (100% translucent)
    this.setLightOpacity(16);
    // default: 0.0F (nothing); maximum: 1.0F (like full sunlight)
    // enderchest:0.5F; torch:0.9375F; fire/glowstone:1.0F
    this.setLightLevel(0.2F);
    // sets the step sound of a block
    this.setStepSound(soundTypePiston);
    this.setUnlocalizedName(Ref.BlockID.QUANTUM_CHARGEPAD);
    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public EnumWorldBlockLayer getBlockLayer() {
    return EnumWorldBlockLayer.CUTOUT;
  }

  @Override
  public boolean isOpaqueCube() {
    return false;
  }

  @Override
  public boolean isFullCube() {
    return false;
  }

  @Override
  public int getRenderType() {
    return 3;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand) {
    /*
    float x1 = pos.getX() + world.rand.nextFloat();
    float y1 = pos.getY() + 0.95F;
    float z1 = pos.getZ() + world.rand.nextFloat();
    world.spawnParticle(EnumParticleTypes.SPELL_MOB, x1, y1, z1, 0.0D, 0.0D, 0.0D, new int[0]);
    */
    AfterHours.proxy.generateQuantumParticles(world, pos, rand);
  }
}
