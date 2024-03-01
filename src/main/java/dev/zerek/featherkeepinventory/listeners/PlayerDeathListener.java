package dev.zerek.featherkeepinventory.listeners;

import dev.zerek.featherkeepinventory.FeatherKeepInventory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
            plugin.getServer().getOnlinePlayers().stream().filter(player2 -> player2.hasPermission("feather.keepinventory.staff")).forEach(staff -> {
                staff.sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("staff-keep"), Placeholder.unparsed("player", event.getPlayer().getName())));
            });
        }
    }
}
