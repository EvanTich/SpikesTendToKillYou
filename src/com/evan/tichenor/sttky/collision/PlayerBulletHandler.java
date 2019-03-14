package com.evan.tichenor.sttky.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.evan.tichenor.sttky.EntityType;
import com.evan.tichenor.sttky.component.BulletControl;

public class PlayerBulletHandler extends CollisionHandler {

    public PlayerBulletHandler() {
        super(EntityType.PLAYER, EntityType.BULLET);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity bullet) {
        player.removeFromWorld();
        bullet.removeFromWorld();
    }
}
