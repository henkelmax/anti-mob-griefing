package de.maxhenkel.antimobgriefing.mixins;

import de.maxhenkel.antimobgriefing.events.EggTrampleEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RemoveBlockGoal.class)
public abstract class BreakBlockGoalMixin extends MoveToBlockGoal {

    @Shadow
    @Final
    private Mob removerMob;

    public BreakBlockGoalMixin(PathfinderMob p_25609_, double p_25610_, int p_25611_) {
        super(p_25609_, p_25610_, p_25611_);
    }


    @Inject(method = "canUse", at = @At(value = "FIELD"), cancellable = true)
    public void shouldExecute(CallbackInfoReturnable<Boolean> info) {
        if (removerMob.level.getBlockState(blockPos).getBlock().equals(Blocks.TURTLE_EGG)) {
            EggTrampleEvent event = new EggTrampleEvent(removerMob.level, removerMob);
            MinecraftForge.EVENT_BUS.post(event);
            if (event.isCanceled()) {
                info.setReturnValue(false);
            }
        }
    }

}
