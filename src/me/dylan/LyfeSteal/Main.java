package me.dylan.LyfeSteal;

import me.dylan.LyfeSteal.Commands.BanCommands;
import me.dylan.LyfeSteal.Events.LyfestealEvents;
import me.dylan.LyfeSteal.Files.ConfigManager;
import me.dylan.LyfeSteal.Files.DataManager;
import me.dylan.LyfeSteal.Commands.LyfestealCommands;
import me.dylan.LyfeSteal.Listeners.TBListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Logger;

/*
Plan From Thread
Lifesteal (Possible Help) https://github.com/SirHenryVII/LifeStealPlugin
Basically, if you seen Lifesteal SMP its where you steal Hearts from others. I want everthing list below for this plugin in this core with the commands listed to.
Killed Hearts - You gain a heart if you kill a player and they loose there heart, if they die on 1 heart then they get ban I can set the deathban timer in the config, also I can change ranks used from luckperms to change different ban time timers for example Default being 6 hours then MVP being 1 hour.
Withdrawable Hearts - You can use /hearts withdraw {amount}
Craftable Hearts - You need a crafting table, you put a netherstart in the middle then 4 gold blocks in the corner then 4 diamond blocks in the middle.
Life Lost - If you die no matter what from you loose a heart, if you find your loot then the heart doesn't drop.
Heart Limit - You can set a heart limit in the config
When you kill someone you can set to true or false in config if there should be a command when killing them for example /crate give {player} 1
Admin Commands - /hearts set {player} {amount}, /hearts reset all - Basically resets all hearts for the map
 */

public class Main extends JavaPlugin {

    public static DataManager data;
    public static ConfigManager config;

    public static HashMap<String, Long> banned = new HashMap<String, Long>();

    public static String Path = "plugins/TempBan" + File.separator + "BanList.dat";
    public TBListener Listener = new TBListener();

    public Logger log;

    @Override
    public void onEnable() {

        File file = new File(Path);
        new File("plugins/TempBan").mkdir();

        if(file.exists()){
            banned = load();
        }

        if(banned == null){
            banned = new HashMap<String, Long>();
        }

        this.config = new ConfigManager(this);
        this.data = new DataManager(this);

        getServer().getPluginManager().registerEvents(new LyfestealEvents(), this);
        getServer().getPluginManager().registerEvents(new TBListener(), this);

        LyfestealCommands Lcommands = new LyfestealCommands();
        getCommand("hearts").setExecutor(Lcommands);

        BanCommands BCommands = new BanCommands();
        getCommand("tempban").setExecutor(BCommands);
        getCommand("tempbanexact").setExecutor(BCommands);
        getCommand("unban").setExecutor(BCommands);
        getCommand("check").setExecutor(BCommands);

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[LyfeSteal]: LyfeSteal is enabled");
        if (!config.getConfig().getBoolean("enabled"))
            getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[LyfeSteal]: LyfeSteal is disabled in the config!");

    }

    @Override
    public void onDisable() {

        save();
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[LyfeSteal]: LyfeSteal is disabled");

    }

    public static void save(){
        File file = new File("plugins/TempBan" + File.separator + "BanList.dat");
        new File("plugins/TempBan").mkdir();
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Path));
            oos.writeObject(banned);
            oos.flush();
            oos.close();
            //Handle I/O exceptions
        }catch(Exception e){
            e.printStackTrace();
        }


    }
    @SuppressWarnings("unchecked")
    public static HashMap<String, Long> load(){
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Path));
            Object result = ois.readObject();
            ois.close();
            return (HashMap<String,Long>)result;
        }catch(Exception e){
            return null;
        }
    }
}
