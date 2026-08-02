package com.example.pvpaddon.modules;

import com.example.pvpaddon.PvpAddon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Items;

public class AutoTotemPlus extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> healthThreshold = sgGeneral.add(new DoubleSetting.Builder()
        .name("salud-minima")
        .description("Vida a la que se re-equipa el totem en la mano off-hand.")
        .defaultValue(10.0)
        .min(1.0)
        .max(20.0)
        .sliderMax(20.0)
        .build()
    );

    private final Setting<Boolean> notify = sgGeneral.add(new BoolSetting.Builder()
        .name("avisar")
        .description("Muestra un mensaje en el chat cuando se re-equipa el totem.")
        .defaultValue(true)
        .build()
    );

    public AutoTotemPlus() {
        super(PvpAddon.CATEGORY, "auto-totem-plus", "Re-equipa automaticamente totems de inmortalidad cuando tu vida baja o se te acaba el totem.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null) return;

        boolean offhandIsTotem = mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING;
        boolean lowHealth = mc.player.getHealth() <= healthThreshold.get();

        if (!offhandIsTotem && (lowHealth || true)) {
            int slot = InvUtils.findItemInHotbar(Items.TOTEM_OF_UNDYING).slot();
            if (slot != -1) {
                InvUtils.swap(slot, true); // true = mover a offhand
                if (notify.get()) {
                    info("Totem re-equipado.");
                }
            }
        }
    }
}
