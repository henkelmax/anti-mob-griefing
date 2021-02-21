package de.maxhenkel.antimobgriefing.events;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;

public class EggTrampleEvent extends Event {

    private World world;
    private Entity trampler;

    public EggTrampleEvent(World world, Entity trampler) {
        this.world = world;
        this.trampler = trampler;
    }

    public World getWorld() {
        return world;
    }

    public Entity getTrampler() {
        return trampler;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
