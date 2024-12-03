package dev.zerek.featherkeepinventory.listeners;

import dev.zerek.featherkeepinventory.FeatherKeepInventory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;


public class PlayerDropItemListener implements Listener {
    private final FeatherKeepInventory plugin;

    public PlayerDropItemListener(FeatherKeepInventory plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!event.getPlayer().hasPermission("feather.keepinventory.keep")) return;
        Player player = event.getPlayer();
        Item item = event.getItemDrop();
        item.setOwner(player.getUniqueId());
        player.sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("pickup-delay-message")));
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> item.setOwner(null), 160L);

    }
}
