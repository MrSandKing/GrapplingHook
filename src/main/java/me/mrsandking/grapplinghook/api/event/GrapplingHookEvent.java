package me.mrsandking.grapplinghook.api.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;

@Setter @Getter
public final class GrapplingHookEvent extends PlayerEvent implements Cancellable {

    private static final @Getter HandlerList handlerList = new HandlerList();

    private Vector vector;
    private boolean cancelled;

    public GrapplingHookEvent(Player player, Vector vector) {
        super(player);
        this.vector = vector;
        this.cancelled = false;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}