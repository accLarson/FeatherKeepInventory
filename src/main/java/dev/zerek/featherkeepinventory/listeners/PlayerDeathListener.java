package dev.zerek.featherkeepinventory.listeners;

import dev.zerek.featherkeepinventory.FeatherKeepInventory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    private final FeatherKeepInventory plugin;
    public PlayerDeathListener(FeatherKeepInventory plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event){
        if (event.getPlayer().hasPermission("feather.keepinventory.keep")) {
            event.setKeepInventory(true);
            event.getDrops().clear();
            event.setShouldDropExperience(false);

            if (!event.getPlayer().hasPermission("feather.deathmessage.silent")) {
                // Capture variables before the delay
                final String playerName = event.getPlayer().getName();
                final int remainingMinutes = plugin.getRemainingMinutes(event.getPlayer());
                
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    plugin.getServer().broadcast(MiniMessage.miniMessage().deserialize(
                        (String) plugin.getConfigMap().get("killed-announce"),
                        Placeholder.unparsed("player", playerName),
                        Placeholder.unparsed("remaining", String.valueOf(remainingMinutes))
                    ));
                }, 5L);
            }
        }
        if (event.getPlayer().getKiller() == null) return;
        if (event.getPlayer().getKiller().hasPermission("feather.keepinventory.keep")) {
            event.setKeepInventory(true);
            event.getDrops().clear();
            event.setShouldDropExperience(false);

            if (!event.getPlayer().hasPermission("feather.deathmessage.silent")) {
                // Capture variables before the delay
                final String attackerName = event.getPlayer().getKiller().getName();
                final String defenderName = event.getPlayer().getName();
                final int remainingMinutes = plugin.getRemainingMinutes(event.getPlayer().getKiller());
                
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    plugin.getServer().broadcast(MiniMessage.miniMessage().deserialize(
                        (String) plugin.getConfigMap().get("killing-announce"),
                        Placeholder.unparsed("attacker", attackerName),
                        Placeholder.unparsed("defender", defenderName),
                        Placeholder.unparsed("remaining", String.valueOf(remainingMinutes))
                    ));
                }, 5L);
            }
        }
    }
}
