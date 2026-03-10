package com.spin360.mod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Spin360Client implements ClientModInitializer {

    public static final String MOD_ID = "spin360";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static KeyBinding spinKey;

    @Override
    public void onInitializeClient() {

        // Register keybind (default: V, rebindable in Options → Controls)
        spinKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.spin360.spin",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.spin360"
        ));

        // Each client tick: check key press, then advance active spin
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // wasPressed() consumes all buffered presses
            while (spinKey.wasPressed()) {
                SpinController.get().startSpin(client.player.getYaw());
            }

            if (SpinController.get().isActive()) {
                float yaw = SpinController.get().tick();
                client.player.setYaw(yaw);
                client.player.bodyYaw = yaw;
                client.player.headYaw = yaw;
            }
        });

        LOGGER.info("[Spin360] Ready — press [V] to 360.");
    }
}
