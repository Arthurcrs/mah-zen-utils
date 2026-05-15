package com.mahghuuuls.mahzenutils.common.config;

import com.mahghuuuls.mahzenutils.mahzenutils.Tags;
import net.minecraftforge.common.config.Config;

@Config(modid = Tags.MOD_NAME)
public class ModConfig {

    @Config.Name("debugCooldowns")
    @Config.Comment({
            "Enables cooldown debug messages in the log."
    })
    public static boolean debugCooldowns = false;

    @Config.Name("debugStacks")
    @Config.Comment({
            "Enables stack debug messages in the log."
    })
    public static boolean debugStacks = false;

    @Config.Name("debugMarkers")
    @Config.Comment({
            "Enables marker debug messages in chat and log."
    })
    public static boolean debugMarkers = false;
}