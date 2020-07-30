package ru.ibusewinner.fundaily.runestones;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.ibusewinner.fundaily.runestones.Objects.Rarity;
import ru.ibusewinner.fundaily.runestones.Objects.Rune;
import ru.ibusewinner.fundaily.runestones.Objects.RunePlayer;
import ru.ibusewinner.fundaily.runestones.Runes.Custom.DoubleXP;
import ru.ibusewinner.fundaily.runestones.Runes.Effecting.*;
import ru.ibusewinner.fundaily.runestones.Utils.FileManager;
import ru.ibusewinner.fundaily.runestones.Utils.RuneConfig;
import ru.ibusewinner.fundaily.runestones.commands.*;
import ru.ibusewinner.fundaily.runestones.listeners.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

public final class RuneStone extends JavaPlugin {

    public static Plugin plugin;
    public static boolean databaseEnabled;
    public static RuneConfig config;
    public static RuneConfig msg;
    public static RuneConfig runeFile;
    public static Economy econ;
    public static double serverVersion;

    static {
        RuneStone.databaseEnabled = false;
    }

    public static String Format(final String s) {
        return s.replaceAll("&", "§");
    }

    @Override
    public void onEnable() {
        ((RuneStone)(RuneStone.plugin = this)).loadRunes();
        this.generateDirectories();
        this.generateFiles();
        createRuneFile();

        RuneStone.config = FileManager.getConfig("config.yml");
        RuneStone.msg = FileManager.getConfig(RuneStone.config.getString("lang"));
        if (RuneStone.msg == null) {
            sendConsole("&cОШИБКА > &7Невозможно найти перевод, проверьте ошибки в config.yml.");
            sendConsole("&c> &7Плагин не может работать без перевода!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        this.registerListeners();
        this.registerCommands();

        loadRarities();
        sendConsole(" ");
        sendConsole("&b&l<------> RuneStones <------>");
        sendConsole("&7Plugin has been &aenabled.");
        sendConsole("&b&l<------> RuneStones <------>");
        sendConsole(" ");
        new BukkitRunnable() {
            public void run() {
                final Iterator<Player> iterator = (Iterator<Player>) Bukkit.getOnlinePlayers().iterator();
                while (iterator.hasNext()) {
                    new RunePlayer(iterator.next());
                }
            }
        }.runTaskLater(this, 20L);

        final String version = Bukkit.getVersion();
        if (version.contains("1.8")) {
            RuneStone.serverVersion = 1.8;
        }
        else if (version.contains("1.9")) {
            RuneStone.serverVersion = 1.9;
        }
        else if (version.contains("1.10")) {
            RuneStone.serverVersion = 1.10;
        }
        else if (version.contains("1.11")) {
            RuneStone.serverVersion = 1.11;
        }
        else if (version.contains("1.12")) {
            RuneStone.serverVersion = 1.12;
        }
        else if (version.contains("1.13")) {
            RuneStone.serverVersion = 1.13;
        }
        else if (version.contains("1.14")) {
            RuneStone.serverVersion = 1.14;
        }
        else if(version.contains("1.15")) {
            RuneStone.serverVersion = 1.15;
        }
        else if(version.contains("1.16")) {
            RuneStone.serverVersion = 1.16;
        }

        sendConsole("&a>&7 Используем " + RuneStone.msg.getName() + " файл.");
        sendConsole("&a>&7 Успешно загружено " + Rune.getAllRunes().size() + " рун.");
        sendConsole("&a>&7 Сервер работает на версии " + RuneStone.serverVersion);
        this.setupEconomy();
    }

    @Override
    public void onDisable() {
        sendConsole(" ");
        sendConsole("&b&l<------> RuneStones <------>");
        sendConsole("&7Плагин был &cвыключен.");
        sendConsole("&b&l<------> RuneStones <------>");
        sendConsole(" ");
        RuneStone.plugin = null;
    }

    private boolean setupEconomy() {
        final RegisteredServiceProvider registration = this.getServer().getServicesManager().getRegistration((Class)Economy.class);
        if (registration != null) {
            RuneStone.econ = (Economy)registration.getProvider();
        }
        return RuneStone.econ != null;
    }

    private void registerListeners() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoin(), this);
        pluginManager.registerEvents(new InventoryClick(), this);
        pluginManager.registerEvents(new DamageEvent(), this);
        pluginManager.registerEvents(new FoodEvent(), this);
        pluginManager.registerEvents(new PlayerDeath(), this);
        pluginManager.registerEvents(new PlayerQuit(), this);
        pluginManager.registerEvents(new EntityDeath(), this);
        pluginManager.registerEvents(new RuneShop(), this);
    }

    private void registerCommands() {
        Objects.requireNonNull(this.getCommand("rs")).setExecutor(new Rs());
        Objects.requireNonNull(this.getCommand("artifact")).setExecutor(new RuneGUI());
        Objects.requireNonNull(this.getCommand("artifacts")).setExecutor(new Runes());
        Objects.requireNonNull(this.getCommand("afbag")).setExecutor(new RuneBag());
        //Objects.requireNonNull(this.getCommand("runeshop")).setExecutor(new RuneShop());
    }

    private void generateDirectories() {
        FileManager.createDirectory(this.getDataFolder().getParent(), "RuneStones");
        FileManager.createDirectory(this.getDataFolder().getPath(), "lang");
        FileManager.createDirectory(this.getDataFolder().getPath(), "data");
    }

    private void generateFiles() {
        FileManager.createConfig("config.yml", this.getDataFolder().getPath());
        FileManager.createConfig("en-US.yml", this.getDataFolder().getPath() + File.separator + "lang");
        FileManager.createConfig("runes.yml", this.getDataFolder().getPath() + File.separator + "lang");
        FileManager.createConfig("playerdata.yml", this.getDataFolder().getPath() + File.separator + "data");
    }

    public static void loadRarities() {
        Rarity[] values;
        for (int length = (values = Rarity.values()).length, i = 0; i < length; ++i) {
            final Rarity rarity = values[i];
            rarity.setForgeCost(RuneStone.config.getInt(rarity.toString().toLowerCase() + ".forge-cost"));
            rarity.setForgeDelay(RuneStone.config.getLong(rarity.toString().toLowerCase() + ".forge-delay"));
            rarity.setShardValue(RuneStone.config.getInt(rarity.toString().toLowerCase() + ".shatter-reward"));
        }
    }

    public static void createRuneFile() {
        RuneStone.runeFile = FileManager.getConfig("runes.yml");
        for (final Rune rune : Rune.getAllRunes()) {
            assert RuneStone.runeFile != null;
            if (!RuneStone.runeFile.getRealConfig().contains(rune.getRealName())) {
                RuneStone.runeFile.set(rune.getRealName() + ".name", rune.getRealName());
                RuneStone.runeFile.set(rune.getRealName() + ".lore", rune.getLore());
                RuneStone.runeFile.set(rune.getRealName() + ".rarity", rune.getRarity().toString());
                RuneStone.runeFile.set(rune.getRealName() + ".cost", rune.getCost());
                try {
                    RuneStone.runeFile.save();
                } catch (IOException ex) { }
            }
            else {
                final String string = RuneStone.runeFile.getString(rune.getRealName() + ".name");
                final Rarity value = Rarity.valueOf(RuneStone.runeFile.getString(rune.getRealName() + ".rarity"));
                final String string2 = RuneStone.runeFile.getString(rune.getRealName() + ".lore");
                rune.setCost(RuneStone.runeFile.getDouble(rune.getRealName() + ".cost"));
                rune.setCustomName(string);
                rune.setLore(string2);
                rune.setRarity(value);
            }
        }
    }

    public void loadRunes() {
        new Speed1("Speed I", "ZOOOOOOM!", Rarity.NORMAL);
        new Speed2("Speed II", "ZOOOOOOM!!", Rarity.NORMAL);
        new Speed3("Speed III", "ZOOOOOOM!!!", Rarity.UNIQUE);
        //new Speed4("Speed IV", "ZOOOOOOM!!!!", Rarity.EPIC);
        //new Speed5("Speed V", "ZOOOOOOM!!!!!", Rarity.LEGENDARY);
        new Jumping1("Jumping I", "Hop your way around.", Rarity.NORMAL);
        new Jumping2("Jumping II", "Hop your way around.", Rarity.UNIQUE);
        //new Jumping3("Jumping III", "Hop your way around.", Rarity.EPIC);
        //new Space("Astronaut", "Alter the world's gravity to\njump higher than ever!", Rarity.LEGENDARY);
        new FireResistance("Fire Resistance", "Fire, not very effective anymore.", Rarity.UNIQUE);
        new Strength1("Strength I", "You hit the gym for a while.", Rarity.NORMAL);
        new Strength2("Strength II", "You hit the gym for a while.", Rarity.UNIQUE);
        //new Strength3("Strength III", "You hit the gym for a while.", Rarity.EPIC);
        //new Strength4("Strength IV", "You hit the gym for a while.", Rarity.EPIC);
        //new Strength5("Strength V", "Arm wrestle?", Rarity.LEGENDARY);
        new Diver("Diver", "This rune stinks of fish.", Rarity.EPIC);
        //new Invisibility("Invisibility", "&d&lPoof!&r&7 Disappear with the\nclick of a button!", Rarity.EPIC);
        new Resistance("Resistance", "Damage? I do not know\nof such a thing.", Rarity.UNIQUE);
        new Regeneration1("Regeneration I", "Who needs health potions anyway?", Rarity.NORMAL);
        new Regeneration2("Regeneration II", "Who needs health potions anyway?", Rarity.EPIC);
        //new Regeneration3("Regeneration III", "Who needs health potions anyway?", Rarity.LEGENDARY);
        new Haste1("Haste I", "Just keep digging\njust keep digging\njust keep digging...", Rarity.UNIQUE);
        new Luck1("Luck I", "Feeling lucky eh?", Rarity.UNIQUE);
        //new Helix1("Helix I", "Creates a mystical helix\naround you.", Rarity.UNIQUE);
        //new Helix2("Helix II", "Creates a sparkly helix\naround you.", Rarity.EPIC);
        //new Helix3("Helix III", "Creates a colourful mix\nof notes around you.", Rarity.LEGENDARY);
        //new Aura1("Aura I", "Give yourself a lit aura.", Rarity.NORMAL);
        //new Aura2("Aura II", "Give yourself a mysterious aura.", Rarity.UNIQUE);
        //new Aura3("Aura III", "Give yourself an enchanting aura.", Rarity.EPIC);
        //new Aura4("Aura IV", "Give yourself a cold aura.", Rarity.EPIC);
        //new Aura5("Aura V", "Give yourself a dark aura.", Rarity.LEGENDARY);
        //new Flight("Flight", "Fly like a pretty bird!", Rarity.LEGENDARY);
        //new FeatherFoot("Feather Foot", "Become as light as\na feather.", Rarity.EPIC);
        //new Saturation("Saturation", "Food no longer interests you.", Rarity.LEGENDARY);
        new DoubleXP("Double Experience", "Double the fun!", Rarity.EPIC);
        for (final Rune rune : Rune.getAllRunes()) {
            rune.setCost(rune.getRarity().getSuggestedCost());
        }
    }

    public static Rune getRune(String trim) {
        trim = ChatColor.stripColor(trim).trim();
        for (final Rune rune : Rune.getAllRunes()) {
            if (trim.equalsIgnoreCase(rune.getRealName()) || trim.equalsIgnoreCase(rune.getCustomName())) {
                return rune;
            }
        }
        return null;
    }

    public static RunePlayer getRunePlayer(final Player obj) {
        for (final RunePlayer runePlayer : RunePlayer.players) {
            if (runePlayer.getPlayer().equals(obj)) {
                return runePlayer;
            }
        }
        return null;
    }

    public static void sendConsole(final String s) {
        RuneStone.plugin.getServer().getConsoleSender().sendMessage(Format(s));
    }

    public static boolean isHigherVersion(double n, double n2) {
        if (n == 1.8) {
            n = 1.0;
        }
        else if (n == 1.9) {
            n = 2.0;
        }
        else if (n == 1.1) {
            n = 3.0;
        }
        else if (n == 1.11) {
            n = 4.0;
        }
        else if (n == 1.12) {
            n = 5.0;
        }
        else if (n == 1.13) {
            n = 6.0;
        }
        else if (n == 1.14) {
            n = 7.0;
        }
        else if (n == 1.15) {
            n = 8.0;
        }
        else if (n == 1.16) {
            n = 9.0;
        }
        if (n2 == 1.8) {
            n2 = 1.0;
        }
        else if (n2 == 1.9) {
            n2 = 2.0;
        }
        else if (n2 == 1.1) {
            n2 = 3.0;
        }
        else if (n2 == 1.11) {
            n2 = 4.0;
        }
        else if (n2 == 1.12) {
            n2 = 5.0;
        }
        else if (n2 == 1.13) {
            n2 = 6.0;
        }
        else if (n2 == 1.14) {
            n2 = 7.0;
        }
        else if (n2 == 1.15) {
            n2 = 8.0;
        }
        else if (n2 == 1.16) {
            n2 = 9.0;
        }
        return n > n2;
    }

    public static boolean hasPassed(final long n, final long n2) {
        return System.currentTimeMillis() - n2 >= n;
    }

    public static Rune getRandomRune(final Rarity rarity) {
        final ArrayList<Rune> list = new ArrayList<>();
        for (final Rune e : Rune.getAllRunes()) {
            if (e.getRarity() == rarity) {
                list.add(e);
            }
        }
        return list.get(new Random().nextInt(list.size()));
    }

}
