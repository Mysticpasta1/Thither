package com.mystic.thither;

import com.mystic.thither.eclipse.init.EclipseRegistration;
import com.mystic.thither.utils.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MODID)
public class ThitherMain {

    public static final Logger LOGGER = LogManager.getLogger(Reference.MODID);

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(Reference.MODID, path);
    }

    public ThitherMain(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        EclipseRegistration.register(bus);
    }
}
