package com.example.pvpaddon.modules;

import com.example.pvpaddon.PvpAddon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.Entity;

import java.util.HashSet;
import java.util.Set;

public class CrystalNotifier extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("rango")
        .description("Distancia maxima para avisar de un crystal.")
        .defaultValue(12.0)
        .min(1.0)
        .sliderMax(32.0)
        .build()
    );

    // Guarda los IDs ya notificados para no spamear el chat cada tick
    private final Set<Integer> notified = new HashSet<>();

    public CrystalNotifier() {
        super(PvpAddon.CATEGORY, "crystal-notifier", "Avisa en el chat cuando se coloca un end crystal cerca tuyo.");
    }

    @Override
    public void onActivate() {
        notified.clear();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.world == null || mc.player == null) return;

        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof EndCrystalEntity)) continue;
            if (notified.contains(entity.getId())) continue;

            double distance = mc.player.distanceTo(entity);
            if (distance <= range.get()) {
                notified.add(entity.getId());
                warning("Crystal colocado a %.1f bloques.", distance);
            }
        }
    }
}
