package com.example.pvpaddon;

import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import com.example.pvpaddon.modules.AutoTotemPlus;
import com.example.pvpaddon.modules.CrystalNotifier;
import com.example.pvpaddon.modules.HoleFiller;
import com.example.pvpaddon.modules.AntiCrystalDamage;
import com.example.pvpaddon.modules.Surround;
import com.example.pvpaddon.modules.AutoMine;
import com.example.pvpaddon.modules.CrystalPlacer;
import com.example.pvpaddon.modules.AnchorPlacer;
import com.example.pvpaddon.modules.HotbarQuickSelect;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.events.meteor.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PvpAddon extends MeteorAddon {

    // Categoria propia para que los modulos aparezcan agrupados en el menu de Meteor
    public static final Category CATEGORY = new Category("PvP Addon");

    private static final Logger LOG = LogManager.getLogger("pvp-addon");

    @Override
    public void onInitialize() {
        LOG.info("Inicializando PvP Addon");

        Modules.get().add(new AutoTotemPlus());
        Modules.get().add(new CrystalNotifier());
        Modules.get().add(new HoleFiller());
        Modules.get().add(new AntiCrystalDamage());
        Modules.get().add(new Surround());
        Modules.get().add(new AutoMine());
        Modules.get().add(new CrystalPlacer());
        Modules.get().add(new AnchorPlacer());
        Modules.get().add(new HotbarQuickSelect());
    }

    @Override
    public String getPackage() {
        return "com.example.pvpaddon";
    }

    // Opcional: si publicas en GitHub, esto habilita el chequeo de actualizaciones
    // @Override
    // public void onRegisterCategories() { }
}
