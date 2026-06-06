package com.mahghuuuls.mahzenutils;

import com.mahghuuuls.mahzenutils.mahzenutils.Tags;
import com.mahghuuuls.mahzenutils.common.event.CapabilityEventHandler;
import com.mahghuuuls.mahzenutils.common.event.PlayerLifecycleHandler;
import com.mahghuuuls.mahzenutils.common.event.TickHandler;
import com.mahghuuuls.mahzenutils.common.init.CapabilityInit;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class MahZenUtilsMod {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CapabilityInit.register();

        MinecraftForge.EVENT_BUS.register(new CapabilityEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerLifecycleHandler());
        MinecraftForge.EVENT_BUS.register(new TickHandler());
    }
}