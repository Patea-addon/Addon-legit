# PvP Addon (Meteor Client 1.21.1)

Addon base para Meteor Client con módulos orientados a PvP:

- **auto-totem-plus**: re-equipa el totem de inmortalidad en tu off-hand automáticamente.
- **crystal-notifier**: te avisa en el chat cuando se coloca un end crystal cerca.
- **hole-filler**: rellena con obsidiana el bloque bajo tus pies si detecta un hueco.
- **anti-crystal-damage**: te avisa (no actúa solo) si un crystal cercano es potencialmente letal.
- **surround**: rellena con obsidiana los bloques alrededor tuyo (protección contra explosiones).
- **auto-mine**: mina automáticamente el bloque al que ya apuntas con la mira.
- **crystal-placer**: al mantener una tecla, coloca el crystal en el bloque exacto al que ya estás apuntando manualmente (obsidiana/bedrock con aire encima). No elige el objetivo por ti.
- **anchor-placer**: al mantener una tecla, usa glowstone en el respawn anchor al que ya estás apuntando manualmente. No elige el objetivo por ti.
- **hotbar-quick-select**: teclas para seleccionar rápido crystal / obsidiana / glowstone en el hotbar sin buscarlos a mano.

## Requisitos

- JDK 21
- Gradle (usa el wrapper, no hace falta instalarlo aparte)
- Meteor Client como dependencia, compilado para 1.21.1

## Pasos para compilar

1. Necesitas el jar de Meteor Client 1.21.1 disponible en un repositorio maven accesible
   (el `maven.meteordevelopment.org` que puse en `build.gradle` es el oficial, pero
   revisa en su GitHub cuál es el `artifact id` y versión exactos para 1.21.1,
   porque cambian entre builds).
2. Genera el wrapper de Gradle si no lo tienes:
   ```
   gradle wrapper --gradle-version 8.10
   ```
3. Compila:
   ```
   ./gradlew build
   ```
4. El jar resultante queda en `build/libs/`. Cópialo a la carpeta `mods` de tu instancia
   de Fabric junto con el jar de Meteor Client y Fabric API.

## Notas importantes

- **Esto es un addon de utilidades/QoL para PvP** (avisos, auto-totem, relleno de huecos,
  surround, auto-mine). No incluye crystal aura, anchor aura, aimbot, killaura ni nada que
  apunte, golpee o coloque explosivos automáticamente contra otros jugadores — eso se dejó
  fuera a propósito y no se va a agregar, sin importar el servidor o el contexto.
- Las coordenadas exactas de las APIs de Meteor Client (`Setting`, `Module`, `InvUtils`, etc.)
  cambian ligeramente entre versiones. Si al compilar te da error de método no encontrado,
  compara contra el código fuente de Meteor Client de la rama/tag `1.21.1` en su GitHub
  para ajustar los imports o firmas de métodos.
- El paquete base es `com.example.pvpaddon` — cámbialo por el tuyo antes de publicar
  (y actualiza `fabric.mod.json` y `build.gradle` acorde).

## Compilar desde el celular (sin PC)

Este proyecto ya trae un workflow de GitHub Actions (`.github/workflows/build.yml`)
que compila el addon en la nube. Pasos desde el navegador o la app de GitHub en el celular:

1. Creá una cuenta en [github.com](https://github.com) si no tenés.
2. Creá un repositorio nuevo (público o privado, cualquiera sirve), por ejemplo `pvp-addon`.
3. Subí **todos** los archivos y carpetas de este zip al repositorio
   (en la app/web de GitHub: "Add file" → "Upload files", arrastrá o seleccioná todo
   el contenido de la carpeta `pvp-addon`, manteniendo la estructura de carpetas).
4. Andá a la pestaña **Actions** del repositorio. Debería arrancar solo un workflow
   llamado "Build addon" apenas subís los archivos (o tocá "Run workflow" si no arranca solo).
5. Esperá a que termine (unos minutos, tarda porque descarga Minecraft/Fabric/Meteor).
6. Si termina en verde ✅, entrá al run terminado → abajo vas a ver un **artefacto
   descargable llamado `pvp-addon-jar`** → tocalo para bajarlo. Ese es tu `.jar` real,
   listo para poner en la carpeta `mods`.
7. Si termina en rojo ❌, entrá al log del paso que falló y pegámelo acá para ayudarte
   a corregirlo (lo más probable es que haya que ajustar la versión exacta de Meteor
   Client en `gradle.properties`/`build.gradle`).

Nota: para usar el `.jar` en Minecraft sí vas a necesitar un dispositivo (PC, o un launcher
de Minecraft compatible con Fabric) donde puedas instalar Fabric Loader + Fabric API +
Meteor Client + este addon — Meteor Client no corre en celular.


```
pvp-addon/
├── build.gradle
├── settings.gradle
├── gradle.properties
└── src/main/
    ├── resources/fabric.mod.json
    └── java/com/example/pvpaddon/
        ├── PvpAddon.java
        └── modules/
            ├── AutoTotemPlus.java
            ├── CrystalNotifier.java
            ├── HoleFiller.java
            └── AntiCrystalDamage.java
```
