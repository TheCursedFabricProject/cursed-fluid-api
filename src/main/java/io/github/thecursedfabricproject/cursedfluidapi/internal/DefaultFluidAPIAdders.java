package io.github.thecursedfabricproject.cursedfluidapi.internal;

import io.github.thecursedfabricproject.cursedfluidapi.internal.lba.LBACompat;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class DefaultFluidAPIAdders implements ModInitializer {

    @Override
    public void onInitialize() {
        //LBA
        if (FabricLoader.getInstance().isModLoaded("libblockattributes")) {
            LBACompat.init();
        }
        //Vanilla
        CauldronCompat.init();
        BucketCompat.init();
    }
    
}
