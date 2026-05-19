package dev.vulpine.simpleEnderButt.command;

import dev.vulpine.simpleEnderButt.SimpleEnderButt;
import dev.vulpine.simpleEnderButt.util.Colorize;
import dev.vulpine.simpleEnderButt.util.PermissionChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {

    private final SimpleEnderButt plugin;

    public MainCommand(SimpleEnderButt plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        String[] message = {
                "",
                "<gray> This server is running",
                "",
                "<light_purple> SimpleEnderButt",
                "<gray> by VulpineFriend87",
                ""
        };

        for (String line : message) {
            sender.sendMessage(Colorize.color(line));
        }

        if (PermissionChecker.hasPermission(sender, "reload")) {

            plugin.reloadConfig();

            sender.sendMessage(Colorize.color("<green> [+] Config reloaded!"));
            sender.sendMessage(Colorize.color("<reset>"));

        }

        return false;
    }

}
