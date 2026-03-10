package me.day.petdisplay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import tech.thatgravyboat.skyblockapi.api.data.SkyBlockRarity;
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription;
import tech.thatgravyboat.skyblockapi.api.events.render.RenderScreenForegroundEvent;
import tech.thatgravyboat.skyblockapi.api.events.screen.ScreenMouseClickEvent;
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI;
import tech.thatgravyboat.skyblockapi.api.profile.PetsAPI;
import tech.thatgravyboat.skyblockapi.api.remote.PetQuery;
import tech.thatgravyboat.skyblockapi.api.remote.RepoPetsAPI;

import java.util.Objects;

public class PetManager {
    public static final PetManager INSTANCE = new PetManager();
    private static final Minecraft MINECRAFT = Minecraft.getInstance();
    private static final Identifier SLOT = Identifier.withDefaultNamespace("container/slot");
    private static final ItemStack DEFAULT_PET = createDefaultPet();

    private static ItemStack createDefaultPet() {
        ItemStack stack = new ItemStack(Items.EGG);
        stack.set(DataComponents.ITEM_NAME, Component.translatable("default.pet.display.name").withColor(5635925));
        return stack;
    }

    private ItemStack getPetOrNull() {
        String name = PetsAPI.INSTANCE.getPet();
        SkyBlockRarity rarity = PetsAPI.INSTANCE.getRarity();
        int level = PetsAPI.INSTANCE.getLevel();

        if (name == null || rarity == null) return DEFAULT_PET;

        PetQuery query = new PetQuery(name.trim().replace(" ", "_").toUpperCase(), rarity, level, null, null);

        return RepoPetsAPI.INSTANCE.getPetAsItemOrNull(query);
    }

    public void renderPetSlot(GuiGraphics graphics, int left, int top, int mouseX, int mouseY) {
        if (!LocationAPI.INSTANCE.isOnSkyBlock()) return;

        int buttonX = left + 106;
        int buttonY = top + 61;

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT, buttonX, buttonY, 18, 18);

        if (mouseX >= buttonX && mouseX <= buttonX + 16 && mouseY >= buttonY && mouseY <= buttonY + 16) {
            graphics.fill(RenderPipelines.GUI, buttonX + 1, buttonY + 1, buttonX + 17, buttonY + 17, -2130706433);
        }
        graphics.renderFakeItem(getPetOrNull(), buttonX + 1, buttonY + 1);
    }

    @Subscription
    public void renderForeground(RenderScreenForegroundEvent event, int left, int top) {
        if (!LocationAPI.INSTANCE.isOnSkyBlock()) return;
        if (!(event.getScreen() instanceof InventoryScreen)) return;

        double mouseX = MINECRAFT.mouseHandler.getScaledXPos(MINECRAFT.getWindow());
        double mouseY = MINECRAFT.mouseHandler.getScaledYPos(MINECRAFT.getWindow());

        int buttonX = left + 106;
        int buttonY = top + 61;

        if (mouseX >= buttonX + 1 && mouseX <= buttonX + 16 && mouseY >= buttonY + 1 && mouseY <= buttonY + 16) {
            event.getGraphics().setTooltipForNextFrame(MINECRAFT.font, getPetOrNull(), (int) mouseX, (int) mouseY);
        }
    }

    @Subscription
    public void onMouseClick(ScreenMouseClickEvent event, int left, int top) {
        if (!LocationAPI.INSTANCE.isOnSkyBlock()) return;
        if (!(event.getScreen() instanceof InventoryScreen)) return;

        int buttonX = left + 106;
        int buttonY = top + 61;

        if (event.getX() >= buttonX && event.getX() <= buttonX + 16 && event.getY() >= buttonY && event.getY() <= buttonY + 16) {
            Objects.requireNonNull(MINECRAFT.player).connection.sendCommand("pets");
        }
    }
}
