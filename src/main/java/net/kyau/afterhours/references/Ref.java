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
    public static final String DARKMATTER_SHOVEL = "darkmatter_shovel";
    public static final String DARKMATTER_PICKAXE = "darkmatter_pickaxe";
    public static final String DARKMATTER_AXE = "darkmatter_axe";
    public static final String DOUGH = "dough";
    public static final String QRD = "qrd";
    public static final String QUANTUMROD = "quantumrod";
    public static final String RAWHIDE = "rawhide";
    public static final String SINGULARITY = "singularity";
    public static final String STABLECORE = "stablecore";
    public static final String UNSTABLECORE = "unstablecore";
    public static final String VOIDCRYSTAL = "voidcrystal";
    public static final String VOIDJOURNAL = "voidjournal";
    public static final String VOIDPEARL = "voidpearl";
    public static final String WORMHOLE_MANIPULATOR = "wormhole_manipulator";
  }

  public static final class ItemCooldown {

    public static final int VOIDPEARL = 12000; // 10 minutes
    public static final int WORMHOLE_MANIPULATOR = 160; // 8 seconds
  }

  public static final class ItemStat {

    public static final String IMPRINTED = "Imprinted";
    public static final String LIMITED = "Limited";
    public static final int ENERGY_WORMHOLE_MANIPULATOR = 100;
    public static final int ENERGYONUSE_WORMHOLE_MANIPULATOR = 10;
    public static final int WORMHOLE_MANIPULATOR_PERTICK = 2;
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
    public static final String QRD_GUI_OPEN = "qrdGuiOpen";
  }

  public static final class Containers {

    public static final String INVENTORY = ModInfo.MOD_ID + ".container:inventory";
    public static final String QRD_MAIN = ModInfo.MOD_ID + ".container:" + ItemID.QRD + "Main";
    public static final String QRD_VOID = ModInfo.MOD_ID + ".container:" + ItemID.QRD + "Void";
    public static final int QRD_MAIN_ROWS = 5;
    public static final int QRD_MAIN_COLS = 9;
    public static final String VOID_SYNC_TRUE = "Sync";
    public static final String VOID_SYNC_FALSE = "";
  }

  public static final class Dimension {

    public static final int DIM = 5;
    public static final int BIOMEID = 222;
    public static final String INTERNAL_NAME = "void";
  }

  public static final class DarkMatter {

    public static final int DURABILITY = 2431;
    public static final int HARVEST_LEVEL = 4;
    public static final float EFFICIENCY = 50.0F;
    public static final float DAMAGE = 9.42F;
    public static final String NAME = "Dark Matter";
  }

  public static final class Enchant {

    public static final String ENTANGLEMENT = "Entanglement";
  }

  public static final class Translation {

    public static final String MORE_INFORMATION = "afterhours.msg.more_information";
    public static final String IMPRINT_SUCCESS = "afterhours.msg.imprint_success";
    public static final String IMPRINT_SCAN_FAILED = "afterhours.msg.imprint_scan_failed";
    public static final String INVALID_DIM = "afterhours.msg.invalid_dimension";
    public static final String PREIMPRINT = "afterhours.msg.preimprint";
    public static final String IMPRINTED = "afterhours.itemstat.imprinted";
    public static final String COOLDOWN = "afterhours.itemstat.cooldown";
    public static final String OWNER = "afterhours.itemstat.owner";
    public static final String ENERGY = "afterhours.itemstat.energy";
  }
}
