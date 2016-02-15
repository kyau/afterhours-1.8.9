package net.kyau.afterhours.references;

public class Ref {

  public static final class BlockID {

    public static final String DARKMATTERCLUSTER = "darkmattercluster";
    public static final String VOIDSTONE = "voidstone";
    public static final String INFUSED_VOIDSTONE = "infused_voidstone";
  }

  public static final class ItemID {

    public static final String ANTENNA = "antenna";
    public static final String DARKMATTER = "darkmatter";
    public static final String DARKMATTER_SWORD = "darkmatter_sword";
    public static final String DOUGH = "dough";
    public static final String RAWHIDE = "rawhide";
    public static final String SINGULARITY = "singularity";
    public static final String STABLECORE = "stablecore";
    public static final String UNSTABLECORE = "unstablecore";
    public static final String VOIDCRYSTAL = "voidcrystal";
    public static final String VOIDJOURNAL = "voidjournal";
    public static final String VOIDPEARL = "voidpearl";
    public static final String VOIDWELL = "voidwell";
    public static final String VRD = "vrd";
  }

  public static final class ItemStat {

    public static final String BOUND = "Imprinted";
    public static final String LIMITED = "Limited";
  }

  public static final class NBT {

    public static final String DISPLAY = "display";
    public static final String ENERGY_LEVEL = "energyLevel";
    public static final String ENERGY_MAX = "energyMax";
    public static final String ITEMS = "Items";
    public static final String ITEMS_MOD = "ItemsMod";
    public static final String MODE = "mode";
    public static final String OWNER = "Owner";
    public static final String LASTUSE = "LastUse";
    public static final String OWNER_UUID_MOST_SIG = "ownerUUIDMostSig";
    public static final String OWNER_UUID_LEAST_SIG = "ownerUUIDLeastSig";
    public static final String UUID_MOST_SIG = "UUIDMostSig";
    public static final String UUID_LEAST_SIG = "UUIDLeastSig";
    public static final String VRD_GUI_OPEN = "vrdGuiOpen";
  }

  public static final class Containers {

    public static final String INVENTORY = ModInfo.MOD_ID + ".container:inventory";
    public static final String VRD_MAIN = ModInfo.MOD_ID + ".container:" + ItemID.VRD + "Main";
    public static final String VRD_VOID = ModInfo.MOD_ID + ".container:" + ItemID.VRD + "Void";
    public static final int VRD_MAIN_ROWS = 5;
    public static final int VRD_MAIN_COLS = 9;
    public static final String VOID_SYNC_TRUE = "Sync";
    public static final String VOID_SYNC_FALSE = "";
  }

  public static final class Dimension {

    public static final int DIM = 5;
    public static final int BIOMEID = 222;
    public static final String INTERNAL_NAME = "void";
  }
}
