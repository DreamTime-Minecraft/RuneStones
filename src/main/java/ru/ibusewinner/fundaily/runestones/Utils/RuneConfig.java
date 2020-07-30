package ru.ibusewinner.fundaily.runestones.Utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RuneConfig {
    private File file;
    private YamlConfiguration con;

    public RuneConfig(final File file) {
        this.file = file;
        this.con = YamlConfiguration.loadConfiguration(file);
        FileManager.configs.add(this);
    }

    public String getString(final String s) {
        return this.con.getString(s);
    }

    public int getInt(final String s) {
        return this.con.getInt(s);
    }

    public List<String> getStringList(final String s) {
        return this.con.getStringList(s);
    }

    public double getDouble(final String s) {
        return this.con.getDouble(s);
    }

    public String getName() {
        return this.file.getName();
    }

    public String getPath() {
        return this.file.getPath();
    }

    public void set(final String s, final Object o) {
        this.con.set(s, o);
    }

    public void save() throws IOException {
        this.con.save(this.file);
    }

    public long getLong(final String s) {
        return this.con.getLong(s);
    }

    public YamlConfiguration getRealConfig() {
        return this.con;
    }

    public void reload() {
        this.con = YamlConfiguration.loadConfiguration(this.file);
    }

    public File getFile() {
        return this.file;
    }
}
