package com.spin360.mod.mixin.client;

import com.spin360.mod.client.SpinController;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks into ClientPlayerEntity#tickMovement so that during a spin the yaw
 * is re-applied AFTER Minecraft's own input handling — preventing the normal
 * mouse look from overwriting our spin mid-tick.
 */
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void spin360_applyYaw(CallbackInfo ci) {
        SpinController ctrl = SpinController.get();
        if (!ctrl.isActive()) return;

        ClientPlayerEntity self = (ClientPlayerEntity)(Object) this;

        // tick() was already called in the ClientTickEvent, so just re-apply
        // the current computed yaw to ensure mouse look doesn't clobber it.
        float yaw = ctrl.getRenderYaw(1.0f);
        self.setYaw(yaw);
        self.bodyYaw = yaw;
        self.headYaw = yaw;
    }
}
