package me.dylan.LyfeSteal.Commands;

import me.dylan.LyfeSteal.Main;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class LyfestealCommands implements CommandExecutor {

    double defaultHealth = Main.config.getConfig().getDouble("defaultHealth");
    double maxHearts = Main.config.getConfig().getDouble("maxHearts");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String value, String[] args) {

        if (cmd.getName().equalsIgnoreCase("hearts")) {

            if((args.length == 0) || (args[0].equalsIgnoreCase("help"))) {
                sender.sendMessage("");
                sender.sendMessage(ChatColor.YELLOW + "Hearts commands:");
                sender.sendMessage(ChatColor.YELLOW + "/hearts help - Shows this help");
                sender.sendMessage(ChatColor.YELLOW + "/hearts give - Give another player some hearts");
                sender.sendMessage(ChatColor.YELLOW + "Admin Commands:");
                sender.sendMessage(ChatColor.YELLOW + "/hearts reset - Resets a players hearts or all players hearts");
                sender.sendMessage(ChatColor.YELLOW + "/hearts set - Sets the hearts for a player");
                return true;
            }

            if (args[0].equalsIgnoreCase("give")) {

                if (!(sender instanceof Player)) {
                    sender.sendMessage("Player only command");
                    return true;
                }

                if (args.length != 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /hearts give <player> <amount>");
                    return true;

                } else {
                    Player player = ((Player) sender).getPlayer();
                    Player target = player.getServer().getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(ChatColor.RED + "Player not found");
                        return true;
                    }

                    try {
                        Integer.parseInt(args[2]);

                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Usage: /hearts give <player> <amount>");
                        return true;
                    }

                    if (Integer.parseInt(args[2]) <= 0) {
                        sender.sendMessage(ChatColor.RED + "Amount can not be negative");
                        return true;
                    }

                    if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() < Integer.parseInt(args[2]) * 2) {
                        sender.sendMessage(ChatColor.RED + "You do not have enough health");
                        return true;
                    }

                    if (player.getGameMode().equals(org.bukkit.GameMode.CREATIVE)) {
                        sender.sendMessage(ChatColor.RED + "You can not do this in creative mode");
                        return true;
                    } else {
                        int withdrawAmount = Integer.parseInt(args[2]);

                        //remove withdraw amount from player
                        target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + withdrawAmount * 2);
                        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - withdrawAmount * 2);

                        sender.sendMessage(ChatColor.GREEN + "You have successfully given " + withdrawAmount + " health to " + target.getName());
                        target.sendMessage(ChatColor.GREEN + "You have received " + withdrawAmount + " health from " + player.getName());

                        Main.data.getConfig().set("players." + target.getUniqueId().toString() + ".health", target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + withdrawAmount * 2);
                        Main.data.getConfig().set("players." + player.getUniqueId().toString() + ".health", player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - withdrawAmount * 2);
                        Main.data.saveConfig();
                        Main.data.reloadConfig();

                    }

                    return true;
                }

            }

            if (args[0].equalsIgnoreCase("set")) {

                if (args.length != 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /hearts set <player> <health>");
                    return true;

                } else {
                    Player player = ((Player) sender).getPlayer();
                    Player target = player.getServer().getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(ChatColor.RED + "Player not found");
                        return true;
                    }

                    try {
                        Integer.parseInt(args[2]);

                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "<amount> needs to be a valid integer");
                        return true;
                    }

                    if (Integer.parseInt(args[2]) <= 0) {
                        sender.sendMessage(ChatColor.RED + "Amount can not be negative");
                        return true;
                    }


                    int newHealth = (Integer.parseInt(args[2]));


                    target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newHealth);

                    sender.sendMessage(ChatColor.GREEN + "You have set " + target.getDisplayName() + "'s health to " + target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                    target.sendMessage(ChatColor.GREEN + "Your new max health is " + target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());


                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("reset")) {

                    if (args.length == 1) {
                        sender.sendMessage(ChatColor.RED + "Usage: /hearts reset <player> | all");
                        return true;
                    }

                    //Set keys = Main.data.getConfig().getKeys(true);
                    ConfigurationSection players = Main.data.getConfig().getConfigurationSection("players");
                    //List<String> keys = new ArrayList();

                    if (args[1].equalsIgnoreCase("all")) {
                        for (String key : players.getKeys(false)) {

                            UUID currentUUID = UUID.fromString(key);
                            //sender.sendMessage(key + " UUID");

                            Player currentPlayer = sender.getServer().getPlayer(currentUUID);

                            if (currentPlayer != null) {
                                currentPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(defaultHealth);
                                currentPlayer.sendMessage(ChatColor.GREEN + "Your max health has been reset");
                            }
                            Main.config.getConfig().set("players." + key + ".hearts", defaultHealth);
                            Main.data.saveDefaultConfig();
                            Main.data.reloadConfig();

                        }
                        sender.sendMessage(ChatColor.GREEN + "All players have been reset");
                        return true;
                    } else {
                        Player target = sender.getServer().getPlayer(args[1]);
                        if (target == null) {
                            sender.sendMessage(ChatColor.RED + "Player not found");
                            return true;
                        }

                        target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(defaultHealth);
                        target.sendMessage(ChatColor.GREEN + "Your max health has been reset");
                        Main.config.getConfig().set("players." + target.getUniqueId() + ".hearts", defaultHealth);
                        sender.sendMessage(ChatColor.GREEN + "You have reset " + target.getDisplayName() + "'s health");
                        Main.data.saveConfig();
                        Main.data.reloadConfig();
                        return true;
                    }
            }

            return true;
        }
        Main.data.saveConfig();
        return true;
    }
}
