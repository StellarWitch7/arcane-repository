package dev.enjarai.arcane_repository.mixin.accessor;

import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemUsageContext.class)
public interface ItemUsageContextAccessor {
    @Accessor
    BlockHitResult getHit();
}
