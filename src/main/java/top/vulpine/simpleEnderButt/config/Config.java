package top.vulpine.simpleEnderButt.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.Header;
import top.vulpine.commons.log.LogLevel;

import java.util.ArrayList;
import java.util.List;

@Header("SimpleEnderButt Configuration - By Vulpine (https://vulpine.top)")
@Header("")
public class Config extends OkaeriConfig {

    @CustomKey("item")
    public Item item = new Item();

    public static class Item extends OkaeriConfig {

        @CustomKey("name")
        public String name = "<light_purple><b>Simple Ender Butt";

        @CustomKey("lore")
        public List<String> lore = new ArrayList<>(List.of("<gray>Right click to use"));

        @Comment("Hotbar slot the item is put in on join, from 0 to 8")
        @CustomKey("slot")
        public int slot = 0;

        @Comment("How hard the player is launched")
        @CustomKey("power")
        public double power = 3;

        @Comment("If true, players outside creative cannot move the item out of its slot")
        @CustomKey("prevent_click")
        public boolean preventClick = true;

    }

    @CustomKey("sound")
    public Sound sound = new Sound();

    public static class Sound extends OkaeriConfig {

        @CustomKey("enabled")
        public boolean enabled = true;

        @Comment("A sound name like BLOCK_NOTE_BLOCK_BIT, or a key like block.note_block.bit")
        @CustomKey("name")
        public String name = "BLOCK_NOTE_BLOCK_BIT";

        @CustomKey("pitch")
        public float pitch = 1.0f;

        @CustomKey("volume")
        public float volume = 1.0f;

    }

    @CustomKey("messages")
    public Messages messages = new Messages();

    public static class Messages extends OkaeriConfig {

        @Comment("Placeholders: %time%")
        @CustomKey("reloaded")
        public String reloaded = "<gray>[<b><light_purple>SEB</b><gray>] <green>Configuration reloaded in <white>%time%ms<green>.";

    }

    @Comment("Log level for the plugin. Can be: DEBUG, INFO, WARN, ERROR.")
    @Comment("Leave as it is if you don't know what to choose.")
    @CustomKey("log_level")
    public LogLevel logLevel = LogLevel.INFO;

}
