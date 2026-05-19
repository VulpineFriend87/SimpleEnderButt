package dev.vulpine.simpleEnderButt;

import dev.vulpine.simpleEnderButt.command.MainCommand;
import dev.vulpine.simpleEnderButt.listener.MainListener;
import dev.vulpine.simpleEnderButt.manager.ConfigManager;
import dev.vulpine.simpleEnderButt.util.Colorize;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SimpleEnderButt extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {

        String[] message = {
                "",
                "§d  _____ _____ _____ ",
                "§d |   __|   __| __  |  &dSimpleEnderButt",
                "§d |__   |   __| __ -|  &7By VulpineFriend87",
                "§d |_____|_____|_____|",
                ""
        };

        for (String line : message) {
            getServer().getConsoleSender().sendMessage(Colorize.color(line));
        }

        getServer().getConsoleSender().sendMessage(Colorize.color("&7 [!] Initializing ConfigManager..."));

        configManager = new ConfigManager(this);

        getServer().getConsoleSender().sendMessage(Colorize.color("&7 [!] Inizializing MainListener..."));

        getServer().getPluginManager().registerEvents(new MainListener(this), this);

        getServer().getConsoleSender().sendMessage(Colorize.color("&7 [!] Inizializing MainCommand..."));

        getCommand("simpleenderbutt").setExecutor(new MainCommand(this));

        getServer().getConsoleSender().sendMessage(Colorize.color("&a [+] Plugin started!"));

        getServer().getConsoleSender().sendMessage("&7");

    }

}
