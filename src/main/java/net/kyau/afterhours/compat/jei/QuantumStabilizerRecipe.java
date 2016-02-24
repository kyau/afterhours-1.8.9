package net.kyau.afterhours.compat.jei;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

public class QuantumStabilizerRecipe extends BlankRecipeWrapper {

  @Nonnull
  private final List<List<ItemStack>> input;
  @Nonnull
  private final List<ItemStack> output;

  public QuantumStabilizerRecipe(@Nonnull List<ItemStack> input, @Nonnull ItemStack output) {
    this.input = Collections.singletonList(input);
    this.output = Collections.singletonList(output);
  }

  @Override
  public List getInputs() {
    return input;
  }

  @Override
  public List getOutputs() {
    return output;
  }
}
