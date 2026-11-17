package com.mystic.thither.doors.init;

import com.mystic.thither.doors.DoorDimension;
import net.minecraftforge.eventbus.api.IEventBus;

public final class DoorRegistration {
    private DoorRegistration() {
    }

    public static void register(IEventBus bus) {
        DoorBlockInit.BLOCKS.register(bus);
        DoorBlockInit.BLOCK_ITEMS.register(bus);
        DoorDimension.CHUNK_GENERATORS.register(bus);
    }
}
