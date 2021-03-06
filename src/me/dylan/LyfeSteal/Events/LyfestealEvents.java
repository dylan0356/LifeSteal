package me.dylan.LyfeSteal.Events;


import me.dylan.LyfeSteal.Commands.BanCommands;
import me.dylan.LyfeSteal.Files.BanUnit;
import me.dylan.LyfeSteal.Main;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;



/*
Plan From Thread
Lifesteal (Possible Help) https://github.com/SirHenryVII/LifeStealPlugin
Basically, if you seen Lifesteal SMP its where you steal Hearts from others. I want everthing list below for this plugin in this core with the commands listed to.
also I can change ranks used from luckperms to change different ban time timers for example Default being 6 hours then MVP being 1 hour.
Withdrawable Hearts - You can use /hearts withdraw {amount}
Craftable Hearts - You need a crafting table, you put a netherstart in the middle then 4 gold blocks in the corner then 4 diamond blocks in the middle.
Life Lost - If you die no matter what from you loose a heart, if you find your loot then the heart doesn't drop.
Heart Limit - You can set a heart limit in the config
When you kill someone you can set to true or false in config if there should be a command when killing them for example /crate give {player} 1
Admin Commands - /hearts set {player} {amount}, /hearts reset all - Basically resets all hearts for the map

spawnerLevel = Main.data.getConfig().getInt("spawners." + locString + ".level");
 */

public class LyfestealEvents implements Listener {


    @EventHandler
    public static void onPlayerKill (PlayerDeathEvent event) {

        double lossOnDeath = Main.config.getConfig().getDouble("lossOnDeath");

        Player killed = event.getEntity();
        Player killer = event.getEntity().getKiller();

        double killedHearts = killed.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double killerHearts = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        double defaultHealth = Main.config.getConfig().getDouble("defaultHealth");


        killed.sendMessage(ChatColor.RED + "You have lost " + (int) (lossOnDeath/2) + " heart!");

        double maxHearts = Main.config.getConfig().getDouble("maxHearts");


        if (killedHearts - lossOnDeath <= 0) {


            if ((Main.data.getConfig().getDouble("players." + killer.getUniqueId() + ".hearts")) < maxHearts) {
                Main.data.getConfig().set("players." + killer.getUniqueId() + ".hearts", killerHearts + lossOnDeath);
                double killerMaxHealth = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(killerMaxHealth + lossOnDeath);

                killer.sendMessage("You have gained " + (int) (lossOnDeath/2) + " heart!");
            } else {
                killer.sendMessage(ChatColor.RED +"You have reached the max hearts!");
            }

            Main.data.getConfig().set("players." + killed.getUniqueId() + ".hearts", defaultHealth);
            killed.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(defaultHealth);

            String[] banLength = Main.config.getConfig().getString("banLength").split(" ");

            long endOfBan = System.currentTimeMillis() + BanUnit.getTicks(banLength[1], Integer.parseInt(banLength[0]));

            long now = System.currentTimeMillis();
            long diff = endOfBan - now;

            BanCommands.setBanned(killed.getDisplayName(), endOfBan);

            String message = BanCommands.getMSG(endOfBan);

            killed.kickPlayer("You have been DeathBanned for " + message);

        } else {

            if ((Main.data.getConfig().getDouble("players." + killer.getUniqueId() + ".hearts")) <= maxHearts) {
                Main.data.getConfig().set("players." + killer.getUniqueId() + ".hearts", killerHearts + lossOnDeath);
                double killerMaxHealth = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(killerMaxHealth + lossOnDeath);

                killer.sendMessage("You have gained " + (int) (lossOnDeath/2) + " heart!");
            } else {
                killer.sendMessage(ChatColor.RED +"You have reached the max hearts!");
            }

            Main.data.getConfig().set("players." + killed.getUniqueId() + ".hearts", killedHearts - lossOnDeath);
            killed.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(killedHearts - lossOnDeath);

        }
        Main.data.saveConfig();
        Main.data.reloadConfig();


    }

    @EventHandler
    public static void onPlayerJoin (PlayerJoinEvent event) {

        Player player = event.getPlayer();

        double defaultHealth = Main.config.getConfig().getDouble("defaultHealth");
        double currentMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

        if (!player.hasPlayedBefore()) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(defaultHealth);
            Main.data.getConfig().set("players." + player.getUniqueId() + ".hearts", currentMaxHealth);
            Main.data.saveConfig();
            Main.data.reloadConfig();
        } else {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Main.data.getConfig().getDouble("players." + player.getUniqueId() + ".hearts"));
        }

    }

    @EventHandler
    public static void onPlayerLeave (PlayerQuitEvent event) {

            Player player = event.getPlayer();

            Main.data.getConfig().set("players." + player.getUniqueId() + ".hearts", player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
            Main.data.saveConfig();
            Main.data.reloadConfig();

    }
}
