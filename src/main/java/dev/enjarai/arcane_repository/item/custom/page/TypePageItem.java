package dev.enjarai.arcane_repository.item.custom.page;

import dev.enjarai.arcane_repository.item.ItemSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class TypePageItem extends PageItem implements InteractingPage {
    public TypePageItem(String id) {
        super(new ItemSettings().rarity(Rarity.RARE), id);
    }

    public boolean mixColor(ItemStack stack) {
        return false;
    }

    public MutableText getTypeDisplayName() {
        return Text.translatable("item.arcane_repository.page.tooltip.type." + id)
                .fillStyle(Style.EMPTY.withColor(getColor()));
    }

    public Text getBookDisplayName() {
        return Text.translatable("item.arcane_repository.repository_book.type." + id);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        tooltip.add(getTypeDisplayName());
    }
}
