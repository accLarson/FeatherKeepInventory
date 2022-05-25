package com.zerek.featherkeepinventory.listeners;

import com.zerek.featherkeepinventory.FeatherKeepInventory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.node.Node;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

public class BlockPlaceListener implements Listener {
    private final FeatherKeepInventory plugin;
    public BlockPlaceListener(FeatherKeepInventory plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if (event.getPlayer().hasPermission("feather.keepinventory.keep") && !event.getPlayer().hasPermission("group.administrator")){
            if (((List<String>) plugin.getConfigMap().get("banned-blocks")).contains(event.getBlock().getType().name())){
                plugin.getLuckPerms().getUserManager().modifyUser(event.getPlayer().getUniqueId(), user -> user.data().remove(Node.builder("feather.keepinventory.keep").build()));
                event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("removed-keep")));
                plugin.getLogger().info(event.getPlayer().getName() + " - feather.keepinventory.keep removed - Placed a banned block: " + event.getBlock().getType().name());
            }
        }
    }
}
