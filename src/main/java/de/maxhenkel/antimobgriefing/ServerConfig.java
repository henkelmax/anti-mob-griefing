package de.maxhenkel.antimobgriefing;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.maxhenkel.corelib.config.ConfigBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ServerConfig extends ConfigBase {

    public final ForgeConfigSpec.BooleanValue disableCropTrampling;
    public final ForgeConfigSpec.BooleanValue disableEggTrampling;
    public final ForgeConfigSpec.BooleanValue disableCreeperExplosionBlockDamage;
    public final ForgeConfigSpec.BooleanValue disableCreeperDamage;
    public final ForgeConfigSpec.BooleanValue creeperFirework;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> creeperFireworkColors;
    public final ForgeConfigSpec.BooleanValue creeperPotionEffectsEnabled;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> creeperPotionEffects;
    public final ForgeConfigSpec.BooleanValue creeperKnockback;
    public final ForgeConfigSpec.DoubleValue creeperKnockbackFactor;
    public final ForgeConfigSpec.BooleanValue disableEndermenBlockPickup;
    public final ForgeConfigSpec.BooleanValue disableRavagerBlockBreaking;
    public final ForgeConfigSpec.BooleanValue disableSilverfishBlockMerging;
    public final ForgeConfigSpec.BooleanValue disableFoxBerryHarvesting;
    public final ForgeConfigSpec.BooleanValue disableRabbitCarrotHarvesting;
    public final ForgeConfigSpec.BooleanValue disableZombieDoorBreaking;
    public final ForgeConfigSpec.BooleanValue disableWitherBlockDamage;
    public final ForgeConfigSpec.BooleanValue disableSnowGolemSnowPlacing;
    public final ForgeConfigSpec.BooleanValue disableBlazeFire;
    public final ForgeConfigSpec.BooleanValue disableWitherRosePlacing;
    public final ForgeConfigSpec.BooleanValue disableGhastFireballBlockDamage;
    public final ForgeConfigSpec.BooleanValue disableVillagerCropFariming;
    public final ForgeConfigSpec.BooleanValue disableSheepGrassEating;
    public final ForgeConfigSpec.BooleanValue disableEvokerSheepConverting;

    public List<MobEffectInstance> creeperEffects = new ArrayList<>();
    public List<Integer> creeperColors = new ArrayList<>();

    public ServerConfig(ForgeConfigSpec.Builder builder) {
        super(builder);

        disableCropTrampling = builder
                .comment("Disable crop trampling")
                .define("disable_crop_trampling", false);

        disableWitherRosePlacing = builder
                .comment("Disable placing a Wither Rose when a mob dies from a wither")
                .define("disable_wither_rose_placing", false);

        disableEggTrampling = builder
                .comment("Disable egg trampling")
                .define("disable_egg_trampling", false);

        disableCreeperExplosionBlockDamage = builder
                .comment("Disable Creeper block griefing")
                .define("creeper.disable_explosion_block_damage", true);

        disableCreeperDamage = builder
                .comment("Disable Creeper damage")
                .define("creeper.disable_damage", false);

        creeperFirework = builder
                .define("creeper.firework.enabled", false);

        creeperFireworkColors = builder
                .comment("The colors that the firework contains")
                .defineList("creeper.firework.colors", Arrays.asList(DyeColor.values()).stream().map(dyeColor -> dyeColor.getName()).collect(Collectors.toList()), Objects::nonNull);

        creeperPotionEffectsEnabled = builder
                .comment("If creepers should spawn a lingering effect cloud upon exploding")
                .define("creeper.lingering_effects.enabled", false);

        creeperPotionEffects = builder
                .comment("The potion effects that the creeper spawns upon exploding")
                .defineList("creeper.lingering_effects.effects", Arrays.asList(new MobEffectInstance(MobEffects.CONFUSION, 200, 1)).stream().map(effectInstance -> effectInstance.save(new CompoundTag()).toString()).collect(Collectors.toList()), Objects::nonNull);

        creeperKnockback = builder
                .comment("If players should be knocked back when a Creeper explodes")
                .define("creeper.knockback.enabled", false);

        creeperKnockbackFactor = builder
                .comment("The amount of speed applied to the player when knocked back")
                .defineInRange("creeper.knockback.factor", 2.5D, 0D, 5D);

        disableEndermenBlockPickup = builder
                .comment("Disable Endermen picking up blocks")
                .define("enderman.disable_block_pickup", true);

        disableRavagerBlockBreaking = builder
                .comment("Disable Ravager breaking blocks")
                .define("ravager.disable_block_breaking", true);

        disableSilverfishBlockMerging = builder
                .comment("Disable Silverfishes getting into blocks and destroying blocks")
                .define("silverfish.disable_block_merging", false);

        disableFoxBerryHarvesting = builder
                .comment("Disable Foxes harvesting berries")
                .define("fox.disable_berry_harvesting", false);

        disableRabbitCarrotHarvesting = builder
                .comment("Disable Rabbits breaking carrot crops")
                .define("rabbit.disable_carrot_harvesting", false);

        disableZombieDoorBreaking = builder
                .comment("Disable Zombies breaking doors")
                .define("zombie.disable_door_breaking", false);

        disableWitherBlockDamage = builder
                .comment("Disable Withers breaking blocks")
                .define("wither.disable_block_damage", true);

        disableSnowGolemSnowPlacing = builder
                .comment("Disable Snow Golems placing snow layers")
                .define("snow_golem.disable_snow_placing", false);

        disableBlazeFire = builder
                .comment("Disable Blazes creating fire with fireballs")
                .define("blaze.disable_fire", false);

        disableGhastFireballBlockDamage = builder
                .comment("Disable Ghast fireball block damage")
                .define("ghast.disable_fireball_block_damage", false);

        disableVillagerCropFariming = builder
                .comment("Disable Villager crop farming")
                .define("villager.disable_crop_farming", false);

        disableSheepGrassEating = builder
                .comment("Disable Sheep grass eating")
                .define("sheep.disable_grass_eating", false);

        disableEvokerSheepConverting = builder
                .comment("Disable Evoker Sheep color converting")
                .define("evoker.disable_sheep_converting", false);
    }

    @Override
    public void onReload(ModConfigEvent event) {
        super.onReload(event);

        creeperEffects = getEffects(creeperPotionEffects.get());
        creeperColors = getDyeColors(creeperFireworkColors.get());
    }

    public static List<MobEffectInstance> getEffects(List<? extends String> list) {
        return list.stream().map(s -> MobEffectInstance.load(deserializeNBT(s))).collect(Collectors.toList());
    }

    public static List<Integer> getDyeColors(List<? extends String> list) {
        return list.stream().map(value -> DyeColor.byName(value, null)).filter(Objects::nonNull).map(DyeColor::getFireworkColor).collect(Collectors.toList());
    }

    public static CompoundTag deserializeNBT(String json) {
        try {
            return TagParser.parseTag(json);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            return new CompoundTag();
        }
    }

}
