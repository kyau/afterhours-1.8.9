package net.kyau.afterhours.init;

import net.kyau.afterhours.items.Dough;
import net.kyau.afterhours.items.RawHide;
import net.kyau.afterhours.references.Ref;
import net.minecraft.item.Item;

public class ModVanilla {

  public static Item dough;
  public static Item rawhide;

  public static void registerItems() {
    dough = new Dough().register(Ref.ItemID.DOUGH);
    rawhide = new RawHide().register(Ref.ItemID.RAWHIDE);
  }

  public static void registerRenders() {
    // dough.registerRender(Ref.ItemID.DOUGH);
    // rawhide.registerRender(Ref.ItemID.RAWHIDE);
  }
}
