package ru.ibusewinner.fundaily.runestones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.ibusewinner.fundaily.runestones.Objects.RunePlayer;
import ru.ibusewinner.fundaily.runestones.RuneStone;

public class Runes implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (commandSender instanceof Player) {
            final Player player = (Player)commandSender;
            if (!player.hasPermission("runestones.use")) {
                player.sendMessage(RuneStone.Format(RuneStone.msg.getString("NoPermission")));
                return false;
            }
            final RunePlayer runePlayer = RuneStone.getRunePlayer(player);
            if (runePlayer != null) {
                RuneGUI.openRuneGUI(runePlayer);
            }
            else {
                player.sendMessage(RuneStone.Format("&c>&7 An error occurred. Please relog."));
            }
        }
        return false;
    }
}
