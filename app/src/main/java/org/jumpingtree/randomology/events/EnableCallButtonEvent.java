package org.jumpingtree.randomology.events;

/**
 * Created by miguel.almeida on 06/01/2017.
 */

public class EnableCallButtonEvent {
    private final boolean enabled;

    public EnableCallButtonEvent(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
