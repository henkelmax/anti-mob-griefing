package de.maxhenkel.antimobgriefing.mixins;

import de.maxhenkel.antimobgriefing.Main;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireballEntity.class)
public abstract class FireballEntityMixin extends AbstractFireballEntity {

    @Shadow
    public int explosionPower;

    public FireballEntityMixin(EntityType<? extends AbstractFireballEntity> type, World level) {
        super(type, level);
    }

    @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
    public void createWitherRose(RayTraceResult result, CallbackInfo info) {
        if (level.isClientSide) {
            return;
        }
        if (!Main.SERVER_CONFIG.disableGhastFireballBlockDamage.get()) {
            return;
        }
        info.cancel();
        level.explode(null, getX(), getY(), getZ(), (float) this.explosionPower, false, Explosion.Mode.NONE);
        remove();
    }

}
