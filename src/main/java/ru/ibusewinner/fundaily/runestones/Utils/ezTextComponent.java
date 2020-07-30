package ru.ibusewinner.fundaily.runestones.Utils;

import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

public class ezTextComponent {
    private TextComponent tc;

    public ezTextComponent(final String s) {
        this.tc = new TextComponent(s);
    }

    public ezTextComponent(final TextComponent tc) {
        this.tc = tc;
    }

    public ezTextComponent withHoverMessage(final String s) {
        this.tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(s).create()));
        return this;
    }

    public ezTextComponent withCommandExecution(String string) {
        if (!string.startsWith("/")) {
            string = "/" + string;
        }
        this.tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, string));
        return this;
    }

    public ezTextComponent withURL(final String s) {
        this.tc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, s));
        return this;
    }

    public ezTextComponent withCommandSuggestion(String string) {
        if (!string.startsWith("/")) {
            string = "/" + string;
        }
        this.tc.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, string));
        return this;
    }

    public TextComponent get() {
        return this.tc;
    }

    public void send(final Player player) {
        player.spigot().sendMessage(this.tc);
    }
}
