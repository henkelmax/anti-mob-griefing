package de.maxhenkel.antimobgriefing.mixins;

import de.maxhenkel.antimobgriefing.events.WitherRosePlaceEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Inject(method = "createWitherRose", at = @At("HEAD"), cancellable = true)
    public void createWitherRose(@Nullable LivingEntity entity, CallbackInfo info) {
        if (!(entity instanceof WitherEntity)) {
            return;
        }
        WitherRosePlaceEvent event = new WitherRosePlaceEvent(level, blockPosition(), (WitherEntity) entity);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }

}
