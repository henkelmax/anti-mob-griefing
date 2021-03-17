package de.maxhenkel.antimobgriefing.mixins;

import de.maxhenkel.antimobgriefing.events.EggTrampleEvent;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.BreakBlockGoal;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BreakBlockGoal.class)
public abstract class BreakBlockGoalMixin extends MoveToBlockGoal {

    @Shadow
    private MobEntity removerMob;

    public BreakBlockGoalMixin(CreatureEntity creature, double speedIn, int length) {
        super(creature, speedIn, length);
    }

    public BreakBlockGoalMixin(CreatureEntity creatureIn, double speed, int length, int p_i48796_5_) {
        super(creatureIn, speed, length, p_i48796_5_);
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
