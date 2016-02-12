package net.kyau.afterhours.blocks;

import net.kyau.afterhours.references.Ref;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Voidstone extends BaseBlock {

  public Voidstone() {
    // tool: "pickaxe", "axe", "shovel"
    // level: 0=wood; 1=stone; 2=iron; 3=diamond tool
    this.setHarvestLevel("pickaxe", 3);
    // stone:1.5F; obsidian:50.0F
    this.setHardness(25.0F);
    // stone:10.0F; obsidian:2000.0F; bedrock:6000000.0F
    this.setResistance(10000.0F);
    // default: 16 (completely opaque); maximum: 0 (100% translucent)
    this.setLightOpacity(16);
    // default: 0.0F (nothing); maximum: 1.0F (like full sunlight)
    // enderchest:0.5F; torch:0.9375F; fire/glowstone:1.0F
    this.setLightLevel(0.0F);
    // sets the step sound of a block
    this.setStepSound(soundTypePiston);
    this.setUnlocalizedName(Ref.BlockID.VOIDSTONE);
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

}
