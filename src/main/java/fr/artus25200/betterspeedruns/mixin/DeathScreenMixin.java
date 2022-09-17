package fr.artus25200.betterspeedruns.mixin;

import fr.artus25200.betterspeedruns.BetterSpeedruns;
import fr.artus25200.betterspeedruns.common.Utils;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {
    @Shadow @Final private List<ButtonWidget> buttons;

    @Shadow protected abstract void quitLevel();

    private boolean firstTime = true;
    private boolean stop = false;

    protected DeathScreenMixin(Text title) {
        super(title);
    }

    private long time = 0;
    private long target_time = 5;

    @ModifyArg(method = "init", index = 0, at = @At(ordinal = 1, value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/DeathScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
    private Element EditReturnToTitleScreenButton(Element par1)
    {
        Element CustomButton = new ButtonWidget(this.width / 2 - 100, this.height / 4 + 96, 200, 20, Text.literal("Stop Speedrunning"), (button) -> {
            stop = true;
            assert client != null;
            Utils.confimQuitting(client, false);
        });
        return BetterSpeedruns.isSpeedruning ? CustomButton : par1;
    }

    @ModifyArg(method = "init", index = 0, at = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/DeathScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
    private Element EditRespawnButton(Element par1)
    {
        Element CustomButton = new ButtonWidget(this.width / 2 - 100, this.height / 4 + 72, 200, 20, Text.literal("."), (button) -> quitLevel());
        return BetterSpeedruns.isSpeedruning ? CustomButton : par1;
    }

    @Inject(method = "init", at =@At("TAIL"))
    private void registerAfterTick(CallbackInfo ci)
    {
        int x = buttons.get(0).x + buttons.get(0).getWidth() + 4;
        int y = buttons.get(0).y;
        int w = buttons.get(0).getWidth() / 2;
        int h = buttons.get(0).getHeight();
        addDrawableChild(new ButtonWidget(x, y, w, h, Text.literal("Respawn"), (onPress)->{
            assert Objects.requireNonNull(this.client).player != null;
            assert this.client.player != null;
            this.client.player.requestRespawn();
            this.client.setScreen(null);
        }));

        ScreenEvents.afterTick((DeathScreen)(Object)this).register((screen) ->{
            if(!BetterSpeedruns.isSpeedruning) return;
            if(firstTime)
            {
                firstTime = false;
                time = System.currentTimeMillis();
                target_time = time + 6000;
            }
            if(buttons.get(0) != null)
            {
                if(stop) {
                    buttons.get(0).setMessage(Text.literal("Reset Run"));
                }
                else if(time <= target_time)
                {
                    time = System.currentTimeMillis();
                    buttons.get(0).setMessage(getText());
                }
                else {
                    quitLevel();
                }
            }
            else{
                stop = true;
            }
        });
    }

    @ModifyArg(method = "quitLevel", index = 0,at = @At(value = "INVOKE", ordinal = 0,target = "Lnet/minecraft/client/gui/screen/MessageScreen;<init>(Lnet/minecraft/text/Text;)V"))
    private Text setMessage(Text txt)
    {
        return BetterSpeedruns.isSpeedruning ? Text.literal("Deleting and Reseting...") : txt;
    }
    private Text getText(){
        return Text.literal("Reset Run " + String.format("(%d)", (target_time-time) / 1000));
    }



}
