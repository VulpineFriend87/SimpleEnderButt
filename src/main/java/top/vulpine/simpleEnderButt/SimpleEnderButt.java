package top.vulpine.simpleEnderButt;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.impl.PlatformScheduler;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import top.vulpine.commons.log.LogAction;
import top.vulpine.commons.log.Logger;
import top.vulpine.commons.text.Colorize;
import top.vulpine.commons.text.Dialect;
import top.vulpine.simpleEnderButt.command.MainCommand;
import top.vulpine.simpleEnderButt.command.annotation.RequiresPermission;
import top.vulpine.simpleEnderButt.config.Config;
import top.vulpine.simpleEnderButt.listener.MainListener;
import top.vulpine.simpleEnderButt.util.PermissionChecker;

import java.io.File;

/**
 * Main class for the SimpleEnderButt plugin.
 * This class initializes the plugin, sets up logging, and registers commands and event listeners.
 */
@Getter
public final class SimpleEnderButt extends JavaPlugin {

    private Config configuration;

    private ItemStack item;
    private final NamespacedKey itemKey = new NamespacedKey(this, "enderbutt");
    private FoliaLib foliaLib;

    private static final String MODRINTH = "https://modrinth.com/plugin/simpleenderbutt";

    private static final int PLUGIN_ID = 31454;

    private static final String MINIMUM = "1.18.2";

    private boolean started;

    private enum Action implements LogAction {
        CONFIG, SETUP
    }

    @Override
    public void onEnable() {

        if (isOlderThan(running(), MINIMUM)) {
            getLogger().severe("SimpleEnderButt needs Minecraft " + MINIMUM + " or newer, this server runs "
                    + running() + ".");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!isPaper()) {
            getLogger().severe("SimpleEnderButt needs Paper or a fork of it, such as Purpur or Folia.");
            getLogger().severe("Latest version: " + MODRINTH);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Colorize.init(Dialect.LEGACY);
        Logger.builder().logger(getComponentLogger()).build();
        this.started = true;

        if (!loadConfiguration()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.foliaLib = new FoliaLib(this);
        Logger.debug(Action.SETUP, "Scheduling through FoliaLib, detected platform: " + foliaLib.getImplType() + ".");

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

        Logger.debug(Action.SETUP, "Registering commands and listeners...");

        Lamp<BukkitCommandActor> lamp = BukkitLamp.builder(this)
                .permissionForAnnotation(RequiresPermission.class, annotation ->
                        actor -> PermissionChecker.hasPermission(actor.sender(), annotation.value()))
                .build();

        lamp.register(new MainCommand(this));

        getServer().getPluginManager().registerEvents(new MainListener(this), this);

        Logger.debug(Action.SETUP, "Initializing metrics...");
        new Metrics(this, PLUGIN_ID);

        new UpdateNotifier(this, "simpleenderbutt",
                "<gray>[<b><light_purple>SEB</b><gray>] <white>A new version of SimpleEnderButt is available! <gray>(<st>%current%</st> <green>%new%<gray>)");

        Logger.system("SimpleEnderButt has been enabled successfully.");
    }

    @Override
    public void onDisable() {

        if (!started) {
            return;
        }

        if (foliaLib != null) {
            foliaLib.getScheduler().cancelAllTasks();
        }

        Logger.close();

    }

    /**
     * Reads the config and rebuilds the EnderButt item from it.
     *
     * @return false if the config could not be read
     */
    public boolean loadConfiguration() {

        try {
            configuration = ConfigManager.create(Config.class, (it) -> {
                it.withConfigurer(new YamlBukkitConfigurer());
                it.withBindFile(new File(this.getDataFolder(), "config.yml"));
                it.saveDefaults();
                it.load(true);
            });
        } catch (Exception e) {
            Logger.error(Action.CONFIG, "Failed to load configuration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        Logger.setLevel(configuration.logLevel);

        item = buildItem(configuration.item);

        return true;
    }

    /**
     * Whether an item is the EnderButt.
     *
     * <p>Checked by tag rather than by comparing it with {@link #getItem()}, so a copy
     * handed out before a reload changed the name or lore is still recognised.</p>
     *
     * @param itemStack the item to check, may be null
     * @return true if it is the EnderButt
     */
    public boolean isEnderButt(ItemStack itemStack) {

        if (itemStack == null || !itemStack.hasItemMeta()) {
            return false;
        }

        return itemStack.getItemMeta().getPersistentDataContainer().has(itemKey, PersistentDataType.BYTE);
    }

    /**
     * Swaps every EnderButt online players are carrying for the current one,
     * so a reload shows up without anyone having to rejoin.
     */
    public void refreshItems() {

        for (Player player : getServer().getOnlinePlayers()) {

            getScheduler().runAtEntity(player, task -> {

                ItemStack[] contents = player.getInventory().getContents();

                for (int slot = 0; slot < contents.length; slot++) {
                    if (isEnderButt(contents[slot])) {
                        player.getInventory().setItem(slot, item);
                    }
                }
            });
        }
    }

    private ItemStack buildItem(Config.Item config) {

        ItemStack itemStack = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = itemStack.getItemMeta();

        // Item text renders italic unless told otherwise, which the names set through
        // the legacy string API never did.
        meta.displayName(upright(Colorize.color(config.name)));
        meta.lore(Colorize.color(config.lore).stream().map(SimpleEnderButt::upright).toList());
        meta.getPersistentDataContainer().set(itemKey, PersistentDataType.BYTE, (byte) 1);

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    private static Component upright(Component component) {
        return Component.empty().decoration(TextDecoration.ITALIC, false).append(component);
    }

    public PlatformScheduler getScheduler() {
        return foliaLib.getScheduler();
    }

    private static boolean isPaper() {

        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * The Minecraft version this server runs.
     *
     * <p>Read from {@code getBukkitVersion}, which every version has, rather than from
     * {@code getMinecraftVersion}, which Paper only added later.</p>
     */
    private String running() {
        return getServer().getBukkitVersion().split("-")[0];
    }

    /**
     * Whether one Minecraft version is older than another.
     *
     * <p>Compared number by number rather than as text, because Paper moved from 1.21 to 26 and
     * every way of ordering those two as strings puts them the wrong way round.</p>
     *
     * @param version what the server reports
     * @param minimum the oldest SimpleEnderButt supports
     * @return whether the server is below it, and false for anything unreadable
     */
    static boolean isOlderThan(String version, String minimum) {

        String[] here = version.split("\\.");
        String[] least = minimum.split("\\.");

        for (int i = 0; i < least.length; i++) {

            int mine;

            try {
                mine = i < here.length ? Integer.parseInt(here[i].trim()) : 0;
            } catch (NumberFormatException e) {
                // An unreadable version is not a reason to refuse to start.
                return false;
            }

            if (mine != Integer.parseInt(least[i])) {
                return mine < Integer.parseInt(least[i]);
            }
        }

        return false;
    }

}
