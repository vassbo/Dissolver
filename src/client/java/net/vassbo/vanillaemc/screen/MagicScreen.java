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
import net.vassbo.vanillaemc.packets.DataSenderClient;

public class MagicScreen extends HandledScreen<MagicScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(VanillaEMC.MOD_ID, "textures/gui/magic_block_gui.png");
    private static final Identifier SCROLLER_TEXTURE = Identifier.ofVanilla("container/creative_inventory/scroller");
    private static final Identifier SCROLLER_DISABLED_TEXTURE = Identifier.ofVanilla("container/creative_inventory/scroller_disabled");
    
    // private final MagicScreenHandler handler;
    
//    static final SimpleInventory INVENTORY = new SimpleInventory(45); // trick the scroll to think it's 
    private float scrollPosition;
    private boolean scrolling;

    private TextFieldWidget searchBox;
    private boolean ignoreTypedCharacter;

    public MagicScreen(MagicScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        // this.handler = handler;
    }

    @Override
    protected void init() {
        super.init();
        titleY = 1000;
        playerInventoryTitleY = 1000;
        // this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
        this.backgroundWidth = 217;
        this.backgroundHeight = 221;
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;
        
        // search box
        this.searchBox = new TextFieldWidget(this.textRenderer, this.x + 104, this.y + 6, 80, 9, Text.translatable("itemGroup.search"));
        this.searchBox.setMaxLength(50);
        this.searchBox.setDrawsBackground(false);
        this.searchBox.setVisible(true);
        this.searchBox.setEditableColor(16777215);
        this.searchBox.setFocusUnlocked(true); // false
        this.searchBox.setFocused(true);
        this.addSelectableChild(this.searchBox);
    }

    int SCROLL_BAR_X = 198;
    int SCROLL_BAR_Y = 18;
    int MOUSE_SCROLL_AREA_WIDTH = 13;
    int MOUSE_SCROLL_AREA_HEIGHT = 106; // 108
    int SCROLL_AREA_HEIGHT = 108;
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        // VanillaEMC.LOGGER.info("TEST VALUE: " + this.handler.test);

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
        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 4210752, false);
        context.drawText(this.textRenderer, this.playerInventoryTitle, this.playerInventoryTitleX, this.playerInventoryTitleY, 4210752, false);

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
            int emc = PlayerDataClient.EMC;
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
        } else {
        String string = this.searchBox.getText();
        if (this.searchBox.charTyped(chr, modifiers)) {
            if (!Objects.equals(string, this.searchBox.getText())) {
                this.search();
            }

            return true;
        } else {
            return false;
        }
        }
    }

    // WIP pressing "e" when search is empty should close the screen
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.ignoreTypedCharacter = false;
        
        boolean bl = this.focusedSlot == null ? false : this.focusedSlot.hasStack(); // !this.isCreativeInventorySlot(this.focusedSlot) || 
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
            } else {
            return this.searchBox.isFocused() && this.searchBox.isVisible() && keyCode != 256 ? true : super.keyPressed(keyCode, scanCode, modifiers);
            }
        }
    }

    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        this.ignoreTypedCharacter = false;
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    private void search() {
        // WIP send to server!!
        (this.handler).itemList.clear();
        // this.searchResultTags.clear();
        String string = this.searchBox.getText();
        if (string.isEmpty()) {
            // (this.handler).refresh(null, VanillaEMCClient.getLearnedItems());
            DataSenderClient.sendDataToServer("search", "");
        } else {
            // ClientPlayNetworkHandler clientPlayNetworkHandler = this.client.getNetworkHandler();
            // if (clientPlayNetworkHandler != null) {
            //     SearchManager searchManager = clientPlayNetworkHandler.getSearchManager();
            //     SearchProvider<ItemStack> searchProvider;
            //     // if (string.startsWith("#")) {
            //     //     string = string.substring(1);
            //     //     searchProvider = searchManager.getItemTagReloadFuture();
            //     //     this.searchForTags(string);
            //     // } else {
            //         searchProvider = searchManager.getItemTooltipReloadFuture();
            //     // }

            //     List<ItemStack> searchedItems = searchProvider.findAll(string.toLowerCase(Locale.ROOT));
            //     List<String> itemIds = new ArrayList<>();
            //     for (ItemStack item : searchedItems) {
            //         itemIds.add(item.getItem().toString());
            //     }
            // }
            DataSenderClient.sendDataToServer("search", string);
        }

        this.scrollPosition = 0.0F;
        DataSenderClient.sendDataToServer("scroll", "0");
    }
    
    // private void searchForTags(String id) {
    //     int i = id.indexOf(58);
    //     Predicate predicate;
    //     if (i == -1) {
    //         predicate = (idx) -> {
    //             return idx.getPath().contains(id);
    //         };
    //     } else {
    //         String string = id.substring(0, i).trim();
    //         String string2 = id.substring(i + 1).trim();
    //         predicate = (idx) -> {
    //             return idx.getNamespace().contains(string) && idx.getPath().contains(string2);
    //         };
    //     }

    //     Stream var10000 = Registries.ITEM.streamTags().filter((tag) -> {
    //         return predicate.test(tag.id());
    //     });
    //     Set var10001 = this.searchResultTags;
    //     Objects.requireNonNull(var10001);
    //     var10000.forEach(var10001::add);
    // }

    // SCROLL    

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0)
            return super.mouseClicked(mouseX, mouseY, button);


        if (mouseX >= x + SCROLL_BAR_X && mouseY >= y + SCROLL_BAR_Y && mouseX < x + SCROLL_BAR_X + MOUSE_SCROLL_AREA_WIDTH && mouseY < y + SCROLL_BAR_Y + MOUSE_SCROLL_AREA_HEIGHT)
            this.scrolling = true;

        // else if (ADD_BUTTON.isIn(this,x,y,mouseX,mouseY))
        //     client.setScreen(EditLibraryScreen.create(this));

        // else if (CLONE_BUTTON.isIn(this,x,y,mouseX,mouseY))
        //     client.setScreen(EditLibraryScreen.clone(this, handler.library));

        // else if (SETTINGS_BUTTON.isIn(this,x,y,mouseX,mouseY))
        //     client.setScreen(EditLibraryScreen.edit(this, handler.library));
        else
            return super.mouseClicked(mouseX, mouseY, button);

        return true;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0)
            this.scrolling = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int i = (PlayerDataClient.LEARNED_ITEMS_SIZE + 9 - 1) / 9 - 5;
        scrollPosition = (float)((double)scrollPosition - amount / (double)i);
        scrollPosition = MathHelper.clamp(scrollPosition, 0.0F, 1F);
        // this.handler.scrollItems(scrollPosition, VanillaEMCClient.getLearnedItems());
        DataSenderClient.sendDataToServer("scroll", Float.toString(scrollPosition));
        return true;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        boolean scrollActive = PlayerDataClient.LEARNED_ITEMS_SIZE > this.handler.CUSTOM_INV_SIZE;
        if (this.scrolling && scrollActive) {
            float i = this.y + SCROLL_BAR_Y;
            float j = i + MOUSE_SCROLL_AREA_HEIGHT;
            scrollPosition = (float)(mouseY - i - 7.5F) / ((j - i) - 15.0F);
            scrollPosition = MathHelper.clamp(scrollPosition, 0.0F, 1F);
            // this.handler.scrollItems(scrollPosition, VanillaEMCClient.getLearnedItems());
            DataSenderClient.sendDataToServer("scroll", Float.toString(scrollPosition));
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }
}
