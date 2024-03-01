package dev.zerek.featherkeepinventory;

import dev.zerek.featherkeepinventory.listeners.*;
import dev.zerek.featherkeepinventory.tasks.CheckTimeStatTask;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class FeatherKeepInventory extends JavaPlugin {
    HashMap<String,Object> configMap = new HashMap<>();
    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        luckPerms = Bukkit.getServicesManager().getRegistration(LuckPerms.class).getProvider();
        getConfig().getKeys(false).forEach(c -> configMap.put(c,getConfig().get(c)));
        this.getServer().getPluginManager().registerEvents(new BlockPlaceListener(this),this);
        this.getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(this),this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(this),this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(this),this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this),this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new CheckTimeStatTask(this), 0L, 1200L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public HashMap<String, Object> getConfigMap() {
        return configMap;
    }
    public LuckPerms getLuckPerms() {
        return luckPerms;
    }
}
