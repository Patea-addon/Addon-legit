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
import net.minecraft.util.math.BlockPos;

/**
 * NO elige objetivo ni escanea entidades. Solo agiliza el gesto manual:
 * mientras mantienes la tecla presionada Y tu cursor ya esta apuntando
 * a un bloque valido para crystal (obsidiana/bedrock con aire encima),
 * selecciona el crystal en el hotbar y lo coloca ahi. El apuntado
 * siempre lo haces tu con el mouse, igual que un click manual pero mas rapido.
 */
public class CrystalPlacer extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Keybind> placeKey = sgGeneral.add(new KeybindSetting.Builder()
        .name("tecla")
        .description("Tecla que mantienes presionada para colocar el crystal donde apuntas.")
        .defaultValue(Keybind.none())
        .build()
    );

    public CrystalPlacer() {
        super(PvpAddon.CATEGORY, "crystal-placer", "Coloca el end crystal en el bloque al que ya estas apuntando manualmente (no elige objetivo por ti).");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        if (!placeKey.get().isPressed()) return;
        if (mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.BLOCK) return;

        BlockHitResult hit = (BlockHitResult) mc.crosshairTarget;
        BlockPos base = hit.getBlockPos();

        // Solo valido si el bloque apuntado es obsidiana o bedrock y hay aire arriba,
        // que son los requisitos normales del juego para colocar un crystal.
        boolean validBase = mc.world.getBlockState(base).isOf(Blocks.OBSIDIAN)
            || mc.world.getBlockState(base).isOf(Blocks.BEDROCK);
        boolean airAbove = mc.world.getBlockState(base.up()).isAir();

        if (!validBase || !airAbove) return;

        int slot = InvUtils.findItemInHotbar(Items.END_CRYSTAL).slot();
        if (slot == -1) return;

        InvUtils.swap(slot, false);
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
        mc.player.swingHand(Hand.MAIN_HAND);
    }
}
