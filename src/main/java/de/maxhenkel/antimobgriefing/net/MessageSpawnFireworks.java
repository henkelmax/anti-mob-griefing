package de.maxhenkel.antimobgriefing.net;

import de.maxhenkel.antimobgriefing.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.item.DyeColor;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MessageSpawnFireworks implements Message<MessageSpawnFireworks> {

    private Vec3d pos;

    public MessageSpawnFireworks(Vec3d pos) {
        this.pos = pos;
    }

    public MessageSpawnFireworks() {
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
        CompoundNBT compound = new CompoundNBT();
        ListNBT explosions = new ListNBT();

        CompoundNBT explosion = new CompoundNBT();
        FireworkRocketItem.Shape shape = FireworkRocketItem.Shape.SMALL_BALL;
        explosion.putByte("Type", (byte) shape.func_196071_a());
        List<Integer> colors = Config.getDyeColors(Config.CREEPER_FIREWORK_COLORS.get());
        explosion.putIntArray("Colors", colors);
        explosion.putBoolean("Flicker", true);
        explosions.add(explosion);

        compound.putByte("Flight", (byte) 1);
        compound.put("Explosions", explosions);
        Minecraft.getInstance().world.makeFireworks(pos.x, pos.y, pos.z, 0D, 0D, 0D, compound);
    }

    @Override
    public MessageSpawnFireworks fromBytes(PacketBuffer buf) {
        pos = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        return this;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeDouble(pos.x);
        buf.writeDouble(pos.y);
        buf.writeDouble(pos.z);
    }
}
