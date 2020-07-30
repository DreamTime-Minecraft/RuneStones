package ru.ibusewinner.fundaily.runestones.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import ru.ibusewinner.fundaily.runestones.Objects.Rarity;
import ru.ibusewinner.fundaily.runestones.Objects.Rune;
import ru.ibusewinner.fundaily.runestones.Objects.RunePlayer;
import ru.ibusewinner.fundaily.runestones.RuneStone;
import ru.ibusewinner.fundaily.runestones.Utils.XMaterial;
import ru.ibusewinner.fundaily.runestones.Utils.ezItem;

import java.util.ArrayList;
import java.util.Random;

public class RuneGUI implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (!command.getLabel().equalsIgnoreCase("artifact")) {
            return false;
        }
        if (!(commandSender instanceof Player)) {
            return false;
        }
        final Player player = (Player)commandSender;
        if (!player.hasPermission("runestones.use")) {
            player.sendMessage(RuneStone.Format(RuneStone.msg.getString("NoPermission")));
            return false;
        }
        final RunePlayer runePlayer = RuneStone.getRunePlayer(player);
        if (runePlayer == null) {
            player.sendMessage(RuneStone.Format("&c>&7 Sorry, an error occurred. Please relog."));
            return false;
        }
        openMainGUI(runePlayer);
        return false;
    }

    public static void openMainGUI(final RunePlayer runePlayer) {
        final Inventory inventory = Bukkit.createInventory(null, 27, RuneStone.Format(RuneStone.msg.getString("Main-Inventory-Name")));
        for (int i = 0; i < 27; ++i) {
            XMaterial xMaterial;
            if (new Random().nextInt(2) == 0) {
                xMaterial = XMaterial.PURPLE_STAINED_GLASS_PANE;
            }
            else {
                xMaterial = XMaterial.MAGENTA_STAINED_GLASS_PANE;
            }
            inventory.setItem(i, new ezItem(xMaterial).withDisplayName(" ").withLore(" ").get());
        }

        inventory.setItem(13, new ezItem(XMaterial.EMERALD).withDisplayName(RuneStone.Format(RuneStone.msg.getString("Rune-View-Item-Name"))).get());
        if(runePlayer.getPlayer().hasPermission("runestones.admin.shop")) {
            inventory.setItem(14, new ezItem(XMaterial.GOLD_INGOT).withDisplayName(RuneStone.Format(RuneStone.msg.getString("Rune-Shop-Item-Name"))).get());
        }
        runePlayer.getPlayer().openInventory(inventory);
    }

    public static void openShardGUI(final RunePlayer runePlayer) {
        if (!runePlayer.getPlayer().hasPermission("runestones.shards")) {
            runePlayer.getPlayer().sendMessage(RuneStone.Format(RuneStone.msg.getString("NoPermission")));
            return;
        }
        final Inventory inventory = Bukkit.createInventory(null, 27, RuneStone.Format(RuneStone.msg.getString("Shard-Menu-Inventory-Name")));
        for (int i = 0; i < 27; ++i) {
            XMaterial xMaterial;
            if (new Random().nextInt(2) == 0) {
                xMaterial = XMaterial.PURPLE_STAINED_GLASS_PANE;
            }
            else {
                xMaterial = XMaterial.MAGENTA_STAINED_GLASS_PANE;
            }
            inventory.setItem(i, new ezItem(xMaterial).withDisplayName(" ").withLore(" ").get());
        }
        inventory.setItem(11, new ezItem(XMaterial.ANVIL).withDisplayName(RuneStone.Format(RuneStone.msg.getString("Forge-Rune-Item-Name"))).withLore(RuneStone.Format("\n&a" + runePlayer.getShards(Rarity.NORMAL) + " " + Rarity.NORMAL.getColor() + Rarity.NORMAL.toString() + "\n&a" + runePlayer.getShards(Rarity.UNIQUE) + " " + Rarity.UNIQUE.getColor() + Rarity.UNIQUE.toString() + "\n&a" + runePlayer.getShards(Rarity.EPIC) + " " + Rarity.EPIC.getColor() + Rarity.EPIC.toString() + "\n&a" + runePlayer.getShards(Rarity.LEGENDARY) + " " + Rarity.LEGENDARY.getColor() + Rarity.LEGENDARY.toString())).get());
        inventory.setItem(15, new ezItem(XMaterial.PRISMARINE_SHARD).withDisplayName(RuneStone.Format(RuneStone.msg.getString("Shatter-Shard-Item-Name"))).get());
        runePlayer.getPlayer().openInventory(inventory);
    }

    public static void openShatterGUI(final RunePlayer runePlayer) {
        final Inventory inventory = Bukkit.createInventory(null, 27, RuneStone.Format(RuneStone.msg.getString("Shatter-Rune-Inventory-Name")));
        for (int i = 0; i < 27; ++i) {
            XMaterial xMaterial;
            if (new Random().nextInt(2) == 0) {
                xMaterial = XMaterial.PURPLE_STAINED_GLASS_PANE;
            }
            else {
                xMaterial = XMaterial.MAGENTA_STAINED_GLASS_PANE;
            }
            inventory.setItem(i, new ezItem(xMaterial).withDisplayName(" ").withLore(" ").get());
        }
        inventory.setItem(13, new ezItem(XMaterial.BARRIER).withDisplayName(RuneStone.Format("&c&lX")).get());
        runePlayer.getPlayer().openInventory(inventory);
    }

    public static void cancelShatter(final Player player, final Inventory inventory, final Rune rune) {
        player.getInventory().addItem(rune.asItem());
        inventory.setItem(13, new ezItem(XMaterial.BARRIER).withDisplayName(RuneStone.Format("&c&lX")).get());
        inventory.setItem(16, new ezItem(XMaterial.PURPLE_STAINED_GLASS_PANE).withDisplayName(" ").get());
        inventory.setItem(10, new ezItem(XMaterial.MAGENTA_STAINED_GLASS_PANE).withDisplayName(" ").get());
        player.updateInventory();
    }

    public static void openForgeGUI(final RunePlayer runePlayer) {
        final int slots = runePlayer.getSlots();
        final Inventory inventory = Bukkit.createInventory(null, 27, RuneStone.Format(RuneStone.msg.getString("Forge-Inventory-Name")));
        for (int i = 0; i < 27; ++i) {
            XMaterial xMaterial;
            if (new Random().nextInt(2) == 0) {
                xMaterial = XMaterial.PURPLE_STAINED_GLASS_PANE;
            }
            else {
                xMaterial = XMaterial.MAGENTA_STAINED_GLASS_PANE;
            }
            inventory.setItem(i, new ezItem(xMaterial).withDisplayName(" ").withLore(" ").get());
        }
        int n = 0;
        Rarity[] values;
        for (int length = (values = Rarity.values()).length, j = 0; j < length; ++j) {
            final Rarity rarity = values[j];
            final long forgeDelay = rarity.getForgeDelay();
            final long n2 = forgeDelay / 1000L / 60L / 60L;
            final long n3 = forgeDelay / 1000L / 60L % 60L;
            String str = "";
            if (n3 != 0L && n2 != 0L) {
                str = n2 + "h " + n3 + "m";
            }
            else if (n3 == 0L && n2 != 0L) {
                str = n2 + "h";
            }
            else if (n3 != 0L && n2 == 0L) {
                str = n3 + "m";
            }
            else if (n3 == 0L && n2 == 0L) {
                str = "instant";
            }
            inventory.setItem(10 + n, new ezItem(XMaterial.EMERALD).withDisplayName(RuneStone.Format("&7Forge " + rarity.getColor() + rarity.toString())).withLore(RuneStone.Format("\n&7> Click to begin forging.\n&7> &n" + str + "&r&7 forge length." + "\n&7> Costs " + rarity.getForgeCost() + " " + rarity.getColor() + rarity.toString() + " shard(s).")).get());
            n += 2;
        }
        new BukkitRunnable() {
            public void run() {
                int n = 0;
                for (int i = 1; i <= 3; ++i) {
                    if (slots >= i) {
                        final ArrayList<Rune> list = new ArrayList<>(runePlayer.getForging().keySet());
                        if (list.size() >= i) {
                            if (list.get(i - 1) != null) {
                                final Rune rune = list.get(i - 1);
                                if (runePlayer.isForged(rune)) {
                                    inventory.setItem(11 + n, new ezItem(XMaterial.ENDER_CHEST).withDisplayName(RuneStone.Format("&b" + rune.getCustomName())).withLore(RuneStone.Format("\n&bClick to claim!")).get());
                                }
                                else {
                                    final long timeLeft = runePlayer.getTimeLeft(rune);
                                    inventory.setItem(11 + n, new ezItem(XMaterial.CHEST).withDisplayName(RuneStone.Format(rune.getRarity().getColor() + "???")).withLore(RuneStone.Format("\n&7> " + (timeLeft / 1000L / 60L / 60L + "h " + timeLeft / 1000L / 60L % 60L + "m " + timeLeft / 1000L % 60L + "s") + " remaining" + "\n" + rune.getRarity().getColor() + rune.getRarity().toString())).get());
                                }
                            }
                            else {
                                inventory.setItem(11 + n, new ezItem(XMaterial.LIME_STAINED_GLASS_PANE).withDisplayName(RuneStone.Format("&aOpen Slot")).get());
                            }
                        }
                        else {
                            inventory.setItem(11 + n, new ezItem(XMaterial.LIME_STAINED_GLASS_PANE).withDisplayName(RuneStone.Format("&aOpen Slot")).get());
                        }
                    }
                    else {
                        inventory.setItem(11 + n, new ezItem(XMaterial.RED_STAINED_GLASS_PANE).withDisplayName(RuneStone.Format("&c&lX")).get());
                    }
                    n += 2;
                }
                if (!inventory.getViewers().contains(runePlayer.getPlayer())) {
                    this.cancel();
                }
            }
        }.runTaskTimer(RuneStone.plugin, 0L, 20L);
        runePlayer.getPlayer().openInventory(inventory);
    }

    public static void openRuneGUI(final RunePlayer runePlayer) {
        final int slots = runePlayer.getSlots();
        int n = 4;
        int n2 = 27;
        if (slots > 4) {
            n2 += 27;
            n = 8;
        }
        final Inventory inventory = Bukkit.createInventory(null, n2, RuneStone.Format(RuneStone.msg.getString("Rune-Inventory-Name")));
        for (int i = 0; i < n2; ++i) {
            XMaterial xMaterial;
            if (new Random().nextInt(2) == 0) {
                xMaterial = XMaterial.PURPLE_STAINED_GLASS_PANE;
            }
            else {
                xMaterial = XMaterial.MAGENTA_STAINED_GLASS_PANE;
            }
            inventory.setItem(i, new ezItem(xMaterial).withDisplayName(" ").withLore(" ").get());
        }
        final ItemStack value = new ezItem(XMaterial.LIME_STAINED_GLASS_PANE).withDisplayName(RuneStone.Format(RuneStone.msg.getString("Available-Rune-Item"))).get();
        final ItemStack value2 = new ezItem(XMaterial.RED_STAINED_GLASS_PANE).withDisplayName(RuneStone.Format(RuneStone.msg.getString("Unavailable-Rune-Item"))).get();
        int n3 = 1;
        for (int j = 1; j <= n; ++j) {
            Rune rune = null;
            if (j - 1 < runePlayer.getRunes().size()) {
                rune = runePlayer.getRunes().get(j - 1);
            }
            if (j <= slots) {
                inventory.setItem(n3, value);
                if (rune != null) {
                    ItemStack item = rune.asItem();
                    item.addUnsafeEnchantment(Enchantment.LUCK, 1);
                    item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    inventory.setItem(n3 + 9, rune.asItem());
                }
                else {
                    inventory.setItem(n3 + 9, new ezItem(XMaterial.BARRIER).withDisplayName(RuneStone.Format("&c&lX")).get());
                }
                inventory.setItem(n3 + 18, value);
            }
            else {
                inventory.setItem(n3, value2);
                inventory.setItem(n3 + 9, new ezItem(XMaterial.BARRIER).withDisplayName(RuneStone.Format("&c&lX")).get());
                inventory.setItem(n3 + 18, value2);
            }
            if (n3 == 7) {
                n3 = 28;
            }
            else {
                n3 += 2;
            }
        }
        runePlayer.getPlayer().openInventory(inventory);
    }
}
