package de.maxhenkel.antimobgriefing.net;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageMotion implements Message<MessageMotion> {

    private Vec3d motion;

    public MessageMotion(Vec3d motion) {
        this.motion = motion;
    }

    public MessageMotion() {
    }

    @Override
    public void executeServerSide(NetworkEvent.Context context) {

    }

    @Override
    public void executeClientSide(NetworkEvent.Context context) {
        execute();
    }

    @OnlyIn(Dist.CLIENT)
    private void execute() {
        Minecraft.getInstance().player.setMotion(motion);
    }

    @Override
    public MessageMotion fromBytes(PacketBuffer buf) {
        motion = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        return this;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeDouble(motion.x);
        buf.writeDouble(motion.y);
        buf.writeDouble(motion.z);
    }
}
