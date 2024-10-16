package dev.vulpine.simpleEnderButt.listener;

import dev.vulpine.simpleEnderButt.SimpleEnderButt;
import it.vulpinefriend87.easyutils.EasyItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;

public class MainListener implements Listener {

    private final SimpleEnderButt plugin;

    EasyItem item;

    public MainListener(SimpleEnderButt plugin) {
        this.plugin = plugin;

        item = new EasyItem(Material.ENDER_PEARL, plugin.getConfigManager().getItemName(), plugin.getConfigManager().getItemLore());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> player.getInventory().setItem(plugin.getConfigManager().getItemSlot(), item.getItemStack()), 10);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRightClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (player.getItemInHand().isSimilar(item.getItemStack()) && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {

            Vector location = player.getLocation().getDirection();

            player.setVelocity(location.multiply(plugin.getConfigManager().getItemPower()));

            if (plugin.getConfigManager().getSoundEnabled()) {
                player.playSound(player.getLocation(), plugin.getConfigManager().getSound(), plugin.getConfigManager().getSoundVolume(), plugin.getConfigManager().getSoundPitch());
            }

            event.setCancelled(true);

        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem().isSimilar(item.getItemStack()) && event.getSlot() == plugin.getConfigManager().getItemSlot()) {

            event.setCancelled(true);

        }

    }

}
