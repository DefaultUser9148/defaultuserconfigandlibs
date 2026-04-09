package net.defaultuserconfigandlibs.mixin;

import net.defaultuserconfigandlibs.WorldSelectButtonRegistry;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SelectWorldScreen.class)
public class SelectWorldScreenButtonMixin {

    @Shadow private ButtonWidget selectButton;

    @Unique private Text dlib_originalSelectLabel = null;

    @Inject(method = "init", at = @At("TAIL"))
    private void dlib_captureOriginalLabel(CallbackInfo ci) {
        if (this.selectButton != null) {
            dlib_originalSelectLabel = this.selectButton.getMessage();
        }
    }

    @Inject(method = "worldSelected", at = @At("TAIL"))
    private void dlib_onWorldSelected(LevelSummary level, CallbackInfo ci) {
        if (this.selectButton == null || level == null) return;

        String label   = WorldSelectButtonRegistry.resolveLabel(level);
        String tooltip = WorldSelectButtonRegistry.resolveTooltip(level);

        // Button label — override or restore vanilla default
        if (label != null) {
            this.selectButton.setMessage(Text.literal(label));
        } else if (dlib_originalSelectLabel != null) {
            this.selectButton.setMessage(dlib_originalSelectLabel);
        }

        // Tooltip — always clear first, then set if override exists
        this.selectButton.setTooltip(null);
        if (tooltip != null) {
            this.selectButton.setTooltip(Tooltip.of(Text.literal(tooltip)));
        }
    }
}
