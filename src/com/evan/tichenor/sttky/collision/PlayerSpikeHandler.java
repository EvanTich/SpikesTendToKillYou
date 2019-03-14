package com.evan.tichenor.sttky.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.evan.tichenor.sttky.EntityType;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 9/19/2017
 */
public class PlayerSpikeHandler extends CollisionHandler {

    private int counter;

    public PlayerSpikeHandler() {
        super(EntityType.PLAYER, EntityType.SPIKE);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity spike) {
        counter = 0;
    }

    @Override
    protected void onCollision(Entity player, Entity spike) {
        if(counter++ > 2) {
            player.removeFromWorld();
        }
    }
}
