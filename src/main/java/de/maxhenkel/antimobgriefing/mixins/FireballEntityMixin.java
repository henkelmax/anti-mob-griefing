package de.maxhenkel.antimobgriefing.mixins;

import de.maxhenkel.antimobgriefing.Main;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LargeFireball.class)
public abstract class FireballEntityMixin extends Fireball {

    @Shadow
    private int explosionPower;

    public FireballEntityMixin(EntityType<? extends Fireball> p_37006_, Level p_37007_) {
        super(p_37006_, p_37007_);
    }

    @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
    public void createWitherRose(HitResult result, CallbackInfo info) {
        if (level.isClientSide) {
            return;
        }
        if (!Main.SERVER_CONFIG.disableGhastFireballBlockDamage.get()) {
            return;
        }
        info.cancel();
        level.explode(null, getX(), getY(), getZ(), (float) this.explosionPower, false, Explosion.BlockInteraction.NONE);
        discard();
    }

}
