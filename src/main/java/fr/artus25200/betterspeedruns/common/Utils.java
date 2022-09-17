package fr.artus25200.betterspeedruns.common;

import fr.artus25200.betterspeedruns.BetterSpeedruns;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class Utils {

    private static MinecraftClient _client;
    private static Screen _prev;

    public static void confimQuitting(MinecraftClient client, boolean returnToGame)
    {
        _client = client;
        _prev = MinecraftClient.getInstance().currentScreen;
        ConfirmScreen confirmScreen = new ConfirmScreen(Utils::onConfirmQuit, Text.translatable("Are you sure you want to stop speedrunning ?"), ScreenTexts.EMPTY, Text.translatable("Yes"), Text.translatable("Nvm"));
        client.setScreen(confirmScreen);
        confirmScreen.disableButtons(20);
    }

    private static void onConfirmQuit(boolean quit) {
        if (quit) {
            quitLevel(_client);
        } else {
            _client.setScreen(_prev);
        }
    }

    private static void quitLevel(MinecraftClient client) {
        BetterSpeedruns.isSpeedruning = false;
        if (client.world != null) {
            client.world.disconnect();
        }

        client.disconnect(new MessageScreen(Text.translatable("menu.savingLevel")));
        client.setScreen(new TitleScreen());
    }
}
