package de.maxhenkel.antimobgriefing.net;

import de.maxhenkel.antimobgriefing.Main;
import de.maxhenkel.corelib.net.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageSpawnFireworks implements Message<MessageSpawnFireworks> {

    private Vector3d pos;

    public MessageSpawnFireworks(Vector3d pos) {
        this.pos = pos;
    }

    public MessageSpawnFireworks() {
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
        CompoundNBT compound = new CompoundNBT();
        ListNBT explosions = new ListNBT();

        CompoundNBT explosion = new CompoundNBT();
        FireworkRocketItem.Shape shape = FireworkRocketItem.Shape.SMALL_BALL;
        explosion.putByte("Type", (byte) shape.getIndex());
        explosion.putIntArray("Colors", Main.SERVER_CONFIG.creeperColors);
        explosion.putBoolean("Flicker", true);
        explosions.add(explosion);

        compound.putByte("Flight", (byte) 1);
        compound.put("Explosions", explosions);
        Minecraft.getInstance().world.makeFireworks(pos.x, pos.y, pos.z, 0D, 0D, 0D, compound);
    }

    @Override
    public MessageSpawnFireworks fromBytes(PacketBuffer buf) {
        pos = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        return this;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeDouble(pos.x);
        buf.writeDouble(pos.y);
        buf.writeDouble(pos.z);
    }
}
