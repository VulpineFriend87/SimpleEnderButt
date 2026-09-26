package top.vulpine.simpleEnderButt.command;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import top.vulpine.commons.text.Colorize;
import top.vulpine.simpleEnderButt.SimpleEnderButt;
import top.vulpine.simpleEnderButt.command.annotation.RequiresPermission;

@Getter
@Command({"simpleenderbutt", "seb", "enderbutt", "eb", "senderbutt", "simpleeb", "senderb"})
public class MainCommand {

    private final SimpleEnderButt plugin;

    public MainCommand(SimpleEnderButt plugin) {
        this.plugin = plugin;
    }

    @Description("Main SimpleEnderButt command")
    public void info(CommandSender sender) {

        sender.sendMessage(Colorize.color(
                "<reset>\n<gray> This server is running\n<reset>\n<light_purple> SimpleEnderButt <gray>[v" + plugin.getDescription().getVersion() + "] " +
                        "\n<gray> By " + String.join(", ", plugin.getDescription().getAuthors()) +
                        "\n<reset>"
        ));

    }

    @Subcommand("reload")
    @RequiresPermission("command.reload")
    @Description("Reloads the SimpleEnderButt configuration")
    public void reload(CommandSender sender) {

        long startTime = System.currentTimeMillis();
        plugin.loadConfiguration();
        plugin.refreshItems();
        long duration = System.currentTimeMillis() - startTime;

        sender.sendMessage(Colorize.color(
                plugin.getConfiguration().messages.reloaded.replace("%time%", String.valueOf(duration))
        ));

    }

}
