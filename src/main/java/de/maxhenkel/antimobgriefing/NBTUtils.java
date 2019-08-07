package de.maxhenkel.antimobgriefing;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;

public class NBTUtils {

    public static String serializeNBT(CompoundNBT compound) {
        return compound.toString();
    }

    public static CompoundNBT deserializeNBT(String json) {
        try {
            return JsonToNBT.getTagFromJson(json);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            return new CompoundNBT();
        }
    }

}
