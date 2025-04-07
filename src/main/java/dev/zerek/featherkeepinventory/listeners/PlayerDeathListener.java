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
                plugin.getServer().broadcast(MiniMessage.miniMessage().deserialize(
                        (String) plugin.getConfigMap().get("killed-announce"),
                        Placeholder.unparsed("player", event.getPlayer().getName())
                ));
            }
        }
        if (event.getPlayer().getKiller() == null) return;
        if (event.getPlayer().getKiller().hasPermission("feather.keepinventory.keep")) {
            event.setKeepInventory(true);
            event.getDrops().clear();
            event.setShouldDropExperience(false);

            if (!event.getPlayer().hasPermission("feather.deathmessage.silent")) {
                plugin.getServer().broadcast(MiniMessage.miniMessage().deserialize(
                        (String) plugin.getConfigMap().get("killing-announce"),
                        Placeholder.unparsed("attacker", event.getPlayer().getKiller().getName()),
                        Placeholder.unparsed("defender", event.getPlayer().getName())
                ));
            }
        }
    }
}
