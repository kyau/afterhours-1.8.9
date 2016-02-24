package net.kyau.afterhours.compat.jei;

import java.util.ArrayList;
import java.util.List;

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
import net.kyau.afterhours.utils.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class QuantumStabilizerRecipeCategory implements IRecipeCategory {

  public static final String UID = "anotherdusts.crusher";

  private static final int fuelSlot = 0;
  private static final int inputSlot1 = 1;
  private static final int inputSlot2 = 2;
  private static final int outputSlot = 3;

  private final String localizedName;
  private final IDrawableStatic background;
  // private final IDrawableAnimated flame;
  private final IDrawableAnimated arrow;

  public QuantumStabilizerRecipeCategory(IGuiHelper guiHelper) {
    localizedName = Translator.translateToLocal("jei.afterhours.quantumStabilizer");

    ResourceLocation location = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/quantum_stabilizer.png");
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
    arrow.draw(minecraft, 44, 13);
  }

  @Override
  public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

    guiItemStacks.init(inputSlot1, true, 0, 0);
    guiItemStacks.init(inputSlot2, true, 18, 0);
    guiItemStacks.init(fuelSlot, true, 9, 25);
    guiItemStacks.init(outputSlot, false, 80, 13);
    LogHelper.info(recipeWrapper.getInputs().get(0));
    List<ItemStack> inputs = (List<ItemStack>) recipeWrapper.getInputs().get(0);
    List<ItemStack> fuel = new ArrayList<ItemStack>();
    List<ItemStack> input1 = new ArrayList<ItemStack>();
    List<ItemStack> input2 = new ArrayList<ItemStack>();
    LogHelper.info("Fuel: " + fuel + " / Input1: " + input1 + " / Input2: " + input2);
    fuel.add((ItemStack) inputs.toArray()[0]);
    input1.add((ItemStack) inputs.toArray()[1]);
    input2.add((ItemStack) inputs.toArray()[2]);
    guiItemStacks.set(inputSlot1, input1);
    guiItemStacks.set(inputSlot2, input2);
    guiItemStacks.set(fuelSlot, fuel);
    guiItemStacks.setFromRecipe(outputSlot, recipeWrapper.getOutputs());
  }

}
