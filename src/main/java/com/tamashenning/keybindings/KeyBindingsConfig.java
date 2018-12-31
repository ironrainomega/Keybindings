package com.tamashenning.keybindings;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = KeyBindings.MODID)
public class KeyBindingsConfig {

    @Config.Comment("Hello world")
    public static String ExportFullPath = "";

    @Mod.EventBusSubscriber(modid = KeyBindings.MODID)
    private static class Handler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {

            if (event.getModID().equals(KeyBindings.MODID)) {
                ConfigManager.sync(KeyBindings.MODID, Config.Type.INSTANCE);
            }
        }
    }
}
