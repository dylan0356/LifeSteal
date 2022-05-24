package me.dylan.LyfeSteal;

import me.dylan.LyfeSteal.Events.LyfestealEvents;
import me.dylan.LyfeSteal.Files.ConfigManager;
import me.dylan.LyfeSteal.Files.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static DataManager data;
    public static ConfigManager config;

    @Override
    public void onEnable() {

        this.config = new ConfigManager(this);
        this.data = new DataManager(this);

        getServer().getPluginManager().registerEvents(new LyfestealEvents(), this);

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[Lyfesteal]: SuperSpawners is enabled");
        if (!config.getConfig().getBoolean("enabled"))
            getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[Lyfesteal]: SuperSpawners is disabled in the config!");

    }

    @Override
    public void onDisable() {

        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[Lyfesteal]: SuperSpawners is disabled");

    }
}
