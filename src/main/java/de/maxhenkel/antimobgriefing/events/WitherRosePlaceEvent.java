package de.maxhenkel.antimobgriefing.events;

import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;

public class WitherRosePlaceEvent extends Event {

    private World world;
    private BlockPos pos;
    private WitherEntity wither;

    public WitherRosePlaceEvent(World world, BlockPos pos, WitherEntity wither) {
        this.world = world;
        this.pos = pos;
        this.wither = wither;
    }

    public World getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }

    public WitherEntity getWither() {
        return wither;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
