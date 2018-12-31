package com.tamashenning.keybindings;

import com.tamashenning.keybindings.proxies.CommonProxy;
import com.google.gson.Gson;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = KeyBindings.MODID, name = KeyBindings.NAME, version = KeyBindings.VERSION, acceptableRemoteVersions = "*", clientSideOnly = true)
public class KeyBindings
{
    public static final String MODID = "keybindings";
    public static final String NAME = "A way to import/export key bindigs. Client side only!";
    public static final String VERSION = "1.0";

    public static Logger logger;
    public static Gson gson = new Gson();
    public static File configurationDirectory;


    @Mod.Instance(KeyBindings.MODID)
    public static KeyBindings instance;

    @SidedProxy(clientSide = "com.tamashenning.keybindings.proxies.ClientProxy", serverSide = "com.tamashenning.keybindings.proxies.ServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);

    }
}
