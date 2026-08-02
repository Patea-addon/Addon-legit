package com.example.pvpaddon.modules;

import com.example.pvpaddon.PvpAddon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

/**
 * Solo cambia el slot seleccionado del hotbar, no interactua ni apunta.
 * Util para no perder tiempo buscando manualmente crystal/obsidiana/glowstone
 * en medio de una pelea.
 */
public class HotbarQuickSelect extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Keybind> crystalKey = sgGeneral.add(new KeybindSetting.Builder()
        .name("tecla-crystal")
        .description("Selecciona end crystal en el hotbar.")
        .defaultValue(Keybind.none())
        .build()
    );

    private final Setting<Keybind> obsidianKey = sgGeneral.add(new KeybindSetting.Builder()
        .name("tecla-obsidiana")
        .description("Selecciona obsidiana en el hotbar.")
        .defaultValue(Keybind.none())
        .build()
    );

    private final Setting<Keybind> glowstoneKey = sgGeneral.add(new KeybindSetting.Builder()
        .name("tecla-glowstone")
        .description("Selecciona glowstone en el hotbar.")
        .defaultValue(Keybind.none())
        .build()
    );

    public HotbarQuickSelect() {
        super(PvpAddon.CATEGORY, "hotbar-quick-select", "Cambia rapido de item en el hotbar con teclas (crystal, obsidiana, glowstone).");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null) return;

        if (crystalKey.get().isPressed()) {
            select(Items.END_CRYSTAL);
        } else if (obsidianKey.get().isPressed()) {
            select(Items.OBSIDIAN);
        } else if (glowstoneKey.get().isPressed()) {
            select(Items.GLOWSTONE);
        }
    }

    private void select(Item item) {
        int slot = InvUtils.findItemInHotbar(item).slot();
        if (slot != -1) {
            mc.player.getInventory().selectedSlot = slot;
        }
    }
}
