package com.zerek.featherkeepinventory.listeners;

import com.zerek.featherkeepinventory.FeatherKeepInventory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.luckperms.api.node.Node;
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
            plugin.getServer().broadcast(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("new-message"), Placeholder.unparsed("PLAYER", event.getPlayer().getName()), Placeholder.unparsed("MINS", plugin.getConfigMap().get("new-minutes").toString())));
            plugin.getLuckPerms().getUserManager().modifyUser(event.getPlayer().getUniqueId(), user -> user.data().add(Node.builder("feather.keepinventory.keep").build()));
        }
    }
}