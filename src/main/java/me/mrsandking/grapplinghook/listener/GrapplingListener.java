package me.mrsandking.grapplinghook.listener;

import me.mrsandking.grapplinghook.GrapplingHookMain;
import me.mrsandking.grapplinghook.api.GrapplingHook;
import me.mrsandking.grapplinghook.api.event.GrapplingHookEvent;
import me.mrsandking.grapplinghook.manager.GrapplingHookManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

public final class GrapplingListener implements Listener {

    private static final double BASE_Y_FACTOR = 2.5D;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFishEvent(PlayerFishEvent event) {
        Player player = event.getPlayer();

        if (event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY || event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            return;
        }

        if (event.getState() != PlayerFishEvent.State.FAILED_ATTEMPT && event.getState() != PlayerFishEvent.State.IN_GROUND) {
            return;
        }

        GrapplingHook grapplingHook = GrapplingHookMain.getInstance().getGrapplingHookManager().getGrapplingHook(player.getItemInHand());
        if (grapplingHook == null) {
            return;
        }

        if (GrapplingHookMain.getInstance().getGrapplingHookManager().hasCooldown(player.getUniqueId())) {
            final String message = GrapplingHookManagerImpl.COOLDOWN_MESSAGE;
            final long seconds = GrapplingHookMain.getInstance().getGrapplingHookManager().getCooldown(player.getUniqueId());

            player.sendMessage(message.replace("%COOLDOWN%", String.valueOf(seconds)));
            return;
        }

        Vector vector = new Vector(
                event.getHook().getLocation().getX() - player.getLocation().getX(),
                BASE_Y_FACTOR,
                event.getHook().getLocation().getZ() - player.getLocation().getZ());

        vector = vector.normalize().multiply(grapplingHook.getMultiplier());
        vector.setY(Math.min(vector.getY(), grapplingHook.getMaxYPower()));

        GrapplingHookEvent grapplingHookEvent = new GrapplingHookEvent(player, vector);
        Bukkit.getPluginManager().callEvent(grapplingHookEvent);
        if (grapplingHookEvent.isCancelled()) {
            return;
        }

        player.setVelocity(vector);
        GrapplingHookMain.getInstance().getGrapplingHookManager().applyCooldown(player, grapplingHook);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL && GrapplingHookMain.getInstance().getGrapplingHookManager().hasFallDamage(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

}