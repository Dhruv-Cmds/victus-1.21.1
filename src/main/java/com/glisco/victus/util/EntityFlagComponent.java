package com.glisco.victus.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.Component;

public class EntityFlagComponent implements Component {

    private int flags = 0;

    public void setFlag(int flag) {
        flags |= flag;
    }

    public void clearFlags() {
        flags = 0;
    }

    public boolean flagSet(int flag) {
        return (flags & flag) != 0;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        flags = tag.getInt("Flags");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("Flags", flags);
    }
}
