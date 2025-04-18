package dev.zerek.featherkeepinventory;

import dev.zerek.featherkeepinventory.listeners.*;
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
        this.getServer().getPluginManager().registerEvents(new PlayerChangedWorldListener(this),this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new CheckTimeStatTask(this), 0L, 1000L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public HashMap<String, Object> getConfigMap() {
        return configMap;
    }
}
