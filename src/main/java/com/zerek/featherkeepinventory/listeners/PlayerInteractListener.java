package com.zerek.featherkeepinventory.listeners;

import com.zerek.featherkeepinventory.FeatherKeepInventory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.node.Node;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    private final FeatherKeepInventory plugin;
    public PlayerInteractListener(FeatherKeepInventory plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if (event.getPlayer().hasPermission("feather.keepinventory.keep") && !event.getPlayer().hasPermission("group.administrator")){
            if (Action.RIGHT_CLICK_BLOCK == event.getAction() && Material.OBSIDIAN == event.getClickedBlock().getType() && Material.END_CRYSTAL == event.getMaterial()) {
                plugin.getLuckPerms().getUserManager().modifyUser(event.getPlayer().getUniqueId(), user -> user.data().remove(Node.builder("feather.keepinventory.keep").build()));
                event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("removed-keep")));
                plugin.getLogger().info(event.getPlayer().getName() + " - feather.keepinventory.keep removed - Placing Crystal");

            }
        }
    }
}
