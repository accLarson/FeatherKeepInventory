package com.zerek.featherkeepinventory.tasks;

import com.zerek.featherkeepinventory.FeatherKeepInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.node.Node;
import org.bukkit.Statistic;

import static net.kyori.adventure.text.Component.text;

public class CheckTimeStatTask implements Runnable{
    private final FeatherKeepInventory plugin;
    public CheckTimeStatTask(FeatherKeepInventory plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getServer().getOnlinePlayers().stream().filter(player -> player.hasPermission("feather.keepinventory.keep")).filter(player -> !player.hasPermission("group.administrator")).forEach(player -> {
            if ((player.getStatistic(Statistic.PLAY_ONE_MINUTE)/20/60) > (Integer) plugin.getConfigMap().get("new-minutes")) {
                plugin.getLuckPerms().getUserManager().modifyUser(player.getUniqueId(), user -> user.data().remove(Node.builder("feather.keepinventory.keep").build()));
                plugin.getLogger().info(player.getName() + " is no longer considered a new player - feather.keepinventory.keep permission node removed.");
                player.sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("timer-remove-keep")));
            }
        });
    }
}
