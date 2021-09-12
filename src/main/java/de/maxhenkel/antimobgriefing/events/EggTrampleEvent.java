package de.maxhenkel.antimobgriefing.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

public class EggTrampleEvent extends Event {

    private Level world;
    private Entity trampler;

    public EggTrampleEvent(Level world, Entity trampler) {
        this.world = world;
        this.trampler = trampler;
    }

    public Level getWorld() {
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
