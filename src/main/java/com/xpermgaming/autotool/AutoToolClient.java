package com.xpermgaming.autotool;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

/**
 * Client entry point and tool-selection logic.
 */
public final class AutoToolClient implements ClientModInitializer {
    private static final float SPEED_EPSILON = 0.0001F;
    private static boolean enabled = true;

    @Override
    public void onInitializeClient() {
        // Mixins perform the runtime hooks; no event registration is required.
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void toggle(MinecraftClient client) {
        enabled = !enabled;

        if (client.player != null) {
            String translationKey = enabled
                    ? "message.autotool.enabled"
                    : "message.autotool.disabled";
            client.player.sendMessage(Text.translatable(translationKey), true);
        }
    }

    /**
     * Selects the best hotbar slot for the targeted block.
     *
     * <p>The method temporarily changes the local selected slot while asking
     * vanilla for the player's complete block-breaking speed. This means
     * enchantments, status effects, underwater penalties, airborne penalties,
     * and the individual item's mining rules are all included in the score.
     * The original slot is restored before the final winning slot is selected.</p>
     */
    public static void selectBestTool(MinecraftClient client, BlockPos pos) {
        if (!enabled || client.player == null || client.world == null || client.player.isSpectator()) {
            return;
        }

        BlockState state = client.world.getBlockState(pos);
        if (state.isAir()) {
            return;
        }

        ClientPlayerEntity player = client.player;
        PlayerInventory inventory = player.getInventory();
        int originalSlot = inventory.selectedSlot;
        Candidate best = null;

        try {
            for (int slot = 0; slot < PlayerInventory.getHotbarSize(); slot++) {
                ItemStack stack = inventory.getStack(slot);
                inventory.selectedSlot = slot;

                Candidate candidate = new Candidate(
                        slot,
                        player.canHarvest(state),
                        player.getBlockBreakingSpeed(state),
                        remainingDurability(stack)
                );

                if (best == null || candidate.isBetterThan(best, originalSlot)) {
                    best = candidate;
                }
            }
        } finally {
            inventory.selectedSlot = originalSlot;
        }

        if (best != null && best.slot() != originalSlot) {
            inventory.setSelectedSlot(best.slot());
        }
    }

    private static int remainingDurability(ItemStack stack) {
        if (!stack.isDamageable()) {
            return Integer.MAX_VALUE;
        }
        return Math.max(0, stack.getMaxDamage() - stack.getDamage());
    }

    /**
     * Ordering rules:
     * 1) a tool that can harvest the block's correct drops;
     * 2) the highest complete vanilla breaking speed;
     * 3) the currently selected slot, to avoid needless slot flicker;
     * 4) the item with more durability remaining;
     * 5) the lower hotbar slot, for deterministic behavior.
     */
    private record Candidate(int slot, boolean suitable, float speed, int remainingDurability) {
        private boolean isBetterThan(Candidate other, int originalSlot) {
            if (suitable != other.suitable) {
                return suitable;
            }

            if (Math.abs(speed - other.speed) > SPEED_EPSILON) {
                return speed > other.speed;
            }

            boolean isCurrent = slot == originalSlot;
            boolean otherIsCurrent = other.slot == originalSlot;
            if (isCurrent != otherIsCurrent) {
                return isCurrent;
            }

            if (remainingDurability != other.remainingDurability) {
                return remainingDurability > other.remainingDurability;
            }

            return slot < other.slot;
        }
    }
}
