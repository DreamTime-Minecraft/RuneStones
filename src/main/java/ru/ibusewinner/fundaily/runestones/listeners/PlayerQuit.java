package ru.ibusewinner.fundaily.runestones.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.ibusewinner.fundaily.runestones.Objects.Rune;
import ru.ibusewinner.fundaily.runestones.Objects.RunePlayer;
import ru.ibusewinner.fundaily.runestones.RuneStone;
import ru.ibusewinner.fundaily.runestones.commands.RuneShop;

public class PlayerQuit implements Listener {
    @EventHandler
    public void onQuit(final PlayerQuitEvent playerQuitEvent) {
        final RunePlayer runePlayer = RuneStone.getRunePlayer(playerQuitEvent.getPlayer());
        if (runePlayer != null) {
            for (Rune rune : runePlayer.getRunes()) {
                rune.deactivate(playerQuitEvent.getPlayer());
            }
            RunePlayer.players.remove(runePlayer);
        }
        if (RuneShop.shoppers.containsKey(playerQuitEvent.getPlayer().getUniqueId())) {
            RuneShop.shoppers.remove(playerQuitEvent.getPlayer().getUniqueId());
        }
    }
}
