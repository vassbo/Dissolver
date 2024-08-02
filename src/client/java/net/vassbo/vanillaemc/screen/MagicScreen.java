package net.vassbo.vanillaemc.screen;

import java.util.Objects;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.data.PlayerDataClient;
import net.vassbo.vanillaemc.helpers.NumberHelpers;
import net.vassbo.vanillaemc.packets.DataSenderClient;

public class MagicScreen extends HandledScreen<MagicScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(VanillaEMC.MOD_ID, "textures/gui/magic_block_gui.png");
    
    // scroll
    private static final Identifier SCROLLER_TEXTURE = Identifier.ofVanilla("container/creative_inventory/scroller");
    private static final Identifier SCROLLER_DISABLED_TEXTURE = Identifier.ofVanilla("container/creative_inventory/scroller_disabled");
    private float scrollPosition;
    private boolean scrolling;

    // search
    private TextFieldWidget searchBox;
    private boolean ignoreTypedCharacter;

    public MagicScreen(MagicScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.backgroundWidth = 217;
        this.backgroundHeight = 221;
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;

        // titleY = 1000;
        // playerInventoryTitleY = 1000;
        // this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
        
        // search box
        this.searchBox = new TextFieldWidget(this.textRenderer, this.x + 104, this.y + 6, 80, 9, Text.translatable("itemGroup.search"));
        this.searchBox.setMaxLength(50);
        this.searchBox.setDrawsBackground(false);
        this.searchBox.setVisible(true);
        this.searchBox.setEditableColor(16777215);
        // this.searchBox.setFocusUnlocked(false);
        this.searchBox.setFocused(true);
        this.addSelectableChild(this.searchBox);
    }

    // DRAW

    int SCROLL_BAR_X = 198;
    int SCROLL_BAR_Y = 18;
    int MOUSE_SCROLL_AREA_WIDTH = 13;
    int MOUSE_SCROLL_AREA_HEIGHT = 106;
    int SCROLL_AREA_HEIGHT = 108;
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        // custom text
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        // search box
        this.searchBox.render(context, mouseX, mouseY, delta);

        // scroll bar
        int i = this.x + SCROLL_BAR_X;
        int j = this.y + SCROLL_BAR_Y;
        int k = j + SCROLL_AREA_HEIGHT;
        boolean scrollActive = PlayerDataClient.LEARNED_ITEMS_SIZE > this.handler.CUSTOM_INV_SIZE;
        Identifier identifier = scrollActive ? SCROLLER_TEXTURE : SCROLLER_DISABLED_TEXTURE;
        context.drawGuiTexture(identifier, i, j + (int)((float)(k - j - 17) * this.scrollPosition), 12, 15);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        // context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 4210752, false);
        // context.drawText(this.textRenderer, this.playerInventoryTitle, this.playerInventoryTitleX, this.playerInventoryTitleY, 4210752, false);

        renderText(context, 33, 6);
    }

    private void renderText(DrawContext context, int x, int y) {
        // https://learn.microsoft.com/en-us/office/vba/api/word.wdcolor
        String MESSAGE = getMessage();
        context.drawText(this.textRenderer, MESSAGE, x, y, 16777215, false);
    }

    private String getMessage() {
        String CUSTOM_MSG = PlayerDataClient.MESSAGE;
        if (CUSTOM_MSG.isEmpty()) {
            String emc = NumberHelpers.format(PlayerDataClient.EMC);
            Text text = Text.translatable("emc.title", emc);
            return text.getString();
        }

        Text text = Text.translatable(CUSTOM_MSG);
        return text.getString();
    }

    // SEARCH

    public boolean charTyped(char chr, int modifiers) {
        if (this.ignoreTypedCharacter) {
            return false;
        }
        
        String string = this.searchBox.getText();
        // close screen if pressing "e" & nothing is searched
        if (string == "" && chr == "e".charAt(0)) {
            this.close();
            return false;
        }

        if (!this.searchBox.charTyped(chr, modifiers)) return false;
        
        if (!Objects.equals(string, this.searchBox.getText())) {
            this.search();
        }

        return true;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.ignoreTypedCharacter = false;
        
        boolean bl = this.focusedSlot == null ? false : this.focusedSlot.hasStack();
        boolean bl2 = InputUtil.fromKeyCode(keyCode, scanCode).toInt().isPresent();
        if (bl && bl2 && this.handleHotbarKeyPressed(keyCode, scanCode)) {
            this.ignoreTypedCharacter = true;
            return true;
        } else {
            String string = this.searchBox.getText();
            if (this.searchBox.keyPressed(keyCode, scanCode, modifiers)) {
                if (!Objects.equals(string, this.searchBox.getText())) {
                    this.search();
                }

                return true;
            }
            
            return this.searchBox.isFocused() && this.searchBox.isVisible() && keyCode != 256 ? true : super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        this.ignoreTypedCharacter = false;
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    private void search() {
        String string = this.searchBox.getText();
        DataSenderClient.sendDataToServer("search", string.isEmpty() ? "" : string);

        // reset scroll on search
        this.scrollPosition = 0.0F;
        DataSenderClient.sendDataToServer("scroll", "0");
    }

    // SCROLL    

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);

        if (mouseX >= x + SCROLL_BAR_X && mouseY >= y + SCROLL_BAR_Y && mouseX < x + SCROLL_BAR_X + MOUSE_SCROLL_AREA_WIDTH && mouseY < y + SCROLL_BAR_Y + MOUSE_SCROLL_AREA_HEIGHT) {
            this.scrolling = true;
        } else {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        return true;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) this.scrolling = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        // setting scroll when reaced end will clear all items, so just return
        if (this.scrollPosition == 0 && verticalAmount > 0 || this.scrollPosition == 1 && verticalAmount < 0) return false;

        this.scrollPosition = getScrollPosition(this.scrollPosition, verticalAmount);
        DataSenderClient.sendDataToServer("scroll", Float.toString(scrollPosition));

        return true;
	}

    protected float getScrollPosition(float current, double amount) {
        return MathHelper.clamp(current - (float)(amount / (double)this.getOverflowRows()), 0.0F, 1.0F);
    }

    protected int getOverflowRows() {
        return MathHelper.ceilDiv(PlayerDataClient.LEARNED_ITEMS_SIZE, 9) - 6;
    }

	@Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        boolean scrollActive = PlayerDataClient.LEARNED_ITEMS_SIZE > this.handler.CUSTOM_INV_SIZE;

        if (!this.scrolling || !scrollActive) return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);

        float i = this.y + SCROLL_BAR_Y;
        float j = i + MOUSE_SCROLL_AREA_HEIGHT;
        scrollPosition = (float)(mouseY - i - 7.5F) / ((j - i) - 15.0F);
        scrollPosition = MathHelper.clamp(scrollPosition, 0.0F, 1F);
        DataSenderClient.sendDataToServer("scroll", Float.toString(scrollPosition));
        
        return true;
    }
}
