package dev.zerek.featherkeepinventory.listeners;

import dev.zerek.featherkeepinventory.FeatherKeepInventory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangedWorldListener implements Listener {
    private final FeatherKeepInventory plugin;

    public PlayerChangedWorldListener(FeatherKeepInventory plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        if (!event.getPlayer().hasPermission("feather.keepinventory.keep")) return;
        if (!event.getPlayer().getWorld().getName().equals("world_the_end")) return;

        Player player = event.getPlayer();
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),"lp user " + player.getName() + " permission unset feather.keepinventory.keep");
        plugin.getLogger().info(player.getName() + " entered The End - feather.keepinventory.keep permission node removed.");
        plugin.getServer().getOnlinePlayers()
                .stream()
                .filter(player2 -> player2.hasPermission("feather.keepinventory.staff"))
                .forEach(staff -> staff.sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("staff-end-remove-keep"),
                        Placeholder.unparsed("player", player.getName()))));

        player.sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("end-remove-keep")));
    }
}