package dev.zerek.featherkeepinventory.listeners;

import dev.zerek.featherkeepinventory.FeatherKeepInventory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

public class EntityDamageByEntityListener implements Listener {

    private final FeatherKeepInventory plugin;
    private final List<Player> informedPlayers = new ArrayList<>();

    public EntityDamageByEntityListener(FeatherKeepInventory plugin) {
        this.plugin = plugin;
    }


    private boolean isPVP(Entity attacker, Entity defender){
        return attacker instanceof Player && defender instanceof Player;
    }


    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (!isPVP(event.getDamager(), event.getEntity())) return;

        Player attacker = (Player) event.getDamager();

        if (!attacker.hasPermission("feather.keepinventory.keep")) return;

        if (this.informedPlayers.contains(attacker)) return;

        attacker.sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("pvp-inform")));
        this.informedPlayers.add(attacker);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> this.informedPlayers.remove(attacker), 1200L);
    }
}
