package de.maxhenkel.antimobgriefing.net;

import de.maxhenkel.antimobgriefing.Main;
import de.maxhenkel.corelib.net.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class MessageSpawnFireworks implements Message<MessageSpawnFireworks> {

    private Vec3 pos;

    public MessageSpawnFireworks(Vec3 pos) {
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
        CompoundTag compound = new CompoundTag();
        ListTag explosions = new ListTag();

        CompoundTag explosion = new CompoundTag();
        FireworkRocketItem.Shape shape = FireworkRocketItem.Shape.SMALL_BALL;
        explosion.putByte("Type", (byte) shape.getId());
        explosion.putIntArray("Colors", Main.SERVER_CONFIG.creeperColors);
        explosion.putBoolean("Flicker", true);
        explosions.add(explosion);

        compound.putByte("Flight", (byte) 1);
        compound.put("Explosions", explosions);
        Minecraft.getInstance().level.createFireworks(pos.x, pos.y, pos.z, 0D, 0D, 0D, compound);
    }

    @Override
    public MessageSpawnFireworks fromBytes(FriendlyByteBuf buf) {
        pos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        return this;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(pos.x);
        buf.writeDouble(pos.y);
        buf.writeDouble(pos.z);
    }
}
