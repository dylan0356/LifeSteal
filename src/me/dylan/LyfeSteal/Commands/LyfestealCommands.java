package me.dylan.LyfeSteal.Commands;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LyfestealCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String value, String[] args) {

        if (cmd.getName().equalsIgnoreCase("withdraw")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Player only command");
                return true;
            }

            if (args.length != 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /withdraw <player> <amount>");
                return true;

            } else {
                Player player = ((Player) sender).getPlayer();
                Player target = player.getServer().getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found");
                    return true;
                }

                try {
                    Integer.parseInt(args[1]);

                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Usage: /withdraw <player> <amount>");
                    return true;
                }

                if (Integer.parseInt(args[1]) <= 0) {
                    sender.sendMessage(ChatColor.RED + "Amount can not be negative");
                    return true;
                }

                if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() < Integer.parseInt(args[1]) * 2) {
                    sender.sendMessage(ChatColor.RED + "You do not have enough health");
                    return true;
                }

                if (player.getGameMode().equals(org.bukkit.GameMode.CREATIVE)) {
                    sender.sendMessage(ChatColor.RED + "You can not do this in creative mode");
                    return true;
                } else {
                    int withdrawAmount = Integer.parseInt(args[1]);

                    //remove withdraw amount from player
                    target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + withdrawAmount * 2);
                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - withdrawAmount * 2);

                    sender.sendMessage(ChatColor.GREEN + "You have successfully given " + withdrawAmount + " health to " + target.getName());
                    target.sendMessage(ChatColor.GREEN + "You have received " + withdrawAmount + " health from " + player.getName());

                }

                return true;
            }


    }

        if (cmd.getName().equalsIgnoreCase("setmaxhealth")) {
            if (args.length != 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /setmaxhealth <player> <health>");
                return true;

            } else {
                Player player = ((Player) sender).getPlayer();
                Player target = player.getServer().getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found");
                    return true;
                }

                try {
                    Integer.parseInt(args[1]);

                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "<amount> needs to be a valid integer");
                    return true;
                }

                if (Integer.parseInt(args[1]) <= 0) {
                    sender.sendMessage(ChatColor.RED + "Amount can not be negative");
                    return true;
                }



                int newHealth = (Integer.parseInt(args[1]));

                //remove withdraw amount from player
                target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newHealth);

                sender.sendMessage(ChatColor.GREEN + "You have set " + target.getDisplayName() + "'s health to " + target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                target.sendMessage(ChatColor.GREEN + "Your new max health is " + target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());



                return true;
            }
        }
        return true;
    }
}
