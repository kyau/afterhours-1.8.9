package net.kyau.afterhours.blocks;

import java.util.Random;

import net.kyau.afterhours.references.Ref;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class QuantumStabilizer extends BaseBlock {

  public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
  public static final PropertyBool ACTIVE = PropertyBool.create("active");
  private Random random = new Random();

  public QuantumStabilizer() {
    // tool: "pickaxe", "axe", "shovel"
    // level: 0=wood; 1=stone; 2=iron; 3=diamond tool
    this.setHarvestLevel("pickaxe", 2);
    // stone:1.5F; obsidian:50.0F
    this.setHardness(5.5F);
    // stone:10.0F; obsidian:2000.0F; bedrock:6000000.0F
    this.setResistance(10000.0F);
    // default: 16 (completely opaque); maximum: 0 (100% translucent)
    this.setLightOpacity(16);
    // default: 0.0F (nothing); maximum: 1.0F (like full sunlight)
    // enderchest:0.5F; torch:0.9375F; fire/glowstone:1.0F
    this.setLightLevel(0.0F);
    // sets the step sound of a block
    this.setStepSound(soundTypePiston);
    // sets the default facing direction
    this.setUnlocalizedName(Ref.BlockID.QUANTUM_STABILIZER);
    this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, Boolean.valueOf(false)));
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float var7, float var8, float var9) {
    IBlockState newState = state;
    if (player.isSneaking()) {
      newState = state.cycleProperty(FACING); // Cycle the facing (down -> up -> north -> south -> west -> east -> down)
    } else {
      if (!world.isRemote) {
        // player.openGui(AfterHours.instance, AfterHours.GUI_QS, player.worldObj, (int) player.posX, (int) player.posY,
        // (int) player.posZ);
      }
      newState = state.cycleProperty(ACTIVE); // Cycle the lit state (true -> false -> true)
      if (state.getValue(ACTIVE)) {
        this.setLightLevel(0.0F);
      } else {
        this.setLightLevel(0.5F);
      }
      world.markBlockForUpdate(pos);
    }
    world.setBlockState(pos, newState);
    return true;
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    int facingIndex = meta & 7; // Extract the facing index (lowest three bits)
    EnumFacing facing = EnumFacing.getFront(facingIndex); // Convert it to the corresponding EnumFacing
    boolean active = (meta & 8) != 0; // Extract the lit state (highest bit)

    return getDefaultState().withProperty(FACING, facing).withProperty(ACTIVE, active);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    /*
    EnumFacing facing = (EnumFacing) state.getValue(FACING);
    boolean type = (boolean) state.getValue(ACTIVE);

    int facingbits = facing.getHorizontalIndex();
    int typebits = (type ? 1 : 0) << 2;
    return facingbits | typebits;
    */
    int facingIndex = ((EnumFacing) state.getValue(FACING)).getIndex(); // Convert the EnumFacing to its index
    int active = (boolean) state.getValue(ACTIVE) ? 1 : 0; // Convert the lit state boolean to 1 or 0
    // Shift lit left three bits so it occupies the highest bit then OR it with the facing index (which occupies the
    // lowest three bits)
    return active << 3 | facingIndex;
  }

  @Override
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] {
        FACING,
        ACTIVE });
  }

  @Override
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    EnumFacing enumfacing = (placer == null) ? EnumFacing.NORTH : EnumFacing.fromAngle(placer.rotationYaw);
    return this.getDefaultState().withProperty(FACING, enumfacing.getOpposite()).withProperty(ACTIVE, false);
  }

  @Override
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
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand) {

    if (state.getValue(ACTIVE)) {
      float x1 = pos.getX() + random.nextFloat();
      float y1 = pos.getY() + 0.95F;
      float z1 = pos.getZ() + random.nextFloat();

      // world.spawnParticle(EnumParticleTypes.PORTAL, x1, y1, z1, 0.0D, 0.0D, 0.0D);
      world.spawnParticle(EnumParticleTypes.SPELL_MOB, x1, y1, z1, 0.0D, 0.0D, 0.0D, new int[0]);
      // LogHelper.info(this.blockState.getBaseState().getValue(PROPERTYACTIVE));
    }
  }

  @Override
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
  }

  public enum EnumActive implements IStringSerializable {
    FALSE(0, "false"),
    TRUE(1, "true");

    private int ID;
    private String name;

    private EnumActive(int ID, String name) {
      this.ID = ID;
      this.name = name;
    }

    @Override
    public String getName() {
      return name;
    }

    public int getID() {
      return ID;
    }

    @Override
    public String toString() {
      return getName();
    }
  }

}
