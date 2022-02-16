package net.messer.mixin;

import net.messer.mystical_index.util.BigStack;
import net.messer.mystical_index.util.LibraryIndex;
import net.messer.mystical_index.util.RequestHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;
    @Final
    @Shadow
    private MinecraftServer server;

    @Inject(method = "onChatMessage", at = @At(value = "INVOKE", target = "Ljava/lang/String;startsWith(Ljava/lang/String;)Z", shift = At.Shift.BEFORE), cancellable = true)
    public void onMessage(ChatMessageC2SPacket packet, CallbackInfo info) {
        String message = packet.getChatMessage();

        // player.getWorld().getBlockState() TODO

        server.execute(() -> {
            LibraryIndex index = LibraryIndex.get(player.getWorld(), player.getBlockPos());
            if (!index.isEmpty()) {
                var itemAmount = RequestHelper.processRequest(index, message);
                if (itemAmount != null)
                    player.sendMessage(itemAmount.item().getName().shallowCopy().append(": " + itemAmount.amount()), false);

                for (BigStack bigStack : index.getItems().getAll()) {
                    MutableText tooltipEntry = bigStack.getItem().getName().shallowCopy();
                    tooltipEntry.append(" x").append(String.valueOf(bigStack.getAmount()));
                    player.sendMessage(tooltipEntry, false);
                }
                player.sendMessage(new LiteralText("yes"), false);
            }
        });
        // info.cancel();
    }
}