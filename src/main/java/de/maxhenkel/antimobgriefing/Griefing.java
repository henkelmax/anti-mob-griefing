package de.maxhenkel.antimobgriefing;

import de.maxhenkel.antimobgriefing.events.EggTrampleEvent;
import de.maxhenkel.antimobgriefing.events.WitherRosePlaceEvent;
import de.maxhenkel.antimobgriefing.net.MessageMotion;
import de.maxhenkel.antimobgriefing.net.MessageSpawnFireworks;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;

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
        if (entity instanceof CreeperEntity) {
            onCreeperGrief(event, (CreeperEntity) entity);
        } else if (entity instanceof WitherEntity) {
            onWitherGrief(event, (WitherEntity) entity);
        } else if (entity instanceof EndermanEntity) {
            onEndermanGrief(event, (EndermanEntity) entity);
        } else if (entity instanceof RavagerEntity) {
            onRavagerGrief(event, (RavagerEntity) entity);
        } else if (entity instanceof SilverfishEntity) {
            onSilverfishGrief(event, (SilverfishEntity) entity);
        } else if (entity instanceof FoxEntity) {
            onFoxGrief(event, (FoxEntity) entity);
        } else if (entity instanceof RabbitEntity) {
            onRabbitGrief(event, (RabbitEntity) entity);
        } else if (entity instanceof SnowGolemEntity) {
            onSnowGolemGrief(event, (SnowGolemEntity) entity);
        } else if (entity instanceof BlazeEntity) {
            onBlazeGrief(event, (BlazeEntity) entity);
        } else if (entity instanceof GhastEntity) {
            onGhastGrief(event, (GhastEntity) entity);
        } else if (entity instanceof VillagerEntity) {
            onVillagerGrief(event, (VillagerEntity) entity);
        } else if (entity instanceof SheepEntity) {
            onSheepGrief(event, (SheepEntity) entity);
        } else if (entity instanceof EvokerEntity) {
            onEvokerGrief(event, (EvokerEntity) entity);
        }
    }

    @SubscribeEvent
    public void onBlockBreak(LivingDestroyBlockEvent event) {
        if (event.getEntity() instanceof ZombieEntity) {
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

    private void onCreeperGrief(EntityMobGriefingEvent event, CreeperEntity creeper) {
        if (Main.SERVER_CONFIG.disableCreeperExplosionBlockDamage.get()) {
            event.setResult(Event.Result.DENY);
        }

        if (Main.SERVER_CONFIG.disableCreeperDamage.get()) {
            CompoundNBT compoundNBT = new CompoundNBT();
            creeper.addAdditionalSaveData(compoundNBT);
            compoundNBT.putByte("ExplosionRadius", (byte) 0);
            creeper.readAdditionalSaveData(compoundNBT);
        }

        if (Main.SERVER_CONFIG.creeperFirework.get()) {
            if (creeper.level instanceof ServerWorld) {
                ServerWorld world = (ServerWorld) creeper.level;
                Vector3d pos = creeper.position().add(0D, creeper.getEyeHeight(), 0D);
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
            creeper.level.getEntitiesOfClass(ServerPlayerEntity.class, creeper.getBoundingBox().inflate(4D)).stream().forEach(player -> {
                if (player.abilities.flying) {
                    return;
                }
                Vector3d motionVec = player.position().subtract(creeper.position()).normalize().scale(Main.SERVER_CONFIG.creeperKnockbackFactor.get()).add(player.getDeltaMovement()).add(0D, 0.25D, 0D);
                Main.SIMPLE_CHANNEL.sendTo(new MessageMotion(motionVec), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                player.setDeltaMovement(motionVec);
            });
        }
    }

    private void onWitherGrief(EntityMobGriefingEvent event, WitherEntity entity) {
        if (Main.SERVER_CONFIG.disableWitherBlockDamage.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onEndermanGrief(EntityMobGriefingEvent event, EndermanEntity entity) {
        if (Main.SERVER_CONFIG.disableEndermenBlockPickup.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onRavagerGrief(EntityMobGriefingEvent event, RavagerEntity entity) {
        if (Main.SERVER_CONFIG.disableRavagerBlockBreaking.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onSilverfishGrief(EntityMobGriefingEvent event, SilverfishEntity entity) {
        if (Main.SERVER_CONFIG.disableSilverfishBlockMerging.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onFoxGrief(EntityMobGriefingEvent event, FoxEntity entity) {
        if (Main.SERVER_CONFIG.disableFoxBerryHarvesting.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onRabbitGrief(EntityMobGriefingEvent event, RabbitEntity entity) {
        if (Main.SERVER_CONFIG.disableRabbitCarrotHarvesting.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onSnowGolemGrief(EntityMobGriefingEvent event, SnowGolemEntity entity) {
        if (Main.SERVER_CONFIG.disableSnowGolemSnowPlacing.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onBlazeGrief(EntityMobGriefingEvent event, BlazeEntity entity) {
        if (Main.SERVER_CONFIG.disableBlazeFire.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onGhastGrief(EntityMobGriefingEvent event, GhastEntity entity) {
        if (Main.SERVER_CONFIG.disableGhastFireballBlockDamage.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onVillagerGrief(EntityMobGriefingEvent event, VillagerEntity entity) {
        if (Main.SERVER_CONFIG.disableVillagerCropFariming.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onSheepGrief(EntityMobGriefingEvent event, SheepEntity entity) {
        if (Main.SERVER_CONFIG.disableSheepGrassEating.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onEvokerGrief(EntityMobGriefingEvent event, EvokerEntity entity) {
        if (Main.SERVER_CONFIG.disableEvokerSheepConverting.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

}
