package net.kyau.afterhours.dimension;

import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.minecraft.util.StatCollector;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.DimensionManager;

public class DimensionHandler {

  public static BiomeGenBase voidBiome;

  public static void init() {
    registerDimension();
    registerBiome();
  }

  private static void registerBiome() {
    voidBiome = new BiomeVoid(Ref.Dimension.BIOMEID).setColor(48).setBiomeName(StatCollector.translateToLocal(ModInfo.MOD_ID + ".biome.name")).setHeight(new BiomeGenBase.Height(0.0F, 0.0F));
  }

  private static void registerDimension() {
    DimensionManager.registerProviderType(Ref.Dimension.DIM, WorldProviderVoid.class, false);
    DimensionManager.registerDimension(Ref.Dimension.DIM, Ref.Dimension.DIM);
  }

}
