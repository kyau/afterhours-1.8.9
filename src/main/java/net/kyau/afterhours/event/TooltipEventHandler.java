package net.kyau.afterhours.event;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.kyau.afterhours.init.ModItems;
import net.kyau.afterhours.items.darkmatter.DarkMatterAxe;
import net.kyau.afterhours.items.darkmatter.DarkMatterPickaxe;
import net.kyau.afterhours.items.darkmatter.DarkMatterShovel;
import net.kyau.afterhours.references.Ref;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;

public class TooltipEventHandler {

  @SubscribeEvent(priority = EventPriority.LOWEST)
  @SideOnly(Side.CLIENT)
  public void itemTooltip(ItemTooltipEvent event) {
    List<String> tooltip = event.toolTip;
    ItemStack stack = event.itemStack;
    // F3+H enabled, remove here then re-add to the bottom of the tooltip
    if (event.showAdvancedItemTooltips) {
      tooltip.removeIf(p -> {
        Pattern pattern = Pattern.compile("Durability: [0-9]+ / [0-9]+");
        Matcher m = pattern.matcher(p);
        if (m.find())
          return true;
        return false;
      });
      tooltip.removeIf(p -> p.contains(((ResourceLocation) Item.itemRegistry.getNameForObject(stack.getItem())).toString()));
      for (int i = tooltip.size() - 1; i >= 0; i--) {
        if (tooltip.get(i).equals("")) {
          tooltip.remove(i);
          break;
        }
      }
      if (stack.hasTagCompound()) {
        tooltip.removeIf(p -> p.contains("NBT: " + stack.getTagCompound().getKeySet().size() + " tag(s)"));
        for (int i = tooltip.size() - 1; i >= 0; i--) {
          if (tooltip.get(i).equals("")) {
            tooltip.remove(i);
            break;
          }
        }
      }
    }
    // Rephrase the enchanting tooltip for WAWLA
    String sneakLine = StatCollector.translateToLocal(Ref.Translation.MORE_INFORMATION);
    // String sneakLine = EnumChatFormatting.GRAY + "Hold " + EnumChatFormatting.WHITE + "SHIFT" +
    // EnumChatFormatting.GRAY + " for more information.";
    Collections.replaceAll(tooltip, "Hold the Sneak key for Enchantment Description", sneakLine);

    // F3+H is disabled, show Item IDs anyway
    if (!(event.showAdvancedItemTooltips)) {
      String id = String.format("#%04d", Item.getIdFromItem(stack.getItem()));
      String name = stack.getDisplayName() + EnumChatFormatting.GRAY + " (" + id + ")";
      tooltip.set(0, name);
    }

    // Burn time
    if (!(TileEntityFurnace.getItemBurnTime(stack) == 0)) {
      String msg = "Burn Time: " + TileEntityFurnace.getItemBurnTime(stack);
      if (!(tooltip.contains(msg))) {
        tooltip.add(EnumChatFormatting.GRAY + msg);
      }
    }

    // Weapons and tools
    if (ModItems.repairList.contains(stack.getUnlocalizedName()) || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool) {
      String toolClass;
      // LogHelper.info(stack.getItem().getToolClasses(stack));
      if (stack.getItem() instanceof DarkMatterPickaxe || (stack.getItem() instanceof ItemTool && stack.getItem().getToolClasses(stack).contains("pickaxe"))) {
        toolClass = "pickaxe";
      } else if (stack.getItem() instanceof DarkMatterAxe || (stack.getItem() instanceof ItemTool && stack.getItem().getToolClasses(stack).contains("axe"))) {
        toolClass = "axe";
      } else if (stack.getItem() instanceof DarkMatterShovel || (stack.getItem() instanceof ItemTool && stack.getItem().getToolClasses(stack).contains("shovel"))) {
        toolClass = "shovel";
      } else {
        toolClass = "damage";
      }
      // LogHelper.info(stack.getItem().getHarvestLevel(stack, "pickaxe"));
      tooltip.removeIf(p -> p.contains("Attack Damage"));
      for (int i = tooltip.size() - 1; i >= 0; i--) {
        if (tooltip.get(i).equals("")) {
          tooltip.remove(i);
          break;
        }
      }

      // Description
      double damage = 0;
      Multimap<String, AttributeModifier> multimap = stack.getAttributeModifiers();
      if (multimap.containsKey(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName())) {
        if (multimap.get(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName()).toArray().length > 0) {
          if (multimap.get(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName()).toArray()[0] instanceof AttributeModifier) {
            AttributeModifier weaponModifier = (AttributeModifier) multimap.get(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName()).toArray()[0];
            damage = weaponModifier.getAmount();
            damage = (double) Math.round(damage * 100d) / 100d;
          }
        }
      }
      if (Keyboard.isKeyDown(0x2A) || Keyboard.isKeyDown(0x36)) {
        float speed = 0.0F;
        if (stack.getItem().getToolClasses(stack).contains("pickaxe")) {
          speed = stack.getItem().getStrVsBlock(stack, Blocks.stone);
          switch (stack.getItem().getHarvestLevel(stack, toolClass)) {
            case 4:
              tooltip.add(EnumChatFormatting.GRAY + "Mining Level: " + EnumChatFormatting.BLUE + "Dark Matter");
              break;
            case 3:
              tooltip.add(EnumChatFormatting.GRAY + "Mining Level: " + EnumChatFormatting.DARK_PURPLE + "Obsidian");
              break;
            case 2:
              tooltip.add(EnumChatFormatting.GRAY + "Mining Level: " + EnumChatFormatting.AQUA + "Diamond");
              break;
            case 1:
              tooltip.add(EnumChatFormatting.GRAY + "Mining Level: " + EnumChatFormatting.DARK_RED + "Iron");
              break;
            case 0:
              tooltip.add(EnumChatFormatting.GRAY + "Mining Level: " + EnumChatFormatting.GRAY + "Stone");
              break;
            default:
              tooltip.add(EnumChatFormatting.GRAY + "Mining Level: " + EnumChatFormatting.RED + "Unknown");
          }
        } else if (toolClass.contains("shovel")) {
          speed = stack.getItem().getStrVsBlock(stack, Blocks.dirt);
        } else if (toolClass.contains("axe")) {
          speed = stack.getItem().getStrVsBlock(stack, Blocks.log);
        }

        tooltip.add(EnumChatFormatting.GRAY + "Damage: " + EnumChatFormatting.DARK_RED + damage);

        tooltip.add(EnumChatFormatting.GRAY + "Durability: " + (stack.getMaxDamage() - stack.getItemDamage()) + "/" + stack.getMaxDamage());
        if (!(speed == 0.0F))
          tooltip.add(EnumChatFormatting.GRAY + "Speed: " + EnumChatFormatting.DARK_GREEN + (double) Math.round(((double) speed / 4.0F) * 100d) / 100d);
      } else {
        tooltip.add(StatCollector.translateToLocal(Ref.Translation.MORE_INFORMATION));
      }
    } else if (stack.isItemStackDamageable()) {
      tooltip.add(EnumChatFormatting.GRAY + "Durability: " + (stack.getMaxDamage() - stack.getItemDamage()) + "/" + stack.getMaxDamage());
    }
    // armor
    if (stack.getItem() instanceof ItemArmor) {
      ItemArmor armor = (ItemArmor) stack.getItem();
      if (Loader.isModLoaded("wawla")) {
        tooltip.removeIf(p -> p.contains("Protection: " + armor.damageReduceAmount));
        for (int i = tooltip.size() - 1; i >= 0; i--) {
          if (tooltip.get(i).equals("")) {
            tooltip.remove(i);
            break;
          }
        }
      }
      tooltip.add("Protection: " + armor.damageReduceAmount);
    }
    String modString = nameFromStack(stack);
    if (Loader.isModLoaded("Waila")) {
      tooltip.removeIf(p -> p.equals("§9§o" + modString));
      for (int i = tooltip.size() - 1; i >= 0; i--) {
        if (tooltip.get(i).equals("")) {
          tooltip.remove(i);
          break;
        }
      }
    }
    String meta = "";
    if (stack.getHasSubtypes()) {
      meta = ":" + stack.getMetadata();
    }
    tooltip.add(EnumChatFormatting.DARK_GRAY + "<" + ((ResourceLocation) Item.itemRegistry.getNameForObject(stack.getItem())).toString() + meta + ">");
    if (stack.hasTagCompound()) {
      tooltip.add(EnumChatFormatting.DARK_GRAY + "NBT: " + stack.getTagCompound().getKeySet().size() + " tag(s)");
    }
    tooltip.add("\u00a79\u00a7o" + modString);
  }

  public static String nameFromStack(ItemStack stack) {
    try {
      ResourceLocation resource = (ResourceLocation) GameData.getItemRegistry().getNameForObject(stack.getItem());
      ModContainer mod = null;
      for (ModContainer mc : Loader.instance().getModList())
        if (resource.getResourceDomain().toLowerCase(Locale.US).equals(mc.getModId().toLowerCase(Locale.US)))
          mod = mc;
      String modName = mod == null ? "Minecraft" : mod.getName();
      return modName;
    } catch (NullPointerException e) {
      return "";
    }
  }
}
