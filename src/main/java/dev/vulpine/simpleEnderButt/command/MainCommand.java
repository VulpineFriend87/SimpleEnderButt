package dev.vulpine.simpleEnderButt.command;

import dev.vulpine.simpleEnderButt.SimpleEnderButt;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    private final SimpleEnderButt plugin;

    public MainCommand(SimpleEnderButt plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }

}
