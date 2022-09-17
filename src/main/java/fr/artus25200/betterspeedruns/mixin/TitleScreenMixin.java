package fr.artus25200.betterspeedruns.mixin;

import fr.artus25200.betterspeedruns.BetterSpeedruns;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static fr.artus25200.betterspeedruns.BetterSpeedruns.shouldReset;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    String Message = "BetterSpeedruns by Artus25200 !";

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "initWidgetsNormal")
    private void addCustomButton(int y, int spacingY, CallbackInfo ci)
    {
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 2, y, 98, 20, Text.literal("Speedrun"), (button) -> {
            BetterSpeedruns.isSpeedruning = true;
            CreateWorldScreen.create(MinecraftClient.getInstance(), this);
        }));
        this.addDrawableChild(new PressableTextWidget(2, 3, this.textRenderer.getWidth(Message), 10, Text.literal(Message), (button) -> {
            Util.getOperatingSystem().open("https://fabricmc.net");
            //System.out.println("test");
        }, this.textRenderer));
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void Reset(CallbackInfo ci)
    {
        if(BetterSpeedruns.isSpeedruning)
        {
            CreateWorldScreen.create(MinecraftClient.getInstance(), this);
        }
    }

    /* @ModifyArg(method = "initWidgetsNormal", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)V"),index = 4)
    private Text injected(Text message)
    {
        return Text.literal("Not Speedrun");
    }*/

    @ModifyArg(method = "initWidgetsNormal", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)V"),index = 2)
    private int injectedInt(int width)
    {
        return 98;
    }




}
