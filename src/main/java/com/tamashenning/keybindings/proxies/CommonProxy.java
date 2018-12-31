package com.tamashenning.keybindings.proxies;

import com.tamashenning.keybindings.KeyBindings;
import com.tamashenning.keybindings.KeyBindingsConfig;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        KeyBindings.configurationDirectory = event.getModConfigurationDirectory();
        KeyBindingsConfig.ExportFullPath = KeyBindings.configurationDirectory.getPath() + "\\keybindings.json";
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }

}
