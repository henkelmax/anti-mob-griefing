package de.maxhenkel.antimobgriefing;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnector implements IMixinConnector {

    @Override
    public void connect() {
        Mixins.addConfiguration("assets/" + Main.MODID + "/antimobgriefing.mixins.json");
    }

}

