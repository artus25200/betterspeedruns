package fr.artus25200.betterspeedruns;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.world.Difficulty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

@Environment(EnvType.CLIENT)
public class BetterSpeedruns implements ModInitializer {

    public static final String MOD_ID = "betterspeedruns";
    public static final String FANCY_MOD_ID = "BetterSpeedruns";
    public static final Logger LOGGER =  LoggerFactory.getLogger(FANCY_MOD_ID);

    private static boolean isDev;
    public static boolean deletingWorld;

    public static boolean isSpeedruning = false;
    public static boolean shouldReset = false;
    public static final Difficulty difficulty = Difficulty.EASY;

    public static final TextRenderer tr = MinecraftClient.getInstance().textRenderer;

    @Override
    @Environment(EnvType.CLIENT)
    public void onInitialize() {
        LOGGER.info("Initializing BetterSpeedruns :focused_face: ");
        isDev = FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static boolean isDev()
    {
        return isDev;
    }

}
