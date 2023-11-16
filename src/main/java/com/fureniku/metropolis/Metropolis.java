package com.fureniku.metropolis;

import com.fureniku.metropolis.utils.Debug;
import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Metropolis.MODID)
public class Metropolis {

    public static final String MODID = "metropolis";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Metropolis() {
        Debug.registerMod("Metropolis");
    }
}
