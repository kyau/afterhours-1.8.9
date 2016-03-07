package net.kyau.afterhours.compat.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class QuantumReciprocatorRecipeHandler implements IRecipeHandler<QuantumReciprocatorRecipe> {

  @Nonnull
  @Override
  public Class<QuantumReciprocatorRecipe> getRecipeClass() {
    return QuantumReciprocatorRecipe.class;
  }

  @Nonnull
  @Override
  public String getRecipeCategoryUid() {
    return QuantumReciprocatorRecipeCategory.UID;
  }

  @Nonnull
  @Override
  public IRecipeWrapper getRecipeWrapper(@Nonnull QuantumReciprocatorRecipe recipe) {
    return recipe;
  }

  @Override
  public boolean isRecipeValid(@Nonnull QuantumReciprocatorRecipe recipe) {
    return recipe.getInputs().size() != 0 && recipe.getOutputs().size() > 0;
  }

}
