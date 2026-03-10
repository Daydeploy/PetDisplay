package me.day.petdisplay.mixin;

import me.day.petdisplay.PetManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.thatgravyboat.skyblockapi.api.events.render.RenderScreenForegroundEvent;
import tech.thatgravyboat.skyblockapi.api.events.screen.ScreenMouseClickEvent;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractRecipeBookScreen<InventoryMenu> {
    public InventoryScreenMixin(InventoryMenu recipeBookMenu, RecipeBookComponent<?> recipeBookComponent, Inventory inventory, Component component) {
        super(recipeBookMenu, recipeBookComponent, inventory, component);
    }

    @Inject(at = @At("TAIL"), method = "renderBg")
    private void renderBg(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {
        PetManager.INSTANCE.renderPetSlot(guiGraphics, this.leftPos, this.topPos, i, j);
    }

    @Inject(at = @At("RETURN"), method = "render")
    private void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        RenderScreenForegroundEvent event = new RenderScreenForegroundEvent((InventoryScreen) (Object) this, guiGraphics);
        PetManager.INSTANCE.renderForeground(event, this.leftPos, this.topPos);
    }

    @Inject(at = @At("HEAD"), method = "mouseReleased")
    private void mouseReleased(MouseButtonEvent mouseButtonEvent, CallbackInfoReturnable<Boolean> cir) {
        ScreenMouseClickEvent event = new ScreenMouseClickEvent.Pre((InventoryScreen) (Object) this, mouseButtonEvent.x(), mouseButtonEvent.y(), mouseButtonEvent.button());
        PetManager.INSTANCE.onMouseClick(event, this.leftPos, this.topPos);
    }
}
