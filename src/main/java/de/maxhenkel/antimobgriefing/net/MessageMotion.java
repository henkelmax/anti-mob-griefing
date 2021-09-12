package de.maxhenkel.antimobgriefing.net;

import de.maxhenkel.corelib.net.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

public class MessageMotion implements Message<MessageMotion> {

    private Vec3 motion;

    public MessageMotion(Vec3 motion) {
        this.motion = motion;
    }

    public MessageMotion() {
    }

    @Override
    public Dist getExecutingSide() {
        return Dist.CLIENT;
    }

    @Override
    public void executeClientSide(NetworkEvent.Context context) {
        execute();
    }

    @OnlyIn(Dist.CLIENT)
    private void execute() {
        Minecraft.getInstance().player.setDeltaMovement(motion);
    }

    @Override
    public MessageMotion fromBytes(FriendlyByteBuf buf) {
        motion = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        return this;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(motion.x);
        buf.writeDouble(motion.y);
        buf.writeDouble(motion.z);
    }
}
