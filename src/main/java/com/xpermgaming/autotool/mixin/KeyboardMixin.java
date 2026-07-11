package com.xpermgaming.autotool.mixin;

import com.xpermgaming.autotool.AutoToolClient;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_T;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

/**
 * Implements the physical X + T chord. T is consumed only when X is held,
 * so the normal chat key continues to work when the chord is not used.
 */
@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onKey(JIIII)V", at = @At("HEAD"), cancellable = true)
    private void autotool$onKey(
            long window,
            int key,
            int scancode,
            int action,
            int modifiers,
            CallbackInfo ci
    ) {
        if (action == GLFW_PRESS
                && key == GLFW_KEY_T
                && InputUtil.isKeyPressed(window, GLFW_KEY_X)
                && client.currentScreen == null
                && client.player != null) {
            AutoToolClient.toggle(client);
            ci.cancel();
        }
    }
}
