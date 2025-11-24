package dev.zerek.featherkeepinventory;

import dev.zerek.featherkeepinventory.listeners.*;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import dev.zerek.featherkeepinventory.listeners.PlayerPortalListener;
import dev.zerek.featherkeepinventory.tasks.CheckTimeStatTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class FeatherKeepInventory extends JavaPlugin {
    HashMap<String,Object> configMap = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getConfig().getKeys(false).forEach(c -> configMap.put(c,getConfig().get(c)));

        this.getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(this),this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(this),this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this),this);
        this.getServer().getPluginManager().registerEvents(new PlayerDropItemListener(this),this);
        this.getServer().getPluginManager().registerEvents(new PlayerPortalListener(this),this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new CheckTimeStatTask(this), 0L, 1000L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public HashMap<String, Object> getConfigMap() {
        return configMap;
    }

    public int getRemainingMinutes(Player player) {
        int playerMinutes = (player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20 / 60);
        int newMinutes = (Integer) configMap.get("new-minutes");
        int remainingMinutes = newMinutes - playerMinutes;
        
        // Ensure we show at least 1 minute if they still have the permission
        if (remainingMinutes < 1) remainingMinutes = 1;
        return remainingMinutes;
    }
}
