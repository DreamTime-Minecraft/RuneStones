package ru.ibusewinner.fundaily.runestones.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import ru.ibusewinner.fundaily.runestones.Objects.Rarity;
import ru.ibusewinner.fundaily.runestones.Objects.Rune;
import ru.ibusewinner.fundaily.runestones.Objects.RunePlayer;
import ru.ibusewinner.fundaily.runestones.RuneStone;
import ru.ibusewinner.fundaily.runestones.Utils.XMaterial;
import ru.ibusewinner.fundaily.runestones.Utils.ezItem;
import ru.ibusewinner.fundaily.runestones.commands.RuneGUI;
import ru.ibusewinner.fundaily.runestones.commands.RuneShop;

public class InventoryClick implements Listener {
    @EventHandler
    public void onClick(final InventoryClickEvent inventoryClickEvent) {
        final Player player = (Player)inventoryClickEvent.getWhoClicked();
        final ItemStack currentItem = inventoryClickEvent.getCurrentItem();
        if (currentItem == null || currentItem.getType() == Material.AIR) {
            return;
        }
        if (ChatColor.stripColor(inventoryClickEvent.getView().getTitle()).equalsIgnoreCase(ChatColor.stripColor(RuneStone.Format(RuneStone.msg.getString("Main-Inventory-Name"))))) {
            if (inventoryClickEvent.getRawSlot() >= inventoryClickEvent.getView().getTopInventory().getSize()) {
                return;
            }
            inventoryClickEvent.setCancelled(true);
            final RunePlayer runePlayer = RuneStone.getRunePlayer(player);
            if (runePlayer == null) {
                return;
            }
            if (currentItem.getType() == Material.EMERALD) {
                RuneGUI.openRuneGUI(runePlayer);
            }
            else if (currentItem.getType() == Material.PRISMARINE_SHARD) {
                RuneGUI.openShardGUI(runePlayer);
            }
            else if (currentItem.getType() == Material.GOLD_INGOT) {
                RuneShop.openGUI(player, 10, 1);
            }
        }
        else if (ChatColor.stripColor(inventoryClickEvent.getView().getTitle()).equalsIgnoreCase(ChatColor.stripColor(RuneStone.Format(RuneStone.msg.getString("Shard-Menu-Inventory-Name"))))) {
            if (inventoryClickEvent.getRawSlot() >= inventoryClickEvent.getView().getTopInventory().getSize()) {
                return;
            }
            inventoryClickEvent.setCancelled(true);
            final RunePlayer runePlayer2 = RuneStone.getRunePlayer(player);
            if (runePlayer2 == null) {
                return;
            }
            if (currentItem.getType() == Material.ANVIL) {
                RuneGUI.openForgeGUI(runePlayer2);
            }
            else if (currentItem.getType() == Material.PRISMARINE_SHARD) {
                RuneGUI.openShatterGUI(runePlayer2);
            }
            else if (currentItem.getType().toString().contains("DIODE")) {
                RuneGUI.openMainGUI(runePlayer2);
            }
        }
        else if (ChatColor.stripColor(inventoryClickEvent.getView().getTitle()).equalsIgnoreCase(ChatColor.stripColor(RuneStone.Format(RuneStone.msg.getString("Shatter-Rune-Inventory-Name"))))) {
            inventoryClickEvent.setCancelled(true);
            final RunePlayer runePlayer3 = RuneStone.getRunePlayer(player);
            if (runePlayer3 == null) {
                return;
            }
            if (currentItem != null && currentItem.getType() == XMaterial.EMERALD.parseMaterial() && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName()) {
                if (inventoryClickEvent.getInventory().getItem(13).getType() != XMaterial.BARRIER.parseMaterial()) {
                    return;
                }
                final Rune rune = RuneStone.getRune(ChatColor.stripColor(currentItem.getItemMeta().getDisplayName().substring(0, currentItem.getItemMeta().getDisplayName().length() - 9)));
                if (rune == null) {
                    return;
                }
                final int shardValue = rune.getRarity().getShardValue();
                if (currentItem.getAmount() != 1) {
                    currentItem.setAmount(currentItem.getAmount() - 1);
                }
                else {
                    player.getInventory().remove(currentItem);
                }
                inventoryClickEvent.getInventory().setItem(13, rune.asItem());
                inventoryClickEvent.getInventory().setItem(16, new ezItem(XMaterial.LIME_STAINED_GLASS_PANE).withDisplayName(RuneStone.Format("&a&lCONFIRM")).withLore(RuneStone.Format("\n&7Shatter the &n" + rune.getCustomName() + "&r&7 rune\n&7" + "to receive &a" + shardValue + " " + rune.getRarity().getColor() + rune.getRarity().toString() + " &r&7shard(s)?\n")).get());
                inventoryClickEvent.getInventory().setItem(10, new ezItem(XMaterial.RED_STAINED_GLASS_PANE).withDisplayName(RuneStone.Format("&c&lCANCEL")).withLore(RuneStone.Format("\n&7Return the &n" + rune.getCustomName() + "&r&7 back\n&7to your inventory.")).get());
                player.updateInventory();
            }
            if (currentItem.getType().toString().contains("DIODE")) {
                final ItemStack item = inventoryClickEvent.getInventory().getItem(13);
                if (item != null && item.getType() == Material.EMERALD && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                    RuneGUI.cancelShatter(player, inventoryClickEvent.getInventory(), RuneStone.getRune(ChatColor.stripColor(item.getItemMeta().getDisplayName().substring(0, item.getItemMeta().getDisplayName().length() - 5))));
                }
                RuneGUI.openShardGUI(runePlayer3);
                return;
            }
            if (currentItem.getType() == XMaterial.RED_STAINED_GLASS_PANE.parseMaterial() || currentItem.getType() == XMaterial.LIME_STAINED_GLASS_PANE.parseMaterial()) {
                final ItemStack item2 = inventoryClickEvent.getInventory().getItem(13);
                if (item2 != null && item2.getType() == Material.EMERALD && item2.hasItemMeta() && item2.getItemMeta().hasDisplayName()) {
                    final Rune rune2 = RuneStone.getRune(ChatColor.stripColor(item2.getItemMeta().getDisplayName().substring(0, item2.getItemMeta().getDisplayName().length() - 9)));
                    if (rune2 == null) {
                        return;
                    }
                    if (currentItem.getDurability() == XMaterial.RED_STAINED_GLASS_PANE.getData()) {
                        RuneGUI.cancelShatter(player, inventoryClickEvent.getInventory(), rune2);
                    }
                    if (currentItem.getDurability() == XMaterial.LIME_STAINED_GLASS_PANE.getData()) {
                        inventoryClickEvent.getInventory().setItem(13, new ezItem(XMaterial.BARRIER).withDisplayName(RuneStone.Format("&c&lX")).get());
                        inventoryClickEvent.getInventory().setItem(16, new ezItem(XMaterial.PURPLE_STAINED_GLASS_PANE).withDisplayName(" ").get());
                        inventoryClickEvent.getInventory().setItem(10, new ezItem(XMaterial.MAGENTA_STAINED_GLASS_PANE).withDisplayName(" ").get());
                        player.updateInventory();
                        runePlayer3.addShards(rune2.getRarity(), rune2.getRarity().getShardValue());
                        player.sendMessage(RuneStone.Format(RuneStone.msg.getString("Successful-Shatter").replaceAll("%rune%", rune2.getCustomName())));
                        player.sendMessage(RuneStone.Format("&a>&7 +" + rune2.getRarity().getShardValue() + " " + rune2.getRarity().getColor() + rune2.getRarity().toString() + " &7shard(s)!"));
                    }
                }
            }
        }
        else if (ChatColor.stripColor(inventoryClickEvent.getView().getTitle()).equalsIgnoreCase(ChatColor.stripColor(RuneStone.Format(RuneStone.msg.getString("Forge-Inventory-Name"))))) {
            inventoryClickEvent.setCancelled(true);
            if (currentItem.getType().toString().contains("STAINED")) {
                return;
            }
            final RunePlayer runePlayer4 = RuneStone.getRunePlayer(player);
            if (runePlayer4 == null) {
                return;
            }
            if (currentItem.getType().toString().contains("DIODE")) {
                RuneGUI.openShardGUI(runePlayer4);
                return;
            }
            if (currentItem != null && currentItem.getType() == Material.ENDER_CHEST && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName()) {
                runePlayer4.collectForge(RuneStone.getRune(ChatColor.stripColor(currentItem.getItemMeta().getDisplayName())));
            }
            if (currentItem != null && currentItem.getType() == Material.EMERALD && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName()) {
                final String stripColor = ChatColor.stripColor(currentItem.getItemMeta().getDisplayName());
                if (runePlayer4.getForging().values().size() >= runePlayer4.getForgeSlots()) {
                    player.sendMessage(RuneStone.Format(RuneStone.msg.getString("No-More-Forge-Slots")));
                    return;
                }
                Rarity rarity;
                if (stripColor.contains("LEGENDARY")) {
                    rarity = Rarity.LEGENDARY;
                }
                else if (stripColor.contains("UNIQUE")) {
                    rarity = Rarity.UNIQUE;
                }
                else if (stripColor.contains("EPIC")) {
                    rarity = Rarity.EPIC;
                }
                else {
                    if (!stripColor.contains("NORMAL")) {
                        return;
                    }
                    rarity = Rarity.NORMAL;
                }
                if (rarity != null) {
                    if (runePlayer4.getShards(rarity) < rarity.getForgeCost()) {
                        player.sendMessage(RuneStone.Format(RuneStone.msg.getString("Not-Enough-Shards")));
                        return;
                    }
                    Rune randomRune;
                    do {
                        randomRune = RuneStone.getRandomRune(rarity);
                    } while (runePlayer4.getForging().containsKey(randomRune));
                    runePlayer4.forge(randomRune);
                }
            }
        }
        else {
            if (ChatColor.stripColor(inventoryClickEvent.getView().getTitle()).equalsIgnoreCase(ChatColor.stripColor(RuneStone.Format(RuneStone.msg.getString("Rune-Inventory-Name"))))) {
                inventoryClickEvent.setCancelled(true);
                if (currentItem != null && currentItem.getType() == Material.EMERALD && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName() && currentItem.hasItemFlag(ItemFlag.HIDE_ENCHANTS) && currentItem.getItemMeta().hasEnchant(Enchantment.LUCK)) {
                    final Rune rune3 = RuneStone.getRune(ChatColor.stripColor(currentItem.getItemMeta().getDisplayName().substring(0, currentItem.getItemMeta().getDisplayName().length() - 9)));
                    if (rune3 == null) {
                        return;
                    }
                    final RunePlayer runePlayer5 = RuneStone.getRunePlayer(player);
                    if (runePlayer5 != null) {
                        inventoryClickEvent.setCancelled(true);
                        if (runePlayer5.isActive(rune3)) {
                            if (inventoryClickEvent.getRawSlot() >= inventoryClickEvent.getView().getTopInventory().getSize()) {
                                player.sendMessage(RuneStone.Format(RuneStone.msg.getString("Already-Active").replaceAll("%rune%", rune3.getCustomName())));
                                return;
                            }
                            if (player.getInventory().firstEmpty() == -1) {
                                player.sendMessage(RuneStone.Format(RuneStone.msg.getString("Full-Inventory")));
                                return;
                            }
                            runePlayer5.deactivate(rune3, false);
                        }
                        else {
                            runePlayer5.activate(rune3, currentItem, true);
                        }
                    }
                }
                else if (currentItem.getType().toString().contains("DIODE")) {
                    final RunePlayer runePlayer6 = RuneStone.getRunePlayer(player);
                    if (runePlayer6 != null) {
                        RuneGUI.openMainGUI(runePlayer6);
                    }
                }
                return;
            }
            final RunePlayer runePlayer7 = RuneStone.getRunePlayer(player);
            if (runePlayer7 != null && runePlayer7.getRuneBagInv() != null && runePlayer7.getRuneBagInv().equals(inventoryClickEvent.getInventory())) {
                if (inventoryClickEvent.getRawSlot() >= inventoryClickEvent.getView().getTopInventory().getSize()) {
                    if(!inventoryClickEvent.getClick().equals(ClickType.NUMBER_KEY)) {
                        if (currentItem == null || currentItem.getType() != Material.EMERALD || !currentItem.hasItemMeta() || !currentItem.getItemMeta().hasDisplayName() || !currentItem.hasItemFlag(ItemFlag.HIDE_ENCHANTS) || !currentItem.getItemMeta().hasEnchant(Enchantment.LUCK)) {
                            inventoryClickEvent.setCancelled(true);
                            player.sendMessage(RuneStone.Format(RuneStone.msg.getString("Rune-Bag-Error")));
                            return;
                        }
                        if (RuneStone.getRune(ChatColor.stripColor(currentItem.getItemMeta().getDisplayName().substring(0, currentItem.getItemMeta().getDisplayName().length() - 9))) == null) {
                            inventoryClickEvent.setCancelled(true);
                            player.sendMessage(RuneStone.Format(RuneStone.msg.getString("Rune-Bag-Error")));
                        }
                    }
                }
                return;
            }
            /*if (inventoryClickEvent.getClick() == ClickType.SHIFT_LEFT && inventoryClickEvent.getRawSlot() >= inventoryClickEvent.getView().getTopInventory().getSize() && currentItem != null && currentItem.getType() == Material.EMERALD && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName() && currentItem.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
                final Rune rune4 = RuneStone.getRune(ChatColor.stripColor(currentItem.getItemMeta().getDisplayName().substring(0, currentItem.getItemMeta().getDisplayName().length() - 9)));
                if (rune4 == null) {
                    return;
                }
                final RunePlayer runePlayer8 = RuneStone.getRunePlayer(player);
                if (runePlayer8 != null) {
                    inventoryClickEvent.setCancelled(true);
                    if (runePlayer8.isActive(rune4)) {
                        player.sendMessage(RuneStone.Format(RuneStone.msg.getString("Already-Active").replaceAll("%rune%", rune4.getCustomName())));
                        return;
                    }
                    runePlayer8.activate(rune4, currentItem, false);
                }
            }*/
        }
    }

    @EventHandler
    public void onClose(final InventoryCloseEvent inventoryCloseEvent) {
        final Inventory inventory = inventoryCloseEvent.getInventory();
        final RunePlayer runePlayer = RuneStone.getRunePlayer((Player)inventoryCloseEvent.getPlayer());
        if (runePlayer != null && runePlayer.getRuneBagInv() != null && runePlayer.getRuneBagInv().equals(inventoryCloseEvent.getInventory())) {
            runePlayer.saveRuneBag(inventoryCloseEvent.getInventory());
        }
        if (ChatColor.stripColor(inventoryCloseEvent.getView().getTitle()).equalsIgnoreCase(ChatColor.stripColor(RuneStone.Format(RuneStone.msg.getString("Shatter-Rune-Inventory-Name"))))) {
            final ItemStack item = inventory.getItem(13);
            if (item != null && item.getType() == Material.EMERALD && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                RuneGUI.cancelShatter((Player)inventoryCloseEvent.getPlayer(), inventoryCloseEvent.getInventory(), RuneStone.getRune(ChatColor.stripColor(item.getItemMeta().getDisplayName().substring(0, item.getItemMeta().getDisplayName().length() - 5))));
            }
        }
    }
}
