package top.vulpine.simpleEnderButt.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;
import top.vulpine.commons.log.Logger;
import top.vulpine.simpleEnderButt.SimpleEnderButt;
import top.vulpine.simpleEnderButt.config.Config;
import top.vulpine.simpleEnderButt.util.SoundKeys;

public class MainListener implements Listener {

    private final SimpleEnderButt plugin;

    public MainListener(SimpleEnderButt plugin) {
        this.plugin = plugin;
    }

    /** @return the config as it stands, which a reload replaces wholesale */
    private Config config() {
        return plugin.getConfiguration();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        plugin.getScheduler().runAtEntityLater(player,
                () -> player.getInventory().setItem(config().item.slot, plugin.getItem()), 10);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRightClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (!plugin.isEnderButt(player.getInventory().getItemInMainHand())
                || (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR)) {
            return;
        }

        Vector direction = player.getLocation().getDirection();

        player.setVelocity(direction.multiply(config().item.power));

        Config.Sound sound = config().sound;

        if (sound.enabled) {

            String key = SoundKeys.resolve(sound.name);

            if (key != null) {
                player.playSound(player.getLocation(), key, sound.volume, sound.pitch);
            } else {
                Logger.warn("Unknown sound '" + sound.name + "' in config.yml.");
            }
        }

        event.setCancelled(true);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (!config().item.preventClick || player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (plugin.isEnderButt(event.getCurrentItem()) && event.getSlot() == config().item.slot) {
            event.setCancelled(true);
        }

    }

}
