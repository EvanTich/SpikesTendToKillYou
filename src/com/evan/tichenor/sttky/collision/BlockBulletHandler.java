package com.evan.tichenor.sttky.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.evan.tichenor.sttky.EntityType;
import com.evan.tichenor.sttky.component.BulletControl;

public class BlockBulletHandler extends CollisionHandler {

    public BlockBulletHandler() {
        super(EntityType.BLOCK, EntityType.BULLET);
    }

    @Override
    protected void onCollisionBegin(Entity block, Entity bullet) {
        bullet.removeFromWorld();
    }
}
