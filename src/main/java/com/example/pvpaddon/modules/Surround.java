package com.example.pvpaddon.modules;

import com.example.pvpaddon.PvpAddon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * Coloca bloques en los 4 lados horizontales del jugador (y opcionalmente
 * abajo) para protegerse de explosiones de crystal/anchor de otros jugadores.
 * No apunta ni interactua con entidades, solo con el mundo.
 */
public class Surround extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> fillBelow = sgGeneral.add(new BoolSetting.Builder()
        .name("rellenar-abajo")
        .description("Tambien rellena el bloque bajo tus pies.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> onlyObsidian = sgGeneral.add(new BoolSetting.Builder()
        .name("solo-obsidiana")
        .description("Usa solo obsidiana del hotbar para rellenar.")
        .defaultValue(true)
        .build()
    );

    public Surround() {
        super(PvpAddon.CATEGORY, "surround", "Rellena los bloques alrededor tuyo para protegerte de explosiones.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;

        BlockPos playerPos = mc.player.getBlockPos();
        Direction[] sides = { Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST };

        for (Direction dir : sides) {
            tryFill(playerPos.offset(dir));
        }

        if (fillBelow.get()) {
            tryFill(playerPos.down());
        }
    }

    private void tryFill(BlockPos pos) {
        if (mc.world == null || mc.player == null) return;
        if (!mc.world.getBlockState(pos).isAir()) return;

        int slot = onlyObsidian.get()
            ? InvUtils.findItemInHotbar(Blocks.OBSIDIAN.asItem()).slot()
            : InvUtils.findItemInHotbar(Blocks.OBSIDIAN.asItem()).slot();

        if (slot == -1) return;

        // Necesita un bloque solido adyacente para poder hacer click y colocar
        Direction placeSide = null;
        BlockPos support = null;
        for (Direction dir : Direction.values()) {
            BlockPos neighbor = pos.offset(dir);
            if (!mc.world.getBlockState(neighbor).isAir()) {
                support = neighbor;
                placeSide = dir.getOpposite();
                break;
            }
        }
        if (support == null) return;

        InvUtils.swap(slot, false);

        BlockHitResult hit = new BlockHitResult(
            mc.player.getPos(),
            placeSide,
            support,
            false
        );

        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
    }
        }
