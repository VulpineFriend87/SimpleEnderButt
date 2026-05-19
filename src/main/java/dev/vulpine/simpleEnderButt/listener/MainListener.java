package dev.vulpine.simpleEnderButt.listener;

import dev.vulpine.simpleEnderButt.SimpleEnderButt;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class MainListener implements Listener {

    private final SimpleEnderButt plugin;

    private final ItemStack item;

    public MainListener(SimpleEnderButt plugin) {
        this.plugin = plugin;

        ItemStack itemStack = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(plugin.getConfigManager().getItemName());
            meta.setLore(plugin.getConfigManager().getItemLore());
            itemStack.setItemMeta(meta);
        }

        this.item = itemStack;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> player.getInventory().setItem(plugin.getConfigManager().getItemSlot(), item), 10);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRightClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (player.getInventory().getItemInMainHand().isSimilar(item) && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {

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

        if (!plugin.getConfigManager().getItemPreventClick() || player.getGameMode() == GameMode.CREATIVE) {

            return;

        }

        if (event.getCurrentItem().isSimilar(item) && event.getSlot() == plugin.getConfigManager().getItemSlot()) {

            event.setCancelled(true);

        }

    }

}
