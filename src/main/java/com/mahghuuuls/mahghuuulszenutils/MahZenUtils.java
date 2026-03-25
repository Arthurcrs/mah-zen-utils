package com.mahghuuuls.mahghuuulszenutils;

import com.mahghuuuls.mahghuuulszenutils.common.event.CapabilityEventHandler;
import com.mahghuuuls.mahghuuulszenutils.common.event.PlayerLifecycleHandler;
import com.mahghuuuls.mahghuuulszenutils.common.event.TickHandler;
import com.mahghuuuls.mahghuuulszenutils.common.init.CapabilityInit;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = MahZenUtils.MOD_ID, name = MahZenUtils.NAME, version = MahZenUtils.VERSION, dependencies = MahZenUtils.DEPENDENCIES)
public class MahZenUtils {
	public static final String MOD_ID = "mahzenutils";
	public static final String NAME = "Mah Zen Utils";
	public static final String VERSION = "0.1.0";
	public static final String DEPENDENCIES = "";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CapabilityInit.register();

        MinecraftForge.EVENT_BUS.register(new CapabilityEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerLifecycleHandler());
        MinecraftForge.EVENT_BUS.register(new TickHandler());
    }

}
