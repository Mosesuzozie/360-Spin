package com.spin360.mod.client;

/**
 * Tracks a 360 spin. Instant mode: fires in a single tick.
 *
 * Lifecycle:
 *   startSpin(yaw)  →  isActive() == true
 *   tick()          →  snaps yaw by +360, sets active = false immediately
 */
public class SpinController {

    private static final SpinController INSTANCE = new SpinController();
    public static SpinController get() { return INSTANCE; }

    private boolean active   = false;
    private float   startYaw = 0f;

    private SpinController() {}

    /** Kick off a spin. No-ops if one is already pending. */
    public void startSpin(float currentYaw) {
        if (active) return;
        active   = true;
        startYaw = currentYaw;
    }

    public boolean isActive() { return active; }

    /**
     * Call once per tick. Instantly snaps +360 and clears active in one go.
     * Returns the new yaw to apply to the player.
     */
    public float tick() {
        active = false;
        return startYaw + 360f;
    }

    /** Returns the snapped yaw (used by the mixin). */
    public float getRenderYaw(float partialTick) {
        return startYaw + 360f;
    }
}
