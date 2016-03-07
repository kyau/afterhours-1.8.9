package net.kyau.afterhours.compat.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.Translator;
import net.kyau.afterhours.references.ModInfo;
import net.kyau.afterhours.references.Ref;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class QuantumReciprocatorRecipeCategory implements IRecipeCategory {

  public static final String UID = ModInfo.MOD_ID + "." + Ref.BlockID.QUANTUM_RECIPROCATOR;

  private static final int inputSlot = 0;
  private static final int outputSlot = 1;

  private final String localizedName;
  private final IDrawableStatic background;
  // private final IDrawableAnimated flame;
  private final IDrawableAnimated arrow;

  public QuantumReciprocatorRecipeCategory(IGuiHelper guiHelper) {
    localizedName = Translator.translateToLocal("jei.afterhours.quantumReciprocator");

    ResourceLocation location = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/quantum_reciprocator.png");
    background = guiHelper.createDrawable(location, 35, 21, 102, 43);

    // IDrawableStatic flameDrawable = guiHelper.createDrawable(location, 176, 0, 14, 14);
    // flame = guiHelper.createAnimatedDrawable(flameDrawable, 300, IDrawableAnimated.StartDirection.TOP, true);

    IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, 176, 0, 24, 17);
    arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
  }

  @Nonnull
  @Override
  public String getUid() {
    return UID;
  }

  @Nonnull
  @Override
  public String getTitle() {
    return localizedName;
  }

  @Nonnull
  @Override
  public IDrawable getBackground() {
    return background;
  }

  @Override
  public void drawExtras(Minecraft minecraft) {

  }

  @Override
  public void drawAnimations(Minecraft minecraft) {
    // flame.draw(minecraft, 2, 20);
    arrow.draw(minecraft, 40, 13);
  }

  @Override
  public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

    guiItemStacks.init(inputSlot, true, 9, 13);
    guiItemStacks.init(outputSlot, false, 80, 13);
    // List og_inputs = recipeWrapper.getInputs();
    // List<ItemStack> inputs = (List<ItemStack>) og_inputs.get(0);
    // List<ItemStack> input = new ArrayList<ItemStack>();
    // input.add((ItemStack) inputs);
    guiItemStacks.set(inputSlot, recipeWrapper.getInputs());
    guiItemStacks.setFromRecipe(outputSlot, recipeWrapper.getOutputs());
  }
}
