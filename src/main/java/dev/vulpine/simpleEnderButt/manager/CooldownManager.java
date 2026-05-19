package dev.vulpine.simpleEnderButt.manager;

import dev.vulpine.simpleEnderButt.SimpleEnderButt;
import dev.vulpine.simpleEnderButt.instance.Cooldown;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CooldownManager {

    private final SimpleEnderButt plugin;

    private final HashMap<ItemStack, Cooldown> cooldowns = new HashMap<>();

    public CooldownManager(SimpleEnderButt plugin) {
        this.plugin = plugin;
    }

    public void addCooldown(ItemStack item) {

        cooldowns.remove(item);

        Cooldown cooldown = new Cooldown(this, plugin.getConfigManager().getCooldownTime());

        cooldown.start();
        cooldowns.put(item, cooldown);

        System.out.println("Added cooldown.");

    }

    public void removeCooldown(ItemStack item) {

        if (cooldowns.containsKey(item)) {

            cooldowns.get(item).cancel();

            cooldowns.remove(item);

            System.out.println("Removed cooldown.");
        }

    }

    public void removeCooldown(Cooldown cooldown) {
        cooldowns.entrySet().removeIf(entry -> {
            boolean match = entry.getValue().equals(cooldown);
            if (match) {
                entry.getValue().cancel();
                System.out.println("Removed cooldown.");
            }
            return match;
        });
    }


    public SimpleEnderButt getPlugin() {
        return plugin;
    }

    public HashMap<ItemStack, Cooldown> getCooldowns() {
        return cooldowns;
    }

    public Cooldown getCooldown(ItemStack item) {
        return cooldowns.get(item);
    }
}
