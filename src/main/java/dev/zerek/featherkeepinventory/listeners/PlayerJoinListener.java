package dev.zerek.featherkeepinventory.listeners;

import dev.zerek.featherkeepinventory.FeatherKeepInventory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final FeatherKeepInventory plugin;
    public PlayerJoinListener(FeatherKeepInventory plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if (!event.getPlayer().hasPlayedBefore()) {
            plugin.getServer().broadcast(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("new-message"),
                    Placeholder.unparsed("player", event.getPlayer().getName()),
                    Placeholder.unparsed("mins", plugin.getConfigMap().get("new-minutes").toString())));
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),"lp user " + event.getPlayer().getName() + " permission set feather.keepinventory.keep true");
        }
    }
}