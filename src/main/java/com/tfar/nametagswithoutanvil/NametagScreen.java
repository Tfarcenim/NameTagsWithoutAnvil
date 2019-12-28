package com.tfar.nametagswithoutanvil;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class NametagScreen extends ContainerScreen<NameTagContainer> implements IContainerListener {
  public NametagScreen(NameTagContainer p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
    super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
  }

  private TextFieldWidget nameField;
  static final ItemStack nametag_icon = new ItemStack(Items.NAME_TAG);
  static final ItemStack ink_sac_icon = new ItemStack(Items.INK_SAC);


  public static final ResourceLocation NAMETAG_GUI = new ResourceLocation(NametagsWithoutAnvil.MODID,"textures/gui/nametag_gui.png");

  protected void init() {
    super.init();
    this.addButton(new Button(guiLeft + 71, guiTop + 50,34,20,"Save", this::onPress));
    this.minecraft.keyboardListener.enableRepeatEvents(true);
    int xPos = (this.width - this.xSize) / 2;
    int yPos = (this.height - this.ySize) / 2;
    this.nameField = new TextFieldWidget(this.font, xPos + 35, yPos + 26, 103, 12, I18n.format("container.repair", new Object[0]));
    this.nameField.setCanLoseFocus(false);
    this.nameField.changeFocus(true);
    this.nameField.setTextColor(-1);
    this.nameField.setDisabledTextColour(-1);
    this.nameField.setEnableBackgroundDrawing(false);
    this.nameField.setMaxStringLength(35);
    this.nameField.func_212954_a(this::func_214075_a);
    this.children.add(this.nameField);
    (this.container).addListener(this);
    this.setFocusedDefault(this.nameField);
  }

  @Override
  public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
    this.renderBackground();
    super.render(p_render_1_, p_render_2_, p_render_3_);

    this.renderHoveredToolTip(p_render_1_, p_render_2_);
    GlStateManager.disableLighting();
    GlStateManager.disableBlend();
    this.nameField.render(p_render_1_, p_render_2_, p_render_3_);
  }

  /**
   * Draws the background layer of this container (behind the items).
   *
   * @param partialTicks
   * @param mouseX
   * @param mouseY
   */
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.minecraft.getTextureManager().bindTexture(NAMETAG_GUI);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    blit(i, j, 0, 0, this.xSize, this.ySize, 256, 256);
    blit(i + 32, j + 22, 0, 166, 110, 16, 256, 256);
    if (NametagsWithoutAnvil.CommonConfig.requires_ink.get()) blit(i + 32, j + 51, 7, 83, 18, 18, 256, 256);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {

    int itemX = 15;
    int itemY = 20;

    GlStateManager.pushMatrix();
    GlStateManager.enableRescaleNormal();
    RenderHelper.enableGUIStandardItemLighting();
    minecraft.getItemRenderer().renderItemAndEffectIntoGUI(nametag_icon, itemX, itemY);
    minecraft.getItemRenderer().renderItemOverlays(minecraft.fontRenderer, nametag_icon, itemX, itemY);

    if (NametagsWithoutAnvil.CommonConfig.requires_ink.get()) {



      itemY += 33;

      if (container.timesincewarning >= 0){
        container.timesincewarning--;
        minecraft.fontRenderer.drawString(I18n.format("text.nametagswithoutanvil.missingink")
                ,110,itemY + 3,0xFF0000);
      }

      minecraft.getItemRenderer().renderItemAndEffectIntoGUI(ink_sac_icon, itemX, itemY);
      minecraft.getItemRenderer().renderItemOverlays(minecraft.fontRenderer, ink_sac_icon, itemX, itemY);
    }

    RenderHelper.disableStandardItemLighting();
    GlStateManager.disableRescaleNormal();
    GlStateManager.popMatrix();
  }

  private void func_214075_a(String p_214075_1_) {
    if (!p_214075_1_.isEmpty()) {
      String lvt_2_1_ = p_214075_1_;
      Slot lvt_3_1_ = this.container.getSlot(0);
      if (lvt_3_1_.getHasStack() && !lvt_3_1_.getStack().hasDisplayName() && p_214075_1_.equals(lvt_3_1_.getStack().getDisplayName().getString())) {
      }

      //this.nameField.setText(lvt_2_1_);
     // this.minecraft.player.connection.sendPacket(new CRenameItemPacket(lvt_2_1_));
    }
  }

  public void sendAllContents(Container p_71110_1_, NonNullList<ItemStack> p_71110_2_) {
    this.sendSlotContents(p_71110_1_, 0, p_71110_1_.getSlot(0).getStack());
  }

  public void sendSlotContents(Container p_71111_1_, int p_71111_2_, ItemStack p_71111_3_) {
    if (p_71111_2_ == 0) {
      this.nameField.setText(p_71111_3_.isEmpty() ? "" : p_71111_3_.getDisplayName().getString());
    }
  }

  public void resize(Minecraft minecraft, int p_resize_2_, int p_resize_3_) {
    String lvt_4_1_ = this.nameField.getText();
    this.init(minecraft, p_resize_2_, p_resize_3_);
    this.nameField.setText(lvt_4_1_);
  }

  public void removed() {
    super.removed();
    this.minecraft.keyboardListener.enableRepeatEvents(false);
    this.container.removeListener(this);
  }

  public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
    if (p_keyPressed_1_ == 256) {
      this.minecraft.player.closeScreen();
    }

    return this.nameField.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_) || this.nameField.func_212955_f() || super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
  }

  @Override
  public void sendWindowProperty(Container container, int i, int i1) {

  }

  private void onPress(Button b) {
    PacketHandler.INSTANCE.sendToServer(new CPacketRename(nameField.getText()));
  }
}
