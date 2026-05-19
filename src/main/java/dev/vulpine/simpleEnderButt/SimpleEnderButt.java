package dev.vulpine.simpleEnderButt;

import dev.vulpine.simpleEnderButt.command.MainCommand;
import dev.vulpine.simpleEnderButt.listener.MainListener;
import dev.vulpine.simpleEnderButt.util.logger.LogLevel;
import dev.vulpine.simpleEnderButt.util.logger.Logger;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SimpleEnderButt extends JavaPlugin {


    @Override
    public void onEnable() {

        saveDefaultConfig();

        LogLevel logLevel;
        try {
            logLevel = LogLevel.valueOf(getConfig().getString("log_level"));
        } catch (IllegalArgumentException e) {
            Logger.warn("Invalid log level in config, defaulting to INFO");
            logLevel = LogLevel.INFO;
        }
        Logger.init(logLevel);

        String[] message = {
                "",
                "<light_purple>  _____ _____ _____ ",
                "<light_purple> |   __|   __| __  |",
                "<light_purple> |__   |   __| __ -|",
                "<light_purple> |_____|_____|_____|",
                "",
                "<gray> By <light_purple>" + String.join(", ", getDescription().getAuthors()),
                "<gray> Version: <light_purple>" + getDescription().getVersion(),
                ""
        };

        for (String line : message) {
            Logger.system(line);
        }

        Logger.debug("Registering commands and listeners...");
        getServer().getPluginManager().registerEvents(new MainListener(this), this);

        getCommand("simpleenderbutt").setExecutor(new MainCommand(this));

        Logger.debug("SimpleEnderButt has been enabled succesfully.");

    }

}
