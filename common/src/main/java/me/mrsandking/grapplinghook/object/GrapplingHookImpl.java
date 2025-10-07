package me.mrsandking.grapplinghook.object;

import lombok.Getter;
import me.mrsandking.grapplinghook.GrapplingHookMain;
import me.mrsandking.grapplinghook.api.GrapplingHook;

@Getter
public final class GrapplingHookImpl implements GrapplingHook {

    public static int SERIAL_NUMBER = GrapplingHookMain.getInstance().getConfig().getInt("SerialNumber", 0);

    private final String id;
    private final String displayName;
    private final double multiplier;
    private final int cooldown;
    private final double maxYPower;

    public GrapplingHookImpl(String id, String displayName, double multiplier, int cooldown, double maxYPower) {
        this.id = id;
        this.displayName = displayName;
        this.multiplier = multiplier;
        this.cooldown = cooldown;
        this.maxYPower = maxYPower;
    }
}