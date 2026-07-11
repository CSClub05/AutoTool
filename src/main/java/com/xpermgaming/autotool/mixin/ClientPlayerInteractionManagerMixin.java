package com.xpermgaming.autotool.mixin;

import com.xpermgaming.autotool.AutoToolClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Chooses the tool before vanilla starts or continues a block-breaking action.
 * Hooking both methods keeps held-button mining seamless across block changes.
 */
@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
    @Inject(
            method = "attackBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z",
            at = @At("HEAD")
    )
    private void autotool$beforeAttackBlock(
            BlockPos pos,
            Direction direction,
            CallbackInfoReturnable<Boolean> cir
    ) {
        AutoToolClient.selectBestTool(MinecraftClient.getInstance(), pos);
    }

    @Inject(
            method = "updateBlockBreakingProgress(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z",
            at = @At("HEAD")
    )
    private void autotool$beforeUpdateBlockBreakingProgress(
            BlockPos pos,
            Direction direction,
            CallbackInfoReturnable<Boolean> cir
    ) {
        AutoToolClient.selectBestTool(MinecraftClient.getInstance(), pos);
    }
}
