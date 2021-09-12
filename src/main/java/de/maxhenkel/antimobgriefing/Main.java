package de.maxhenkel.antimobgriefing;

import de.maxhenkel.antimobgriefing.net.MessageMotion;
import de.maxhenkel.antimobgriefing.net.MessageSpawnFireworks;
import de.maxhenkel.corelib.CommonRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

@Mod(Main.MODID)
public class Main {

    public static final String MODID = "antimobgriefing";
    public static SimpleChannel SIMPLE_CHANNEL;

    public static ServerConfig SERVER_CONFIG;

    public Main() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);

        SERVER_CONFIG = CommonRegistry.registerConfig(ModConfig.Type.SERVER, ServerConfig.class, true);
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new Griefing());

        SIMPLE_CHANNEL = CommonRegistry.registerChannel(Main.MODID, "default");

        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 0, MessageSpawnFireworks.class);
        CommonRegistry.registerMessage(SIMPLE_CHANNEL, 1, MessageMotion.class);
    }

}
