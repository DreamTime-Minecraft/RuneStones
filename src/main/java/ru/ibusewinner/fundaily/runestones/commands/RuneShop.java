package ru.ibusewinner.fundaily.runestones.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.ibusewinner.fundaily.runestones.Objects.Rune;
import ru.ibusewinner.fundaily.runestones.Objects.Types.Cosmetic;
import ru.ibusewinner.fundaily.runestones.Objects.Types.Custom;
import ru.ibusewinner.fundaily.runestones.Objects.Types.Effecting;
import ru.ibusewinner.fundaily.runestones.RuneStone;
import ru.ibusewinner.fundaily.runestones.Utils.XMaterial;
import ru.ibusewinner.fundaily.runestones.Utils.ezItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class RuneShop implements CommandExecutor, Listener {
    public static HashMap<UUID, Integer> shoppers;

    static {
        RuneShop.shoppers = new HashMap<>();
    }

    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (!(commandSender instanceof Player)) {
            return false;
        }
        final Player player = (Player)commandSender;
        if (!player.hasPermission("runestones.runeshop")) {
            player.sendMessage(RuneStone.Format(RuneStone.msg.getString("NoPermission")));
            return false;
        }
        openGUI(player, 10, 1);
        return false;
    }

    public static void openGUI(final Player player, final int n, final int i) {
        if (RuneStone.econ == null) {
            if (player.isOp()) {
                player.sendMessage(RuneStone.Format("&c>&7 Vault is required to use this feature. Is your economy setup properly?"));
            }
            else {
                player.sendMessage(RuneStone.Format("&c>&7 This feature is unavailable."));
            }
            return;
        }
        if (!player.hasPermission("runestones.runeshop")) {
            player.sendMessage(RuneStone.Format(RuneStone.msg.getString("NoPermission")));
            return;
        }
        final ArrayList<Rune> list = new ArrayList<>();
        for (final Rune rune : Rune.getAllRunes()) {
            if ((int)rune.getCost() != -1) {
                if (RuneStone.isHigherVersion(rune.getVersion(), RuneStone.serverVersion)) {
                    continue;
                }
                if (n == 1) {
                    if (!(rune instanceof Effecting)) {
                        continue;
                    }
                    list.add(rune);
                }
                else if (n == 2) {
                    if (!(rune instanceof Cosmetic)) {
                        continue;
                    }
                    list.add(rune);
                }
                else if (n == 3) {
                    if (!(rune instanceof Custom)) {
                        continue;
                    }
                    list.add(rune);
                }
                else {
                    list.add(rune);
                }
            }
        }
        final ArrayList<Inventory> list2 = new ArrayList<>();
        final double ceil = Math.ceil(list.size() * 1.0 / 27.0);
        if (i > ceil || i < 1) {
            return;
        }
        int n2 = 0;
        for (int n3 = 0; n3 < ceil; ++n3) {
            final Inventory inventory = Bukkit.createInventory(null, 36, RuneStone.Format(RuneStone.msg.getString("Rune-Shop-Name")));
            for (int j = 27; j <= 35; ++j) {
                inventory.setItem(j, new ezItem(XMaterial.BLACK_STAINED_GLASS_PANE).withDisplayName(" ").withLore(" ").get());
            }
            inventory.setItem(27, new ezItem(XMaterial.PAPER).withDisplayName(RuneStone.Format("&cPrevious Page")).get());
            inventory.setItem(35, new ezItem(XMaterial.PAPER).withDisplayName(RuneStone.Format("&aNext Page")).get());
            for (int k = 0; k < 27; ++k) {
                if (n2 < list.size()) {
                    final Rune rune2 = list.get(n2);
                    inventory.addItem(new ezItem(rune2.asItem()).withLore(RuneStone.Format(RuneStone.msg.getString("Rune-Item-Lore").replaceAll("%cost%", String.valueOf(rune2.getCost())).replaceAll("%rarity%", rune2.getRarity().getColor() + rune2.getRarity().toString()))).get());
                    ++n2;
                }
            }
            list2.add(inventory);
        }
        player.openInventory(list2.get(i - 1));
        RuneShop.shoppers.put(player.getUniqueId(), i);
    }

    @EventHandler
    public void shopClick(final InventoryClickEvent inventoryClickEvent) {
        final Player player = (Player)inventoryClickEvent.getWhoClicked();
        final ItemStack currentItem = inventoryClickEvent.getCurrentItem();
        if (currentItem != null && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName()) {
            final String stripColor = ChatColor.stripColor(currentItem.getItemMeta().getDisplayName());
            if (ChatColor.stripColor(inventoryClickEvent.getView().getTitle()).equalsIgnoreCase(ChatColor.stripColor(RuneStone.Format(RuneStone.msg.getString("Rune-Shop-Name"))))) {
                inventoryClickEvent.setCancelled(true);
                if (currentItem.getType() == Material.PAPER) {
                    int n = -1;
                    for (int i = 0; i < inventoryClickEvent.getInventory().getSize(); ++i) {
                        if (inventoryClickEvent.getInventory().getItem(i) != null && inventoryClickEvent.getInventory().getItem(i).equals(currentItem)) {
                            n = i;
                        }
                    }
                    if (RuneShop.shoppers.containsKey(player.getUniqueId())) {
                        final int intValue = RuneShop.shoppers.get(player.getUniqueId());
                        if (n == 27) {
                            openGUI(player, 10, intValue - 1);
                        }
                        else if (n == 35) {
                            openGUI(player, 10, intValue + 1);
                        }
                    }
                }
                else if (currentItem.getType() == Material.EMERALD) {
                    final Rune rune = RuneStone.getRune(stripColor.substring(0, stripColor.length() - 9));
                    if (rune != null) {
                        if (RuneStone.econ.getBalance(player) < rune.getCost()) {
                            player.sendMessage(RuneStone.Format(RuneStone.msg.getString("Cannot-Afford-Rune")));
                            return;
                        }
                        if (player.getInventory().firstEmpty() == -1) {
                            player.sendMessage(RuneStone.Format(RuneStone.msg.getString("Full-Inventory")));
                            return;
                        }
                        final double cost = rune.getCost();
                        RuneStone.econ.withdrawPlayer(player, cost);
                        player.getInventory().addItem(rune.asItem());
                        player.sendMessage(RuneStone.Format(RuneStone.msg.getString("Bought-Rune").replaceAll("%rune%", rune.getCustomName()).replaceAll("%cost%", new StringBuilder(String.valueOf(cost)).toString())));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClose(final InventoryCloseEvent inventoryCloseEvent) {
        if (ChatColor.stripColor(inventoryCloseEvent.getView().getTitle()).equalsIgnoreCase(ChatColor.stripColor(RuneStone.Format(RuneStone.msg.getString("Rune-Shop-Name")))) && RuneShop.shoppers.containsKey(inventoryCloseEvent.getPlayer().getUniqueId())) {
            RuneShop.shoppers.remove(inventoryCloseEvent.getPlayer().getUniqueId());
        }
    }
}
