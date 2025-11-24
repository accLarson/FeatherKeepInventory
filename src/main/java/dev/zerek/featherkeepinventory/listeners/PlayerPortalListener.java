package dev.zerek.featherkeepinventory.listeners;

import dev.zerek.featherkeepinventory.FeatherKeepInventory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.util.Vector;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PlayerPortalListener implements Listener {
    private final FeatherKeepInventory plugin;
    private final List<Player> recentlyBlocked = new ArrayList<>();

    public PlayerPortalListener(FeatherKeepInventory plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        player.setVelocity(new Vector(0, 0, 0));

        if (!player.hasPermission("feather.keepinventory.keep")) return;
        if (event.getTo().getWorld() == null) return;
        if (!event.getTo().getWorld().getEnvironment().equals(World.Environment.THE_END)) return;

        event.setCancelled(true);
        
        int remainingMinutes = plugin.getRemainingMinutes(player);
        
        player.setVelocity(new Vector(0, 1, 0));

        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 25, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 40, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 80, 0));
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> { if (player.isOnline()) player.setFireTicks(0); }, 5L);

        if (!this.recentlyBlocked.contains(player)) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(
                    (String) plugin.getConfigMap().get("end-entry-blocked"),
                    Placeholder.unparsed("remaining", String.valueOf(remainingMinutes))
            ));

            plugin.getServer().getOnlinePlayers()
                    .stream()
                    .filter(staff -> staff.hasPermission("feather.keepinventory.staff"))
                    .forEach(staff -> staff.sendMessage(MiniMessage.miniMessage().deserialize(
                            (String) plugin.getConfigMap().get("staff-end-entry-blocked"),
                            Placeholder.unparsed("player", player.getName()),
                            Placeholder.unparsed("remaining", String.valueOf(remainingMinutes))
                    )));

            this.recentlyBlocked.add(player);
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> this.recentlyBlocked.remove(player), 500L);
        }
        
        plugin.getLogger().info(player.getName() + " attempted to enter The End with keep-inventory and was blocked.");
    }
}
