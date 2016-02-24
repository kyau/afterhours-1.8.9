package net.kyau.afterhours.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IItemRegistry;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {

  public static IJeiHelpers jeiHelpers;

  @Override
  public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers) {
    JEIPlugin.jeiHelpers = jeiHelpers;
  }

  @Override
  public void onItemRegistryAvailable(IItemRegistry itemRegistry) {
    // NOOP
  }

  @Override
  public void register(IModRegistry registry) {
    IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
    registry.addRecipeCategories(new QuantumStabilizerRecipeCategory(guiHelper));
    registry.addRecipeHandlers(new QuantumStabilizerRecipeHandler());
    registry.addRecipes(QuantumStabilizerRecipeMaker.getRecipes(jeiHelpers));
  }

  @Override
  public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {
    // NOOP
  }

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
    // NOOP
  }

}
