package ru.ibusewinner.fundaily.runestones.Utils;

import org.bukkit.configuration.file.YamlConfiguration;
import ru.ibusewinner.fundaily.runestones.RuneStone;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static List<RuneConfig> configs;

    static {
        FileManager.configs = new ArrayList<RuneConfig>();
    }

    public static void createDirectory(final String parent, final String child) {
        final File file = new File(parent, child);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static RuneConfig getConfig(final String anotherString) {
        for (final RuneConfig config : FileManager.configs) {
            if (config.getName().equalsIgnoreCase(anotherString)) {
                return config;
            }
        }
        return null;
    }

    public static boolean copyDefaults(final RuneConfig config, final InputStream in) {
        boolean b = false;
        final YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(new InputStreamReader(in));
        for (final String s : loadConfiguration.getConfigurationSection("").getKeys(true)) {
            if (!config.getRealConfig().contains(s)) {
                config.set(s, loadConfiguration.get(s));
                if (b) {
                    continue;
                }
                b = true;
            }
        }
        for (final String s2 : config.getRealConfig().getConfigurationSection("").getKeys(true)) {
            if (!loadConfiguration.contains(s2)) {
                config.set(s2, null);
                if (b) {
                    continue;
                }
                b = true;
            }
        }
        return b;
    }

    public static RuneConfig createConfig(final String child, final String parent) {
        final File file = new File(parent, child);
        final boolean hasDefault = hasDefault(child);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) { }
        }
        final RuneConfig config = new RuneConfig(file);
        if (!child.equalsIgnoreCase("runes.yml") && hasDefault && copyDefaults(config, RuneStone.plugin.getResource(child))) {
            try {
                config.save();
            }catch (IOException ex) { }
        }
        System.out.println("RS > " + config.getName() + " зарегистрирован.");
        return config;
    }

    public static RuneConfig createConfig(final File file) {
        if (!file.exists() && !hasDefault(file.getName())) {
            try {
                file.createNewFile();
            } catch (IOException ex) { }
        }
        final RuneConfig config = new RuneConfig(file);
        System.out.println("RS > " + config.getName() + " зарегистрирован.");
        return config;
    }

    public static boolean hasDefault(final String s) {
        return RuneStone.plugin.getResource(s) != null;
    }

    public static void reloadConfigs() {
        System.out.println("RS > Reloading all files...");
        for (final RuneConfig config : FileManager.configs) {
            config.reload();
            System.out.println("RS > " + config.getName() + " перезагружен.");
        }
        RuneStone.createRuneFile();
        RuneStone.loadRarities();
    }
}
