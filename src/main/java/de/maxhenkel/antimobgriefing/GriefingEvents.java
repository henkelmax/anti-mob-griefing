package de.maxhenkel.antimobgriefing;

import de.maxhenkel.antimobgriefing.net.MessageMotion;
import de.maxhenkel.antimobgriefing.net.MessageSpawnFireworks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.network.NetworkDirection;

import java.lang.reflect.Field;

public class GriefingEvents {

    @SubscribeEvent
    public void onMobGriefing(EntityMobGriefingEvent event) {
        Entity entity = event.getEntity();
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        for (int i = 0; i < trace.length; i++) {
            StackTraceElement element = trace[i];
            if (element.getClassName().contains(Entity.class.getSimpleName())) {
                if (element.getMethodName().equals("canTrample") || element.getMethodName().equals("func_212570_a")) {
                    return;
                }
            }
            if (element.getClassName().contains(TurtleEggBlock.class.getSimpleName())) {
                if (element.getMethodName().equals("canTrample") || element.getMethodName().equals("func_212570_a")) {
                    //onEggTrample(event, entity);
                    return;
                }
            }
        }

        if (entity instanceof LivingEntity) {
            if (onLivingGrief(event, (LivingEntity) entity)) {
                return;
            }
        }

        if (entity instanceof CreeperEntity) {
            onCreeperGrief(event, (CreeperEntity) entity);
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
        } else if (entity instanceof WitherEntity) {
            onWitherGrief(event, (WitherEntity) entity);
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

    private boolean onLivingGrief(EntityMobGriefingEvent event, LivingEntity entity) {
        if (!entity.isAlive()) {
            if (Config.DISABLE_WITHER_ROSE_PLACING.get()) {
                event.setResult(Event.Result.DENY);
                return true;
            }
        }
        return false;
    }

    private void onCreeperGrief(EntityMobGriefingEvent event, CreeperEntity creeper) {
        try {
            Field field;
            try {
                field = ObfuscationReflectionHelper.findField(CreeperEntity.class, "field_70833_d");
            } catch (ObfuscationReflectionHelper.UnableToFindFieldException e) {
                field = ObfuscationReflectionHelper.findField(CreeperEntity.class, "timeSinceIgnited");
            }
            int time = (int) field.get(creeper);
            if (time <= 0) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.setResult(Event.Result.DENY);
            return;
        }

        if (Config.DISABLE_CREEPER_EXPLOSION_BLOCK_DAMAGE.get()) {
            event.setResult(Event.Result.DENY);
        }

        if (Config.DISABLE_CREEPER_DAMAGE.get()) {
            CompoundNBT compoundNBT = new CompoundNBT();
            creeper.writeAdditional(compoundNBT);
            compoundNBT.putByte("ExplosionRadius", (byte) 0);
            creeper.readAdditional(compoundNBT);
        }

        if (Config.CREEPER_FIREWORK.get()) {
            if (creeper.world instanceof ServerWorld) {
                ServerWorld world = (ServerWorld) creeper.world;
                Vec3d pos = creeper.getPositionVec().add(0D, creeper.getEyeHeight(), 0D);
                MessageSpawnFireworks msg = new MessageSpawnFireworks(pos);

                world.getPlayers(serverPlayerEntity -> Math.sqrt(serverPlayerEntity.getDistanceSq(pos)) < 128D).stream().forEach(serverPlayerEntity -> {
                    Main.SIMPLE_CHANNEL.sendTo(msg, serverPlayerEntity.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
                });
            }
        }

        if (Config.CREEPER_POTION_EFFECTS_ENABLED.get()) {
            Config.getEffects(Config.CREEPER_POTION_EFFECTS.get()).stream().forEach(creeper::addPotionEffect);
        }

        if (Config.CREEPER_KNOCKBACK.get()) {
            creeper.world.getEntitiesWithinAABB(ServerPlayerEntity.class, creeper.getBoundingBox().grow(4D)).stream().forEach(player -> {
                if (player.abilities.isFlying) {
                    return;
                }
                Vec3d motionVec = player.getPositionVec().subtract(creeper.getPositionVec()).normalize().scale(Config.CREEPER_KNOCKBACK_FACTOR.get()).add(player.getMotion()).add(0D, 0.25D, 0D);
                Main.SIMPLE_CHANNEL.sendTo(new MessageMotion(motionVec), player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
                player.setMotion(motionVec);
            });
        }
    }

    private void onEndermanGrief(EntityMobGriefingEvent event, EndermanEntity entity) {
        if (Config.DISABLE_ENDERMEN_BLOCK_PICKUP.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onRavagerGrief(EntityMobGriefingEvent event, RavagerEntity entity) {
        if (Config.DISABLE_RAVAGER_BLOCK_BREAKING.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onSilverfishGrief(EntityMobGriefingEvent event, SilverfishEntity entity) {
        if (Config.DISABLE_SILVERFISH_BLOCK_MERGING.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onFoxGrief(EntityMobGriefingEvent event, FoxEntity entity) {
        if (Config.DISABLE_FOX_BERRY_HARVESTING.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onRabbitGrief(EntityMobGriefingEvent event, RabbitEntity entity) {
        if (Config.DISABLE_RABBIT_CARROT_HARVESTING.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    /*
    private void onEggTrample(EntityMobGriefingEvent event, Entity entity) {
        if (Config.DISABLE_EGG_TRAMPLING.get()) {
            if (entity instanceof ZombieEntity) {
                ZombieEntity zombie = (ZombieEntity) entity;
                zombie.goalSelector.removeGoal(zombie.goalSelector.getRunningGoals().filter(prioritizedGoal -> prioritizedGoal.getGoal() instanceof BreakBlockGoal).findAny().orElse(null));
            }
            event.setResult(Event.Result.DENY);
        }
    }
    */

    private void onWitherGrief(EntityMobGriefingEvent event, WitherEntity entity) {
        if (Config.DISABLE_WITHER_BLOCK_DAMAGE.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onSnowGolemGrief(EntityMobGriefingEvent event, SnowGolemEntity entity) {
        if (Config.DISABLE_SNOW_GOLEM_SNOW_PLACING.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onBlazeGrief(EntityMobGriefingEvent event, BlazeEntity entity) {
        if (Config.DISABLE_BLAZE_FIRE.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onGhastGrief(EntityMobGriefingEvent event, GhastEntity entity) {
        if (Config.DISABLE_GHAST_FIREBALL_BLOCK_DAMAGE.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onVillagerGrief(EntityMobGriefingEvent event, VillagerEntity entity) {
        if (Config.DISABLE_VILLAGER_CROP_FARIMING.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onSheepGrief(EntityMobGriefingEvent event, SheepEntity entity) {
        if (Config.DISABLE_SHEEP_GRASS_EATING.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    private void onEvokerGrief(EntityMobGriefingEvent event, EvokerEntity entity) {
        if (Config.DISABLE_EVOKER_SHEEP_CONVERTING.get()) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public void onBlockBreak(LivingDestroyBlockEvent event) {
        if (event.getEntity() instanceof ZombieEntity) {
            if (event.getState().getBlock() instanceof DoorBlock) {
                if (Config.DISABLE_ZOMBIE_DOOR_BREAKING.get()) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onFarmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        if (Config.DISABLE_CROP_TRAMPLING.get()) {
            event.setCanceled(true);
        }
    }


}
