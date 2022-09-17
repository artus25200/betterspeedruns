package fr.artus25200.betterspeedruns.mixin;

import fr.artus25200.betterspeedruns.BetterSpeedruns;
import fr.artus25200.betterspeedruns.common.Utils;

import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @ModifyArg(method = "initWidgets", index = 4, at = @At(ordinal = 7, value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)V"))
    private Text EditDisconnectButton(Text message)
    {
        Text ResetText = Text.literal("Reset Run");
        return BetterSpeedruns.isSpeedruning ? ResetText : message;
    }

    @Inject(method = "initWidgets", at = @At("TAIL"))
    private void AddStopSpeedrunningButton(CallbackInfo ci){
        if(!BetterSpeedruns.isSpeedruning) return;
        this.addDrawableChild(new ButtonWidget(this.width - (100 + 10), this.height - (20 + 20), 100, 20, Text.literal("Stop Speedrunning"), (button) -> {
            assert client != null;
            Utils.confimQuitting(client, true);
        }));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci)
    {

    }

}
