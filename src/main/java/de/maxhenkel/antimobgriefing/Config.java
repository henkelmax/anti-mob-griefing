package de.maxhenkel.antimobgriefing;

import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Config {

    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    public static ForgeConfigSpec.BooleanValue DISABLE_CROP_TRAMPLING;
    // public static ForgeConfigSpec.BooleanValue DISABLE_EGG_TRAMPLING;
    public static ForgeConfigSpec.BooleanValue DISABLE_CREEPER_EXPLOSION_BLOCK_DAMAGE;
    public static ForgeConfigSpec.BooleanValue DISABLE_CREEPER_DAMAGE;
    public static ForgeConfigSpec.BooleanValue CREEPER_FIREWORK;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> CREEPER_FIREWORK_COLORS;
    public static ForgeConfigSpec.BooleanValue CREEPER_POTION_EFFECTS_ENABLED;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> CREEPER_POTION_EFFECTS;
    public static ForgeConfigSpec.BooleanValue CREEPER_KNOCKBACK;
    public static ForgeConfigSpec.DoubleValue CREEPER_KNOCKBACK_FACTOR;
    public static ForgeConfigSpec.BooleanValue DISABLE_ENDERMEN_BLOCK_PICKUP;
    public static ForgeConfigSpec.BooleanValue DISABLE_RAVAGER_BLOCK_BREAKING;
    public static ForgeConfigSpec.BooleanValue DISABLE_SILVERFISH_BLOCK_MERGING;
    public static ForgeConfigSpec.BooleanValue DISABLE_FOX_BERRY_HARVESTING;
    public static ForgeConfigSpec.BooleanValue DISABLE_RABBIT_CARROT_HARVESTING;
    public static ForgeConfigSpec.BooleanValue DISABLE_ZOMBIE_DOOR_BREAKING;
    public static ForgeConfigSpec.BooleanValue DISABLE_WITHER_BLOCK_DAMAGE;
    public static ForgeConfigSpec.BooleanValue DISABLE_SNOW_GOLEM_SNOW_PLACING;
    public static ForgeConfigSpec.BooleanValue DISABLE_BLAZE_FIRE;
    public static ForgeConfigSpec.BooleanValue DISABLE_WITHER_ROSE_PLACING;
    public static ForgeConfigSpec.BooleanValue DISABLE_GHAST_FIREBALL_BLOCK_DAMAGE;
    public static ForgeConfigSpec.BooleanValue DISABLE_VILLAGER_CROP_FARIMING;
    public static ForgeConfigSpec.BooleanValue DISABLE_SHEEP_GRASS_EATING;
    public static ForgeConfigSpec.BooleanValue DISABLE_EVOKER_SHEEP_CONVERTING;

    static {
        Pair<ServerConfig, ForgeConfigSpec> specPairServer = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_SPEC = specPairServer.getRight();
        SERVER = specPairServer.getLeft();
    }

    public static class ServerConfig {


        public ServerConfig(ForgeConfigSpec.Builder builder) {
            DISABLE_CROP_TRAMPLING = builder
                    .comment("Disable crop trampling")
                    .define("disable_crop_trampling", false);


            DISABLE_WITHER_ROSE_PLACING = builder
                    .comment("Disable placing a Wither Rose when a mob dies from a wither")
                    .define("disable_wither_rose_placing", false);

            /*
            DISABLE_EGG_TRAMPLING = builder
                    .comment("Disable egg trampling")
                    .define("disable_egg_trampling", false);
             */

            DISABLE_CREEPER_EXPLOSION_BLOCK_DAMAGE = builder
                    .comment("Disable Creeper block griefing")
                    .define("creeper.disable_explosion_block_damage", true);

            DISABLE_CREEPER_DAMAGE = builder
                    .comment("Disable Creeper damage")
                    .define("creeper.disable_damage", false);

            CREEPER_FIREWORK = builder
                    .define("creeper.firework.enabled", false);

            CREEPER_FIREWORK_COLORS = builder
                    .comment("The colors that the firework contains")
                    .defineList("creeper.firework.colors", Arrays.asList(DyeColor.values()).stream().map(dyeColor -> dyeColor.getTranslationKey()).collect(Collectors.toList()), Objects::nonNull);

            CREEPER_POTION_EFFECTS_ENABLED = builder
                    .define("creeper.lingering_effects.enabled", false);

            CREEPER_POTION_EFFECTS = builder
                    .comment("The potion effects that the creeper spawns upon exploding")
                    .defineList("creeper.lingering_effects.effects", Arrays.asList(new EffectInstance(Effects.NAUSEA, 200, 1)).stream().map(effectInstance -> NBTUtils.serializeNBT(effectInstance.write(new CompoundNBT()))).collect(Collectors.toList()), Objects::nonNull);

            CREEPER_KNOCKBACK = builder
                    .define("creeper.knockback.enabled", false);

            CREEPER_KNOCKBACK_FACTOR = builder
                    .defineInRange("creeper.knockback.factor", 2.5D, 0D, 5D);

            DISABLE_ENDERMEN_BLOCK_PICKUP = builder
                    .comment("Disable Endermen picking up blocks")
                    .define("enderman.disable_block_pickup", true);

            DISABLE_RAVAGER_BLOCK_BREAKING = builder
                    .comment("Disable Ravager breaking blocks")
                    .define("ravager.disable_block_breaking", true);

            DISABLE_SILVERFISH_BLOCK_MERGING = builder
                    .comment("Disable Silverfishes getting into blocks and destroying blocks")
                    .define("silverfish.disable_block_merging", false);

            DISABLE_FOX_BERRY_HARVESTING = builder
                    .comment("Disable Foxes harvesting berries")
                    .define("fox.disable_berry_harvesting", false);

            DISABLE_RABBIT_CARROT_HARVESTING = builder
                    .comment("Disable Rabbits breaking carrot crops")
                    .define("rabbit.disable_carrot_harvesting", false);

            DISABLE_ZOMBIE_DOOR_BREAKING = builder
                    .comment("Disable Zombies breaking doors")
                    .define("zombie.disable_door_breaking", false);

            DISABLE_WITHER_BLOCK_DAMAGE = builder
                    .comment("Disable Withers breaking blocks")
                    .define("wither.disable_block_damage", false);

            DISABLE_SNOW_GOLEM_SNOW_PLACING = builder
                    .comment("Disable Snow Golems placing snow layers")
                    .define("snow_golem.disable_snow_placing", false);

            DISABLE_BLAZE_FIRE = builder
                    .comment("Disable Blazes creating fire with fireballs")
                    .define("blaze.disable_fire", false);

            DISABLE_GHAST_FIREBALL_BLOCK_DAMAGE = builder
                    .comment("Disable Ghast fireball block damage")
                    .define("ghast.disable_fireball_block_damage", false);

            DISABLE_VILLAGER_CROP_FARIMING = builder
                    .comment("Disable Villager crop farming")
                    .define("villager.disable_crop_farming", false);

            DISABLE_SHEEP_GRASS_EATING = builder
                    .comment("Disable Sheep grass eating")
                    .define("sheep.disable_grass_eating", false);

            DISABLE_EVOKER_SHEEP_CONVERTING = builder
                    .comment("Disable Evoker Sheep color converting")
                    .define("evoker.disable_sheep_converting", false);
        }
    }

    public static List<EffectInstance> getEffects(List<? extends String> list) {
        return list.stream().map(s -> EffectInstance.read(NBTUtils.deserializeNBT(s))).collect(Collectors.toList());
    }

    public static List<Integer> getDyeColors(List<? extends String> list) {
        return list.stream().map(value -> DyeColor.byTranslationKey(value, null)).filter(Objects::nonNull).map(value -> value.getFireworkColor()).collect(Collectors.toList());
    }

}

