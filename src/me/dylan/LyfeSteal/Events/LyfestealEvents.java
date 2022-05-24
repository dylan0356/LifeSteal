package me.dylan.LyfeSteal.Events;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class LyfestealEvents implements Listener {

    @EventHandler
    public static void onPlayerKill (PlayerDeathEvent event) {

        String killed = event.getEntity().getName();
        String killer = event.getEntity().getKiller().getName();



    }
}
