package dev.vulpine.simpleEnderButt.command;

import dev.vulpine.simpleEnderButt.SimpleEnderButt;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

    private final SimpleEnderButt plugin;

    public MainCommand(SimpleEnderButt plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String[] message = {
                "",
                "§7 This server is running",
                "",
                "§d SimpleEnderButt",
                "§7 by VulpineFriend87",
                ""
        };

        for (String line : message) {
            sender.sendMessage(line);
        }

        if (sender.hasPermission("simpleenderbutt.admin")) {

            plugin.getConfigManager().reload();

            sender.sendMessage("§a [+] Config reloaded!");
            sender.sendMessage("§r");

        }

        return false;
    }

}
