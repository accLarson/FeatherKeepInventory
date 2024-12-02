package dev.zerek.featherkeepinventory.listeners;

import dev.zerek.featherkeepinventory.FeatherKeepInventory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.luckperms.api.node.Node;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

public class BlockPlaceListener implements Listener {
    private final FeatherKeepInventory plugin;
    public BlockPlaceListener(FeatherKeepInventory plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if (event.getPlayer().hasPermission("feather.keepinventory.keep")){
            if (((List<String>) plugin.getConfigMap().get("banned-blocks")).contains(event.getBlock().getType().name())){
                plugin.getLuckPerms().getUserManager().modifyUser(event.getPlayer().getUniqueId(), user -> user.data().remove(Node.builder("feather.keepinventory.keep").build()));
                event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("removed-keep")));
                plugin.getServer().getOnlinePlayers().stream().filter(player2 -> player2.hasPermission("feather.keepinventory.staff")).forEach(staff -> {
                    staff.sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("staff-remove-keep"), Placeholder.unparsed("player", event.getPlayer().getName())));
                });
                plugin.getLogger().info(event.getPlayer().getName() + " - feather.keepinventory.keep removed - Placed a banned block: " + event.getBlock().getType().name());
            }
        }
    }
}
