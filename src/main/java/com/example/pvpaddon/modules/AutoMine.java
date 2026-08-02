package com.example.pvpaddon.modules;

import com.example.pvpaddon.PvpAddon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

/**
 * Mina automaticamente el bloque al que ya estas apuntando con el cursor
 * (no mueve la camara ni selecciona objetivos, solo automatiza el click
 * mantenido mientras miras un bloque).
 */
public class AutoMine extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> requireSneak = sgGeneral.add(new BoolSetting.Builder()
        .name("solo-agachado")
        .description("Solo mina automaticamente mientras estas agachado, para evitar romper bloques sin querer.")
        .defaultValue(false)
        .build()
    );

    private BlockPos lastPos = null;

    public AutoMine() {
        super(PvpAddon.CATEGORY, "auto-mine", "Mina automaticamente el bloque al que apuntas con la mira.");
    }

    @Override
    public void onActivate() {
        lastPos = null;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null || mc.crosshairTarget == null) return;
        if (requireSneak.get() && !mc.player.isSneaking()) return;

        if (mc.crosshairTarget.getType() != HitResult.Type.BLOCK) {
            lastPos = null;
            return;
        }

        BlockHitResult blockHit = (BlockHitResult) mc.crosshairTarget;
        BlockPos pos = blockHit.getBlockPos();

        if (mc.world.getBlockState(pos).isAir()) {
            lastPos = null;
            return;
        }

        // attackBlock inicia/continua la minería igual que mantener click izquierdo
        mc.interactionManager.attackBlock(pos, blockHit.getSide());
        mc.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
        lastPos = pos;
    }
}
