package dev.zerek.featherkeepinventory.tasks;

import dev.zerek.featherkeepinventory.FeatherKeepInventory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Statistic;
import org.bukkit.inventory.Inventory;

public class CheckTimeStatTask implements Runnable{
    private final FeatherKeepInventory plugin;
    private final Integer keepInvMinutes;
    public CheckTimeStatTask(FeatherKeepInventory plugin) {
        this.plugin = plugin;
        this.keepInvMinutes = (Integer) plugin.getConfig().get("new-minutes");
    }

    @Override
    public void run() {
        plugin.getServer().getOnlinePlayers()
                .stream()
                .filter(player -> player.hasPermission("feather.keepinventory.keep"))
                .forEach(player -> {
                    int playerMinutes = (player.getStatistic(Statistic.PLAY_ONE_MINUTE)/20/60);
                    if (playerMinutes >= this.keepInvMinutes) {
                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),"lp user " + player.getName() + " permission unset feather.keepinventory.keep");
                        plugin.getLogger().info(player.getName() + " is no longer considered a new player - feather.keepinventory.keep permission node removed.");
                        plugin.getServer().getOnlinePlayers()
                                .stream()
                                .filter(player2 -> player2.hasPermission("feather.keepinventory.staff"))
                                .forEach(staff -> staff.sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("staff-timer-remove-keep"),
                                        Placeholder.unparsed("player", player.getName()))));

                player.sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("timer-remove-keep")));
            }
        });
    }
}
