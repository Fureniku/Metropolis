package com.fureniku.metropolis;

import com.fureniku.metropolis.test.RegistrationTest;
import com.fureniku.metropolis.utils.Debug;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLLoader;
import org.slf4j.Logger;


@Mod(Metropolis.MODID)
public class Metropolis {

    public static final String MODID = "metropolis";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Metropolis INSTANCE;

    public static final boolean ENABLE_DEBUG = true; // For testing metropolis stuff internally. Should be disabled on release.
    public static RegistrationTest registrationTest;

    public Metropolis() {
        INSTANCE = this;
        Debug.registerMod("Metropolis");
        if (FMLLoader.isProduction()) {
            Debug.Log("Metropolis now loading!");
        } else {
            Debug.Log("Metropolis loading in dev environment. Welcome to the fun side >:D");
            if (ENABLE_DEBUG) {
                Debug.LogWarning("Metropolis is now loading internal test content. Are we sure we want this?");
                IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
                registrationTest = new RegistrationTest(MODID, modEventBus);
                registrationTest.init(modEventBus);
            }
        }
    }
}
