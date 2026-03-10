package me.day.petdisplay.mixin;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI;

@Mixin(AbstractRecipeBookScreen.class)
public class AbstractRecipeBookScreenMixin {
    @Inject(at = @At("TAIL"), method = "initButton")
    private void initButton(CallbackInfo ci) {
        if (!LocationAPI.INSTANCE.isOnSkyBlock()) return;

        ((AbstractRecipeBookScreen<?>) (Object) this).children().stream()
                .filter(element -> element instanceof ImageButton)
                .map(element -> (ImageButton) element)
                .forEach(button -> button.visible = false);
    }
}
