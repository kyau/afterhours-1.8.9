package net.kyau.afterhours.blocks;

import java.util.Random;

import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.references.Ref;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DarkMatterCluster extends BaseBlock {

  public DarkMatterCluster() {
    // tool: "pickaxe", "axe", "shovel"
    // level: 0=wood; 1=stone; 2=iron; 3=diamond tool
    this.setHarvestLevel("pickaxe", 3);
    // stone:1.5F; obsidian:50.0F
    this.setHardness(40.0F);
    // stone:10.0F; obsidian:2000.0F; bedrock:6000000.0F
    this.setResistance(10000.0F);
    // default: 16 (completely opaque); maximum: 0 (100% translucent)
    this.setLightOpacity(16);
    // default: 0.0F (nothing); maximum: 1.0F (like full sunlight)
    // enderchest:0.5F; torch:0.9375F; fire/glowstone:1.0F
    this.setLightLevel(0.5F);
    // sets the step sound of a block
    this.setStepSound(soundTypePiston);
    this.setUnlocalizedName(Ref.BlockID.DARKMATTERCLUSTER);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public float getAmbientOcclusionLightValue() {
    return 1.0F;
  }

  @SideOnly(Side.CLIENT)
  public EnumWorldBlockLayer getBlockLayer() {
    return EnumWorldBlockLayer.SOLID;
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

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return ModItems.darkmatter;
  }

  @Override
  public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
    super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
  }

  @Override
  public int quantityDropped(Random random) {
    return 1;
  }

  @Override
  public int quantityDroppedWithBonus(int fortune, Random random) {
    if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped((IBlockState) this.getBlockState().getValidStates().iterator().next(), random, fortune)) {
      int i = random.nextInt(fortune + 2) - 1;

      if (i < 0) {
        i = 0;
      }

      return this.quantityDropped(random) * (i + 1);
    } else {
      return this.quantityDropped(random);
    }
  }

  @Override
  public int getExpDrop(net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
    IBlockState state = world.getBlockState(pos);
    Random rand = world instanceof World ? ((World) world).rand : new Random();
    if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this)) {
      int i = MathHelper.getRandomIntegerInRange(rand, 4, 8);
      return i;
    }
    return 0;
  }

}
