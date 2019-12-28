package com.tfar.nametagswithoutanvil;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class NameTagContainer extends Container {

  int timesincewarning = -1;

  protected IItemHandler ink = new ItemStackHandler(1);

  public NameTagContainer(int windowId, PlayerInventory inv) {
    super(NametagsWithoutAnvil.NAMETAG_CONTAINER, windowId);

    int x = 8;
    int y = 84;

    if (NametagsWithoutAnvil.CommonConfig.requires_ink.get()) addSlot(new SlotInk(ink, 0, 33, 52));

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        this.addSlot(new Slot(inv, j + i * 9 + 9, j * 18 + x, i * 18 + y));
      }
    }

    y += 58;

    for (int i = 0; i < 9; i++) {
      this.addSlot(new Slot(inv, i, i * 18 + x, y));
    }
  }

  @Nonnull
  @Override
  public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(index);
    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (index == 0) {
        if (!this.mergeItemStack(itemstack1, 1, 37, true)) {
          return ItemStack.EMPTY;
        }

        slot.onSlotChange(itemstack1, itemstack);
      } else if (this.mergeItemStack(itemstack1, 0, 1, false)) { //Forge Fix Shift Clicking in beacons with stacks larger then 1.
        return ItemStack.EMPTY;
      } else if (index < 28) {
        if (!this.mergeItemStack(itemstack1, 28, 37, false)) {
          return ItemStack.EMPTY;
        }
      } else if (index < 37) {
        if (!this.mergeItemStack(itemstack1, 1, 28, false)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.mergeItemStack(itemstack1, 1, 37, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);
      } else {
        slot.onSlotChanged();
      }

      if (itemstack1.getCount() == itemstack.getCount()) {
        return ItemStack.EMPTY;
      }

      slot.onTake(playerIn, itemstack1);
    }

    return itemstack;
  }

  @Override
  public void onContainerClosed(PlayerEntity player) {
    super.onContainerClosed(player);
    if (NametagsWithoutAnvil.CommonConfig.requires_ink.get())
    this.clearContainer(player);
  }

  protected void clearContainer(PlayerEntity player) {
    if (!player.isAlive() || player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).hasDisconnected()) {
      player.dropItem(ink.getStackInSlot(0), false);
    } else {
      player.inventory.placeItemBackInInventory(player.world, ink.getStackInSlot(0));
    }
  }

  /**
   * Determines whether supplied player can use this container
   *
   * @param playerIn
   */
  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return true;
  }
}
