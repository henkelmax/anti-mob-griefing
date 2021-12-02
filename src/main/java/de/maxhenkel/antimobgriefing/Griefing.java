package de.maxhenkel.antimobgriefing;

import de.maxhenkel.antimobgriefing.events.EggTrampleEvent;
import de.maxhenkel.antimobgriefing.events.WitherRosePlaceEvent;
import de.maxhenkel.antimobgriefing.net.MessageMotion;
import de.maxhenkel.antimobgriefing.net.MessageSpawnFireworks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkDirection;

public class Griefing {

    @SubscribeEvent
    public void onWitherRosePlace(WitherRosePlaceEvent event) {
        if (Main.SERVER_CONFIG.disableWitherRosePlacing.get()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onTrampleEgg(EggTrampleEvent event) {
        if (Main.SERVER_CONFIG.disableEggTrampling.get()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onGriefing(EntityMobGriefingEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Creeper creeper) {
            onCreeperGrief(event, creeper);
        } else if (entity instanceof WitherBoss wither) {
            onWitherGrief(event, wither);
        } else if (entity instanceof EnderMan enderman) {
            onEndermanGrief(event, enderman);
        } else if (entity instanceof Ravager ravager) {
            onRavagerGrief(event, ravager);
        } else if (entity instanceof Silverfish silverfish) {
            onSilverfishGrief(event, silverfish);
        } else if (entity instanceof Fox fox) {
            onFoxGrief(event, fox);
        } else if (entity instanceof Rabbit rabbit) {
            onRabbitGrief(event, rabbit);
        } else if (entity instanceof SnowGolem snowGolem) {
            onSnowGolemGrief(event, snowGolem);
        } else if (entity instanceof Blaze blaze) {
            onBlazeGrief(event, blaze);
        } else if (entity instanceof Ghast ghast) {
            onGhastGrief(event, ghast);
        } else if (entity instanceof Villager villager) {
            onVillagerGrief(event, villager);
        } else if (entity instanceof Sheep sheep) {
            onSheepGrief(event, sheep);
        } else if (entity instanceof Evoker evoker) {
            onEvokerGrief(event, evoker);
        }
    }

    @SubscribeEvent
    public void onBlockBreak(LivingDestroyBlockEvent event) {
        if (event.getEntity() instanceof Zombie) {
            if (event.getState().getBlock() instanceof DoorBlock) {
                if (Main.SERVER_CONFIG.disableZombieDoorBreaking.get()) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onFarmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        if (Main.SERVER_CONFIG.disableCropTrampling.get()) {
            event.setCanceled(true);
        }
    }

    private void onCreeperGrief(EntityMobGriefingEvent event, Creeper creeper) {
        try {
            if (creeper.swell < creeper.maxSwell) {
                return;
            }
        } catch (Exception e) {
            event.setResult(Event.Result.DENY);
            return;
        }

        if (Main.SERVER_CONFIG.disableCreeperExplosionBlockDamage.get()) {
            event.setResult(Event.Result.DENY);
        }

        if (Main.SERVER_CONFIG.disableCreeperDamage.get()) {
            CompoundTag compoundNBT = new CompoundTag();
            creeper.addAdditionalSaveData(compoundNBT);
            compoundNBT.putByte("ExplosionRadius", (byte) 0);
            creeper.readAdditionalSaveData(compoundNBT);
        }

        if (Main.SERVER_CONFIG.creeperFirework.get()) {
            if (creeper.level instanceof ServerLevel) {
                ServerLevel world = (ServerLevel) creeper.level;
                Vec3 pos = creeper.position().add(0D, creeper.getEyeHeight(), 0D);
                MessageSpawnFireworks msg = new MessageSpawnFireworks(pos);

                world.getPlayers(serverPlayerEntity -> Math.sqrt(serverPlayerEntity.distanceToSqr(pos)) < 128D).stream().forEach(serverPlayerEntity -> {
                    Main.SIMPLE_CHANNEL.sendTo(msg, serverPlayerEntity.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                });
            }
        }

        if (Main.SERVER_CONFIG.creeperPotionEffectsEnabled.get()) {
            Main.SERVER_CONFIG.creeperEffects.forEach(creeper::addEffect);
        }

        if (Main.SERVER_CONFIG.creeperKnockback.get()) {
            creeper.level.getEntitiesOfClass(ServerPlayer.class, creeper.getBoundingBox().inflate(4D)).stream().forEach(player -> {
                if (player.getAbilities().flying) {
                    return;
                }
                Vec3 motionVec = player.position().subtract(creeper.position()).normalize().scale(Main.SERVER_CONFIG.creeperKnockbackFactor.get()).add(player.getDeltaMovement()).add(0D, 0.25D, 0D);
                Main.SIMPLE_CHANNEL.sendTo(new MessageMotion(motionVec), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                player.setDeltaMovement(motionVec);
            });
        }
    }

    private void onWitherGrief(EntityMobGriefingEvent event, WitherBoss entity) {
        if (Main.SERVER_CONFIG.disableWitherBlockDamage.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onEndermanGrief(EntityMobGriefingEvent event, EnderMan entity) {
        if (Main.SERVER_CONFIG.disableEndermenBlockPickup.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onRavagerGrief(EntityMobGriefingEvent event, Ravager entity) {
        if (Main.SERVER_CONFIG.disableRavagerBlockBreaking.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onSilverfishGrief(EntityMobGriefingEvent event, Silverfish entity) {
        if (Main.SERVER_CONFIG.disableSilverfishBlockMerging.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onFoxGrief(EntityMobGriefingEvent event, Fox entity) {
        if (Main.SERVER_CONFIG.disableFoxBerryHarvesting.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onRabbitGrief(EntityMobGriefingEvent event, Rabbit entity) {
        if (Main.SERVER_CONFIG.disableRabbitCarrotHarvesting.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onSnowGolemGrief(EntityMobGriefingEvent event, SnowGolem entity) {
        if (Main.SERVER_CONFIG.disableSnowGolemSnowPlacing.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onBlazeGrief(EntityMobGriefingEvent event, Blaze entity) {
        if (Main.SERVER_CONFIG.disableBlazeFire.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onGhastGrief(EntityMobGriefingEvent event, Ghast entity) {
        if (Main.SERVER_CONFIG.disableGhastFireballBlockDamage.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onVillagerGrief(EntityMobGriefingEvent event, Villager entity) {
        if (Main.SERVER_CONFIG.disableVillagerCropFariming.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onSheepGrief(EntityMobGriefingEvent event, Sheep entity) {
        if (Main.SERVER_CONFIG.disableSheepGrassEating.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onEvokerGrief(EntityMobGriefingEvent event, Evoker entity) {
        if (Main.SERVER_CONFIG.disableEvokerSheepConverting.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

}
