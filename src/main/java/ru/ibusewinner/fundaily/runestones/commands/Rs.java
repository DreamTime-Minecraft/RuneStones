package ru.ibusewinner.fundaily.runestones.commands;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.ibusewinner.fundaily.runestones.Objects.Rarity;
import ru.ibusewinner.fundaily.runestones.Objects.Rune;
import ru.ibusewinner.fundaily.runestones.Objects.RunePlayer;
import ru.ibusewinner.fundaily.runestones.Objects.Types.Cosmetic;
import ru.ibusewinner.fundaily.runestones.Objects.Types.Custom;
import ru.ibusewinner.fundaily.runestones.Objects.Types.Effecting;
import ru.ibusewinner.fundaily.runestones.RuneStone;
import ru.ibusewinner.fundaily.runestones.Utils.CenteredMessage;
import ru.ibusewinner.fundaily.runestones.Utils.FileManager;
import ru.ibusewinner.fundaily.runestones.Utils.ezTextComponent;

import java.util.ArrayList;
import java.util.List;

public class Rs implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (commandSender instanceof Player && !commandSender.hasPermission("runestones.admin")) {
            commandSender.sendMessage(RuneStone.Format(RuneStone.msg.getString("NoPermission")));
            return false;
        }
        if (array.length == 0) {
            commandSender.sendMessage("");
            commandSender.sendMessage(RuneStone.Format(CenteredMessage.getCenteredMessage("&b{&m-+-&r&b< &aRuneStones Help &b>&m-+-&r&b}")));
            commandSender.sendMessage(RuneStone.Format("&a> &7/rs give <Игрок> <Количество> <Руна>"));
            commandSender.sendMessage(RuneStone.Format("&a> &7/rs list (<cosmetic/other/effecting>)"));
            commandSender.sendMessage(RuneStone.Format("&a> &7/rs shards give <Игрок> <Количество> <Редкость>"));
            commandSender.sendMessage(RuneStone.Format("&a> &7/rs shards take <Игрок> <Количество> <Редкость>"));
            commandSender.sendMessage(RuneStone.Format("&a> &7/rs shards balance <Игрок>"));
            commandSender.sendMessage(RuneStone.Format("&a> &7/rs reload"));
            commandSender.sendMessage(RuneStone.Format("&a> &7/rs open <Игрок>"));
            commandSender.sendMessage("");
            return false;
        }
        if (array.length == 5 && array[0].equalsIgnoreCase("shards")) {
            final Player player = Bukkit.getPlayer(array[2]);
            if (player == null) {
                commandSender.sendMessage(RuneStone.Format(RuneStone.msg.getString("OfflinePlayer")));
                return false;
            }
            final RunePlayer runePlayer = RuneStone.getRunePlayer(player);
            if (runePlayer == null) {
                return false;
            }
            int int1;
            try {
                int1 = Integer.parseInt(array[3]);
            }
            catch (NumberFormatException ex) {
                commandSender.sendMessage(RuneStone.Format("&c>&7 Please enter a proper amount."));
                return false;
            }
            Rarity value;
            try {
                value = Rarity.valueOf(array[4].toUpperCase());
            }
            catch (IllegalArgumentException ex2) {
                commandSender.sendMessage(RuneStone.Format("&c>&7 Invalid rarity. Options: [NORMAL, UNIQUE, EPIC, LEGENDARY]"));
                return false;
            }
            if (array[1].equalsIgnoreCase("give")) {
                runePlayer.addShards(value, int1);
                commandSender.sendMessage(RuneStone.Format(RuneStone.msg.getString("GiveShards").replaceAll("%player%", runePlayer.getPlayer().getName()).replaceAll("%rarity%", value.getColor() + value.toString() + "&r").replaceAll("%amount%", new StringBuilder(String.valueOf(int1)).toString())));
            }
            else if (array[1].equalsIgnoreCase("take")) {
                runePlayer.removeShards(value, int1);
                commandSender.sendMessage(RuneStone.Format(RuneStone.msg.getString("TakeShards").replaceAll("%player%", runePlayer.getPlayer().getName()).replaceAll("%rarity%", value.getColor() + value.toString() + "&r").replaceAll("%amount%", new StringBuilder(String.valueOf(int1)).toString())));
            }
        }
        if (array.length == 3 && ((array[0].equalsIgnoreCase("shards") && array[1].equalsIgnoreCase("balance")) || array[1].equalsIgnoreCase("bal"))) {
            final Player player2 = Bukkit.getPlayer(array[2]);
            if (player2 == null) {
                commandSender.sendMessage(RuneStone.Format(RuneStone.msg.getString("OfflinePlayer")));
                return false;
            }
            final RunePlayer runePlayer2 = RuneStone.getRunePlayer(player2);
            if (runePlayer2 != null) {
                commandSender.sendMessage(RuneStone.Format("&a>&7 Rune Shard Balance For &b" + runePlayer2.getPlayer().getName()));
                commandSender.sendMessage(RuneStone.Format(" &7> " + runePlayer2.getShards(Rarity.NORMAL) + Rarity.NORMAL.getColor() + " NORMAL"));
                commandSender.sendMessage(RuneStone.Format(" &7> " + runePlayer2.getShards(Rarity.UNIQUE) + Rarity.UNIQUE.getColor() + " UNIQUE"));
                commandSender.sendMessage(RuneStone.Format(" &7> " + runePlayer2.getShards(Rarity.EPIC) + Rarity.EPIC.getColor() + " EPIC"));
                commandSender.sendMessage(RuneStone.Format(" &7> " + runePlayer2.getShards(Rarity.LEGENDARY) + Rarity.LEGENDARY.getColor() + " LEGENDARY"));
            }
        }
        if (array.length == 1) {
            if (array[0].equalsIgnoreCase("give")) {
                commandSender.sendMessage(RuneStone.Format("&c> &7Usage: /rs give <Игрок> <Количество> <Руна>"));
                return false;
            }
            if (array[0].equalsIgnoreCase("reload")) {
                final long nanoTime = System.nanoTime();
                FileManager.reloadConfigs();
                commandSender.sendMessage(RuneStone.Format(RuneStone.msg.getString("ReloadFiles") + " &7(" + (System.nanoTime() - nanoTime) / 1000000L + "ms)"));
                return false;
            }
            if(array[0].equalsIgnoreCase("open")) {
                commandSender.sendMessage(RuneStone.Format("&c> &7Использование: /rs open <Игрок>"));
            }
        }
        if (array.length >= 1 && array.length <= 2 && array[0].equalsIgnoreCase("list")) {
            List<Rune> list = new ArrayList<>();
            commandSender.sendMessage("");
            commandSender.sendMessage(RuneStone.Format(CenteredMessage.getCenteredMessage("&b{&m-+-&r&b< &aRuneStones Library &b>&m-+-&r&b}")));
            if (array.length == 2) {
                if (array[1].equalsIgnoreCase("cosmetic")) {
                    commandSender.sendMessage(RuneStone.Format(CenteredMessage.getCenteredMessage("&a[Cosmetic]")));
                    for (final Rune rune : Rune.getAllRunes()) {
                        if (rune instanceof Cosmetic) {
                            list.add(rune);
                        }
                    }
                }
                else if (array[1].equalsIgnoreCase("effecting")) {
                    commandSender.sendMessage(RuneStone.Format(CenteredMessage.getCenteredMessage("&a[Effecting]")));
                    for (final Rune rune2 : Rune.getAllRunes()) {
                        if (rune2 instanceof Effecting) {
                            list.add(rune2);
                        }
                    }
                }
                else if (array[1].equalsIgnoreCase("other")) {
                    commandSender.sendMessage(RuneStone.Format(CenteredMessage.getCenteredMessage("&a[Other]")));
                    for (final Rune rune3 : Rune.getAllRunes()) {
                        if (rune3 instanceof Custom) {
                            list.add(rune3);
                        }
                    }
                }
                else {
                    list = Rune.getAllRunes();
                }
            }
            else {
                list = Rune.getAllRunes();
            }
            final TextComponent textComponent = new TextComponent(RuneStone.Format("&a&l> &r&f["));
            int n = 0;
            for (final Rune rune4 : list) {
                String string = "";
                if (RuneStone.isHigherVersion(rune4.getVersion(), RuneStone.serverVersion)) {
                    string = "\n&c> &7" + rune4.getVersion() + "+ only!";
                }
                ++n;
                textComponent.addExtra(new ezTextComponent(RuneStone.Format("&f" + rune4.getRealName())).withHoverMessage(RuneStone.Format("&aRune Name > " + rune4.getRealName() + "\n&aRarity > " + rune4.getRarity().getColor() + rune4.getRarity() + "\n&aCustom Name: &b" + rune4.getCustomName() + string)).get());
                if (n != list.size()) {
                    textComponent.addExtra(RuneStone.Format("&f, "));
                }
            }
            textComponent.addExtra(RuneStone.Format("&f]"));
            if (commandSender instanceof Player) {
                ((Player)commandSender).spigot().sendMessage(new ezTextComponent(textComponent).get());
            }
            else {
                commandSender.sendMessage(RuneStone.Format(textComponent.toPlainText()));
            }
            commandSender.sendMessage("");
        }
        if (array.length >= 3 && array[0].equalsIgnoreCase("give")) {
            final Player player3 = Bukkit.getPlayer(array[1]);
            if (player3 == null) {
                commandSender.sendMessage(RuneStone.Format(RuneStone.msg.getString("OfflinePlayer")));
                return false;
            }
            int int2;
            try {
                int2 = Integer.parseInt(array[2]);
            }
            catch (NumberFormatException ex5) {
                player3.sendMessage(RuneStone.Format("&c>&7 Usage: /rs give <player> <amount> <rune>"));
                return false;
            }
            if (int2 == 0) {
                player3.sendMessage(RuneStone.Format("&c>&7 Usage: /rs give <player> <amount> <rune>"));
            }
            String string2 = "";
            for (int i = 3; i < array.length; ++i) {
                string2 = string2 + array[i] + " ";
            }
            final Rune rune5 = RuneStone.getRune(string2);
            if (rune5 == null) {
                commandSender.sendMessage(RuneStone.Format(RuneStone.msg.getString("RuneNotFound")));
                return false;
            }
            if (RuneStone.isHigherVersion(rune5.getVersion(), RuneStone.serverVersion)) {
                commandSender.sendMessage(RuneStone.Format(RuneStone.msg.getString("Invalid-Version").replaceAll("%version%", String.valueOf(RuneStone.serverVersion))));
                return false;
            }
            for (int j = 0; j < int2; ++j) {
                if (player3.getInventory().firstEmpty() == -1) {
                    player3.getWorld().dropItemNaturally(player3.getLocation(), rune5.asItem());
                }
                else {
                    player3.getInventory().addItem(rune5.asItem());
                }
            }
            player3.updateInventory();
            commandSender.sendMessage(RuneStone.Format(RuneStone.msg.getString("GiveRune").replaceAll("%rune%", rune5.getRealName()).replaceAll("%player%", player3.getName())));
        }
        if(array.length >= 2 && array[0].equalsIgnoreCase("open")) {
            final Player player4 = Bukkit.getPlayer(array[1]);
            if (player4 == null) {
                commandSender.sendMessage(RuneStone.Format(RuneStone.msg.getString("OfflinePlayer")));
                return false;
            }

            RunePlayer rp = RuneStone.getRunePlayer(player4);
            Player p = (Player)commandSender;
            assert rp != null;
            p.openInventory(rp.getRuneBagInv());
            p.sendMessage(RuneStone.Format("&a> &7Вы открыли мешок с рунами игрока "+player4.getName()+"!"));
            p.sendMessage(RuneStone.Format("&7"));
            p.sendMessage(RuneStone.Format("&a> &7Все активированные руны игрока "+player4.getName()+":"));
            for(Rune rune : rp.getRunes()) {
                p.sendMessage(RuneStone.Format("&e> Название: &b"+rune.getCustomName()+" &7Редкость: &a"+rune.getRarity()+" &7Версия: &d"+rune.getVersion()));
            }
        }
        return false;
    }
}
