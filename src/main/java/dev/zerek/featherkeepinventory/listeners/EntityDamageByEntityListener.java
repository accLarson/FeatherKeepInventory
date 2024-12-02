package dev.zerek.featherkeepinventory.listeners;

import dev.zerek.featherkeepinventory.FeatherKeepInventory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.luckperms.api.node.Node;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class EntityDamageByEntityListener implements Listener {
    private final FeatherKeepInventory plugin;
    public EntityDamageByEntityListener(FeatherKeepInventory plugin) {
        this.plugin = plugin;
    }

    private boolean equipmentPass(Player p) {
        ArrayList<String> playerEquipment = new ArrayList<String>();

        for (ItemStack e : p.getEquipment().getArmorContents()) if (e != null) playerEquipment.add(e.getType().name());
        playerEquipment.add(p.getEquipment().getItemInMainHand().getType().name());
        playerEquipment.add(p.getEquipment().getItemInOffHand().getType().name());
        playerEquipment.retainAll((List<String>) plugin.getConfigMap().get("banned-equipment"));
        return playerEquipment.isEmpty();
    }

    private boolean isPVP(Entity attacker, Entity defender){
        return attacker instanceof Player && defender instanceof Player;
    }

    private boolean effectPass(Player p){
        ArrayList<String> playerEffectsList = new ArrayList<String>();
        p.getActivePotionEffects().forEach(e -> playerEffectsList.add(e.getType().getName()));
        playerEffectsList.retainAll((List<String>) plugin.getConfigMap().get("banned-effects"));
        return playerEffectsList.isEmpty();
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        //If the damage is PvP and the attacker has keep inventory and is not an admin.
        if (isPVP(event.getDamager() , event.getEntity()) && event.getDamager().hasPermission("feather.keepinventory.keep")) {
            Player p = (Player) event.getDamager();
            if (!effectPass(p)) {
                p.removePotionEffect(PotionEffectType.STRENGTH);
                p.sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("removed-strength")));
                plugin.getServer().getOnlinePlayers().stream().filter(player2 -> player2.hasPermission("feather.keepinventory.staff")).forEach(staff -> {
                    staff.sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("staff-remove-strength"), Placeholder.unparsed("player", p.getName())));
                });
                plugin.getLogger().info(p.getName() + " Attacked with STRENGTH effect while also having new player keep-inventory. STRENGTH removed.");

            }
            if (!equipmentPass(p)) {
                plugin.getLuckPerms().getUserManager().modifyUser(p.getUniqueId(), user -> user.data().remove(Node.builder("feather.keepinventory.keep").build()));
                p.sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("removed-keep")));
                plugin.getServer().getOnlinePlayers().stream().filter(player2 -> player2.hasPermission("feather.keepinventory.staff")).forEach(staff -> {
                    staff.sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("staff-remove-keep"), Placeholder.unparsed("player", p.getName())));
                });
                plugin.getLogger().info(p.getName() + " - feather.keepinventory.keep removed - Attacked with high tier equipment");
            }

            //If the damage is PvP and the defender has keep inventory and is not an admin.
            if (isPVP(event.getDamager(), event.getEntity()) && event.getEntity().hasPermission("feather.keepinventory.keep")) {
                if (!equipmentPass((Player) event.getEntity())){
                    plugin.getLuckPerms().getUserManager().modifyUser(event.getEntity().getUniqueId(), user -> user.data().remove(Node.builder("feather.keepinventory.keep").build()));
                    event.getEntity().sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("removed-keep")));
                    plugin.getServer().getOnlinePlayers().stream().filter(player2 -> player2.hasPermission("feather.keepinventory.staff")).forEach(staff -> {
                        staff.sendMessage(MiniMessage.miniMessage().deserialize((String) plugin.getConfigMap().get("staff-remove-keep"), Placeholder.unparsed("player", p.getName())));
                    });
                    plugin.getLogger().info(event.getEntity().getName() + " - feather.keepinventory.keep removed - Was attacked while wearing high tier equipment");

                }
            }
        }
    }
}
