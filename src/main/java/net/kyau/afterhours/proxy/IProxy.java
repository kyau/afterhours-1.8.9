package net.kyau.afterhours.proxy;

import java.util.Random;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IProxy {

  public abstract ClientProxy getClientProxy();

  public abstract void registerEventHandlers();

  public abstract void registerClientEventHandlers();

  public abstract void registerKeybindings();

  public abstract void initRenderingAndTextures();

  public abstract boolean isSinglePlayer();

  public abstract void openJournal();

  public abstract void generateQuantumParticles(World world, BlockPos pos, Random rand);
}
