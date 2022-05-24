package me.dylan.LyfeSteal.Files;

import me.dylan.LyfeSteal.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class ConfigManager {

    private Main plugin;
    private FileConfiguration configData = null;
    private File fileConfig = null;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        //saves and initializes config
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (this.fileConfig == null)
            this.fileConfig = new File(this.plugin.getDataFolder(), "config.yml");

        this.configData = YamlConfiguration.loadConfiguration(this.fileConfig);

        InputStream defaultStream = this.plugin.getResource("config.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration((new InputStreamReader(defaultStream)));
            this.configData.setDefaults(defaultConfig);
        }

    }

    public FileConfiguration getConfig() {
        if (this.configData == null)
            reloadConfig();

        return this.configData;
    }

    public void saveConfig() {
        if (this.configData == null || this.fileConfig == null)
            return;

        try {
            this.getConfig().save(this.fileConfig);
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.fileConfig, e);
        }
    }

    public void saveDefaultConfig() {
        if (this.fileConfig == null)
            this.fileConfig = new File(this.plugin.getDataFolder(), "config.yml");

        if (!this.fileConfig.exists()) {
            this.plugin.saveResource("config.yml", false);
        }
    }
}
