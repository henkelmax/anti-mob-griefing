package de.maxhenkel.antimobgriefing;

import de.maxhenkel.antimobgriefing.net.MessageMotion;
import de.maxhenkel.antimobgriefing.net.MessageSpawnFireworks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(Main.MODID)
public class Main {

    public static final String MODID = "antimobgriefing";
    public static SimpleChannel SIMPLE_CHANNEL;

    public Main() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);

    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new GriefingEvents());

        SIMPLE_CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Main.MODID, "default"), () -> "1.0.0", s -> true, s -> true);
        SIMPLE_CHANNEL.registerMessage(0, MessageSpawnFireworks.class, (msg, buf) -> msg.toBytes(buf), (buf) -> new MessageSpawnFireworks().fromBytes(buf), (msg, fun) -> msg.executeClientSide(fun.get()));
        SIMPLE_CHANNEL.registerMessage(1, MessageMotion.class, (msg, buf) -> msg.toBytes(buf), (buf) -> new MessageMotion().fromBytes(buf), (msg, fun) -> msg.executeClientSide(fun.get()));
    }

}
