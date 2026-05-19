package dev.vulpine.simpleEnderButt;

import dev.vulpine.simpleEnderButt.command.MainCommand;
import dev.vulpine.simpleEnderButt.listener.MainListener;
import dev.vulpine.simpleEnderButt.scheduler.BukkitSchedulerAdapter;
import dev.vulpine.simpleEnderButt.scheduler.FoliaScheduler;
import dev.vulpine.simpleEnderButt.scheduler.SchedulerAdapter;
import dev.vulpine.simpleEnderButt.util.logger.LogLevel;
import dev.vulpine.simpleEnderButt.util.logger.Logger;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SimpleEnderButt extends JavaPlugin {

    private SchedulerAdapter scheduler;

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

        boolean folia;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        } catch (ClassNotFoundException e) {
            folia = false;
        }
        this.scheduler = folia ? new FoliaScheduler(this) : new BukkitSchedulerAdapter(this);
        Logger.debug("Detected " + (folia ? "Folia" : "Bukkit/Spigot") + " server, using "
                + scheduler.getClass().getSimpleName());


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

        new UpdateNotifier(this, "simpleenderbutt",
                "<gray>[<b><light_purple>SEB</b><gray>] <white>A new version of SimpleEnderButt is available! <gray>(<st>%current%</st> <green>%new%<gray>)");

        Logger.debug("SimpleEnderButt has been enabled succesfully.");

    }

}
