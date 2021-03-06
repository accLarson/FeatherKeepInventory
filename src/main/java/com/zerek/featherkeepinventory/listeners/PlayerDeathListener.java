package com.zerek.featherkeepinventory.listeners;

import com.zerek.featherkeepinventory.FeatherKeepInventory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    private final FeatherKeepInventory plugin;
    public PlayerDeathListener(FeatherKeepInventory plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        if (event.getPlayer().hasPermission("feather.keepinventory.keep") && !event.getPlayer().hasPermission("group.administrator")){
            event.setKeepInventory(true);
            event.getDrops().clear();
            event.setShouldDropExperience(false);
            plugin.getLogger().info(event.getPlayer().getName() + "is a new player and kept their items.");
            event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("keep")));
        }
    }
}
