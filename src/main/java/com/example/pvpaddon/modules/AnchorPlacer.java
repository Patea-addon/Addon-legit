package com.example.pvpaddon.modules;

import com.example.pvpaddon.PvpAddon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

/**
 * NO elige objetivo ni escanea entidades. Mientras mantienes la tecla
 * presionada Y estas apuntando manualmente a un respawn anchor, selecciona
 * glowstone en el hotbar y hace click en el anchor (cargarlo o detonarlo
 * segun su estado, igual que un click manual pero mas rapido).
 */
public class AnchorPlacer extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Keybind> useKey = sgGeneral.add(new KeybindSetting.Builder()
        .name("tecla")
        .description("Tecla que mantienes presionada para usar glowstone en el anchor al que apuntas.")
        .defaultValue(Keybind.none())
        .build()
    );

    public AnchorPlacer() {
        super(PvpAddon.CATEGORY, "anchor-placer", "Usa glowstone en el respawn anchor al que ya estas apuntando manualmente (no elige objetivo por ti).");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        if (!useKey.get().isPressed()) return;
        if (mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.BLOCK) return;

        BlockHitResult hit = (BlockHitResult) mc.crosshairTarget;

        boolean validBase = mc.world.getBlockState(hit.getBlockPos()).isOf(Blocks.RESPAWN_ANCHOR);
        if (!validBase) return;

        int slot = InvUtils.findItemInHotbar(Items.GLOWSTONE).slot();
        if (slot == -1) return;

        InvUtils.swap(slot, false);
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
        mc.player.swingHand(Hand.MAIN_HAND);
    }
}
