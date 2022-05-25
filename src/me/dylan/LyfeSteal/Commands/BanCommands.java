package me.dylan.LyfeSteal.Commands;

import me.dylan.LyfeSteal.Files.BanUnit;
import me.dylan.LyfeSteal.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.security.auth.kerberos.KerberosTicket;

import java.util.HashMap;

import static org.bukkit.ChatColor.*;


//Credit to https://github.com/Nerdsie for a lot of this which I then made small changes
public class BanCommands implements CommandExecutor {

    public Main plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String desc, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;

            ////// Player commands....

            if(cmd.getName().equalsIgnoreCase("tempban")){
                if(args.length!=3){
                    player.sendMessage(RED + "Error: /tempban <player> <amount> <unit>");
                    return true;
                }

                Player target = player.getServer().getPlayer(args[0]);

                if(!player.isOp()){
                    player.sendMessage(RED + "[TempBan] You don't have permission to do this.");
                    return true;
                }

                if(target==null || !target.isOnline()){
                    player.sendMessage(RED + "[TempBan] Player could not be found!");
                    return true;
                }

                long endOfBan = System.currentTimeMillis() + BanUnit.getTicks(args[2], Integer.parseInt(args[1]));

                long now = System.currentTimeMillis();
                long diff = endOfBan - now;

                if(diff > 0){
                    setBanned(target.getName().toLowerCase(), endOfBan);

                    String message = getMSG(endOfBan);

                    player.getServer().broadcastMessage(GOLD + "[TempBan] " + GREEN + "The player " + AQUA + target.getName() + GREEN + " is now banned for " + AQUA  + message);
                    target.kickPlayer("[TempBan] You are temp-banned for " + message);
                    return true;
                }else{
                    player.sendMessage(RED + "Error: Unit or time not valid.");
                    return true;
                }
            }

            if(cmd.getName().equalsIgnoreCase("tempbanexact")){
                if(args.length!=3){
                    player.sendMessage(RED + "Error: /tempban <player> <amount> <unit>");
                    return true;
                }

                if(!player.isOp()){
                    player.sendMessage(RED + "[TempBan] You don't have permission to do this.");
                    return true;
                }

                long endOfBan = System.currentTimeMillis() + BanUnit.getTicks(args[2], Integer.parseInt(args[1]));

                long now = System.currentTimeMillis();
                long diff = endOfBan - now;

                if(diff > 0){
                    setBanned(args[0].toLowerCase(), endOfBan);

                    String message = getMSG(endOfBan);

                    player.getServer().broadcastMessage(GOLD + "[TempBan] " + GREEN + "The player " + AQUA + args[0].toLowerCase() + GREEN + " is now banned for " + AQUA + message);
                    return true;
                }else{
                    player.sendMessage(RED + "Error: Unit or time not valid.");
                    return true;
                }
            }

            if(cmd.getName().equalsIgnoreCase("unban")){
                if(args.length!=1){
                    player.sendMessage(RED + "Error: /unban <player>");
                    return true;
                }

                String targetString = args[0];
                Player target = player.getServer().getPlayer(targetString);

                if(!player.isOp()){
                    player.sendMessage(RED + "[TempBan] You don't have permission to do this.");
                    return true;
                }

                if(getBanned().containsKey(targetString.toLowerCase())){
                    getBanned().remove(targetString.toLowerCase());
                    player.sendMessage(GOLD + "[TempBan] " + GREEN + "The player " + AQUA + targetString + GREEN + " is now un-banned.");
                    return true;
                }else{
                    player.sendMessage(RED + "Error: The player " + targetString + " isn't banned.");
                    return true;
                }
            }

            if(cmd.getName().equalsIgnoreCase("check")){
                if(args.length!=1){
                    player.sendMessage(RED + "Error: /check <player>");
                    return true;
                }

                String targetString = args[0];
                Player target = player.getServer().getPlayer(targetString);

                if(!player.isOp()){
                    player.sendMessage(RED + "[TempBan] You don't have permission to do this.");
                    return true;
                }

                if(getBanned().containsKey(targetString.toLowerCase())){
                    player.sendMessage(GOLD + "[TempBan] " + GREEN + "The player " + AQUA + args[0] + GREEN + " is banned for " + AQUA + getMSG(getBanned().get(targetString.toLowerCase())));
                    return true;
                }else{
                    player.sendMessage(GOLD + "[TempBan] " + GREEN + "The player " + AQUA + args[0] + GREEN + " is not banned.");
                    return true;
                }
            }
        }else{
            //////// Console Commands....

            if(cmd.getName().equalsIgnoreCase("tempban")){
                if(args.length!=3){
                    System.out.println(RED + "Error: /tempban <player> <amount> <unit>");
                    return true;
                }

                Player target = plugin.getServer().getPlayer(args[0]);

                if(target==null || !target.isOnline()){
                    System.out.println("[TempBan] Player could not be found!");
                    return true;
                }

                long endOfBan = System.currentTimeMillis() + BanUnit.getTicks(args[2], Integer.parseInt(args[1]));

                long now = System.currentTimeMillis();
                long diff = endOfBan - now;

                if(diff > 0){
                    setBanned(target.getName().toLowerCase(), endOfBan);

                    String message = getMSG(endOfBan);

                    target.getServer().broadcastMessage("[TempBan] The player " + target.getName() + " is now banned for " + message);
                    target.kickPlayer("[TempBan] You are temp-banned for " + message);
                    return true;
                }else{
                    System.out.println("Error: Unit or time not valid.");
                    return true;
                }
            }

            if(cmd.getName().equalsIgnoreCase("tempbanexact")){
                if(args.length!=3){
                    System.out.println("Error: /tempbanexact <player> <amount> <unit>");
                    return true;
                }

                long endOfBan = System.currentTimeMillis() + BanUnit.getTicks(args[2], Integer.parseInt(args[1]));

                long now = System.currentTimeMillis();
                long diff = endOfBan - now;

                if(diff > 0){
                    setBanned(args[0].toLowerCase(), endOfBan);

                    String message = getMSG(endOfBan);

                    plugin.getServer().broadcastMessage("[TempBan] The player " + args[0].toLowerCase() + " is now banned for " + message);
                    return true;
                }else{
                    System.out.println("Error: Unit or time not valid.");
                    return true;
                }
            }

            if(cmd.getName().equalsIgnoreCase("unban")){
                if(args.length!=1){
                    System.out.println("Error: /unban <player>");
                    return true;
                }

                String targetString = args[0];
                Player target = plugin.getServer().getPlayer(targetString);

                if(getBanned().containsKey(targetString.toLowerCase())){
                    getBanned().remove(targetString.toLowerCase());
                    //plugin.getServer().getPlayerExact(targetString).setBanned(false);
                    System.out.println("[TempBan] The player " + args[0] + " is now un-banned.");
                    return true;
                }else{
                    System.out.println("Error: The player " + args[0] + " isn't banned.");
                    return true;
                }
            }

            if(cmd.getName().equalsIgnoreCase("check")){
                if(args.length!=1){
                    System.out.println("Error: /check <player>");
                    return true;
                }

                String target = args[0];

                if(getBanned().containsKey(target.toLowerCase())){
                    System.out.println("[TempBan] The player " + args[0] + " is banned for " + getMSG(getBanned().get(args[0].toLowerCase())));
                    return true;
                }else{
                    System.out.println("[TempBan] The player " + args[0] + " is not banned.");
                    return true;
                }
            }
        }

        return false;
    }

    public static HashMap<String, Long> getBanned(){
        return Main.banned;
    }

    public static void setBanned(String name, long end){
        getBanned().put(name, end);
    }

    public static String getMSG(long endOfBan){
        String message = "";

        long now = System.currentTimeMillis();
        long diff = endOfBan - now;
        int seconds = (int) (diff / 1000);

        if(seconds >= 60*60*24){
            int days = seconds / (60*60*24);
            seconds = seconds % (60*60*24);

            message += days + " Day(s) ";
        }
        if(seconds >= 60*60){
            int hours = seconds / (60*60);
            seconds = seconds % (60*60);

            message += hours + " Hour(s) ";
        }
        if(seconds >= 60){
            int min = seconds / 60;
            seconds = seconds % 60;

            message += min + " Minute(s) ";
        }
        if(seconds >= 0){
            message += seconds + " Second(s) ";
        }

        return message;
    }
}
