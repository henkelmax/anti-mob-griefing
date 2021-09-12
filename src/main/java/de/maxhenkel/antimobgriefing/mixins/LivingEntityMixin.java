package de.maxhenkel.antimobgriefing.mixins;

import de.maxhenkel.antimobgriefing.events.WitherRosePlaceEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Inject(method = "createWitherRose", at = @At("HEAD"), cancellable = true)
    public void createWitherRose(@Nullable LivingEntity entity, CallbackInfo info) {
        if (!(entity instanceof WitherBoss)) {
            return;
        }
        WitherRosePlaceEvent event = new WitherRosePlaceEvent(level, blockPosition(), (WitherBoss) entity);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }

}
