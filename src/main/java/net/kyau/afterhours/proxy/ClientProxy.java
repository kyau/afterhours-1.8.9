package net.kyau.afterhours.proxy;

import java.util.Random;

import net.kyau.afterhours.client.gui.GuiHUD;
import net.kyau.afterhours.client.gui.GuiVoidJournal;
import net.kyau.afterhours.client.renderer.EntityParticleFXQuantum;
import net.kyau.afterhours.event.TooltipEventHandler;
import net.kyau.afterhours.init.ModBlocks;
import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.init.ModVanilla;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

  private static Minecraft minecraft = Minecraft.getMinecraft();
  private static long lastUseVoidPearl = 0;

  @Override
  public ClientProxy getClientProxy() {
    return this;
  }

  @Override
  public void registerEventHandlers() {
    super.registerEventHandlers();
    MinecraftForge.EVENT_BUS.register(new GuiHUD(minecraft));
    // LogManager.getLogger(ModInfo.MOD_ID).log(Level.INFO, "ClientProxy: registerEventHandlers() SUCCESS!");
  }

  @Override
  public void registerKeybindings() {

  }

  @Override
  public void initRenderingAndTextures() {
    ModItems.registerRenders();
    ModBlocks.registerRenders();
    ModVanilla.registerRenders();
  }

  @Override
  public boolean isSinglePlayer() {
    return Minecraft.getMinecraft().isSingleplayer();
  }

  @Override
  public void openJournal() {
    Minecraft.getMinecraft().displayGuiScreen(new GuiVoidJournal());
  }

  @Override
  public void registerClientEventHandlers() {
    MinecraftForge.EVENT_BUS.register(new TooltipEventHandler());
  }

  @Override
  public void generateQuantumParticles(World world, BlockPos pos, Random rand) {

    float motionX = (float) (rand.nextGaussian() * 0.02F);
    float motionY = (float) (rand.nextGaussian() * 0.02F);
    float motionZ = (float) (rand.nextGaussian() * 0.02F);
    // for (int i = 0; i < 2; i++) {
    EntityFX particleMysterious = new EntityParticleFXQuantum(world, pos.getX() + rand.nextFloat() * 0.7F + 0.2F, pos.getY() + 0.1F, pos.getZ() + rand.nextFloat() * 0.8F + 0.2F, motionX, motionY, motionZ);
    Minecraft.getMinecraft().effectRenderer.addEffect(particleMysterious);
    // }
  }

  @Override
  public long getVoidPearlLastUse() {
    return lastUseVoidPearl;
  }

  @Override
  public void setVoidPearlLastUse(long cooldown) {
    this.lastUseVoidPearl = cooldown;
  }

}
