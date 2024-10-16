package dev.vulpine.simpleEnderButt.manager;

import dev.vulpine.simpleEnderButt.SimpleEnderButt;
import it.vulpinefriend87.easyutils.Colorize;
import org.bukkit.Sound;

import java.util.List;

public record ConfigManager(SimpleEnderButt plugin) {

    public ConfigManager(SimpleEnderButt plugin) {
        this.plugin = plugin;

        plugin.getServer().getConsoleSender().sendMessage(Colorize.color("  &7[!] Loading config.yml..."));

        try {
            plugin.saveDefaultConfig();
        } catch (Exception e) {
            plugin.getServer().getConsoleSender().sendMessage(Colorize.color("  &c[-] Failed to load config.yml!\nError: " + e.getMessage()));
            return;
        }

        plugin.getServer().getConsoleSender().sendMessage(Colorize.color("  &a[+] Config.yml successfully loaded!"));
    }

    public void reload() {
        plugin.reloadConfig();
    }

    public String getCooldown() {
        String cooldown;

        cooldown = plugin.getConfig().getString("cooldown");

        if (cooldown == "" || cooldown == "-1") {

            return null;

        }

        return cooldown;
    }

    public String getItemName() {
        return Colorize.color(plugin.getConfig().getString("item.name"));
    }

    public List<String> getItemLore() {
        return plugin.getConfig().getStringList("item.lore");
    }

    public int getItemSlot() {
        return plugin.getConfig().getInt("item.slot");
    }

    public int getItemPower() {
        return plugin.getConfig().getInt("item.power");
    }

    public boolean getItemPreventClick() {
        return plugin.getConfig().getBoolean("item.prevent_click");
    }

    public boolean getSoundEnabled() {
        return plugin.getConfig().getBoolean("sound.enabled");
    }

    public String getSoundName() {
        return plugin.getConfig().getString("sound.name");
    }

    public Sound getSound() {
        return Sound.valueOf(getSoundName());
    }

    public float getSoundPitch() {
        return (float) plugin.getConfig().getDouble("sound.pitch");
    }

    public float getSoundVolume() {
        return (float) plugin.getConfig().getDouble("sound.volume");
    }

    public String getCooldownMessage() {
        return Colorize.color(plugin.getConfig().getString("messages.cooldown"));
    }

}
