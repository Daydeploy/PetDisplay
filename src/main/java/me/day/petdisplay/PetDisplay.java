package me.day.petdisplay;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PetDisplay implements ClientModInitializer {
    public static final String MOD_ID = "petdisplay";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing PetDisplay");
    }
}
