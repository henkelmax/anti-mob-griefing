package de.maxhenkel.antimobgriefing.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

public class WitherRosePlaceEvent extends Event {

    private Level world;
    private BlockPos pos;
    private WitherBoss wither;

    public WitherRosePlaceEvent(Level world, BlockPos pos, WitherBoss wither) {
        this.world = world;
        this.pos = pos;
        this.wither = wither;
    }

    public Level getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }

    public WitherBoss getWither() {
        return wither;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
