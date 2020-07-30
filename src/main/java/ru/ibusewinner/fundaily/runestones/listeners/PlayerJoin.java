package ru.ibusewinner.fundaily.runestones.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.ibusewinner.fundaily.runestones.Objects.Rune;
import ru.ibusewinner.fundaily.runestones.Objects.RunePlayer;
import ru.ibusewinner.fundaily.runestones.RuneStone;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onJoin(final PlayerJoinEvent playerJoinEvent) {
        final RunePlayer runePlayer = new RunePlayer(playerJoinEvent.getPlayer());
        for (final Rune rune : runePlayer.getForging().keySet()) {
            if (runePlayer.isForged(rune)) {
                runePlayer.sendMessage(RuneStone.msg.getString("Rune-Forging-Complete").replaceAll("%rune%", rune.getCustomName()));
            }
        }
    }
}
