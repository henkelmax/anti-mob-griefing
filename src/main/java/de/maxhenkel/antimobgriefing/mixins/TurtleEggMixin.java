package de.maxhenkel.antimobgriefing.mixins;

import de.maxhenkel.antimobgriefing.events.EggTrampleEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TurtleEggBlock.class)
public abstract class TurtleEggMixin extends Block {

    public TurtleEggMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "canDestroyEgg", at = @At(value = "RETURN"), cancellable = true)
    public void canTrample(Level world, Entity trampler, CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValue()) {
            return;
        }
        EggTrampleEvent event = new EggTrampleEvent(world, trampler);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            info.setReturnValue(false);
        }
    }

}
