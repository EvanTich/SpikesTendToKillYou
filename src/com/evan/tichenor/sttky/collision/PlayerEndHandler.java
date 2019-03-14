package com.evan.tichenor.sttky.collision;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.PositionComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.evan.tichenor.sttky.EntityType;
import com.evan.tichenor.sttky.STTKYApp;
import com.evan.tichenor.sttky.component.PlayerControl;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 12/21/2017
 */
public class PlayerEndHandler extends CollisionHandler {

    public PlayerEndHandler() {
        super(EntityType.PLAYER, EntityType.LEVEL_END);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity endWall) {
        if(player.getX() > FXGL.getAppWidth() * 3 / 4f) {
            FXGL.<STTKYApp>getAppCast().nextLevel();
        }
    }
}
