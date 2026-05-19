package dev.vulpine.simpleEnderButt.listener;

import dev.vulpine.simpleEnderButt.SimpleEnderButt;
import dev.vulpine.simpleEnderButt.instance.Cooldown;
import dev.vulpine.simpleEnderButt.manager.CooldownManager;
import dev.vulpine.simpleEnderButt.util.Colorize;
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
            meta.setDisplayName(Colorize.color(plugin.getConfig().getString("item.name")));
            meta.setLore(Colorize.color(plugin.getConfig().getStringList("item.lore")));
            itemStack.setItemMeta(meta);
        }

        this.item = itemStack;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        plugin.getScheduler().runEntityLater(player, () -> player.getInventory().setItem(plugin.getConfig().getInt("item.slot"), item), 10);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRightClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        event.setCancelled(true);

        CooldownManager cooldownManager = plugin.getCooldownManager();
        Cooldown cooldown = cooldownManager.getCooldown(player.getItemInHand());

        if (cooldown != null) {

            if (cooldown.isActive()) {

                player.sendMessage(Colorize.color(plugin.getConfig().getString("messages.cooldown").replace("%cooldown%", String.valueOf(cooldown.getRemainingTime()))));
                return;

            }

        }

        if (player.getInventory().getItemInMainHand().isSimilar(item) && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {

            Vector location = player.getLocation().getDirection();

            player.setVelocity(location.multiply(plugin.getConfig().getDouble("item.power")));

            if (plugin.getConfig().getBoolean("sound.enabled")) {
                player.playSound(player.getLocation(), plugin.getConfig().getString("sound.name"), (float) plugin.getConfig().getDouble("sound.volume"), (float) plugin.getConfig().getDouble("sound.pitch"));
            }

            cooldownManager.addCooldown(player.getItemInHand());

        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (!plugin.getConfig().getBoolean("item.prevent_click") || player.getGameMode() == GameMode.CREATIVE) {

            return;

        }

        if (event.getCurrentItem().isSimilar(item) && event.getSlot() == plugin.getConfig().getInt("item.slot")) {

            event.setCancelled(true);

        }

    }

}
