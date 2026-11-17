package com.mystic.thither;

import com.mystic.thither.doors.init.DoorRegistration;
import com.mystic.thither.eclipse.init.EclipseRegistration;
import com.mystic.thither.utils.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Thither.MODID)
public class Thither {

    public static final Logger LOGGER = LogManager.getLogger(Thither.MODID);

    public static final String MODID = "thither";

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(Thither.MODID, path);
    }

    public Thither(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
    }
}
