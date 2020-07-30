package ru.ibusewinner.fundaily.runestones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.ibusewinner.fundaily.runestones.Objects.RunePlayer;
import ru.ibusewinner.fundaily.runestones.RuneStone;

public class RuneBag implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (!(commandSender instanceof Player)) {
            return false;
        }
        final Player player = (Player)commandSender;
        if (!player.hasPermission("runestones.runebag.use")) {
            player.sendMessage(RuneStone.Format(RuneStone.msg.getString("NoPermission")));
            return false;
        }
        final RunePlayer runePlayer = RuneStone.getRunePlayer(player);
        if (runePlayer == null) {
            return false;
        }
        runePlayer.openRuneBag();
        return false;
    }
}
