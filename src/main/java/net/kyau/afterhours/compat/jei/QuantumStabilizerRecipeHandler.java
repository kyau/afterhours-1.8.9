package net.kyau.afterhours.compat.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class QuantumStabilizerRecipeHandler implements IRecipeHandler<QuantumStabilizerRecipe> {

  @Nonnull
  @Override
  public Class<QuantumStabilizerRecipe> getRecipeClass() {
    return QuantumStabilizerRecipe.class;
  }

  @Nonnull
  @Override
  public String getRecipeCategoryUid() {
    return QuantumStabilizerRecipeCategory.UID;
  }

  @Nonnull
  @Override
  public IRecipeWrapper getRecipeWrapper(@Nonnull QuantumStabilizerRecipe recipe) {
    return recipe;
  }

  @Override
  public boolean isRecipeValid(@Nonnull QuantumStabilizerRecipe recipe) {
    return recipe.getInputs().size() != 0 && recipe.getOutputs().size() > 0;
  }

}
