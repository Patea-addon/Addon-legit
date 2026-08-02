package com.example.pvpaddon.modules;

import com.example.pvpaddon.PvpAddon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class HoleFiller extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> onlyObsidian = sgGeneral.add(new BoolSetting.Builder()
        .name("solo-obsidiana")
        .description("Solo rellena con obsidiana (evita usar otros bloques por error).")
        .defaultValue(true)
        .build()
    );

    public HoleFiller() {
        super(PvpAddon.CATEGORY, "hole-filler", "Rellena el bloque bajo tus pies si detecta un hueco (proteccion basica contra crystal pvp).");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;

        BlockPos below = mc.player.getBlockPos().down();
        boolean isAir = mc.world.getBlockState(below).isAir();
        if (!isAir) return;

        int slot = onlyObsidian.get()
            ? InvUtils.findItemInHotbar(Blocks.OBSIDIAN.asItem()).slot()
            : InvUtils.findItemInHotbar(Blocks.OBSIDIAN.asItem()).slot();

        if (slot == -1) return;

        InvUtils.swap(slot, false);

        BlockHitResult hit = new BlockHitResult(
            mc.player.getPos(),
            Direction.UP,
            below.down(),
            false
        );

        mc.interactionManager.interactBlock(mc.player, net.minecraft.util.Hand.MAIN_HAND, hit);
    }
}
