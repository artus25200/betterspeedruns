package fr.artus25200.betterspeedruns.mixin;

import fr.artus25200.betterspeedruns.BetterSpeedruns;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.world.Difficulty;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;


@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin extends Screen{

    @Shadow
    private Difficulty currentDifficulty;

    protected CreateWorldScreenMixin(Text title) {
        super(title);
    }

    @Shadow protected abstract void createLevel();

    @Shadow private String levelName;

    @Shadow private TextFieldWidget levelNameField;

    @Inject(method = "init", at = @At("TAIL"))
    private void Inject(CallbackInfo ci) throws IOException {
        File speedrunWorld = new File(FilenameUtils.normalize(MinecraftClient.getInstance().runDirectory + "/saves/BetterSpeedrunWorld"));
        //System.out.println(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve("saves").resolve("BetterSpeedrunWorld"))); Debug purposes
        if(speedrunWorld.exists())
        {
            FileUtils.deleteDirectory(speedrunWorld);
        }
        levelName = "BetterSpeedrunWorld";
        levelNameField.setText(levelName);
        currentDifficulty = BetterSpeedruns.difficulty;
        createLevel();
    }

}
