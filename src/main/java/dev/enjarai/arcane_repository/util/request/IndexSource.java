package dev.enjarai.arcane_repository.util.request;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record IndexSource(ItemStack book, @Nullable BlockEntity blockEntity) {
    public ItemStack getBook() {
        return book;
    }

    @Nullable
    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    public void onInteractionComplete() {
        if (blockEntity instanceof IndexInteractable interactable) {
            interactable.onInteractionComplete();
        }
    }
}
