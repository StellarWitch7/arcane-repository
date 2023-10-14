package net.messer.mystical_index.mixin.library;

import com.google.common.collect.ImmutableList;
import net.messer.mystical_index.item.custom.book.MysticalBookItem;
import net.messer.mystical_index.item.custom.page.type.ItemStorageTypePage;
import net.messer.mystical_index.util.request.IndexInteractable;
import net.messer.mystical_index.util.request.IndexSource;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ChiseledBookshelfBlockEntity.class)
public abstract class ChiseledBookshelfBlockEntityMixin extends BlockEntity implements IndexInteractable {
    public ChiseledBookshelfBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Shadow public abstract int size();
    @Shadow public abstract ItemStack getStack(int slot);

    @Override
    public List<IndexSource> getSources() {
        ImmutableList.Builder<IndexSource> builder = ImmutableList.builder();

        for (int i = 0; i < size(); i++) {
            var book = getStack(i);
            if (book.getItem() instanceof MysticalBookItem mysticalBookItem) {
                if (mysticalBookItem.getTypePage(book) instanceof ItemStorageTypePage) {
                    builder.add(new IndexSource(book, this));
                }
            }
        }

        return builder.build();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}